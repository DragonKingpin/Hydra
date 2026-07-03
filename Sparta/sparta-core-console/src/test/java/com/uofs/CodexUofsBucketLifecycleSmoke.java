package com.uofs;

import com.pinecone.Pinecone;
import com.pinecone.framework.system.CascadeSystem;
import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.file.ibatis.hydranium.FileMappingDriver;
import com.pinecone.hydra.storage.bucket.Bucket;
import com.pinecone.hydra.storage.bucket.BucketInstrument;
import com.pinecone.hydra.storage.bucket.BucketNodeManipulator;
import com.pinecone.hydra.storage.bucket.BucketPathCacheManipulator;
import com.pinecone.hydra.storage.bucket.GenericBucket;
import com.pinecone.hydra.storage.bucket.TitanBucketInstrument;
import com.pinecone.hydra.storage.bucket.purge.BucketPurgePhase;
import com.pinecone.hydra.storage.bucket.purge.BucketPurgeProgress;
import com.pinecone.hydra.storage.bucket.purge.BucketPurgeReport;
import com.pinecone.hydra.storage.file.KOMFileSystem;
import com.pinecone.hydra.storage.file.entity.FileNode;
import com.pinecone.hydra.storage.file.fat.entity.FileChunk;
import com.pinecone.hydra.storage.file.fat.entity.FileChunkLocation;
import com.pinecone.hydra.storage.file.source.FileMasterManipulator;
import com.pinecone.hydra.unit.imperium.source.TreeMasterManipulator;
import com.pinecone.hydra.volume.ibatis.hydranium.VolumeMappingDriver;
import com.pinecone.slime.jelly.source.ibatis.IbatisClient;
import com.pinecone.ulf.util.guid.GUIDs;
import org.apache.ibatis.session.SqlSession;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

class CodexUofsBucketLifecycleHydra extends UofsManualHydra {
    public CodexUofsBucketLifecycleHydra( String[] args, CascadeSystem parent ) {
        super( args, parent );
    }

    @Override
    public void vitalize() throws Exception {
        Debug.trace( "Titan UOFS bucket lifecycle smoke start" );
        UofsSmokePaths.ensureLocalWorkspace();

        IbatisClient ibatisClient = this.openIbatisClient();
        FileMappingDriver fileMappingDriver = new FileMappingDriver(
                this,
                ibatisClient,
                this.getDispenserCenter()
        );
        VolumeMappingDriver volumeMappingDriver = new VolumeMappingDriver(
                this,
                ibatisClient,
                this.getDispenserCenter()
        );

        try ( SqlSession sqlSession = ibatisClient.openSession( true ) ) {
            UofsSmokeContext context = new UofsSmokeContext(
                    this,
                    ibatisClient,
                    sqlSession,
                    fileMappingDriver,
                    volumeMappingDriver,
                    this.bindMappers( volumeMappingDriver ),
                    UofsSmokePaths.SOURCE_FILE,
                    UofsSmokePaths.ROOT + "\\bucket-lifecycle",
                    UofsSmokePaths.TEMP_FOLDER + "\\bucket-lifecycle",
                    UofsSmokePaths.READBACK_ROOT + "\\bucket-lifecycle",
                    "79"
            );
            context.ensureLocalWorkspace();
            this.runPurgeBucket( context );
            this.runFormatBucket( context );
        }

        Debug.trace( "Titan UOFS bucket lifecycle smoke done" );
    }

    protected void runPurgeBucket( UofsSmokeContext context ) throws Exception {
        String caseName = "bucket-purge-direct-object";
        String seed = context.seed( "01" );
        UofsVolumeFixture fixture = new UofsVolumeFactory( context ).directObjectSimple( caseName, seed );
        Bucket bucket = this.ensureReadyBucket( context, caseName, fixture.volumeGuid, seed );
        KOMFileSystem fileSystem = context.hydra.createFileSystem( context.fileMappingDriver, fixture.volumeGuid, context.tempFolder );

        FileNode firstFileNode = UofsSmokeTools.writeFile( fileSystem, fixture, "root@" + caseName + "/purge/a/avatar.png", context.sourceFile );
        FileNode secondFileNode = UofsSmokeTools.writeFile( fileSystem, fixture, "root@" + caseName + "/purge/b/avatar.png", context.sourceFile );
        List<File> objectFiles = new ArrayList<>();
        objectFiles.addAll( this.captureObjectFiles( fileSystem, fixture, firstFileNode ) );
        objectFiles.addAll( this.captureObjectFiles( fileSystem, fixture, secondFileNode ) );
        this.assertTrue( !objectFiles.isEmpty(), "purge bucket smoke should produce object files" );

        List<BucketPurgeProgress> progresses = new ArrayList<>();
        BucketPurgeReport report = fileSystem.purgeBucket( bucket.getGuid(), fixture.volumeManager, progresses::add );

        this.assertTrue( !report.getFailed(), "purge bucket report should not fail" );
        this.assertTrue( BucketPurgePhase.DONE == report.getPhase(), "purge bucket report should finish with DONE" );
        this.assertTrue( this.hasProgressPhase( progresses, BucketPurgePhase.DONE ), "purge bucket listener should receive DONE" );
        this.assertBucketStatus( context, bucket.getGuid(), "DELETED" );
        this.assertBucketDataGone( context, fileSystem, bucket.getGuid() );
        for ( File objectFile : objectFiles ) {
            this.assertTrue( !objectFile.exists(), "purge bucket should release object data: " + objectFile.getPath() );
        }
        Debug.trace( "purge bucket verified", bucket.getGuid(), "progress", progresses.size() );
    }

