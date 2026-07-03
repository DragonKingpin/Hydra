package com.pinecone.hydra.storage.file.transfer;

import com.pinecone.framework.system.Nullable;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.file.KOMFileSystem;
import com.pinecone.hydra.storage.file.entity.ElementNode;
import com.pinecone.hydra.storage.file.entity.ExternalSymbolic;
import com.pinecone.hydra.storage.file.entity.FileNode;
import com.pinecone.hydra.storage.file.entity.FileTreeNode;
import com.pinecone.hydra.storage.file.entity.Folder;
import com.pinecone.hydra.storage.file.external.ExternalFile;
import com.pinecone.hydra.storage.file.external.ExternalFolder;
import com.pinecone.hydra.storage.file.query.FileChildPage;
import com.pinecone.hydra.storage.file.query.FileChildQuery;
import com.pinecone.hydra.storage.file.transfer.service.UofsTransferService;
import com.pinecone.hydra.storage.file.transfer.service.UofsTransferProgressListener;
import com.pinecone.hydra.storage.file.transfer.source.UofsTransferItemManipulator;
import com.pinecone.hydra.storage.file.transfer.source.UofsTransferTaskManipulator;
import com.pinecone.hydra.storage.file.transmit.channel.UFileChannel;
import com.pinecone.hydra.storage.file.transmit.channel.UFileOpenOption;
import com.pinecone.hydra.storage.volume.VolumeManager;
import com.pinecone.ulf.util.guid.GUIDs;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GenericUofsTransferService implements UofsTransferService {
    protected static final int            BATCH_LIMIT = 500;
    protected final KOMFileSystem         mFileSystem;
    protected final VolumeManager         mVolumeManager;
    protected final UofsTransferTaskManipulator mTaskManipulator;
    protected final UofsTransferItemManipulator mItemManipulator;
    protected final UofsTransferPathHasher mPathHasher = new UofsTransferPathHasher();
    protected final ExecutorService       mExecutor = Executors.newSingleThreadExecutor();

    public GenericUofsTransferService(
            KOMFileSystem fileSystem,
            VolumeManager volumeManager,
            UofsTransferTaskManipulator taskManipulator,
            UofsTransferItemManipulator itemManipulator
    ) {
        this.mFileSystem = fileSystem;
        this.mVolumeManager = volumeManager;
        this.mTaskManipulator = taskManipulator;
        this.mItemManipulator = itemManipulator;
    }

    @Override
    public UofsTransferPlan plan( UofsTransferRequest request ) {
        UofsTransferRequest normalized = this.normalizeRequest( request );
        UofsTransferPlan plan = new UofsTransferPlan();
        plan.setOperation( normalized.getOperation() );
        plan.setSourcePath( normalized.getSourcePath() );
        plan.setSourcePaths( normalized.getSourcePaths() );
        plan.setTargetPath( normalized.getTargetPath() );
        plan.setTargetMode( normalized.getTargetMode() );
        plan.setDirectoryPolicy( normalized.getDirectoryPolicy() );
        plan.setItemTrackingMode( normalized.getItemTrackingMode() );
        plan.setSourceCleanupPolicy( normalized.getSourceCleanupPolicy() );
        if ( normalized.getItemTrackingMode() == UofsTransferItemTrackingMode.TASK_ONLY ) {
            plan.addWarning( "Transfer item ledger is disabled. This task does not support item-level rollback or resume." );
        }
        if ( this.isBatchRequest( normalized ) && normalized.getTargetMode() != UofsTransferTargetMode.DIRECTORY ) {
            plan.addBlocker( "BATCH_TARGET_MODE_UNSUPPORTED" );
            return plan;
        }
        try {
            List<UofsTransferRootPlan> rootPlans = this.resolveRootPlans( normalized );
            plan.setRootPlans( rootPlans );
            this.validateMoveTargetBoundary( normalized, rootPlans );
            this.inspectMovePlan( plan, normalized, rootPlans );
            if ( !plan.isExecutable() ) {
                return plan;
            }
            this.addDirectoryPolicyWarnings( plan, normalized );
            if ( !plan.isExecutable() ) {
                return plan;
            }
            List<GenericUofsTransferItem> items = normalized.getOperation() == UofsTransferOperation.MOVE
                    ? this.newMoveItems( this.newTaskGuid(), normalized )
                    : this.scanItems( this.newTaskGuid(), normalized );
            long totalBytes = 0L;
            for ( GenericUofsTransferItem item : items ) {
                totalBytes += item.getTotalBytes();
            }
            plan.setTotalCount( items.size() );
            plan.setTotalBytes( totalBytes );
            if ( !items.isEmpty() ) {
                plan.setSourceType( items.get( 0 ).getSourceType() );
            }
        }
        catch ( RuntimeException e ) {
            plan.addBlocker( e.getMessage() );
        }
        return plan;
    }

    @Override
    public UofsTransferTask execute( UofsTransferRequest request ) {
        return this.execute( request, null );
    }

    @Override
    public UofsTransferTask execute( UofsTransferRequest request, @Nullable UofsTransferProgressListener progressListener ) {
        UofsTransferRequest normalized = this.normalizeRequest( request );
        GUID taskGuid = this.newTaskGuid();
        List<GenericUofsTransferItem> items = new ArrayList<>();
        UofsTransferPlan plan = this.plan( normalized );
        if ( plan.isExecutable() ) {
            this.ensureTransferTargetDirectories( normalized );
            items = normalized.getOperation() == UofsTransferOperation.MOVE
                    ? this.newMoveItems( taskGuid, normalized )
                    : this.scanItems( taskGuid, normalized );
        }
        GenericUofsTransferTask task = this.newTask( taskGuid, normalized, plan, items );
        this.mTaskManipulator.insert( task );
        if ( this.isItemLedgerMode( normalized ) && !items.isEmpty() ) {
            this.mItemManipulator.insertBatch( items );
        }
        if ( !plan.isExecutable() ) {
            this.mTaskManipulator.updateStatus( taskGuid, UofsTransferStatus.FAILED.name(), UofsTransferPhase.FAILED.name(), "Transfer plan is blocked.", null );
            return this.getTask( taskGuid );
        }
        if ( normalized.getOperation() == UofsTransferOperation.MOVE ) {
            final List<GenericUofsTransferItem> transferItems = items;
            if ( this.isMetadataMoveTask( transferItems ) ) {
                this.mExecutor.submit( () -> this.runMetadataMoveTask(
                        taskGuid,
                        transferItems,
                        normalized.getItemTrackingMode(),
                        progressListener
                ) );
            }
            else {
                this.mExecutor.submit( () -> this.runDoubleTransactionMoveTask(
                        taskGuid,
                        transferItems,
                        normalized.getItemTrackingMode(),
                        normalized.getSourceCleanupPolicy(),
                        progressListener
                ) );
            }
        }
        else if ( this.isItemLedgerMode( normalized ) ) {
            this.mExecutor.submit( () -> this.runItemLedgerTask( taskGuid, progressListener ) );
        }
        else {
            final List<GenericUofsTransferItem> transferItems = items;
            this.mExecutor.submit( () -> this.runTaskOnly( taskGuid, transferItems, progressListener ) );
        }
        return this.getTask( taskGuid );
    }

    @Override
    public UofsTransferTask getTask( GUID taskGuid ) {
        UofsTransferTask task = this.mTaskManipulator.get( taskGuid );
        if ( task == null ) {
            throw new IllegalArgumentException( "UOFS transfer task not found: " + taskGuid );
        }
        return task;
    }

    @Override
    public long countTasks( UofsTransferOperation operation, UofsTransferStatus status, GUID sourceBucketGuid, GUID targetBucketGuid ) {
        return this.mTaskManipulator.count( operation, status, sourceBucketGuid, targetBucketGuid );
    }

    @Override
    public List<? extends UofsTransferTask> listTasks(
            UofsTransferOperation operation,
            UofsTransferStatus status,
            GUID sourceBucketGuid,
            GUID targetBucketGuid,
            int offset,
            int limit
    ) {
        return this.mTaskManipulator.listPage( operation, status, sourceBucketGuid, targetBucketGuid, offset, limit );
    }

    @Override
    public List<? extends UofsTransferItem> listItems( GUID taskGuid, UofsTransferItemStatus status, int offset, int limit ) {
        return this.mItemManipulator.listByTaskGuid( taskGuid, status, offset, limit );
    }

    @Override
    public void cancel( GUID taskGuid ) {
        this.mTaskManipulator.updateStatus(
                taskGuid,
                UofsTransferStatus.CANCELED.name(),
                UofsTransferPhase.CANCELED.name(),
                "Transfer task canceled by operator.",
                null
        );
    }

    @Override
    public void purgeTask( GUID taskGuid ) {
        UofsTransferTask task = this.getTask( taskGuid );
        if ( !this.isTerminalTaskStatus( task.getStatus() ) ) {
            throw new IllegalStateException( "UOFS transfer task is not terminal. Cancel it before purging: " + taskGuid );
        }
        this.mItemManipulator.deleteByTaskGuid( taskGuid );
        this.mTaskManipulator.delete( taskGuid );
    }

    protected void runItemLedgerTask( GUID taskGuid, @Nullable UofsTransferProgressListener progressListener ) {
        try {
            this.mTaskManipulator.updateStatus( taskGuid, UofsTransferStatus.RUNNING.name(), UofsTransferPhase.COPYING_DATA.name(), "Transfer started.", null );
            List<? extends UofsTransferItem> items = this.mItemManipulator.listByTaskGuid( taskGuid, null, 0, Integer.MAX_VALUE );
            this.prepareOverwriteTargets( items );
            long doneCount = 0L;
            long failedCount = 0L;
            long doneBytes = 0L;
            long totalCount = items.size();
            long totalBytes = this.sumTotalBytes( items );
            for ( UofsTransferItem item : items ) {
                try {
                    this.ensureTaskNotCanceled( taskGuid );
                    this.executeItem( item, taskGuid, totalCount, doneCount, failedCount, totalBytes, doneBytes, true, progressListener );
                    doneCount++;
                    doneBytes += item.getTotalBytes();
                    this.ensureTaskNotCanceled( taskGuid );
                    this.updateTaskProgress( taskGuid, item, totalCount, doneCount, failedCount, totalBytes, doneBytes, "Transfer item copied.", progressListener );
                }
                catch ( UofsTransferCanceledException e ) {
                    this.finishCanceledTask( taskGuid, UofsTransferItemTrackingMode.ITEM_LEDGER, totalCount, doneCount, failedCount, totalBytes, doneBytes, progressListener );
                    return;
                }
                catch ( RuntimeException | IOException e ) {
                    failedCount++;
                    this.mItemManipulator.updateStatus(
                            item.getGuid(),
                            UofsTransferItemStatus.FAILED.name(),
                            UofsTransferPhase.FAILED.name(),
                            "Transfer item failed.",
                            e.getMessage()
                    );
                    this.mTaskManipulator.updateStatus(
                            taskGuid,
                            UofsTransferStatus.FAILED.name(),
                            UofsTransferPhase.FAILED.name(),
                            "Transfer task failed.",
                            e.getMessage()
                    );
                    return;
                }
            }
            boolean itemGcDone = this.gcTransferItems( taskGuid );
            this.mTaskManipulator.updateDone(
                    taskGuid,
                    this.snapshotResult( UofsTransferItemTrackingMode.ITEM_LEDGER, "DONE", totalCount, doneCount, failedCount, totalBytes, doneBytes, itemGcDone ),
                    "Transfer task done."
            );
            this.notifyProgress(
                    progressListener,
                    taskGuid,
                    null,
                    UofsTransferPhase.DONE,
                    totalCount,
                    doneCount,
                    failedCount,
                    totalBytes,
                    doneBytes,
                    "Transfer task done."
            );
        }
        catch ( RuntimeException e ) {
            this.mTaskManipulator.updateStatus(
                    taskGuid,
                    UofsTransferStatus.FAILED.name(),
                    UofsTransferPhase.FAILED.name(),
                    "Transfer task failed.",
                    e.getMessage()
            );
        }
    }

    protected void runTaskOnly(
            GUID taskGuid,
            List<GenericUofsTransferItem> items,
            @Nullable UofsTransferProgressListener progressListener
    ) {
        try {
            this.mTaskManipulator.updateStatus( taskGuid, UofsTransferStatus.RUNNING.name(), UofsTransferPhase.COPYING_DATA.name(), "Transfer started.", null );
            this.prepareOverwriteTargets( items );
            long doneCount = 0L;
            long failedCount = 0L;
            long doneBytes = 0L;
            long totalCount = items.size();
            long totalBytes = this.sumTotalBytes( items );
            for ( UofsTransferItem item : items ) {
                try {
                    this.ensureTaskNotCanceled( taskGuid );
                    this.executeItem( item, taskGuid, totalCount, doneCount, failedCount, totalBytes, doneBytes, false, progressListener );
                    doneCount++;
                    doneBytes += item.getTotalBytes();
                    this.ensureTaskNotCanceled( taskGuid );
                    this.updateTaskProgress( taskGuid, item, totalCount, doneCount, failedCount, totalBytes, doneBytes, "Transfer item copied.", progressListener );
                }
                catch ( UofsTransferCanceledException e ) {
                    this.finishCanceledTask( taskGuid, UofsTransferItemTrackingMode.TASK_ONLY, totalCount, doneCount, failedCount, totalBytes, doneBytes, progressListener );
                    return;
                }
                catch ( RuntimeException | IOException e ) {
                    failedCount++;
                    this.mTaskManipulator.updateStatus(
                            taskGuid,
                            UofsTransferStatus.FAILED.name(),
                            UofsTransferPhase.FAILED.name(),
                            "Transfer task failed.",
                            e.getMessage()
                    );
                    return;
                }
            }
            this.mTaskManipulator.updateDone(
                    taskGuid,
                    this.snapshotResult( UofsTransferItemTrackingMode.TASK_ONLY, "DONE", totalCount, doneCount, failedCount, totalBytes, doneBytes, true ),
                    "Transfer task done."
            );
            this.notifyProgress(
                    progressListener,
                    taskGuid,
                    null,
                    UofsTransferPhase.DONE,
                    totalCount,
                    doneCount,
                    failedCount,
                    totalBytes,
                    doneBytes,
                    "Transfer task done."
            );
        }
        catch ( RuntimeException e ) {
            this.mTaskManipulator.updateStatus(
                    taskGuid,
                    UofsTransferStatus.FAILED.name(),
                    UofsTransferPhase.FAILED.name(),
                    "Transfer task failed.",
                    e.getMessage()
            );
        }
    }

    protected void runMetadataMoveTask(
            GUID taskGuid,
            List<GenericUofsTransferItem> items,
            UofsTransferItemTrackingMode itemTrackingMode,
            @Nullable UofsTransferProgressListener progressListener
    ) {
        long totalCount = items.size();
        long totalBytes = this.sumTotalBytes( items );
        long doneCount = 0L;
        long doneBytes = 0L;
        long failedCount = 0L;
        try {
            this.mTaskManipulator.updateStatus(
                    taskGuid,
                    UofsTransferStatus.RUNNING.name(),
                    UofsTransferPhase.MOVING_METADATA.name(),
                    "Metadata move started.",
                    null
            );
            if ( items.isEmpty() ) {
                throw new IllegalStateException( "UOFS metadata move task has no item: " + taskGuid );
            }
            for ( UofsTransferItem item : items ) {
                this.ensureTaskNotCanceled( taskGuid );
                if ( itemTrackingMode == UofsTransferItemTrackingMode.ITEM_LEDGER ) {
                    this.mItemManipulator.updateProgress(
                            item.getGuid(),
                            UofsTransferPhase.MOVING_METADATA.name(),
                            0L,
                            "Moving metadata."
                    );
                }
                this.prepareOverwriteTarget( item );
                this.mFileSystem.relocateNode(
                        item.getSourceGuid(),
                        item.getTargetParentGuid(),
                        this.fileName( item.getTargetPath() )
                );
                this.mTaskManipulator.updateStatus(
                        taskGuid,
                        UofsTransferStatus.RUNNING.name(),
                        UofsTransferPhase.VERIFYING.name(),
                        "Verifying metadata move.",
                        null
                );
                ElementNode targetNode = this.verifyMetadataMove( item );
                GUID targetGuid = targetNode.getGuid();
                if ( itemTrackingMode == UofsTransferItemTrackingMode.ITEM_LEDGER ) {
                    this.mItemManipulator.updateDone( item.getGuid(), targetGuid, "Metadata moved." );
                }
                doneCount++;
                this.updateTaskProgress(
                        taskGuid,
                        item,
                        UofsTransferPhase.MOVING_METADATA,
                        totalCount,
                        doneCount,
                        failedCount,
                        totalBytes,
                        doneBytes,
                        "Metadata moved.",
                        progressListener
                );
            }
            boolean itemGcDone = itemTrackingMode != UofsTransferItemTrackingMode.ITEM_LEDGER || this.gcTransferItems( taskGuid );
            this.mTaskManipulator.updateDone(
                    taskGuid,
                    this.snapshotResult( itemTrackingMode, "DONE", totalCount, doneCount, failedCount, totalBytes, doneBytes, itemGcDone ),
                    "Metadata move task done."
            );
            this.notifyProgress(
                    progressListener,
                    taskGuid,
                    null,
                    UofsTransferPhase.DONE,
                    totalCount,
                    doneCount,
                    failedCount,
                    totalBytes,
                    doneBytes,
                    "Metadata move task done."
            );
        }
        catch ( UofsTransferCanceledException e ) {
            this.finishCanceledTask( taskGuid, itemTrackingMode, totalCount, doneCount, failedCount, totalBytes, doneBytes, progressListener );
        }
        catch ( RuntimeException e ) {
            failedCount = 1L;
            this.mTaskManipulator.updateStatus(
                    taskGuid,
                    UofsTransferStatus.FAILED.name(),
                    UofsTransferPhase.FAILED.name(),
                    "Metadata move task failed.",
                    e.getMessage()
            );
        }
    }

    protected void runDoubleTransactionMoveTask(
            GUID taskGuid,
            List<GenericUofsTransferItem> items,
            UofsTransferItemTrackingMode itemTrackingMode,
            UofsTransferSourceCleanupPolicy sourceCleanupPolicy,
            @Nullable UofsTransferProgressListener progressListener
    ) {
        TransferRunStats stats = new TransferRunStats( items.size(), this.sumTotalBytes( items ) );
        boolean itemLedgerMode = itemTrackingMode == UofsTransferItemTrackingMode.ITEM_LEDGER;
        try {
            if ( items.isEmpty() ) {
                throw new IllegalStateException( "UOFS move task has no transfer item: " + taskGuid );
            }
            this.assertNoMetadataMoveItemsInCopyDelete( items );
            this.mTaskManipulator.updateStatus(
                    taskGuid,
                    UofsTransferStatus.RUNNING.name(),
                    UofsTransferPhase.COPYING_DATA.name(),
                    "Move copy transaction started.",
                    null
            );
            this.prepareOverwriteTargets( items );
            this.copyTransferItems( taskGuid, items, stats, itemLedgerMode, progressListener );
            this.ensureTaskNotCanceled( taskGuid );
            this.mTaskManipulator.updateStatus(
                    taskGuid,
                    UofsTransferStatus.RUNNING.name(),
                    UofsTransferPhase.VERIFYING.name(),
                    "Move verify barrier started.",
                    null
            );
            this.verifyTransferItems( taskGuid, items, stats, progressListener );
            this.ensureTaskNotCanceled( taskGuid );
            this.mTaskManipulator.updateStatus(
                    taskGuid,
                    UofsTransferStatus.RUNNING.name(),
                    UofsTransferPhase.CLEANING_SOURCE.name(),
                    "Move source cleanup started.",
                    null
            );
            this.notifyProgress(
                    progressListener,
                    taskGuid,
                    null,
                    UofsTransferPhase.CLEANING_SOURCE,
                    stats.getTotalCount(),
                    stats.getDoneCount(),
                    stats.getFailedCount(),
                    stats.getTotalBytes(),
                    stats.getDoneBytes(),
                    "Move source cleanup started."
            );
            this.cleanMoveSources( items, sourceCleanupPolicy );
            boolean itemGcDone = !itemLedgerMode || this.gcTransferItems( taskGuid );
            this.mTaskManipulator.updateDone(
                    taskGuid,
                    this.snapshotResult(
                            itemTrackingMode,
                            "DONE",
                            stats.getTotalCount(),
                            stats.getDoneCount(),
                            stats.getFailedCount(),
                            stats.getTotalBytes(),
                            stats.getDoneBytes(),
                            itemGcDone
                    ),
                    "Move task done."
            );
            this.notifyProgress(
                    progressListener,
                    taskGuid,
                    null,
                    UofsTransferPhase.DONE,
                    stats.getTotalCount(),
                    stats.getDoneCount(),
                    stats.getFailedCount(),
                    stats.getTotalBytes(),
                    stats.getDoneBytes(),
                    "Move task done."
            );
        }
        catch ( UofsTransferCanceledException e ) {
            this.finishCanceledTask(
                    taskGuid,
                    itemTrackingMode,
                    stats.getTotalCount(),
                    stats.getDoneCount(),
                    stats.getFailedCount(),
                    stats.getTotalBytes(),
                    stats.getDoneBytes(),
                    progressListener
            );
        }
        catch ( MoveVerifyException e ) {
            this.mTaskManipulator.updateStatus(
                    taskGuid,
                    UofsTransferStatus.FAILED.name(),
                    UofsTransferPhase.VERIFYING.name(),
                    "Move verify barrier failed. Source is retained.",
                    e.getMessage()
            );
        }
        catch ( MoveCleanupException e ) {
            this.mTaskManipulator.updateStatus(
                    taskGuid,
                    UofsTransferStatus.FAILED.name(),
                    UofsTransferPhase.CLEANING_SOURCE.name(),
                    "Move source cleanup failed. Target copy is retained.",
                    e.getMessage()
            );
        }
        catch ( RuntimeException | IOException e ) {
            this.mTaskManipulator.updateStatus(
                    taskGuid,
                    UofsTransferStatus.FAILED.name(),
                    UofsTransferPhase.FAILED.name(),
                    "Move copy transaction failed. Source is retained.",
                    e.getMessage()
            );
        }
    }

    protected void copyTransferItems(
            GUID taskGuid,
            List<? extends UofsTransferItem> items,
            TransferRunStats stats,
            boolean itemLedgerMode,
            @Nullable UofsTransferProgressListener progressListener
    ) throws IOException {
        for ( UofsTransferItem item : items ) {
            try {
                this.ensureTaskNotCanceled( taskGuid );
                this.executeItem(
                        item,
                        taskGuid,
                        stats.getTotalCount(),
                        stats.getDoneCount(),
                        stats.getFailedCount(),
                        stats.getTotalBytes(),
                        stats.getDoneBytes(),
                        itemLedgerMode,
                        progressListener
                );
                stats.addDone( item.getTotalBytes() );
                this.ensureTaskNotCanceled( taskGuid );
                this.updateTaskProgress(
                        taskGuid,
                        item,
                        stats.getTotalCount(),
                        stats.getDoneCount(),
                        stats.getFailedCount(),
                        stats.getTotalBytes(),
                        stats.getDoneBytes(),
                        "Move copy item copied.",
                        progressListener
                );
            }
            catch ( UofsTransferCanceledException e ) {
                throw e;
            }
            catch ( RuntimeException | IOException e ) {
                stats.addFailed();
                if ( itemLedgerMode ) {
                    this.mItemManipulator.updateStatus(
                            item.getGuid(),
                            UofsTransferItemStatus.FAILED.name(),
                            UofsTransferPhase.FAILED.name(),
                            "Move copy item failed.",
                            e.getMessage()
                    );
                }
                throw e;
            }
        }
    }

    protected void assertNoMetadataMoveItemsInCopyDelete( List<? extends UofsTransferItem> items ) {
        for ( UofsTransferItem item : items ) {
            if ( item.getPhase() == UofsTransferPhase.MOVING_METADATA ) {
                throw new IllegalStateException( "UOFS metadata move item should not enter copy-delete move path: " + item.getSourcePath() );
            }
        }
    }

    protected void executeItem(
            UofsTransferItem item,
            GUID taskGuid,
            long totalCount,
            long doneCount,
            long failedCount,
            long totalBytes,
            long doneBytes,
            boolean itemLedgerMode,
            @Nullable UofsTransferProgressListener progressListener
    ) throws IOException {
        if ( item.getSourceType() == UofsTransferSourceType.UOFS_FOLDER || item.getSourceType() == UofsTransferSourceType.NATIVE_EXTERNAL_FOLDER ) {
            ElementNode folder = this.mFileSystem.affirmFolderElement( item.getTargetPath() );
            this.ensureTaskNotCanceled( taskGuid );
            if ( itemLedgerMode ) {
                this.mItemManipulator.updateDone( item.getGuid(), folder.getGuid(), "Folder metadata created." );
            }
            this.updateTaskProgress( taskGuid, item, totalCount, doneCount, failedCount, totalBytes, doneBytes, "Folder metadata created.", progressListener );
            return;
        }
        if ( item.getSourceType() != UofsTransferSourceType.UOFS_FILE && item.getSourceType() != UofsTransferSourceType.NATIVE_EXTERNAL_FILE ) {
            throw new IllegalArgumentException( "Unsupported UOFS transfer item source type: " + item.getSourceType() );
        }
        this.copyFile( item, taskGuid, totalCount, doneCount, failedCount, totalBytes, doneBytes, itemLedgerMode, progressListener );
    }

    protected void copyFile(
            UofsTransferItem item,
            GUID taskGuid,
            long totalCount,
            long doneCount,
            long failedCount,
            long totalBytes,
            long doneBytes,
            boolean itemLedgerMode,
            @Nullable UofsTransferProgressListener progressListener
    ) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate( 1024 * 1024 );
        long itemDoneBytes = 0L;
        try ( UFileChannel sourceChannel = this.mFileSystem.open( item.getSourcePath(), UFileOpenOption.READ, this.mVolumeManager );
              UFileChannel targetChannel = this.mFileSystem.open( item.getTargetPath(), UFileOpenOption.CREATE_OVERWRITE, this.mVolumeManager ) ) {
            while ( true ) {
                this.ensureTaskNotCanceled( taskGuid );
                buffer.clear();
                int read = sourceChannel.read( buffer );
                if ( read < 0 ) {
                    break;
                }
                if ( read == 0 ) {
                    break;
                }
                buffer.flip();
                while ( buffer.hasRemaining() ) {
                    int written = targetChannel.write( buffer );
                    itemDoneBytes += written;
                }
                if ( itemLedgerMode ) {
                    this.mItemManipulator.updateProgress( item.getGuid(), UofsTransferPhase.COPYING_DATA.name(), itemDoneBytes, "Copying file data." );
                }
                this.ensureTaskNotCanceled( taskGuid );
                this.updateTaskProgress(
                        taskGuid,
                        item,
                        totalCount,
                        doneCount,
                        failedCount,
                        totalBytes,
                        doneBytes + itemDoneBytes,
                        "Copying file data.",
                        progressListener
                );
            }
        }
        GUID targetGuid = null;
        ElementNode targetNode = this.mFileSystem.queryElement( item.getTargetPath() );
        if ( targetNode != null ) {
            targetGuid = targetNode.getGuid();
        }
        if ( itemLedgerMode ) {
            this.mItemManipulator.updateDone( item.getGuid(), targetGuid, "File copied." );
        }
    }

    protected void ensureTaskNotCanceled( GUID taskGuid ) {
        UofsTransferTask task = this.mTaskManipulator.get( taskGuid );
        if ( task == null ) {
            return;
        }
        if ( task.getStatus() == UofsTransferStatus.CANCELED ) {
            throw new UofsTransferCanceledException();
        }
    }

    protected void finishCanceledTask(
            GUID taskGuid,
            UofsTransferItemTrackingMode itemTrackingMode,
            long totalCount,
            long doneCount,
            long failedCount,
            long totalBytes,
            long doneBytes,
            @Nullable UofsTransferProgressListener progressListener
    ) {
        String message = "Transfer task canceled by operator.";
        if ( itemTrackingMode == UofsTransferItemTrackingMode.ITEM_LEDGER ) {
            message = "Transfer task canceled by operator. Transfer item ledger retained for rollback or diagnosis.";
        }
        this.mTaskManipulator.updateStatus(
                taskGuid,
                UofsTransferStatus.CANCELED.name(),
                UofsTransferPhase.CANCELED.name(),
                message,
                null
        );
        this.notifyProgress(
                progressListener,
                taskGuid,
                null,
                UofsTransferPhase.CANCELED,
                totalCount,
                doneCount,
                failedCount,
                totalBytes,
                doneBytes,
                message
        );
    }

    protected ElementNode verifyMetadataMove( UofsTransferItem item ) {
        ElementNode targetNode = this.mFileSystem.queryElement( item.getTargetPath() );
        if ( targetNode == null ) {
            throw new MoveVerifyException( "UOFS metadata move target not found: " + item.getTargetPath() );
        }
        if ( item.getSourceGuid() != null && !item.getSourceGuid().equals( targetNode.getGuid() ) ) {
            throw new MoveVerifyException( "UOFS metadata move target GUID mismatch: " + item.getTargetPath() );
        }
        ElementNode oldSourceNode = this.mFileSystem.queryElement( item.getSourcePath() );
        if ( oldSourceNode != null && item.getSourceGuid() != null && item.getSourceGuid().equals( oldSourceNode.getGuid() ) ) {
            throw new MoveVerifyException( "UOFS metadata move source path still resolves to source GUID: " + item.getSourcePath() );
        }
        return targetNode;
    }

    protected void verifyTransferItems(
            GUID taskGuid,
            List<? extends UofsTransferItem> items,
            TransferRunStats stats,
            @Nullable UofsTransferProgressListener progressListener
    ) {
        for ( UofsTransferItem item : items ) {
            this.ensureTaskNotCanceled( taskGuid );
            this.verifyTransferItem( item );
            this.notifyProgress(
                    progressListener,
                    taskGuid,
                    item.getGuid(),
                    UofsTransferPhase.VERIFYING,
                    stats.getTotalCount(),
                    stats.getDoneCount(),
                    stats.getFailedCount(),
                    stats.getTotalBytes(),
                    stats.getDoneBytes(),
                    "Move target item verified."
            );
        }
    }

    protected void verifyTransferItem( UofsTransferItem item ) {
        ElementNode targetNode = this.mFileSystem.queryElement( item.getTargetPath() );
        if ( targetNode == null || this.isMissingExternalFile( targetNode ) ) {
            throw new MoveVerifyException( "UOFS move target not found after copy: " + item.getTargetPath() );
        }
        if ( item.getSourceType() == UofsTransferSourceType.UOFS_FOLDER
                || item.getSourceType() == UofsTransferSourceType.NATIVE_EXTERNAL_FOLDER ) {
            if ( targetNode.evinceFolder() == null && !( targetNode instanceof ExternalFolder ) ) {
                throw new MoveVerifyException( "UOFS move target should be folder: " + item.getTargetPath() );
            }
            return;
        }
        if ( item.getSourceType() == UofsTransferSourceType.UOFS_FILE
                || item.getSourceType() == UofsTransferSourceType.NATIVE_EXTERNAL_FILE ) {
            if ( !( targetNode instanceof FileTreeNode ) ) {
                throw new MoveVerifyException( "UOFS move target should be file tree node: " + item.getTargetPath() );
            }
            long targetSize = this.nodeSize( (FileTreeNode) targetNode );
            if ( targetSize != item.getTotalBytes() ) {
                throw new MoveVerifyException( "UOFS move target size mismatch: " + item.getTargetPath() );
            }
            return;
        }
        throw new MoveVerifyException( "Unsupported UOFS move verify source type: " + item.getSourceType() );
    }

    protected void validateMoveTargetBoundary( UofsTransferRequest request, List<UofsTransferRootPlan> rootPlans ) {
        if ( request.getOperation() != UofsTransferOperation.MOVE ) {
            return;
        }
        for ( UofsTransferRootPlan rootPlan : rootPlans ) {
            String sourcePath = this.normalizePath( rootPlan.getSourcePath() );
            if ( "/".equals( sourcePath ) ) {
                throw new IllegalArgumentException( "UOFS move source should not be root path." );
            }
            String effectiveTargetPath = this.normalizePath( rootPlan.getFinalTargetPath() );
            if ( effectiveTargetPath.equals( sourcePath ) ) {
                throw new IllegalArgumentException( "UOFS move target should not be source itself or under source: " + effectiveTargetPath );
            }
            if ( effectiveTargetPath.startsWith( sourcePath + "/" ) ) {
                throw new IllegalArgumentException( "UOFS move target should not be source itself or under source: " + effectiveTargetPath );
            }
        }
    }

    protected void cleanMoveSource( UofsTransferItem rootItem, UofsTransferSourceCleanupPolicy sourceCleanupPolicy ) {
        try {
            this.ensureMoveSourceCleanupAllowed( rootItem, sourceCleanupPolicy );
            this.mFileSystem.remove( rootItem.getSourcePath(), this.mVolumeManager );
            ElementNode sourceNode = this.mFileSystem.queryElement( rootItem.getSourcePath() );
            if ( sourceNode == null || this.isMissingExternalFile( sourceNode ) ) {
                return;
            }
            if ( rootItem.getSourceGuid() == null || rootItem.getSourceGuid().equals( sourceNode.getGuid() ) ) {
                throw new MoveCleanupException( "UOFS move source still exists after cleanup: " + rootItem.getSourcePath() );
            }
        }
        catch ( MoveCleanupException e ) {
            throw e;
        }
        catch ( RuntimeException e ) {
            throw new MoveCleanupException( e.getMessage(), e );
        }
    }

    protected void cleanMoveSources( List<GenericUofsTransferItem> items, UofsTransferSourceCleanupPolicy sourceCleanupPolicy ) {
        for ( UofsTransferItem item : this.rootItems( items ) ) {
            this.cleanMoveSource( item, sourceCleanupPolicy );
        }
    }

    protected List<UofsTransferItem> rootItems( List<? extends UofsTransferItem> items ) {
        List<UofsTransferItem> ret = new ArrayList<>();
        for ( UofsTransferItem item : items ) {
            if ( item.getDepth() == 0 ) {
                ret.add( item );
            }
        }
        return ret;
    }

    protected void prepareOverwriteTargets( List<? extends UofsTransferItem> items ) {
        for ( UofsTransferItem item : this.rootItems( items ) ) {
            this.prepareOverwriteTarget( item );
        }
    }

    protected void prepareOverwriteTarget( UofsTransferItem item ) {
        ElementNode target = this.mFileSystem.queryElement( item.getTargetPath() );
        if ( target == null || this.isMissingExternalFile( target ) ) {
            return;
        }
        if ( item.getSourceGuid() != null && item.getSourceGuid().equals( target.getGuid() ) ) {
            throw new IllegalArgumentException( "UOFS transfer overwrite target should not be source itself: " + item.getTargetPath() );
        }
        this.mFileSystem.remove( item.getTargetPath(), this.mVolumeManager );
        ElementNode existing = this.mFileSystem.queryElement( item.getTargetPath() );
        if ( existing != null && !this.isMissingExternalFile( existing ) ) {
            throw new IllegalStateException( "UOFS transfer overwrite target still exists after cleanup: " + item.getTargetPath() );
        }
    }

    protected List<GenericUofsTransferItem> scanItems( GUID taskGuid, UofsTransferRequest request ) {
        return this.scanItems( taskGuid, request, this.resolveRootPlans( request ) );
    }

    protected List<GenericUofsTransferItem> scanItems(
            GUID taskGuid,
            UofsTransferRequest request,
            List<UofsTransferRootPlan> rootPlans
    ) {
        if ( this.isBatchRequest( request ) ) {
            return this.scanBatchDirectoryTargetItems( taskGuid, request, rootPlans );
        }
        List<GenericUofsTransferItem> ret = new ArrayList<>();
        FileTreeNode source = this.sourceNode( request.getSourcePath() );
        if ( request.getTargetMode() == UofsTransferTargetMode.EXACT_PATH ) {
            this.scanExactPathTargetItems( ret, taskGuid, source, request, rootPlans.get( 0 ) );
            return ret;
        }
        this.scanDirectoryTargetItems( ret, taskGuid, source, request, rootPlans.get( 0 ) );
        return ret;
    }

    protected List<GenericUofsTransferItem> scanBatchDirectoryTargetItems(
            GUID taskGuid,
            UofsTransferRequest request,
            List<UofsTransferRootPlan> rootPlans
    ) {
        if ( request.getTargetMode() != UofsTransferTargetMode.DIRECTORY ) {
            throw new IllegalArgumentException( "UOFS transfer batch source only supports directory target mode." );
        }
        List<GenericUofsTransferItem> ret = new ArrayList<>();
        GUID targetBucketGuid = this.resolveTransferFolderBucketGuid(
                request.getTargetPath(),
                request.getDirectoryPolicy(),
                "UOFS transfer target should be a folder: "
        );
        for ( UofsTransferRootPlan rootPlan : rootPlans ) {
            String sourcePath = rootPlan.getSourcePath();
            FileTreeNode source = this.sourceNode( sourcePath );
            String targetPath = rootPlan.getFinalTargetPath();
            this.scanNode( ret, taskGuid, source, sourcePath, targetPath, targetBucketGuid, 0 );
        }
        return ret;
    }

    protected List<GenericUofsTransferItem> newMoveItems( GUID taskGuid, UofsTransferRequest request ) {
        return this.newMoveItems( taskGuid, request, this.resolveRootPlans( request ) );
    }

    protected List<GenericUofsTransferItem> newMoveItems(
            GUID taskGuid,
            UofsTransferRequest request,
            List<UofsTransferRootPlan> rootPlans
    ) {
        List<GenericUofsTransferItem> metadataItems = this.newMetadataMoveItems( taskGuid, request, rootPlans );
        if ( metadataItems != null ) {
            return metadataItems;
        }
        return this.scanItems( taskGuid, request, rootPlans );
    }

    protected List<GenericUofsTransferItem> newMetadataMoveItems(
            GUID taskGuid,
            UofsTransferRequest request,
            List<UofsTransferRootPlan> rootPlans
    ) {
        List<GenericUofsTransferItem> ret = new ArrayList<>();
        for ( UofsTransferRootPlan rootPlan : rootPlans ) {
            FileTreeNode source = this.sourceNode( rootPlan.getSourcePath() );
            MetadataMoveTarget target = this.resolveMetadataMoveTarget( source, rootPlan );
            if ( target == null ) {
                return null;
            }
            GenericUofsTransferItem item = this.newItem(
                    taskGuid,
                    ret.size(),
                    source,
                    rootPlan.getSourcePath(),
                    target.getTargetPath(),
                    target.getBucketGuid(),
                    0
            );
            item.setPhase( UofsTransferPhase.MOVING_METADATA );
            item.setTotalBytes( 0L );
            item.setTargetParentGuid( target.getParentGuid() );
            item.setTargetGuid( item.getSourceGuid() );
            item.setMessage( "Metadata move item prepared." );
            ret.add( item );
        }
        return ret;
    }

    protected MetadataMoveTarget resolveMetadataMoveTarget( FileTreeNode source, UofsTransferRootPlan rootPlan ) {
        UofsTransferSourceType sourceType = this.sourceType( source );
        if ( sourceType != UofsTransferSourceType.UOFS_FILE && sourceType != UofsTransferSourceType.UOFS_FOLDER ) {
            return null;
        }
        if ( !( source instanceof ElementNode ) ) {
            return null;
        }
        ElementNode sourceElement = (ElementNode) source;
        String targetFolderPath = this.parentPath( rootPlan.getFinalTargetPath() );
        TransferTargetFolder targetFolder = this.resolveTransferFolder(
                targetFolderPath,
                UofsTransferDirectoryPolicy.CREATE_MISSING,
                "UOFS metadata move target should be a folder: "
        );
        if ( targetFolder.getNode() != null && !( targetFolder.getNode() instanceof Folder ) ) {
            return null;
        }
        if ( sourceElement.getBucketGuid() == null || targetFolder.getBucketGuid() == null ) {
            return null;
        }
        if ( !sourceElement.getBucketGuid().equals( targetFolder.getBucketGuid() ) ) {
            return null;
        }
        GUID parentGuid = targetFolder.getNode() == null ? null : targetFolder.getNode().getGuid();
        return new MetadataMoveTarget( rootPlan.getFinalTargetPath(), parentGuid, targetFolder.getBucketGuid() );
    }

    protected void scanDirectoryTargetItems(
            List<GenericUofsTransferItem> ret,
            GUID taskGuid,
            FileTreeNode source,
            UofsTransferRequest request,
            UofsTransferRootPlan rootPlan
    ) {
        GUID targetBucketGuid = this.resolveTransferFolderBucketGuid(
                request.getTargetPath(),
                request.getDirectoryPolicy(),
                "UOFS transfer target should be a folder: "
        );
        String targetPath = rootPlan.getFinalTargetPath();
        this.scanNode( ret, taskGuid, source, request.getSourcePath(), targetPath, targetBucketGuid, 0 );
    }

    protected void scanExactPathTargetItems(
            List<GenericUofsTransferItem> ret,
            GUID taskGuid,
            FileTreeNode source,
            UofsTransferRequest request,
            UofsTransferRootPlan rootPlan
    ) {
        UofsTransferSourceType sourceType = this.sourceType( source );
        if ( sourceType == UofsTransferSourceType.UOFS_FOLDER || sourceType == UofsTransferSourceType.NATIVE_EXTERNAL_FOLDER ) {
            throw new IllegalArgumentException( "UOFS transfer exact target only supports file source: " + request.getSourcePath() );
        }
        GUID targetBucketGuid = this.resolveTransferFolderBucketGuid(
                this.parentPath( request.getTargetPath() ),
                request.getDirectoryPolicy(),
                "UOFS transfer exact target parent should be a folder: "
        );
        this.scanNode( ret, taskGuid, source, request.getSourcePath(), rootPlan.getFinalTargetPath(), targetBucketGuid, 0 );
    }

    protected void addDirectoryPolicyWarnings( UofsTransferPlan plan, UofsTransferRequest request ) {
        if ( request.getDirectoryPolicy() != UofsTransferDirectoryPolicy.CREATE_MISSING ) {
            return;
        }
        String folderPath = this.transferTargetFolderPath( request );
        TransferTargetFolder targetFolder = this.resolveTransferFolder(
                folderPath,
                request.getDirectoryPolicy(),
                request.getTargetMode() == UofsTransferTargetMode.EXACT_PATH
                        ? "UOFS transfer exact target parent should be a folder: "
                        : "UOFS transfer target should be a folder: "
        );
        if ( !targetFolder.isMissing() ) {
            return;
        }
        if ( request.getTargetMode() == UofsTransferTargetMode.EXACT_PATH ) {
            plan.addWarning( "UOFS transfer exact target parent directory will be created: " + folderPath );
            return;
        }
        plan.addWarning( "UOFS transfer target directory will be created: " + folderPath );
    }

    protected void ensureTransferTargetDirectories( UofsTransferRequest request ) {
        if ( request.getDirectoryPolicy() != UofsTransferDirectoryPolicy.CREATE_MISSING ) {
            return;
        }
        String folderPath = this.transferTargetFolderPath( request );
        TransferTargetFolder targetFolder = this.resolveTransferFolder(
                folderPath,
                request.getDirectoryPolicy(),
                request.getTargetMode() == UofsTransferTargetMode.EXACT_PATH
                        ? "UOFS transfer exact target parent should be a folder: "
                        : "UOFS transfer target should be a folder: "
        );
        if ( !targetFolder.isMissing() ) {
            return;
        }
        this.mFileSystem.affirmFolderElement( folderPath );
    }

    protected GUID resolveTransferFolderBucketGuid(
            String folderPath,
            UofsTransferDirectoryPolicy directoryPolicy,
            String failureMessagePrefix
    ) {
        TransferTargetFolder targetFolder = this.resolveTransferFolder( folderPath, directoryPolicy, failureMessagePrefix );
        if ( targetFolder.getBucketGuid() == null ) {
            throw new IllegalArgumentException( "UOFS transfer target cannot be created outside bucket boundary: " + targetFolder.getPath() );
        }
        return targetFolder.getBucketGuid();
    }

    protected TransferTargetFolder resolveTransferFolder(
            String folderPath,
            UofsTransferDirectoryPolicy directoryPolicy,
            String failureMessagePrefix
    ) {
        String normalized = this.normalizePath( folderPath );
        this.validateCreatableTransferPath( normalized );
        ElementNode folder = this.mFileSystem.queryElement( normalized );
        if ( folder != null ) {
            if ( this.isMissingExternalFile( folder ) ) {
                if ( directoryPolicy != UofsTransferDirectoryPolicy.CREATE_MISSING ) {
                    throw new IllegalArgumentException( failureMessagePrefix + normalized );
                }
                ElementNode ancestor = this.nearestExistingTransferFolder( normalized, failureMessagePrefix );
                return new TransferTargetFolder( normalized, null, this.resolveTransferBucketGuid( normalized, ancestor ), true );
            }
            if ( this.isTransferFolder( normalized, folder, failureMessagePrefix ) ) {
                return new TransferTargetFolder( normalized, folder, this.resolveTransferBucketGuid( normalized, folder ), false );
            }
            throw new IllegalArgumentException( failureMessagePrefix + normalized );
        }
        if ( directoryPolicy != UofsTransferDirectoryPolicy.CREATE_MISSING ) {
            throw new IllegalArgumentException( failureMessagePrefix + normalized );
        }
        ElementNode ancestor = this.nearestExistingTransferFolder( normalized, failureMessagePrefix );
        return new TransferTargetFolder( normalized, null, this.resolveTransferBucketGuid( normalized, ancestor ), true );
    }

    protected ElementNode nearestExistingTransferFolder( String folderPath, String failureMessagePrefix ) {
        String cursor = folderPath;
        while ( !"/".equals( cursor ) ) {
            cursor = this.parentPath( cursor );
            ElementNode node = this.mFileSystem.queryElement( cursor );
            if ( node == null ) {
                continue;
            }
            if ( this.isMissingExternalFile( node ) ) {
                continue;
            }
            if ( !this.isTransferFolder( cursor, node, failureMessagePrefix ) ) {
                throw new IllegalArgumentException( failureMessagePrefix + cursor );
            }
            return node;
        }
        ElementNode root = this.mFileSystem.queryElement( "/" );
        if ( root != null && root.evinceFolder() != null ) {
            return root;
        }
        throw new IllegalArgumentException( failureMessagePrefix + folderPath );
    }

    protected boolean isTransferFolder( String path, ElementNode node, String failureMessagePrefix ) {
        if ( node.evinceFolder() != null ) {
            return true;
        }
        if ( node instanceof ExternalFolder ) {
            return ( (ExternalFolder) node ).getNativeFile().isDirectory();
        }
        if ( node instanceof ExternalSymbolic ) {
            try {
                ElementNode externalNode = this.mFileSystem.affirmFolderElement( path );
                return externalNode != null && ( externalNode instanceof ExternalFolder || externalNode.evinceFolder() != null );
            }
            catch ( RuntimeException e ) {
                throw new IllegalArgumentException( failureMessagePrefix + path, e );
            }
        }
        return false;
    }

    protected boolean isMissingExternalFile( ElementNode node ) {
        return node instanceof ExternalFile && !( (ExternalFile) node ).exists();
    }

    protected GUID resolveTransferBucketGuid( String path, ElementNode node ) {
        if ( node != null && node.getBucketGuid() != null ) {
            return node.getBucketGuid();
        }
        String cursor = this.normalizePath( path );
        while ( true ) {
            ElementNode cursorNode = this.mFileSystem.queryElement( cursor );
            if ( cursorNode != null && cursorNode.getBucketGuid() != null ) {
                return cursorNode.getBucketGuid();
            }
            if ( "/".equals( cursor ) ) {
                return null;
            }
            cursor = this.parentPath( cursor );
        }
    }

    protected void validateCreatableTransferPath( String folderPath ) {
        String normalized = this.normalizePath( folderPath );
        if ( !normalized.startsWith( "/" ) ) {
            throw new IllegalArgumentException( "UOFS transfer target should be an absolute path: " + normalized );
        }
        String[] segments = normalized.split( "/" );
        for ( String segment : segments ) {
            if ( segment.isEmpty() ) {
                continue;
            }
            if ( ".".equals( segment ) || "..".equals( segment ) ) {
                throw new IllegalArgumentException( "UOFS transfer target path contains illegal segment: " + normalized );
            }
        }
    }

    protected String transferTargetFolderPath( UofsTransferRequest request ) {
        if ( request.getTargetMode() == UofsTransferTargetMode.EXACT_PATH ) {
            return this.parentPath( request.getTargetPath() );
        }
        return request.getTargetPath();
    }

    protected void validateTargetConflict( String targetPath, UofsTransferConflictPolicy conflictPolicy ) {
        if ( conflictPolicy != UofsTransferConflictPolicy.FAIL ) {
            return;
        }
        ElementNode target = this.mFileSystem.queryElement( targetPath );
        if ( target != null ) {
            if ( this.isMissingExternalFile( target ) ) {
                return;
            }
            throw new IllegalArgumentException( "UOFS transfer target already exists: " + targetPath );
        }
    }

    protected void scanNode(
            List<GenericUofsTransferItem> items,
            GUID taskGuid,
            FileTreeNode source,
            String sourcePath,
            String targetPath,
            GUID targetBucketGuid,
            int depth
    ) {
        GenericUofsTransferItem item = this.newItem( taskGuid, items.size(), source, sourcePath, targetPath, targetBucketGuid, depth );
        items.add( item );
        if ( item.getSourceType() != UofsTransferSourceType.UOFS_FOLDER && item.getSourceType() != UofsTransferSourceType.NATIVE_EXTERNAL_FOLDER ) {
            return;
        }
        int offset = 0;
        while ( true ) {
            FileChildQuery query = new FileChildQuery();
            query.setOffset( offset );
            query.setLimit( FileChildQuery.MaxLimit );
            FileChildPage page = this.mFileSystem.fetchChildren( sourcePath, query );
            for ( FileTreeNode child : page.getRecords() ) {
                String childSourcePath = this.joinPath( sourcePath, child.getName() );
                String childTargetPath = this.joinPath( targetPath, child.getName() );
                this.scanNode( items, taskGuid, child, childSourcePath, childTargetPath, targetBucketGuid, depth + 1 );
            }
            if ( !page.getHasMore() ) {
                break;
            }
            offset += page.getRecords().size();
        }
    }

    protected GenericUofsTransferItem newItem(
            GUID taskGuid,
            int itemIndex,
            FileTreeNode source,
            String sourcePath,
            String targetPath,
            GUID targetBucketGuid,
            int depth
    ) {
        GenericUofsTransferItem item = new GenericUofsTransferItem();
        item.setGuid( this.newTaskGuid() );
        item.setTaskGuid( taskGuid );
        item.setItemIndex( itemIndex );
        item.setDepth( depth );
        item.setSourceType( this.sourceType( source ) );
        if ( source instanceof ElementNode ) {
            ElementNode elementNode = (ElementNode) source;
            item.setSourceGuid( elementNode.getGuid() );
            item.setSourceBucketGuid( elementNode.getBucketGuid() );
        }
        item.setSourcePath( this.normalizePath( sourcePath ) );
        item.setSourcePathHash( this.mPathHasher.hash( item.getSourcePath() ) );
        item.setTargetBucketGuid( targetBucketGuid );
        item.setTargetPath( this.normalizePath( targetPath ) );
        item.setTargetPathHash( this.mPathHasher.hash( item.getTargetPath() ) );
        item.setStatus( UofsTransferItemStatus.PREPARED );
        item.setPhase( UofsTransferPhase.PREPARE );
        item.setTotalBytes( this.nodeSize( source ) );
        item.setDoneBytes( 0L );
        item.setMessage( "Transfer item prepared." );
        return item;
    }

    protected GenericUofsTransferTask newTask(
            GUID taskGuid,
            UofsTransferRequest request,
            UofsTransferPlan plan,
            List<GenericUofsTransferItem> items
    ) {
        GenericUofsTransferTask task = new GenericUofsTransferTask();
        task.setGuid( taskGuid );
        task.setOperation( request.getOperation() );
        task.setSourceCount( request.getSourceCount() );
        if ( plan.isExecutable() ) {
            task.setStatus( UofsTransferStatus.PREPARED );
            task.setPhase( UofsTransferPhase.PREPARE );
        }
        else {
            task.setStatus( UofsTransferStatus.FAILED );
            task.setPhase( UofsTransferPhase.FAILED );
        }
        task.setSourcePath( request.getSourcePath() );
        task.setSourcePathHash( this.mPathHasher.hash( request.getSourcePath() ) );
        task.setTargetPath( request.getTargetPath() );
        task.setTargetPathHash( this.mPathHasher.hash( request.getTargetPath() ) );
        task.setConflictPolicy( request.getConflictPolicy() );
        task.setLinkPolicy( request.getLinkPolicy() );
        task.setTotalCount( items.size() );
        task.setTotalBytes( this.sumTotalBytes( items ) );
        task.setMessage( "Transfer task prepared." );
        task.setPlanSnapshot( this.snapshotPlan( plan ) );
        task.setOperatorGuid( request.getOperatorGuid() );
        task.setExtConfig( request.getExtConfig() );
        if ( !items.isEmpty() ) {
            GenericUofsTransferItem first = items.get( 0 );
            task.setSourceBucketGuid( first.getSourceBucketGuid() );
            task.setSourceGuid( first.getSourceGuid() );
            task.setSourceType( first.getSourceType() );
            task.setTargetBucketGuid( first.getTargetBucketGuid() );
            task.setTargetParentGuid( first.getTargetParentGuid() );
            task.setTargetGuid( first.getTargetGuid() );
        }
        return task;
    }

    protected UofsTransferRequest normalizeRequest( UofsTransferRequest request ) {
        if ( request == null ) {
            throw new IllegalArgumentException( "UOFS transfer request should not be null." );
        }
        if ( request.getOperation() == null ) {
            request.setOperation( UofsTransferOperation.COPY );
        }
        this.normalizeSourcePaths( request );
        if ( request.getSourcePath() == null || request.getSourcePath().isBlank() ) {
            throw new IllegalArgumentException( "UOFS transfer sourcePath should not be blank." );
        }
        if ( request.getTargetPath() == null || request.getTargetPath().isBlank() ) {
            throw new IllegalArgumentException( "UOFS transfer targetPath should not be blank." );
        }
        request.setTargetPath( this.normalizePath( request.getTargetPath() ) );
        if ( request.getConflictPolicy() == null ) {
            request.setConflictPolicy( UofsTransferConflictPolicy.FAIL );
        }
        if ( request.getLinkPolicy() == null ) {
            request.setLinkPolicy( UofsTransferLinkPolicy.FOLLOW_TARGET );
        }
        if ( request.getTargetMode() == null ) {
            request.setTargetMode( UofsTransferTargetMode.DIRECTORY );
        }
        if ( request.getDirectoryPolicy() == null ) {
            request.setDirectoryPolicy( UofsTransferDirectoryPolicy.CREATE_MISSING );
        }
        if ( request.getItemTrackingMode() == null ) {
            request.setItemTrackingMode( UofsTransferItemTrackingMode.TASK_ONLY );
        }
        if ( request.getSourceCleanupPolicy() == null ) {
            request.setSourceCleanupPolicy( UofsTransferSourceCleanupPolicy.UOFS_ONLY );
        }
        return request;
    }

    protected void normalizeSourcePaths( UofsTransferRequest request ) {
        LinkedHashSet<String> paths = new LinkedHashSet<>();
        if ( request.getSourcePaths() != null ) {
            for ( String path : request.getSourcePaths() ) {
                if ( path != null && !path.isBlank() ) {
                    paths.add( this.normalizePath( path ) );
                }
            }
        }
        if ( paths.isEmpty() && request.getSourcePath() != null && !request.getSourcePath().isBlank() ) {
            paths.add( this.normalizePath( request.getSourcePath() ) );
        }
        if ( paths.isEmpty() ) {
            return;
        }
        List<String> normalized = new ArrayList<>( paths );
        this.validateBatchSourceBoundaries( normalized );
        request.setSourcePaths( normalized );
        request.setSourcePath( normalized.get( 0 ) );
    }

    protected void validateBatchSourceBoundaries( List<String> sourcePaths ) {
        if ( sourcePaths.size() < 2 ) {
            return;
        }
        for ( String sourcePath : sourcePaths ) {
            if ( "/".equals( sourcePath ) ) {
                throw new IllegalArgumentException( "UOFS transfer batch source root is not allowed." );
            }
            for ( String otherPath : sourcePaths ) {
                if ( sourcePath.equals( otherPath ) ) {
                    continue;
                }
                if ( this.isPathAncestorOf( sourcePath, otherPath ) ) {
                    throw new IllegalArgumentException( "UOFS transfer batch source paths should not contain parent-child relationship: " + sourcePath + " -> " + otherPath );
                }
            }
        }
    }

    protected boolean isBatchRequest( UofsTransferRequest request ) {
        return request != null && request.getSourceCount() > 1;
    }

    protected boolean isPathAncestorOf( String ancestorPath, String childPath ) {
        String ancestor = this.normalizePath( ancestorPath );
        String child = this.normalizePath( childPath );
        if ( "/".equals( ancestor ) ) {
            return !"/".equals( child );
        }
        return child.startsWith( ancestor + "/" );
    }

    protected FileTreeNode sourceNode( String sourcePath ) {
        ElementNode elementNode = this.mFileSystem.queryElement( sourcePath );
        if ( elementNode == null ) {
            throw new IllegalArgumentException( "UOFS transfer source not found: " + sourcePath );
        }
        if ( !( elementNode instanceof FileTreeNode ) ) {
            throw new IllegalArgumentException( "UOFS transfer source is not a file tree node: " + sourcePath );
        }
        return (FileTreeNode) elementNode;
    }

    protected UofsTransferSourceType sourceType( FileTreeNode source ) {
        if ( source instanceof ExternalSymbolic ) {
            return UofsTransferSourceType.EXTERNAL_SYMBOLIC;
        }
        if ( source instanceof ExternalFolder ) {
            return UofsTransferSourceType.NATIVE_EXTERNAL_FOLDER;
        }
        if ( source instanceof ExternalFile ) {
            return UofsTransferSourceType.NATIVE_EXTERNAL_FILE;
        }
        if ( source instanceof Folder ) {
            return UofsTransferSourceType.UOFS_FOLDER;
        }
        if ( source instanceof FileNode ) {
            return UofsTransferSourceType.UOFS_FILE;
        }
        return UofsTransferSourceType.INTERNAL_SYMBOLIC;
    }

    protected void inspectMovePlan(
            UofsTransferPlan plan,
            UofsTransferRequest request,
            List<UofsTransferRootPlan> rootPlans
    ) {
        if ( request.getOperation() != UofsTransferOperation.MOVE ) {
            return;
        }
        boolean allMetadataMove = true;
        boolean hasExternalCleanupSource = false;
        for ( UofsTransferRootPlan rootPlan : rootPlans ) {
            FileTreeNode source = this.sourceNode( rootPlan.getSourcePath() );
            UofsTransferSourceType sourceType = this.sourceType( source );
            if ( plan.getSourceType() == null ) {
                plan.setSourceType( sourceType );
            }
            if ( sourceType == UofsTransferSourceType.INTERNAL_SYMBOLIC ) {
                plan.setMoveMode( UofsTransferMoveMode.UNSUPPORTED );
                plan.addBlocker( "MOVE_INTERNAL_SYMBOLIC_UNSUPPORTED" );
                return;
            }
            if ( this.isExternalCleanupSource( sourceType ) ) {
                hasExternalCleanupSource = true;
                allMetadataMove = false;
                rootPlan.setMoveMode( UofsTransferMoveMode.COPY_VERIFY_DELETE );
                continue;
            }
            if ( this.resolveMetadataMoveTarget( source, rootPlan ) == null ) {
                allMetadataMove = false;
                rootPlan.setMoveMode( UofsTransferMoveMode.COPY_VERIFY_DELETE );
            }
            else {
                rootPlan.setMoveMode( UofsTransferMoveMode.INTERNAL_METADATA_MOVE );
            }
        }
        if ( hasExternalCleanupSource ) {
            if ( request.getSourceCleanupPolicy() != UofsTransferSourceCleanupPolicy.ALLOW_EXTERNAL_CONFIRMED ) {
                plan.setMoveMode( UofsTransferMoveMode.COPY_VERIFY_DELETE );
                plan.addBlocker( "MOVE_EXTERNAL_SOURCE_DELETE_CONFIRM_REQUIRED" );
                return;
            }
            plan.addWarning( "MOVE_EXTERNAL_SOURCE_DELETE_CONFIRMED" );
        }
        if ( allMetadataMove ) {
            plan.setMoveMode( UofsTransferMoveMode.INTERNAL_METADATA_MOVE );
            plan.addWarning( "MOVE_INTERNAL_METADATA" );
            return;
        }
        plan.setMoveMode( UofsTransferMoveMode.COPY_VERIFY_DELETE );
        plan.addWarning( "MOVE_COPY_VERIFY_DELETE" );
    }

    protected List<UofsTransferRootPlan> resolveRootPlans( UofsTransferRequest request ) {
        List<UofsTransferRootPlan> ret = new ArrayList<>();
        LinkedHashSet<String> usedTargetPaths = new LinkedHashSet<>();
        for ( String sourcePath : request.getSourcePaths() ) {
            FileTreeNode source = this.sourceNode( sourcePath );
            String requestedTargetPath = request.getTargetMode() == UofsTransferTargetMode.EXACT_PATH
                    ? request.getTargetPath()
                    : this.joinPath( request.getTargetPath(), source.getName() );
            UofsTransferRootPlan rootPlan = this.resolveRootPlan(
                    source,
                    sourcePath,
                    requestedTargetPath,
                    request.getConflictPolicy(),
                    usedTargetPaths
            );
            usedTargetPaths.add( rootPlan.getFinalTargetPath() );
            ret.add( rootPlan );
        }
        return ret;
    }

    protected UofsTransferRootPlan resolveRootPlan(
            FileTreeNode source,
            String sourcePath,
            String requestedTargetPath,
            UofsTransferConflictPolicy conflictPolicy,
            LinkedHashSet<String> usedTargetPaths
    ) {
        String normalizedSourcePath = this.normalizePath( sourcePath );
        String normalizedRequestedTargetPath = this.normalizePath( requestedTargetPath );
        UofsTransferSourceType sourceType = this.sourceType( source );
        UofsTransferRootPlan rootPlan = new UofsTransferRootPlan();
        rootPlan.setSourcePath( normalizedSourcePath );
        if ( source instanceof ElementNode ) {
            rootPlan.setSourceGuid( ( (ElementNode) source ).getGuid() );
        }
        rootPlan.setSourceType( sourceType );
        rootPlan.setRequestedTargetPath( normalizedRequestedTargetPath );
        rootPlan.setConflictPolicy( conflictPolicy );

        if ( conflictPolicy == UofsTransferConflictPolicy.RENAME ) {
            String finalTargetPath = this.resolveRenameTargetPath( normalizedRequestedTargetPath, usedTargetPaths );
            rootPlan.setFinalTargetPath( finalTargetPath );
            rootPlan.setConflictAction( finalTargetPath.equals( normalizedRequestedTargetPath )
                    ? UofsTransferConflictAction.NONE
                    : UofsTransferConflictAction.RENAME );
            return rootPlan;
        }

        ElementNode target = this.mFileSystem.queryElement( normalizedRequestedTargetPath );
        if ( usedTargetPaths.contains( normalizedRequestedTargetPath ) ) {
            throw new IllegalArgumentException( "UOFS transfer target already exists: " + normalizedRequestedTargetPath );
        }
        boolean targetExists = target != null && !this.isMissingExternalFile( target );
        if ( !targetExists ) {
            rootPlan.setFinalTargetPath( normalizedRequestedTargetPath );
            rootPlan.setConflictAction( UofsTransferConflictAction.NONE );
            return rootPlan;
        }
        if ( conflictPolicy == UofsTransferConflictPolicy.FAIL ) {
            throw new IllegalArgumentException( "UOFS transfer target already exists: " + normalizedRequestedTargetPath );
        }
        if ( target == null || this.isMissingExternalFile( target ) ) {
            throw new IllegalArgumentException( "UOFS transfer target already exists: " + normalizedRequestedTargetPath );
        }
        UofsTransferSourceType targetType = this.sourceType( target );
        if ( !this.isOverwriteCompatible( sourceType, targetType ) ) {
            throw new IllegalArgumentException( "UOFS transfer overwrite target type mismatch: " + normalizedRequestedTargetPath );
        }
        if ( normalizedRequestedTargetPath.equals( normalizedSourcePath )
                || normalizedRequestedTargetPath.startsWith( normalizedSourcePath + "/" ) ) {
            throw new IllegalArgumentException( "UOFS transfer overwrite target should not be source itself or under source: " + normalizedRequestedTargetPath );
        }
        rootPlan.setFinalTargetPath( normalizedRequestedTargetPath );
        rootPlan.setConflictAction( UofsTransferConflictAction.OVERWRITE );
        rootPlan.setOverwrittenTargetGuid( target.getGuid() );
        rootPlan.setOverwrittenTargetType( targetType );
        return rootPlan;
    }

    protected String resolveRenameTargetPath( String requestedTargetPath, LinkedHashSet<String> usedTargetPaths ) {
        String candidate = this.normalizePath( requestedTargetPath );
        if ( !this.targetPathOccupied( candidate, usedTargetPaths ) ) {
            return candidate;
        }
        String parent = this.parentPath( candidate );
        String name = this.fileName( candidate );
        String baseName = this.renameBaseName( name );
        String extension = this.renameExtension( name );
        int index = 1;
        while ( true ) {
            String nextName = baseName + "_" + index + extension;
            String nextPath = this.joinPath( parent, nextName );
            if ( !this.targetPathOccupied( nextPath, usedTargetPaths ) ) {
                return nextPath;
            }
            index++;
        }
    }

    protected boolean targetPathOccupied( String targetPath, LinkedHashSet<String> usedTargetPaths ) {
        if ( usedTargetPaths.contains( targetPath ) ) {
            return true;
        }
        ElementNode target = this.mFileSystem.queryElement( targetPath );
        return target != null && !this.isMissingExternalFile( target );
    }

    protected String renameBaseName( String name ) {
        int dotIndex = this.renameExtensionDotIndex( name );
        return dotIndex < 0 ? name : name.substring( 0, dotIndex );
    }

    protected String renameExtension( String name ) {
        int dotIndex = this.renameExtensionDotIndex( name );
        return dotIndex < 0 ? "" : name.substring( dotIndex );
    }

    protected int renameExtensionDotIndex( String name ) {
        int dotIndex = name.lastIndexOf( "." );
        if ( dotIndex <= 0 ) {
            return -1;
        }
        return dotIndex;
    }

    protected boolean isOverwriteCompatible( UofsTransferSourceType sourceType, UofsTransferSourceType targetType ) {
        if ( this.isFolderSourceType( sourceType ) ) {
            return this.isFolderSourceType( targetType );
        }
        if ( this.isFileSourceType( sourceType ) ) {
            return this.isFileSourceType( targetType );
        }
        return sourceType == targetType;
    }

    protected boolean isFolderSourceType( UofsTransferSourceType sourceType ) {
        return sourceType == UofsTransferSourceType.UOFS_FOLDER
                || sourceType == UofsTransferSourceType.NATIVE_EXTERNAL_FOLDER;
    }

    protected boolean isFileSourceType( UofsTransferSourceType sourceType ) {
        return sourceType == UofsTransferSourceType.UOFS_FILE
                || sourceType == UofsTransferSourceType.NATIVE_EXTERNAL_FILE;
    }

    protected long nodeSize( FileTreeNode source ) {
        if ( source instanceof FileNode ) {
            return ( (FileNode) source ).getDefinitionSize();
        }
        if ( source instanceof ExternalFile ) {
            return ( (ExternalFile) source ).getNativeFile().length();
        }
        return 0L;
    }

    protected long sumTotalBytes( List<? extends UofsTransferItem> items ) {
        long total = 0L;
        for ( UofsTransferItem item : items ) {
            total += item.getTotalBytes();
        }
        return total;
    }

    protected boolean isItemLedgerMode( UofsTransferRequest request ) {
        if ( request == null ) {
            return false;
        }
        return request.getItemTrackingMode() == UofsTransferItemTrackingMode.ITEM_LEDGER;
    }

    protected void ensureMoveSourceCleanupAllowed(
            UofsTransferItem rootItem,
            UofsTransferSourceCleanupPolicy sourceCleanupPolicy
    ) {
        if ( rootItem == null || !this.isExternalCleanupSource( rootItem.getSourceType() ) ) {
            return;
        }
        if ( sourceCleanupPolicy == UofsTransferSourceCleanupPolicy.ALLOW_EXTERNAL_CONFIRMED ) {
            return;
        }
        throw new MoveCleanupException( "UOFS move external source cleanup requires explicit confirmation." );
    }

    protected boolean isExternalCleanupSource( UofsTransferSourceType sourceType ) {
        return sourceType == UofsTransferSourceType.EXTERNAL_SYMBOLIC
                || sourceType == UofsTransferSourceType.NATIVE_EXTERNAL_FILE
                || sourceType == UofsTransferSourceType.NATIVE_EXTERNAL_FOLDER;
    }

    protected boolean isMetadataMoveTask( List<? extends UofsTransferItem> items ) {
        if ( items == null || items.isEmpty() ) {
            return false;
        }
        for ( UofsTransferItem item : items ) {
            if ( item.getPhase() != UofsTransferPhase.MOVING_METADATA
                    || item.getSourceGuid() == null
                    || !item.getSourceGuid().equals( item.getTargetGuid() )
                    || item.getTargetParentGuid() == null ) {
                return false;
            }
        }
        return true;
    }

    protected boolean gcTransferItems( GUID taskGuid ) {
        try {
            this.mItemManipulator.deleteByTaskGuid( taskGuid );
            return true;
        }
        catch ( RuntimeException e ) {
            return false;
        }
    }

    protected boolean isTerminalTaskStatus( UofsTransferStatus status ) {
        return status == UofsTransferStatus.DONE || status == UofsTransferStatus.FAILED || status == UofsTransferStatus.CANCELED;
    }

    protected String snapshotPlan( UofsTransferPlan plan ) {
        StringBuilder builder = new StringBuilder();
        builder.append( "{" );
        this.appendJsonField( builder, "operation", this.enumName( plan.getOperation() ), true );
        this.appendJsonField( builder, "sourcePath", plan.getSourcePath(), false );
        this.appendJsonField( builder, "sourceCount", plan.getSourceCount(), false );
        this.appendJsonStringArrayField( builder, "sourcePaths", plan.getSourcePaths(), false );
        this.appendJsonField( builder, "targetPath", plan.getTargetPath(), false );
        this.appendJsonField( builder, "targetMode", this.enumName( plan.getTargetMode() ), false );
        this.appendJsonField( builder, "sourceType", this.enumName( plan.getSourceType() ), false );
        this.appendJsonField( builder, "moveMode", this.enumName( plan.getMoveMode() ), false );
        this.appendJsonField( builder, "sourceCleanupPolicy", this.enumName( plan.getSourceCleanupPolicy() ), false );
        this.appendJsonField( builder, "directoryPolicy", this.enumName( plan.getDirectoryPolicy() ), false );
        this.appendJsonField( builder, "itemTrackingMode", this.enumName( plan.getItemTrackingMode() ), false );
        this.appendJsonField( builder, "totalCount", plan.getTotalCount(), false );
        this.appendJsonField( builder, "totalBytes", plan.getTotalBytes(), false );
        this.appendJsonField( builder, "executable", plan.isExecutable(), false );
        this.appendJsonStringArrayField( builder, "blockers", plan.getBlockers(), false );
        this.appendJsonStringArrayField( builder, "warnings", plan.getWarnings(), false );
        this.appendJsonRootPlanArrayField( builder, "rootPlans", plan.getRootPlans(), false );
        builder.append( "}" );
        return builder.toString();
    }

    protected String snapshotResult(
            UofsTransferItemTrackingMode itemTrackingMode,
            String status,
            long totalCount,
            long doneCount,
            long failedCount,
            long totalBytes,
            long doneBytes,
            boolean itemGcDone
    ) {
        StringBuilder builder = new StringBuilder();
        builder.append( "{" );
        this.appendJsonField( builder, "status", status, true );
        this.appendJsonField( builder, "itemTrackingMode", this.enumName( itemTrackingMode ), false );
        this.appendJsonField( builder, "totalCount", totalCount, false );
        this.appendJsonField( builder, "doneCount", doneCount, false );
        this.appendJsonField( builder, "failedCount", failedCount, false );
        this.appendJsonField( builder, "totalBytes", totalBytes, false );
        this.appendJsonField( builder, "doneBytes", doneBytes, false );
        this.appendJsonField( builder, "itemGcDone", itemGcDone, false );
        this.appendJsonField( builder, "rollbackSupported", itemTrackingMode == UofsTransferItemTrackingMode.ITEM_LEDGER, false );
        builder.append( "}" );
        return builder.toString();
    }

    protected void appendJsonStringArrayField( StringBuilder builder, String name, List<String> values, boolean first ) {
        if ( !first ) {
            builder.append( "," );
        }
        builder.append( this.jsonQuote( name ) );
        builder.append( ":" );
        builder.append( "[" );
        boolean itemFirst = true;
        for ( String value : values ) {
            if ( !itemFirst ) {
                builder.append( "," );
            }
            itemFirst = false;
            builder.append( this.jsonQuote( value ) );
        }
        builder.append( "]" );
    }

    protected void appendJsonRootPlanArrayField(
            StringBuilder builder,
            String name,
            List<UofsTransferRootPlan> values,
            boolean first
    ) {
        if ( !first ) {
            builder.append( "," );
        }
        builder.append( this.jsonQuote( name ) );
        builder.append( ":" );
        builder.append( "[" );
        boolean itemFirst = true;
        for ( UofsTransferRootPlan value : values ) {
            if ( !itemFirst ) {
                builder.append( "," );
            }
            itemFirst = false;
            builder.append( "{" );
            this.appendJsonField( builder, "sourcePath", value.getSourcePath(), true );
            this.appendJsonField( builder, "sourceGuid", value.getSourceGuid(), false );
            this.appendJsonField( builder, "sourceType", this.enumName( value.getSourceType() ), false );
            this.appendJsonField( builder, "requestedTargetPath", value.getRequestedTargetPath(), false );
            this.appendJsonField( builder, "finalTargetPath", value.getFinalTargetPath(), false );
            this.appendJsonField( builder, "conflictPolicy", this.enumName( value.getConflictPolicy() ), false );
            this.appendJsonField( builder, "conflictAction", this.enumName( value.getConflictAction() ), false );
            this.appendJsonField( builder, "overwrittenTargetGuid", value.getOverwrittenTargetGuid(), false );
            this.appendJsonField( builder, "overwrittenTargetType", this.enumName( value.getOverwrittenTargetType() ), false );
            this.appendJsonField( builder, "moveMode", this.enumName( value.getMoveMode() ), false );
            builder.append( "}" );
        }
        builder.append( "]" );
    }

    protected void appendJsonField( StringBuilder builder, String name, Object value, boolean first ) {
        if ( !first ) {
            builder.append( "," );
        }
        builder.append( this.jsonQuote( name ) );
        builder.append( ":" );
        if ( value == null ) {
            builder.append( "null" );
            return;
        }
        if ( value instanceof Number || value instanceof Boolean ) {
            builder.append( value );
            return;
        }
        builder.append( this.jsonQuote( String.valueOf( value ) ) );
    }

    protected String jsonQuote( String value ) {
        if ( value == null ) {
            return "null";
        }
        return "\"" + value.replace( "\\", "\\\\" ).replace( "\"", "\\\"" ) + "\"";
    }

    protected String enumName( Enum<?> value ) {
        if ( value == null ) {
            return null;
        }
        return value.name();
    }

    protected void updateTaskProgress(
            GUID taskGuid,
            UofsTransferItem item,
            long totalCount,
            long doneCount,
            long failedCount,
            long totalBytes,
            long doneBytes,
            String message,
            @Nullable UofsTransferProgressListener progressListener
    ) {
        this.updateTaskProgress(
                taskGuid,
                item,
                UofsTransferPhase.COPYING_DATA,
                totalCount,
                doneCount,
                failedCount,
                totalBytes,
                doneBytes,
                message,
                progressListener
        );
    }

    protected void updateTaskProgress(
            GUID taskGuid,
            UofsTransferItem item,
            UofsTransferPhase phase,
            long totalCount,
            long doneCount,
            long failedCount,
            long totalBytes,
            long doneBytes,
            String message,
            @Nullable UofsTransferProgressListener progressListener
    ) {
        this.mTaskManipulator.updateProgress(
                taskGuid,
                phase.name(),
                totalCount,
                doneCount,
                failedCount,
                totalBytes,
                doneBytes,
                item.getGuid(),
                item.getSourceGuid(),
                item.getTargetGuid(),
                message
        );
        this.notifyProgress(
                progressListener,
                taskGuid,
                item.getGuid(),
                phase,
                totalCount,
                doneCount,
                failedCount,
                totalBytes,
                doneBytes,
                message
        );
    }

    protected void notifyProgress(
            @Nullable UofsTransferProgressListener progressListener,
            GUID taskGuid,
            GUID itemGuid,
            UofsTransferPhase phase,
            long totalCount,
            long doneCount,
            long failedCount,
            long totalBytes,
            long doneBytes,
            String message
    ) {
        if ( progressListener == null ) {
            return;
        }
        UofsTransferProgress progress = new UofsTransferProgress();
        progress.setTaskGuid( taskGuid );
        progress.setItemGuid( itemGuid );
        progress.setPhase( phase );
        progress.setTotalCount( totalCount );
        progress.setDoneCount( doneCount );
        progress.setFailedCount( failedCount );
        progress.setTotalBytes( totalBytes );
        progress.setDoneBytes( doneBytes );
        progress.setMessage( message );
        progressListener.onProgress( progress );
    }

    protected GUID newTaskGuid() {
        return GUIDs.GUID128( UUID.randomUUID().toString() );
    }

    protected String normalizePath( String path ) {
        String ret = path.trim().replace( "\\", "/" );
        while ( ret.length() > 1 && ret.endsWith( "/" ) ) {
            ret = ret.substring( 0, ret.length() - 1 );
        }
        return ret;
    }

    protected String joinPath( String parentPath, String name ) {
        String parent = this.normalizePath( parentPath );
        if ( "/".equals( parent ) ) {
            return parent + name;
        }
        return parent + "/" + name;
    }

    protected String parentPath( String path ) {
        String normalized = this.normalizePath( path );
        if ( "/".equals( normalized ) ) {
            return "/";
        }
        int index = normalized.lastIndexOf( "/" );
        if ( index <= 0 ) {
            return "/";
        }
        return normalized.substring( 0, index );
    }

    protected String fileName( String path ) {
        String normalized = this.normalizePath( path );
        if ( "/".equals( normalized ) ) {
            throw new IllegalArgumentException( "UOFS transfer file name should not be root path." );
        }
        int index = normalized.lastIndexOf( "/" );
        return index < 0 ? normalized : normalized.substring( index + 1 );
    }

    protected static class MetadataMoveTarget {
        protected final String mszTargetPath;
        protected final GUID   mParentGuid;
        protected final GUID   mBucketGuid;

        protected MetadataMoveTarget( String targetPath, GUID parentGuid, GUID bucketGuid ) {
            this.mszTargetPath = targetPath;
            this.mParentGuid = parentGuid;
            this.mBucketGuid = bucketGuid;
        }

        protected String getTargetPath() {
            return this.mszTargetPath;
        }

        protected GUID getParentGuid() {
            return this.mParentGuid;
        }

        protected GUID getBucketGuid() {
            return this.mBucketGuid;
        }
    }

    protected static class TransferTargetFolder {
        protected final String      mszPath;
        protected final ElementNode mNode;
        protected final GUID        mBucketGuid;
        protected final boolean     mbMissing;

        protected TransferTargetFolder( String path, ElementNode node, GUID bucketGuid, boolean missing ) {
            this.mszPath = path;
            this.mNode = node;
            this.mBucketGuid = bucketGuid;
            this.mbMissing = missing;
        }

        protected String getPath() {
            return this.mszPath;
        }

        protected ElementNode getNode() {
            return this.mNode;
        }

        protected GUID getBucketGuid() {
            return this.mBucketGuid;
        }

        protected boolean isMissing() {
            return this.mbMissing;
        }
    }

    protected static class TransferRunStats {
        protected final long mnTotalCount;
        protected final long mnTotalBytes;
        protected long       mnDoneCount;
        protected long       mnFailedCount;
        protected long       mnDoneBytes;

        protected TransferRunStats( long totalCount, long totalBytes ) {
            this.mnTotalCount = totalCount;
            this.mnTotalBytes = totalBytes;
        }

        protected void addDone( long doneBytes ) {
            this.mnDoneCount++;
            this.mnDoneBytes += doneBytes;
        }

        protected void addFailed() {
            this.mnFailedCount++;
        }

        protected long getTotalCount() {
            return this.mnTotalCount;
        }

        protected long getTotalBytes() {
            return this.mnTotalBytes;
        }

        protected long getDoneCount() {
            return this.mnDoneCount;
        }

        protected long getFailedCount() {
            return this.mnFailedCount;
        }

        protected long getDoneBytes() {
            return this.mnDoneBytes;
        }
    }

    protected static class MoveVerifyException extends RuntimeException {
        protected MoveVerifyException( String message ) {
            super( message );
        }
    }

    protected static class MoveCleanupException extends RuntimeException {
        protected MoveCleanupException( String message ) {
            super( message );
        }

        protected MoveCleanupException( String message, Throwable cause ) {
            super( message, cause );
        }
    }

    protected static class UofsTransferCanceledException extends RuntimeException {
    }
}
