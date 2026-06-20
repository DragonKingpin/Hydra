package com.pinecone.hydra.storage.lifecycle;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.bucket.Bucket;
import com.pinecone.hydra.storage.bucket.BucketInstrument;
import com.pinecone.hydra.storage.bucket.GenericBucket;
import com.pinecone.hydra.storage.bucket.purge.BucketPurgeProgress;
import com.pinecone.hydra.storage.bucket.purge.BucketPurgeReport;
import com.pinecone.hydra.storage.file.KOMFileSystem;
import com.pinecone.hydra.storage.file.source.FileMasterManipulator;
import com.pinecone.hydra.storage.lifecycle.service.StorageLifecycleExecutor;
import com.pinecone.hydra.storage.lifecycle.service.StorageLifecyclePlanner;
import com.pinecone.hydra.storage.lifecycle.service.StorageLifecycleService;
import com.pinecone.hydra.storage.lifecycle.source.StorageLifecycleTaskManipulator;
import com.pinecone.hydra.storage.volume.VolumeManager;
import com.pinecone.hydra.storage.volume.core.VolumePhysical;
import com.pinecone.hydra.storage.volume.core.VolumePhysicalStatus;
import com.pinecone.hydra.storage.volume.core.VolumeRecord;
import com.pinecone.hydra.storage.volume.core.VolumeStatus;
import com.pinecone.ulf.util.guid.GUIDs;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GenericStorageLifecycleService implements StorageLifecycleService, StorageLifecyclePlanner, StorageLifecycleExecutor {
    protected static final int DEPENDENCY_SAMPLE_LIMIT = 20;

    protected final KOMFileSystem                  mFileSystem;
    protected final VolumeManager                  mVolumeManager;
    protected final StorageLifecycleTaskManipulator mTaskManipulator;
    protected final ExecutorService                mExecutor = Executors.newSingleThreadExecutor();

    public GenericStorageLifecycleService(
            KOMFileSystem fileSystem,
            VolumeManager volumeManager,
            StorageLifecycleTaskManipulator taskManipulator
    ) {
        this.mFileSystem = fileSystem;
        this.mVolumeManager = volumeManager;
        this.mTaskManipulator = taskManipulator;
    }

    @Override
    public Map<String, Object> kernelStatus() {
        Map<String, Object> ret = new LinkedHashMap<>();
        ret.put( "bucketCount", this.safeCountBuckets() );
        ret.put( "readyLogicalVolumeCount", this.safeCountReadyVolumes() );
        ret.put( "readyPhysicalVolumeCount", this.safeCountReadyPhysicals() );
        ret.put( "runningTaskCount", this.countTasks( null, null, StorageLifecycleTaskStatus.RUNNING, null ) );
        ret.put( "failedTaskCount", this.countTasks( null, null, StorageLifecycleTaskStatus.FAILED, null ) );
        ret.put( "blockedTaskCount", this.countTasks( null, null, StorageLifecycleTaskStatus.BLOCKED, null ) );
        ret.put( "health", "UP" );
        ret.put( "status", "READY" );
        return ret;
    }

    @Override
    public StorageLifecyclePlan plan( StorageLifecycleRequest request ) {
        this.requireTaskManipulator();
        StorageLifecycleRequest normalized = this.normalizeRequest( request );
        StorageLifecyclePlan plan = new StorageLifecyclePlan();
        plan.setTaskType( normalized.getTaskType() );
        plan.setTargetType( normalized.getTargetType() );
        plan.setTargetGuid( normalized.getTargetGuid() );
        plan.setTargetName( normalized.getTargetName() );
        plan.setOperationMode( normalized.getOperationMode() );
        plan.setRiskLevel( this.riskLevel( normalized.getTaskType() ) );

        if ( normalized.getTargetType() == StorageLifecycleTargetType.BUCKET ) {
            this.planBucket( normalized, plan );
        }
        else if ( normalized.getTargetType() == StorageLifecycleTargetType.LOGICAL_VOLUME ) {
            this.planLogicalVolume( normalized, plan );
        }
        else if ( normalized.getTargetType() == StorageLifecycleTargetType.PHYSICAL_VOLUME ) {
            this.planPhysicalVolume( normalized, plan );
        }
        else {
            plan.addBlocker( "UNSUPPORTED_TARGET", "Unsupported lifecycle target.", 0L );
        }
        return plan;
    }

    @Override
    public StorageLifecycleTask execute( StorageLifecycleRequest request ) {
        StorageLifecycleRequest normalized = this.normalizeRequest( request );
        StorageLifecyclePlan plan = this.plan( normalized );
        GenericStorageLifecycleTask task = this.newTask( normalized, plan );
        this.mTaskManipulator.insert( task );
        StorageLifecycleTask inserted = this.getTask( task.getGuid() );
        if ( !plan.isExecutable() ) {
            return inserted;
        }
        this.mExecutor.submit( () -> this.runTask( task.getGuid(), normalized.getTaskType(), normalized.getTargetGuid() ) );
        return inserted;
    }

    @Override
    public long countTasks(
            StorageLifecycleTaskType taskType,
            StorageLifecycleTargetType targetType,
            StorageLifecycleTaskStatus status,
            GUID targetGuid
    ) {
        this.requireTaskManipulator();
        return this.mTaskManipulator.count( taskType, targetType, status, targetGuid );
    }

    @Override
    public List<? extends StorageLifecycleTask> listTasks(
            StorageLifecycleTaskType taskType,
            StorageLifecycleTargetType targetType,
            StorageLifecycleTaskStatus status,
            GUID targetGuid,
            int offset,
            int limit
    ) {
        this.requireTaskManipulator();
        return this.mTaskManipulator.listPage( taskType, targetType, status, targetGuid, offset, limit );
    }

    @Override
    public StorageLifecycleTask getTask( GUID taskGuid ) {
        this.requireTaskManipulator();
        StorageLifecycleTask task = this.mTaskManipulator.get( taskGuid );
        if ( task == null ) {
            throw new IllegalArgumentException( "Lifecycle task not found: " + taskGuid );
        }
        return task;
    }

    @Override
    public void cancel( GUID taskGuid ) {
        this.requireTaskManipulator();
        this.mTaskManipulator.updateStatus(
                taskGuid,
                StorageLifecycleTaskStatus.CANCELED.name(),
                StorageLifecyclePhase.CANCELED.name(),
                null,
                "Task canceled by operator."
        );
    }

    protected void runTask( GUID taskGuid, StorageLifecycleTaskType taskType, GUID targetGuid ) {
        this.mTaskManipulator.updateStatus(
                taskGuid,
                StorageLifecycleTaskStatus.RUNNING.name(),
                StorageLifecyclePhase.CHECKING_DEPENDENCY.name(),
                null,
                "Lifecycle task started."
        );
        try {
            if ( taskType == StorageLifecycleTaskType.BUCKET_PURGE ) {
                this.runBucket( taskGuid, targetGuid, false );
            }
            else if ( taskType == StorageLifecycleTaskType.BUCKET_FORMAT ) {
                this.runBucket( taskGuid, targetGuid, true );
            }
            else if ( taskType == StorageLifecycleTaskType.LOGICAL_VOLUME_RETIRE ) {
                this.runLogicalVolumeRetire( taskGuid, targetGuid );
            }
            else if ( taskType == StorageLifecycleTaskType.PHYSICAL_VOLUME_RETIRE ) {
                this.runPhysicalVolumeRetire( taskGuid, targetGuid );
            }
            else {
                throw new IllegalArgumentException( "Unsupported lifecycle task type: " + taskType );
            }
        }
        catch ( RuntimeException e ) {
            this.mTaskManipulator.updateStatus(
                    taskGuid,
                    StorageLifecycleTaskStatus.FAILED.name(),
                    StorageLifecyclePhase.FAILED.name(),
                    e.getMessage(),
                    "Lifecycle task failed."
            );
        }
    }

    protected void runBucket( GUID taskGuid, GUID bucketGuid, boolean format ) {
        BucketPurgeReport report = format
                ? this.mFileSystem.formatBucket( bucketGuid, this.mVolumeManager, progress -> this.updateBucketProgress( taskGuid, progress ) )
                : this.mFileSystem.purgeBucket( bucketGuid, this.mVolumeManager, progress -> this.updateBucketProgress( taskGuid, progress ) );
        this.mTaskManipulator.updateDone(
                taskGuid,
                StorageLifecyclePhase.DONE.name(),
                report.getTotalCount(),
                report.getDoneCount(),
                report.getMessage(),
                this.snapshotReport( report )
        );
    }

    protected void runLogicalVolumeRetire( GUID taskGuid, GUID volumeGuid ) {
        StorageLifecycleRequest request = new StorageLifecycleRequest();
        request.setTaskType( StorageLifecycleTaskType.LOGICAL_VOLUME_RETIRE );
        request.setTargetType( StorageLifecycleTargetType.LOGICAL_VOLUME );
        request.setTargetGuid( volumeGuid );
        StorageLifecyclePlan plan = this.plan( request );
        if ( !plan.isExecutable() ) {
            this.mTaskManipulator.updateBlocked( taskGuid, StorageLifecyclePhase.CHECKING_DEPENDENCY.name(), this.snapshotPlan( plan ) );
            return;
        }
        this.mTaskManipulator.updateStatus(
                taskGuid,
                StorageLifecycleTaskStatus.RUNNING.name(),
                StorageLifecyclePhase.RETIRING.name(),
                null,
                "Retiring logical volume."
        );
        this.mVolumeManager.retireVolume( volumeGuid );
        this.mTaskManipulator.updateDone(
                taskGuid,
                StorageLifecyclePhase.DONE.name(),
                1L,
                1L,
                "Logical volume retired.",
                this.snapshotPlan( plan )
        );
    }

    protected void runPhysicalVolumeRetire( GUID taskGuid, GUID physicalGuid ) {
        StorageLifecycleRequest request = new StorageLifecycleRequest();
        request.setTaskType( StorageLifecycleTaskType.PHYSICAL_VOLUME_RETIRE );
        request.setTargetType( StorageLifecycleTargetType.PHYSICAL_VOLUME );
        request.setTargetGuid( physicalGuid );
        StorageLifecyclePlan plan = this.plan( request );
        if ( !plan.isExecutable() ) {
            this.mTaskManipulator.updateBlocked( taskGuid, StorageLifecyclePhase.CHECKING_DEPENDENCY.name(), this.snapshotPlan( plan ) );
            return;
        }
        this.mTaskManipulator.updateStatus(
                taskGuid,
                StorageLifecycleTaskStatus.RUNNING.name(),
                StorageLifecyclePhase.RETIRING.name(),
                null,
                "Retiring physical volume."
        );
        this.mVolumeManager.retirePhysical( physicalGuid );
        this.mTaskManipulator.updateDone(
                taskGuid,
                StorageLifecyclePhase.DONE.name(),
                1L,
                1L,
                "Physical volume retired.",
                this.snapshotPlan( plan )
        );
    }

    protected void updateBucketProgress( GUID taskGuid, BucketPurgeProgress progress ) {
        this.mTaskManipulator.updateProgress(
                taskGuid,
                progress.getPhase() == null ? StorageLifecyclePhase.RELEASING_DATA.name() : progress.getPhase().name(),
                progress.getTotalCount(),
                progress.getDoneCount(),
                progress.getCurrentGuid() == null ? null : progress.getCurrentGuid().toString(),
                progress.getMessage()
        );
    }

    protected void planBucket( StorageLifecycleRequest request, StorageLifecyclePlan plan ) {
        Bucket bucket = this.bucketKernel().get( request.getTargetGuid() );
        if ( bucket == null ) {
            plan.addBlocker( "BUCKET_NOT_FOUND", "Bucket not found: " + request.getTargetGuid(), 0L );
            return;
        }
        plan.setTargetName( bucket.getUserIdentifier() + "@" + bucket.getBucketName() );
        plan.setEstimatedTotalCount( 1L );
        plan.addWarning( "ASYNC_PURGE", "Bucket lifecycle operation will run as a background task.", 1L );
        plan.addAction(
                request.getTaskType() == StorageLifecycleTaskType.BUCKET_FORMAT ? "FORMAT_BUCKET_SLOW" : "PURGE_BUCKET_SLOW",
                "Release file data, purge FAT residue, metadata, path cache and tree records.",
                1L
        );
    }

    protected void planLogicalVolume( StorageLifecycleRequest request, StorageLifecyclePlan plan ) {
        GUID volumeGuid = request.getTargetGuid();
        VolumeRecord record = this.mVolumeManager.affirmVolumeRecord( volumeGuid );
        plan.setTargetName( record.getName() );
        long bucketCount = this.bucketKernel().countByVolumeGuid( volumeGuid );
        long childRefCount = this.mVolumeManager.countVolumeChildReferences( volumeGuid );
        long mountCount = this.mVolumeManager.countVolumeMountReferences( volumeGuid );
        long locationCount = this.fileMasterManipulator().getFileChunkLocationManipulator().countByVolumeGuid( volumeGuid );
        if ( bucketCount > 0L ) {
            StorageLifecycleBlocker blocker = plan.addBlocker( "BUCKET_REFERENCE", "Logical volume is still bound by buckets.", bucketCount );
            this.addBucketDependencies( blocker, volumeGuid, bucketCount );
        }
        if ( childRefCount > 0L ) {
            plan.addBlocker( "PARENT_VOLUME_REFERENCE", "Logical volume is used as a member by parent volumes.", childRefCount );
        }
        if ( mountCount > 0L ) {
            plan.addBlocker( "MOUNT_REFERENCE", "Logical volume still has active mount records.", mountCount );
        }
        if ( locationCount > 0L ) {
            plan.addBlocker( "FAT_LOCATION_REFERENCE", "Logical volume still has FAT chunk locations.", locationCount );
        }
        plan.addAction( "RETIRE_LOGICAL_VOLUME", "Mark volume DELETED and remove its own backing extents.", 1L );
        plan.addWarning( "NO_CASCADE", "This operation does not cascade buckets or member volumes.", 1L );
    }

    protected void planPhysicalVolume( StorageLifecycleRequest request, StorageLifecyclePlan plan ) {
        GUID physicalGuid = request.getTargetGuid();
        VolumePhysical physical = this.mVolumeManager.affirmPhysicalRecord( physicalGuid );
        plan.setTargetName( physical.getName() );
        long referenceCount = this.mVolumeManager.countPhysicalReferences( physicalGuid );
        if ( referenceCount > 0L ) {
            StorageLifecycleBlocker blocker = plan.addBlocker( "LOGICAL_VOLUME_REFERENCE", "Physical volume is still referenced by logical volumes.", referenceCount );
            this.addLogicalVolumeDependencies( blocker, physicalGuid, referenceCount );
        }
        plan.addAction( "RETIRE_PHYSICAL_VOLUME", "Mark physical resource and support trait DELETED.", 1L );
        plan.addWarning( "NO_DISK_DELETE", "This operation does not delete files or directories on disk.", 1L );
    }

    protected void addBucketDependencies( StorageLifecycleBlocker blocker, GUID volumeGuid, long totalCount ) {
        List<GenericBucket> buckets = this.bucketKernel().listByVolumeGuid( volumeGuid, 0, DEPENDENCY_SAMPLE_LIMIT );
        for ( GenericBucket bucket : buckets ) {
            StorageLifecycleDependency dependency = new StorageLifecycleDependency();
            dependency.setType( StorageLifecycleTargetType.BUCKET.name() );
            dependency.setGuid( bucket.getGuid() == null ? null : bucket.getGuid().toString() );
            dependency.setName( bucket.getBucketIdentifier() );
            dependency.setStatus( bucket.getStatus() );
            dependency.setRelation( "BUCKET_VOLUME" );
            dependency.putExt( "userIdentifier", bucket.getUserIdentifier() );
            dependency.putExt( "bucketName", bucket.getBucketName() );
            dependency.putExt( "volumeGuid", bucket.getVolumeGuid() == null ? null : bucket.getVolumeGuid().toString() );
            blocker.addDependency( dependency );
        }
        blocker.setHasMore( blocker.getDependencies().size() >= DEPENDENCY_SAMPLE_LIMIT && totalCount > blocker.getDependencies().size() );
    }

    protected void addLogicalVolumeDependencies( StorageLifecycleBlocker blocker, GUID physicalGuid, long totalCount ) {
        List<VolumeRecord> records = this.mVolumeManager.listVolumeReferencesByPhysicalGuid( physicalGuid, DEPENDENCY_SAMPLE_LIMIT );
        for ( VolumeRecord record : records ) {
            StorageLifecycleDependency dependency = new StorageLifecycleDependency();
            dependency.setType( StorageLifecycleTargetType.LOGICAL_VOLUME.name() );
            dependency.setGuid( record.getGuid() == null ? null : record.getGuid().toString() );
            dependency.setName( record.getName() );
            dependency.setStatus( record.getStatus() );
            dependency.setRelation( "EXTENT_PHYSICAL" );
            dependency.putExt( "volumeType", record.getVolumeType() );
            dependency.putExt( "mappingMode", record.getMappingMode() );
            dependency.putExt( "logicalSize", record.getLogicalSize() );
            blocker.addDependency( dependency );
        }
        blocker.setHasMore( blocker.getDependencies().size() >= DEPENDENCY_SAMPLE_LIMIT && totalCount > blocker.getDependencies().size() );
    }

    protected GenericStorageLifecycleTask newTask( StorageLifecycleRequest request, StorageLifecyclePlan plan ) {
        GenericStorageLifecycleTask task = new GenericStorageLifecycleTask();
        task.setGuid( GUIDs.GUID128( UUID.randomUUID().toString() ) );
        task.setTaskType( request.getTaskType() );
        task.setTargetType( request.getTargetType() );
        task.setTargetGuid( request.getTargetGuid() );
        task.setTargetName( plan.getTargetName() == null ? request.getTargetName() : plan.getTargetName() );
        task.setOperationMode( plan.getOperationMode() );
        task.setStatus( plan.isExecutable() ? StorageLifecycleTaskStatus.PREPARED : StorageLifecycleTaskStatus.BLOCKED );
        task.setPhase( plan.isExecutable() ? StorageLifecyclePhase.PREPARE : StorageLifecyclePhase.CHECKING_DEPENDENCY );
        task.setTotalCount( plan.getEstimatedTotalCount() );
        task.setDoneCount( 0L );
        task.setRiskLevel( plan.getRiskLevel() );
        task.setMessage( plan.isExecutable() ? "Lifecycle task prepared." : "Lifecycle task is blocked by dependencies." );
        task.setPlanSnapshot( this.snapshotPlan( plan ) );
        task.setOperatorGuid( request.getOperatorGuid() );
        task.setExtConfig( request.getExtConfig() );
        return task;
    }

    protected StorageLifecycleRequest normalizeRequest( StorageLifecycleRequest request ) {
        if ( request == null ) {
            throw new IllegalArgumentException( "Lifecycle request should not be null." );
        }
        if ( request.getTaskType() == null ) {
            throw new IllegalArgumentException( "Lifecycle taskType should not be null." );
        }
        if ( request.getTargetGuid() == null ) {
            throw new IllegalArgumentException( "Lifecycle targetGuid should not be null." );
        }
        if ( request.getTargetType() == null ) {
            request.setTargetType( this.targetTypeOf( request.getTaskType() ) );
        }
        if ( request.getOperationMode() == null ) {
            request.setOperationMode( this.operationModeOf( request.getTaskType() ) );
        }
        return request;
    }

    protected StorageLifecycleTargetType targetTypeOf( StorageLifecycleTaskType taskType ) {
        if ( taskType == StorageLifecycleTaskType.BUCKET_PURGE || taskType == StorageLifecycleTaskType.BUCKET_FORMAT ) {
            return StorageLifecycleTargetType.BUCKET;
        }
        if ( taskType == StorageLifecycleTaskType.LOGICAL_VOLUME_RETIRE ) {
            return StorageLifecycleTargetType.LOGICAL_VOLUME;
        }
        if ( taskType == StorageLifecycleTaskType.PHYSICAL_VOLUME_RETIRE ) {
            return StorageLifecycleTargetType.PHYSICAL_VOLUME;
        }
        throw new IllegalArgumentException( "Unsupported lifecycle task type: " + taskType );
    }

    protected StorageLifecycleOperationMode operationModeOf( StorageLifecycleTaskType taskType ) {
        if ( taskType == StorageLifecycleTaskType.BUCKET_FORMAT ) {
            return StorageLifecycleOperationMode.SLOW_FORMAT;
        }
        if ( taskType == StorageLifecycleTaskType.BUCKET_PURGE ) {
            return StorageLifecycleOperationMode.SLOW_PURGE;
        }
        return StorageLifecycleOperationMode.RETIRE;
    }

    protected StorageLifecycleRiskLevel riskLevel( StorageLifecycleTaskType taskType ) {
        return taskType == StorageLifecycleTaskType.BUCKET_PURGE || taskType == StorageLifecycleTaskType.BUCKET_FORMAT
                ? StorageLifecycleRiskLevel.HIGH
                : StorageLifecycleRiskLevel.NORMAL;
    }

    protected BucketInstrument bucketKernel() {
        BucketInstrument bucketInstrument = this.mFileSystem == null ? null : this.mFileSystem.bucketInstrument();
        if ( bucketInstrument == null ) {
            throw new IllegalStateException( "Titan storage bucket kernel is not ready." );
        }
        return bucketInstrument;
    }

    protected FileMasterManipulator fileMasterManipulator() {
        FileMasterManipulator manipulator = this.mFileSystem == null ? null : this.mFileSystem.getFileMasterManipulator();
        if ( manipulator == null ) {
            throw new IllegalStateException( "Titan UOFS file master manipulator is not ready." );
        }
        return manipulator;
    }

    protected long safeCountBuckets() {
        try {
            return this.bucketKernel().count( null, null, null );
        }
        catch ( RuntimeException e ) {
            return 0L;
        }
    }

    protected long safeCountReadyVolumes() {
        try {
            return this.mVolumeManager.countVolumes( null, null, null, VolumeStatus.READY );
        }
        catch ( RuntimeException e ) {
            return 0L;
        }
    }

    protected long safeCountReadyPhysicals() {
        try {
            return this.mVolumeManager.countPhysicals( null, null, VolumePhysicalStatus.READY, null );
        }
        catch ( RuntimeException e ) {
            return 0L;
        }
    }

    protected String snapshotReport( BucketPurgeReport report ) {
        Map<String, Object> ret = new LinkedHashMap<>();
        ret.put( "bucketGuid", report.getBucketGuid() == null ? null : report.getBucketGuid().toString() );
        ret.put( "operation", report.getOperation() == null ? null : report.getOperation().name() );
        ret.put( "phase", report.getPhase() == null ? null : report.getPhase().name() );
        ret.put( "totalCount", report.getTotalCount() );
        ret.put( "doneCount", report.getDoneCount() );
        ret.put( "failed", report.getFailed() );
        ret.put( "message", report.getMessage() );
        return this.snapshot( ret );
    }

    protected String snapshotPlan( StorageLifecyclePlan plan ) {
        Map<String, Object> ret = new LinkedHashMap<>();
        ret.put( "taskType", plan.getTaskType() == null ? null : plan.getTaskType().name() );
        ret.put( "targetType", plan.getTargetType() == null ? null : plan.getTargetType().name() );
        ret.put( "targetGuid", plan.getTargetGuid() == null ? null : plan.getTargetGuid().toString() );
        ret.put( "targetName", plan.getTargetName() );
        ret.put( "operationMode", plan.getOperationMode() == null ? null : plan.getOperationMode().name() );
        ret.put( "status", plan.getStatus() == null ? null : plan.getStatus().name() );
        ret.put( "riskLevel", plan.getRiskLevel() == null ? null : plan.getRiskLevel().name() );
        ret.put( "estimatedTotalCount", plan.getEstimatedTotalCount() );
        ret.put( "executable", plan.isExecutable() );
        ret.put( "blockers", plan.getBlockers() );
        ret.put( "warnings", plan.getWarnings() );
        ret.put( "actions", plan.getActions() );
        return this.snapshot( ret );
    }

    protected String snapshot( Object value ) {
        if ( value == null ) {
            return "null";
        }
        if ( value instanceof Map ) {
            StringBuilder builder = new StringBuilder( "{" );
            boolean first = true;
            for ( Object entryObject : ( (Map<?, ?>) value ).entrySet() ) {
                Map.Entry<?, ?> entry = (Map.Entry<?, ?>) entryObject;
                if ( !first ) {
                    builder.append( "," );
                }
                first = false;
                builder.append( this.snapshot( String.valueOf( entry.getKey() ) ) );
                builder.append( ":" );
                builder.append( this.snapshot( entry.getValue() ) );
            }
            builder.append( "}" );
            return builder.toString();
        }
        if ( value instanceof Iterable ) {
            StringBuilder builder = new StringBuilder( "[" );
            boolean first = true;
            for ( Object item : (Iterable<?>) value ) {
                if ( !first ) {
                    builder.append( "," );
                }
                first = false;
                builder.append( this.snapshot( item ) );
            }
            builder.append( "]" );
            return builder.toString();
        }
        if ( value instanceof StorageLifecycleBlocker ) {
            StorageLifecycleBlocker issue = (StorageLifecycleBlocker) value;
            Map<String, Object> ret = new LinkedHashMap<>();
            ret.put( "code", issue.getCode() );
            ret.put( "messageKey", issue.getMessageKey() );
            ret.put( "message", issue.getMessage() );
            ret.put( "count", issue.getCount() );
            ret.put( "dependencies", issue.getDependencies() );
            ret.put( "hasMore", issue.isHasMore() );
            return this.snapshot( ret );
        }
        if ( value instanceof StorageLifecycleDependency ) {
            StorageLifecycleDependency dependency = (StorageLifecycleDependency) value;
            Map<String, Object> ret = new LinkedHashMap<>();
            ret.put( "type", dependency.getType() );
            ret.put( "guid", dependency.getGuid() );
            ret.put( "name", dependency.getName() );
            ret.put( "status", dependency.getStatus() );
            ret.put( "relation", dependency.getRelation() );
            ret.put( "ext", dependency.getExt() );
            return this.snapshot( ret );
        }
        if ( value instanceof Number || value instanceof Boolean ) {
            return String.valueOf( value );
        }
        return "\"" + String.valueOf( value ).replace( "\\", "\\\\" ).replace( "\"", "\\\"" ) + "\"";
    }

    protected void requireTaskManipulator() {
        if ( this.mTaskManipulator == null ) {
            throw new IllegalStateException( "Storage lifecycle task manipulator is not configured." );
        }
    }
}