    protected void runFormatBucket( UofsSmokeContext context ) throws Exception {
        String caseName = "bucket-format-direct-object";
        String seed = context.seed( "02" );
        UofsVolumeFixture fixture = new UofsVolumeFactory( context ).directObjectSimple( caseName, seed );
        Bucket bucket = this.ensureReadyBucket( context, caseName, fixture.volumeGuid, seed );
        KOMFileSystem fileSystem = context.hydra.createFileSystem( context.fileMappingDriver, fixture.volumeGuid, context.tempFolder );

        FileNode fileNode = UofsSmokeTools.writeFile( fileSystem, fixture, "root@" + caseName + "/format/avatar.png", context.sourceFile );
        List<File> objectFiles = this.captureObjectFiles( fileSystem, fixture, fileNode );
        this.assertTrue( !objectFiles.isEmpty(), "format bucket smoke should produce object files" );

        BucketPurgeReport report = fileSystem.formatBucket( bucket.getGuid(), fixture.volumeManager, null );

        this.assertTrue( !report.getFailed(), "format bucket report should not fail" );
        this.assertTrue( BucketPurgePhase.DONE == report.getPhase(), "format bucket report should finish with DONE" );
        this.assertBucketStatus( context, bucket.getGuid(), "READY" );
        this.assertBucketDataGone( context, fileSystem, bucket.getGuid() );
        for ( File objectFile : objectFiles ) {
            this.assertTrue( !objectFile.exists(), "format bucket should release object data: " + objectFile.getPath() );
        }
        Debug.trace( "format bucket verified", bucket.getGuid(), report.getDoneCount(), "/", report.getTotalCount() );
    }

    protected Bucket ensureReadyBucket( UofsSmokeContext context, String bucketName, GUID volumeGuid, String seed ) {
        BucketInstrument bucketInstrument = this.createBucketInstrument( context );
        Bucket bucket = bucketInstrument.getByUserIdentifierAndBucket( "root", bucketName );
        if ( bucket == null ) {
            GUID bucketGuid = GUIDs.GUID128( "01990000-0000-" + seed + "-8000-000000000701" );
            bucket = new GenericBucket( bucketGuid, "root", bucketName, volumeGuid );
            bucketInstrument.insert( bucket );
            Debug.trace( "bucket created", "root@" + bucketName, bucketGuid, "volume", volumeGuid );
            return bucket;
        }
        bucketInstrument.updateVolume( bucket.getGuid(), volumeGuid );
        bucketInstrument.updateStatus( bucket.getGuid(), "READY" );
        return bucketInstrument.get( bucket.getGuid() );
    }

    protected BucketInstrument createBucketInstrument( UofsSmokeContext context ) {
        FileMasterManipulator fileMasterManipulator = (FileMasterManipulator) context.fileMappingDriver.getMasterManipulator();
        return new TitanBucketInstrument( fileMasterManipulator.getBucketManipulator() );
    }

    protected void assertBucketStatus( UofsSmokeContext context, GUID bucketGuid, String expectedStatus ) {
        Bucket bucket = this.createBucketInstrument( context ).get( bucketGuid );
        this.assertTrue( bucket != null, "bucket should exist: " + bucketGuid );
        this.assertTrue( expectedStatus.equals( bucket.getStatus() ), "bucket status mismatch: " + bucket.getStatus() );
    }

