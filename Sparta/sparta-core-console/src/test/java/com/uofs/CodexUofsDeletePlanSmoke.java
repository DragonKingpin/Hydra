package com.uofs;

import com.pinecone.Pinecone;
import com.pinecone.framework.system.CascadeSystem;
import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.file.ibatis.hydranium.FileMappingDriver;
import com.pinecone.hydra.storage.file.KOMFileSystem;
import com.pinecone.hydra.storage.file.entity.FileNode;
import com.pinecone.hydra.storage.file.fat.entity.FileChunk;
import com.pinecone.hydra.storage.file.fat.entity.FileChunkLocation;
import com.pinecone.hydra.storage.volume.core.VolumeAllocationMode;
import com.pinecone.hydra.storage.volume.core.VolumeFreeIntent;
import com.pinecone.hydra.volume.ibatis.hydranium.VolumeMappingDriver;
import com.pinecone.slime.jelly.source.ibatis.IbatisClient;
import org.apache.ibatis.session.SqlSession;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

class CodexUofsDeletePlanHydra extends UofsManualHydra {
    public CodexUofsDeletePlanHydra( String[] args, CascadeSystem parent ) {
        super( args, parent );
    }

    @Override
    public void vitalize() throws Exception {
        Debug.trace( "Titan UOFS delete plan smoke start" );
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
                    UofsSmokePaths.ROOT + "\\delete-plan",
                    UofsSmokePaths.TEMP_FOLDER + "\\delete-plan",
                    UofsSmokePaths.READBACK_ROOT + "\\delete-plan",
                    "78"
            );
            context.ensureLocalWorkspace();
            this.runDirectObjectDelete( context );
            this.runBlockFreeIntentDelete( context );
        }

        Debug.trace( "Titan UOFS delete plan smoke done" );
    }

    protected void runDirectObjectDelete( UofsSmokeContext context ) throws Exception {
        String caseName = "delete-direct-object";
        String seed = context.seed( "11" );
        UofsVolumeFixture fixture = new UofsVolumeFactory( context ).directObjectSimple( caseName, seed );
        UofsSmokeTools.ensureBucket( context, caseName, fixture.volumeGuid, seed );
        KOMFileSystem fileSystem = context.hydra.createFileSystem( context.fileMappingDriver, fixture.volumeGuid, context.tempFolder );
        String uofsPath = "root@" + caseName + "/delete-me/avatar.png";

        UofsSmokeTools.cleanupOldUofsFile( fileSystem, fixture, uofsPath );
        FileNode fileNode = UofsSmokeTools.writeFile( fileSystem, fixture, uofsPath, context.sourceFile );
        List<File> objectFiles = this.captureObjectFiles( fileSystem, fixture, fileNode );
        this.assertTrue( !objectFiles.isEmpty(), "direct object smoke should produce object files" );

        fileSystem.remove( uofsPath, fixture.volumeManager );

        this.assertTrue( fileSystem.queryElement( uofsPath ) == null, "deleted direct object path should be undefined" );
        this.assertTrue(
                fileSystem.getFatChunkInstrument().fetchChunks( fileNode.getGuid() ).isEmpty(),
                "deleted direct object file should have no FAT chunks"
        );
        for ( File objectFile : objectFiles ) {
            this.assertTrue( !objectFile.exists(), "object data should be released: " + objectFile.getPath() );
        }
        Debug.trace( "delete direct object verified", fileNode.getGuid() );
    }

    protected void runBlockFreeIntentDelete( UofsSmokeContext context ) throws Exception {
        String caseName = "delete-block-simple";
        String seed = context.seed( "12" );
        UofsVolumeFixture fixture = new UofsVolumeFactory( context ).blockSimple( caseName, seed, VolumeAllocationMode.THIN );
        UofsSmokeTools.ensureBucket( context, caseName, fixture.volumeGuid, seed );
        KOMFileSystem fileSystem = context.hydra.createFileSystem( context.fileMappingDriver, fixture.volumeGuid, context.tempFolder );
        String uofsPath = "root@" + caseName + "/delete-me/avatar.png";

        UofsSmokeTools.cleanupOldUofsFile( fileSystem, fixture, uofsPath );
        FileNode fileNode = UofsSmokeTools.writeFile( fileSystem, fixture, uofsPath, context.sourceFile );
        List<GUID> locationGuids = this.captureLocationGuids( fileSystem, fileNode );
        this.assertTrue( !locationGuids.isEmpty(), "block smoke should produce FAT locations" );

        fileSystem.remove( uofsPath, fixture.volumeManager );

        this.assertTrue( fileSystem.queryElement( uofsPath ) == null, "deleted block path should be undefined" );
        this.assertTrue(
                fileSystem.getFatChunkInstrument().fetchChunks( fileNode.getGuid() ).isEmpty(),
                "deleted block file should have no FAT chunks"
        );
        this.assertTrue(
                this.hasFreeIntentForAnyLocation( fixture, locationGuids ),
                "block delete should record a volume free intent"
        );
        Debug.trace( "delete block free intent verified", fileNode.getGuid() );
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

    protected List<GUID> captureLocationGuids( KOMFileSystem fileSystem, FileNode fileNode ) {
        List<GUID> ret = new ArrayList<>();
        for ( FileChunk chunk : fileSystem.getFatChunkInstrument().fetchChunks( fileNode.getGuid() ) ) {
            for ( FileChunkLocation location : fileSystem.getFatChunkInstrument().fetchLocations( chunk.getGuid() ) ) {
                ret.add( location.getGuid() );
            }
        }
        return ret;
    }

    protected boolean hasFreeIntentForAnyLocation( UofsVolumeFixture fixture, List<GUID> locationGuids ) {
        for ( VolumeFreeIntent intent : fixture.volumeManager.listVolumeFreeIntents( fixture.volumeGuid ) ) {
            if ( locationGuids.contains( intent.getSourceLocationGuid() ) ) {
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

public class CodexUofsDeletePlanSmoke {
    public static void main( String[] args ) throws Exception {
        Pinecone.init( (Object... cfg) -> {
            CodexUofsDeletePlanHydra smoke = (CodexUofsDeletePlanHydra) Pinecone.sys().getTaskManager().add(
                    new CodexUofsDeletePlanHydra( args, Pinecone.sys() )
            );
            smoke.vitalize();
            return 0;
        }, (Object[]) args );
    }
}
