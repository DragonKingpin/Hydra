package com.pinecone.hydra.storage.bucket.purge;

import com.pinecone.framework.system.Nullable;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.bucket.Bucket;
import com.pinecone.hydra.storage.bucket.BucketNodeManipulator;
import com.pinecone.hydra.storage.bucket.BucketPathCacheManipulator;
import com.pinecone.hydra.storage.bucket.purge.BucketPurgeExecutor;
import com.pinecone.hydra.storage.bucket.purge.BucketPurgeOperation;
import com.pinecone.hydra.storage.bucket.purge.BucketPurgePhase;
import com.pinecone.hydra.storage.bucket.purge.BucketPurgeProgress;
import com.pinecone.hydra.storage.bucket.purge.BucketPurgeProgressListener;
import com.pinecone.hydra.storage.bucket.purge.BucketPurgeReport;
import com.pinecone.hydra.storage.bucket.source.BucketManipulator;
import com.pinecone.hydra.storage.file.KOMFileSystem;
import com.pinecone.hydra.storage.file.entity.FileNode;
import com.pinecone.hydra.storage.file.fat.entity.FileChunk;
import com.pinecone.hydra.storage.file.fat.entity.FileChunkLocation;
import com.pinecone.hydra.storage.file.fat.io.TitanFatChunkStore;
import com.pinecone.hydra.storage.file.source.ExternalSymbolicManipulator;
import com.pinecone.hydra.storage.file.source.FileMasterManipulator;
import com.pinecone.hydra.storage.file.source.SymbolicManipulator;
import com.pinecone.hydra.storage.volume.VolumeManager;
import com.pinecone.hydra.system.ko.driver.KOISkeletonMasterManipulator;
import com.pinecone.hydra.unit.imperium.source.TreeMasterManipulator;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class GenericBucketPurgeExecutor implements BucketPurgeExecutor {
    protected static final int PAGE_SIZE = 256;

    protected static final String STATUS_READY = "READY";
    protected static final String STATUS_DELETING = "DELETING";
    protected static final String STATUS_DELETED = "DELETED";

    protected final KOMFileSystem mFileSystem;
    protected final VolumeManager mVolumeManager;
    protected final FileMasterManipulator mFileMasterManipulator;
    protected final BucketManipulator mBucketManipulator;

    public GenericBucketPurgeExecutor( KOMFileSystem fileSystem, VolumeManager volumeManager ) {
        if ( fileSystem == null ) {
            throw new IllegalArgumentException( "fileSystem should not be null." );
        }
        if ( volumeManager == null ) {
            throw new IllegalArgumentException( "volumeManager should not be null." );
        }
        this.mFileSystem            = fileSystem;
        this.mVolumeManager         = volumeManager;
        this.mFileMasterManipulator = fileSystem.getFileMasterManipulator();
        this.mBucketManipulator     = this.mFileMasterManipulator.getBucketManipulator();
    }

    @Override
    public BucketPurgeReport purgeBucket( GUID bucketGuid, @Nullable BucketPurgeProgressListener listener ) {
        return this.execute( bucketGuid, BucketPurgeOperation.PURGE, listener );
    }

    @Override
    public BucketPurgeReport formatBucket( GUID bucketGuid, @Nullable BucketPurgeProgressListener listener ) {
        return this.execute( bucketGuid, BucketPurgeOperation.FORMAT, listener );
    }

    protected BucketPurgeReport execute(
            GUID bucketGuid,
            BucketPurgeOperation operation,
            @Nullable BucketPurgeProgressListener listener
    ) {
        BucketPurgeReport report = this.newReport( bucketGuid, operation );
        try {
            Bucket bucket = this.assertBucket( bucketGuid, operation );
            if ( bucket == null ) {
                this.finishDone( report, listener, "Bucket is already purged." );
                return report;
            }
            this.lockBucket( bucketGuid, report, listener );
            long totalCount = this.scanTotalCount( bucketGuid, report, listener );
            report.setTotalCount( totalCount );
            this.releaseFileData( bucketGuid, report, listener );
            this.purgeFatResidue( bucketGuid, report, listener );
            this.purgeJournal( bucketGuid, report, listener );
            this.purgeSymbolic( bucketGuid, report, listener );
            this.purgePathCache( bucketGuid, report, listener );
            this.purgeTree( bucketGuid, report, listener );
            this.purgeEntity( bucketGuid, report, listener );
            this.finalizeBucket( bucketGuid, operation, report, listener );
            this.finishDone( report, listener, "Bucket purge completed." );
            return report;
        }
        catch ( RuntimeException | IOException e ) {
            report.setFailed( true );
            report.setPhase( BucketPurgePhase.FAILED );
            report.setMessage( e.getMessage() );
            this.emit( report, listener, BucketPurgePhase.FAILED, null, e.getMessage() );
            if ( e instanceof RuntimeException ) {
                throw (RuntimeException) e;
            }
            throw new IllegalStateException( "Failed to purge UOFS bucket: " + bucketGuid, e );
        }
    }

    protected Bucket assertBucket( GUID bucketGuid, BucketPurgeOperation operation ) {
        if ( bucketGuid == null ) {
            throw new IllegalArgumentException( "bucketGuid should not be null." );
        }
        Bucket bucket = this.mBucketManipulator.get( bucketGuid );
        if ( bucket == null ) {
            throw new IllegalArgumentException( "UOFS bucket not found: " + bucketGuid );
        }
        if ( operation == BucketPurgeOperation.PURGE && STATUS_DELETED.equals( bucket.getStatus() ) ) {
            return null;
        }
        return bucket;
    }

    protected void lockBucket(
            GUID bucketGuid,
            BucketPurgeReport report,
            @Nullable BucketPurgeProgressListener listener
    ) {
        this.emit( report, listener, BucketPurgePhase.LOCKING_BUCKET, bucketGuid, "Locking bucket for synchronous purge." );
        this.mBucketManipulator.updateStatus( bucketGuid, STATUS_DELETING );
    }

    protected long scanTotalCount(
            GUID bucketGuid,
            BucketPurgeReport report,
            @Nullable BucketPurgeProgressListener listener
    ) {
        this.emit( report, listener, BucketPurgePhase.SCANNING, bucketGuid, "Scanning bucket purge workload." );
        long totalCount = 0L;
        totalCount += this.mFileMasterManipulator.getFileManipulator().countByBucketGuid( bucketGuid );
        totalCount += this.mFileMasterManipulator.getFolderManipulator().countByBucketGuid( bucketGuid );
        totalCount += this.mFileMasterManipulator.getFileChunkManipulator().countByBucketGuid( bucketGuid );
        totalCount += this.mFileMasterManipulator.getFileChunkLocationManipulator().countByBucketGuid( bucketGuid );
        totalCount += this.mFileMasterManipulator.getJournalManipulator().countByBucketGuid( bucketGuid );
        totalCount += this.mFileMasterManipulator.getJournalItemManipulator().countByBucketGuid( bucketGuid );
        totalCount += this.mFileMasterManipulator.getSymbolicManipulator().countByBucketGuid( bucketGuid );
        totalCount += this.mFileMasterManipulator.getExternalSymbolicManipulator().countByBucketGuid( bucketGuid );
        totalCount += this.countPathCacheByBucketGuid( bucketGuid );
        totalCount += this.countTreeByBucketGuid( bucketGuid );
        totalCount += this.countNodesByBucketGuid( bucketGuid );
        report.setTotalCount( totalCount );
        this.emit( report, listener, BucketPurgePhase.SCANNING, bucketGuid, "Bucket purge workload scanned." );
        return totalCount;
    }

    protected void releaseFileData(
            GUID bucketGuid,
            BucketPurgeReport report,
            @Nullable BucketPurgeProgressListener listener
    ) throws IOException {
        this.emit( report, listener, BucketPurgePhase.RELEASING_FILE_DATA, bucketGuid, "Releasing file data." );
        GUID lastGuid = null;
        while ( true ) {
            List<GUID> fileGuids = this.mFileMasterManipulator.getFileManipulator().listGuidsByBucketGuid( bucketGuid, lastGuid, PAGE_SIZE );
            if ( fileGuids == null || fileGuids.isEmpty() ) {
                return;
            }
            for ( GUID fileGuid : fileGuids ) {
                this.releaseOneFileData( fileGuid, report, listener );
                lastGuid = fileGuid;
            }
        }
    }

    protected void releaseOneFileData(
            GUID fileGuid,
            BucketPurgeReport report,
            @Nullable BucketPurgeProgressListener listener
    ) throws IOException {
        FileNode fileNode = this.mFileMasterManipulator.getFileManipulator().getFileNodeByGuid( fileGuid );
        if ( fileNode == null ) {
            this.advance( report, listener, BucketPurgePhase.RELEASING_FILE_DATA, fileGuid, 0L, "File metadata is already missing." );
            return;
        }
        TitanFatChunkStore chunkStore = new TitanFatChunkStore( this.mVolumeManager );
        long releasedCount = 0L;
        for ( FileChunk chunk : this.mFileSystem.getFatChunkInstrument().fetchChunks( fileGuid ) ) {
            for ( FileChunkLocation location : this.mFileSystem.getFatChunkInstrument().fetchLocations( chunk.getGuid() ) ) {
                chunkStore.delete( location );
                releasedCount += 1L;
            }
            releasedCount += 1L;
        }
        this.mFileSystem.getFatChunkInstrument().deleteFileChunks( fileGuid );
        this.advance( report, listener, BucketPurgePhase.RELEASING_FILE_DATA, fileGuid, releasedCount, "Released file data." );
    }

    protected void purgeFatResidue(
            GUID bucketGuid,
            BucketPurgeReport report,
            @Nullable BucketPurgeProgressListener listener
    ) {
        this.emit( report, listener, BucketPurgePhase.PURGING_FAT, bucketGuid, "Purging FAT residue." );
        long locationCount = this.mFileMasterManipulator.getFileChunkLocationManipulator().countByBucketGuid( bucketGuid );
        this.mFileMasterManipulator.getFileChunkLocationManipulator().deleteByBucketGuid( bucketGuid );
        this.advance( report, listener, BucketPurgePhase.PURGING_FAT, bucketGuid, locationCount, "Purged FAT locations." );
        long chunkCount = this.mFileMasterManipulator.getFileChunkManipulator().countByBucketGuid( bucketGuid );
        this.mFileMasterManipulator.getFileChunkManipulator().deleteByBucketGuid( bucketGuid );
        this.advance( report, listener, BucketPurgePhase.PURGING_FAT, bucketGuid, chunkCount, "Purged FAT chunks." );
    }

    protected void purgeJournal(
            GUID bucketGuid,
            BucketPurgeReport report,
            @Nullable BucketPurgeProgressListener listener
    ) {
        this.emit( report, listener, BucketPurgePhase.PURGING_JOURNAL, bucketGuid, "Purging file journals." );
        long itemCount = this.mFileMasterManipulator.getJournalItemManipulator().countByBucketGuid( bucketGuid );
        this.mFileMasterManipulator.getJournalItemManipulator().deleteByBucketGuid( bucketGuid );
        this.advance( report, listener, BucketPurgePhase.PURGING_JOURNAL, bucketGuid, itemCount, "Purged journal items." );
        long journalCount = this.mFileMasterManipulator.getJournalManipulator().countByBucketGuid( bucketGuid );
        this.mFileMasterManipulator.getJournalManipulator().deleteByBucketGuid( bucketGuid );
        this.advance( report, listener, BucketPurgePhase.PURGING_JOURNAL, bucketGuid, journalCount, "Purged journals." );
    }

    protected void purgeSymbolic(
            GUID bucketGuid,
            BucketPurgeReport report,
            @Nullable BucketPurgeProgressListener listener
    ) {
        this.emit( report, listener, BucketPurgePhase.PURGING_SYMBOLIC, bucketGuid, "Purging symbolic nodes." );
        SymbolicManipulator symbolicManipulator = this.mFileMasterManipulator.getSymbolicManipulator();
        long internalCount = symbolicManipulator.countByBucketGuid( bucketGuid );
        symbolicManipulator.deleteByBucketGuid( bucketGuid );
        this.advance( report, listener, BucketPurgePhase.PURGING_SYMBOLIC, bucketGuid, internalCount, "Purged internal symbolic nodes." );
        ExternalSymbolicManipulator externalSymbolicManipulator = this.mFileMasterManipulator.getExternalSymbolicManipulator();
        long externalCount = externalSymbolicManipulator.countByBucketGuid( bucketGuid );
        externalSymbolicManipulator.deleteByBucketGuid( bucketGuid );
        this.advance( report, listener, BucketPurgePhase.PURGING_SYMBOLIC, bucketGuid, externalCount, "Purged external symbolic nodes." );
    }

    protected void purgePathCache(
            GUID bucketGuid,
            BucketPurgeReport report,
            @Nullable BucketPurgeProgressListener listener
    ) {
        this.emit( report, listener, BucketPurgePhase.PURGING_PATH_CACHE, bucketGuid, "Purging path cache." );
        BucketPathCacheManipulator manipulator = this.getBucketPathCacheManipulator();
        if ( manipulator == null ) {
            return;
        }
        long count = manipulator.countPathCacheByBucketGuid( bucketGuid );
        manipulator.deletePathCacheByBucketGuid( bucketGuid );
        this.advance( report, listener, BucketPurgePhase.PURGING_PATH_CACHE, bucketGuid, count, "Purged path cache." );
    }

    protected void purgeTree(
            GUID bucketGuid,
            BucketPurgeReport report,
            @Nullable BucketPurgeProgressListener listener
    ) {
        this.emit( report, listener, BucketPurgePhase.PURGING_TREE, bucketGuid, "Purging tree edges." );
        BucketNodeManipulator manipulator = this.getBucketNodeManipulator();
        if ( manipulator == null ) {
            return;
        }
        long count = manipulator.countTreeByBucketGuid( bucketGuid );
        manipulator.deleteTreeByBucketGuid( bucketGuid );
        this.advance( report, listener, BucketPurgePhase.PURGING_TREE, bucketGuid, count, "Purged tree edges." );
    }

    protected void purgeEntity(
            GUID bucketGuid,
            BucketPurgeReport report,
            @Nullable BucketPurgeProgressListener listener
    ) {
        this.emit( report, listener, BucketPurgePhase.PURGING_ENTITY, bucketGuid, "Purging UOFS entities." );
        long fileCount = this.mFileMasterManipulator.getFileManipulator().countByBucketGuid( bucketGuid );
        this.mFileMasterManipulator.getFileManipulator().deleteByBucketGuid( bucketGuid );
        this.advance( report, listener, BucketPurgePhase.PURGING_ENTITY, bucketGuid, fileCount, "Purged file entities." );
        long folderCount = this.mFileMasterManipulator.getFolderManipulator().countByBucketGuid( bucketGuid );
        this.mFileMasterManipulator.getFolderManipulator().deleteByBucketGuid( bucketGuid );
        this.advance( report, listener, BucketPurgePhase.PURGING_ENTITY, bucketGuid, folderCount, "Purged folder entities." );
        BucketNodeManipulator manipulator = this.getBucketNodeManipulator();
        if ( manipulator == null ) {
            return;
        }
        long nodeCount = manipulator.countNodesByBucketGuid( bucketGuid );
        manipulator.deleteNodesByBucketGuid( bucketGuid );
        this.advance( report, listener, BucketPurgePhase.PURGING_ENTITY, bucketGuid, nodeCount, "Purged tree nodes." );
    }

    protected void finalizeBucket(
            GUID bucketGuid,
            BucketPurgeOperation operation,
            BucketPurgeReport report,
            @Nullable BucketPurgeProgressListener listener
    ) {
        this.emit( report, listener, BucketPurgePhase.FINALIZING, bucketGuid, "Finalizing bucket operation." );
        if ( operation == BucketPurgeOperation.PURGE ) {
            this.mBucketManipulator.updateStatus( bucketGuid, STATUS_DELETED );
        }
        else {
            this.mBucketManipulator.updateStatus( bucketGuid, STATUS_READY );
        }
    }

    protected long countPathCacheByBucketGuid( GUID bucketGuid ) {
        BucketPathCacheManipulator manipulator = this.getBucketPathCacheManipulator();
        if ( manipulator == null ) {
            return 0L;
        }
        return manipulator.countPathCacheByBucketGuid( bucketGuid );
    }

    protected long countTreeByBucketGuid( GUID bucketGuid ) {
        BucketNodeManipulator manipulator = this.getBucketNodeManipulator();
        if ( manipulator == null ) {
            return 0L;
        }
        return manipulator.countTreeByBucketGuid( bucketGuid );
    }

    protected long countNodesByBucketGuid( GUID bucketGuid ) {
        BucketNodeManipulator manipulator = this.getBucketNodeManipulator();
        if ( manipulator == null ) {
            return 0L;
        }
        return manipulator.countNodesByBucketGuid( bucketGuid );
    }

    protected BucketPathCacheManipulator getBucketPathCacheManipulator() {
        TreeMasterManipulator treeMasterManipulator = this.getTreeMasterManipulator();
        if ( treeMasterManipulator == null ) {
            return null;
        }
        if ( treeMasterManipulator.getTriePathCacheManipulator() instanceof BucketPathCacheManipulator ) {
            return (BucketPathCacheManipulator) treeMasterManipulator.getTriePathCacheManipulator();
        }
        return null;
    }

    protected BucketNodeManipulator getBucketNodeManipulator() {
        TreeMasterManipulator treeMasterManipulator = this.getTreeMasterManipulator();
        if ( treeMasterManipulator == null ) {
            return null;
        }
        if ( treeMasterManipulator.getTrieTreeManipulator() instanceof BucketNodeManipulator ) {
            return (BucketNodeManipulator) treeMasterManipulator.getTrieTreeManipulator();
        }
        return null;
    }

    protected TreeMasterManipulator getTreeMasterManipulator() {
        KOISkeletonMasterManipulator skeletonMasterManipulator = this.mFileMasterManipulator.getSkeletonMasterManipulator();
        if ( skeletonMasterManipulator instanceof TreeMasterManipulator ) {
            return (TreeMasterManipulator) skeletonMasterManipulator;
        }
        return null;
    }

    protected void finishDone(
            BucketPurgeReport report,
            @Nullable BucketPurgeProgressListener listener,
            String message
    ) {
        report.setPhase( BucketPurgePhase.DONE );
        report.setMessage( message );
        this.emit( report, listener, BucketPurgePhase.DONE, report.getBucketGuid(), message );
    }

    protected void advance(
            BucketPurgeReport report,
            @Nullable BucketPurgeProgressListener listener,
            BucketPurgePhase phase,
            GUID currentGuid,
            long step,
            String message
    ) {
        report.setDoneCount( report.getDoneCount() + step );
        this.emit( report, listener, phase, currentGuid, message );
    }

    protected void emit(
            BucketPurgeReport report,
            @Nullable BucketPurgeProgressListener listener,
            BucketPurgePhase phase,
            GUID currentGuid,
            String message
    ) {
        report.setPhase( phase );
        report.setMessage( message );
        if ( listener == null ) {
            return;
        }
        BucketPurgeProgress progress = new BucketPurgeProgress();
        progress.setBucketGuid( report.getBucketGuid() );
        progress.setOperation( report.getOperation() );
        progress.setPhase( phase );
        progress.setTotalCount( report.getTotalCount() );
        progress.setDoneCount( report.getDoneCount() );
        progress.setCurrentGuid( currentGuid );
        progress.setMessage( message );
        progress.setCreateTime( LocalDateTime.now() );
        listener.onProgress( progress );
    }

    protected BucketPurgeReport newReport( GUID bucketGuid, BucketPurgeOperation operation ) {
        BucketPurgeReport report = new BucketPurgeReport();
        report.setBucketGuid( bucketGuid );
        report.setOperation( operation );
        report.setPhase( BucketPurgePhase.LOCKING_BUCKET );
        return report;
    }
}