    protected void assertBucketDataGone( UofsSmokeContext context, KOMFileSystem fileSystem, GUID bucketGuid ) {
        FileMasterManipulator fileMasterManipulator = (FileMasterManipulator) context.fileMappingDriver.getMasterManipulator();
        this.assertTrue( fileMasterManipulator.getFileManipulator().countByBucketGuid( bucketGuid ) == 0L, "bucket files should be empty" );
        this.assertTrue( fileMasterManipulator.getFolderManipulator().countByBucketGuid( bucketGuid ) == 0L, "bucket folders should be empty" );
        this.assertTrue( fileMasterManipulator.getFileChunkManipulator().countByBucketGuid( bucketGuid ) == 0L, "bucket chunks should be empty" );
        this.assertTrue( fileMasterManipulator.getFileChunkLocationManipulator().countByBucketGuid( bucketGuid ) == 0L, "bucket locations should be empty" );
        this.assertTrue( fileMasterManipulator.getJournalManipulator().countByBucketGuid( bucketGuid ) == 0L, "bucket journals should be empty" );
        this.assertTrue( fileMasterManipulator.getJournalItemManipulator().countByBucketGuid( bucketGuid ) == 0L, "bucket journal items should be empty" );
        this.assertTrue( fileMasterManipulator.getSymbolicManipulator().countByBucketGuid( bucketGuid ) == 0L, "bucket symbolic nodes should be empty" );
        this.assertTrue( fileMasterManipulator.getExternalSymbolicManipulator().countByBucketGuid( bucketGuid ) == 0L, "bucket external symbolic nodes should be empty" );
        this.assertBucketNodeDataGone( fileSystem, bucketGuid );
        this.assertBucketPathCacheGone( fileSystem, bucketGuid );
    }

    protected void assertBucketNodeDataGone( KOMFileSystem fileSystem, GUID bucketGuid ) {
        TreeMasterManipulator treeMasterManipulator = this.getTreeMasterManipulator( fileSystem );
        if ( treeMasterManipulator.getTrieTreeManipulator() instanceof BucketNodeManipulator ) {
            BucketNodeManipulator bucketNodeManipulator = (BucketNodeManipulator) treeMasterManipulator.getTrieTreeManipulator();
            this.assertTrue( bucketNodeManipulator.countTreeByBucketGuid( bucketGuid ) == 0L, "bucket tree edges should be empty" );
            this.assertTrue( bucketNodeManipulator.countNodesByBucketGuid( bucketGuid ) == 0L, "bucket nodes should be empty" );
        }
    }

    protected void assertBucketPathCacheGone( KOMFileSystem fileSystem, GUID bucketGuid ) {
        TreeMasterManipulator treeMasterManipulator = this.getTreeMasterManipulator( fileSystem );
        if ( treeMasterManipulator.getTriePathCacheManipulator() instanceof BucketPathCacheManipulator ) {
            BucketPathCacheManipulator bucketPathCacheManipulator = (BucketPathCacheManipulator) treeMasterManipulator.getTriePathCacheManipulator();
            this.assertTrue( bucketPathCacheManipulator.countPathCacheByBucketGuid( bucketGuid ) == 0L, "bucket path cache should be empty" );
        }
    }

    protected TreeMasterManipulator getTreeMasterManipulator( KOMFileSystem fileSystem ) {
        if ( fileSystem.getFileMasterManipulator().getSkeletonMasterManipulator() instanceof TreeMasterManipulator ) {
            return (TreeMasterManipulator) fileSystem.getFileMasterManipulator().getSkeletonMasterManipulator();
        }
        throw new IllegalStateException( "UOFS skeleton master manipulator should support tree operations." );
    }

    protected List<File> captureObjectFiles( KOMFileSystem fileSystem, UofsVolumeFixture fixture, FileNode fileNode ) {
        List<File> ret = new ArrayList<>();
        for ( FileChunk chunk : fileSystem.getFatChunkInstrument().fetchChunks( fileNode.getGuid() ) ) {
            for ( FileChunkLocation location : fileSystem.getFatChunkInstrument().fetchLocations( chunk.getGuid() ) ) {
                if ( location.getObjectKey() != null && fixture.objectRoot != null ) {
                    ret.add( new File( fixture.objectRoot, location.getObjectKey() ) );
                }
            }
        }
        return ret;
    }

    protected boolean hasProgressPhase( List<BucketPurgeProgress> progresses, BucketPurgePhase phase ) {
        for ( BucketPurgeProgress progress : progresses ) {
            if ( phase == progress.getPhase() ) {
                return true;
            }
        }
        return false;
    }

    protected void assertTrue( boolean condition, String message ) {
        if ( !condition ) {
            throw new IllegalStateException( message );
        }
    }
}

public class CodexUofsBucketLifecycleSmoke implements Pinenut {
    public static void main( String[] args ) throws Exception {
        Pinecone.init( (Object... cfg) -> {
            CodexUofsBucketLifecycleHydra smoke = (CodexUofsBucketLifecycleHydra) Pinecone.sys().getTaskManager().add(
                    new CodexUofsBucketLifecycleHydra( args, Pinecone.sys() )
            );
            smoke.vitalize();
            return 0;
        }, (Object[]) args );
    }
}
