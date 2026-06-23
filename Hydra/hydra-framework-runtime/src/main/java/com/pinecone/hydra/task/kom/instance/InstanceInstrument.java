package com.pinecone.hydra.task.kom.instance;

import com.pinecone.framework.system.Nullable;
import com.pinecone.framework.system.regime.Instrument;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.system.ko.MetaPersistenceException;
import com.pinecone.hydra.task.TaskInstanceStatus;
import com.pinecone.hydra.task.kom.TaskInstrument;
import com.pinecone.slime.meta.TableIndexMeta;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface InstanceInstrument extends Instrument {


    void addInstance( InstanceEntry instanceEntry );

    void addInstance( GUID taskGuid, InstanceEntry instanceEntry );

    void updateInstance( InstanceEntry instanceEntry ) throws MetaPersistenceException;

    InstanceEntry getInstanceEntry( GUID insGuid );

    long countInstances( TaskInstanceQuery query );

    List<InstanceEntry> fetchInstances( TaskInstanceQuery query );

    List<InstanceEntry> fetchInstanceDigests( TaskInstanceQuery query );

    default TaskInstancePage pageInstances( TaskInstanceQuery query ) {
        if ( query == null ) {
            query = new TaskInstanceQuery();
        }
        long nTotal = this.countInstances( query );
        List<InstanceEntry> items = this.fetchInstances( query );
        return new TaskInstancePage( items, nTotal, query.getOffset(), query.getLimit() );
    }

    default TaskInstancePage pageInstanceDigests( TaskInstanceQuery query ) {
        if ( query == null ) {
            query = new TaskInstanceQuery();
        }
        long nTotal = this.countInstances( query );
        List<InstanceEntry> items = this.fetchInstanceDigests( query );
        return new TaskInstancePage( items, nTotal, query.getOffset(), query.getLimit() );
    }

    List<InstanceEntry> queryInstances( String taskTreePath, long offset, long pageSize );

    long countInstanceByGuid( GUID taskGuid );

    default List<InstanceEntry> queryInstances( String taskTreePath ) {
        return this.queryInstances( this.getTaskInstrument().queryGUIDByPath( taskTreePath ) );
    }

    default List<InstanceEntry> queryInstances( GUID taskGuid ) {
        return this.queryInstances( taskGuid, 0, this.countInstanceByGuid( taskGuid ) );
    }

    List<InstanceEntry> queryInstances( GUID taskGuid, long offset, long pageSize );

    InstanceEntry queryInstanceByTaskGuidAndExpectTime( GUID taskGuid, LocalDateTime expectTime );

    InstanceEntry queryInstanceByTaskGuidAndBusinessTime( GUID taskGuid, LocalDateTime businessTime );

    int transitStatus( GUID instanceGuid, TaskInstanceStatus fromStatus, TaskInstanceStatus toStatus );

    int transitStatusWithScheduleTime(
            GUID instanceGuid, TaskInstanceStatus fromStatus, TaskInstanceStatus toStatus, LocalDateTime scheduleTime
    );

    int transitStatusIn( GUID instanceGuid, Collection<TaskInstanceStatus> fromStatuses, TaskInstanceStatus toStatus );

    int transitStatusInMonotonic( GUID instanceGuid, Collection<TaskInstanceStatus> fromStatuses, TaskInstanceStatus toStatus );

    int transitStatusInMonotonicWithFields(
            GUID instanceGuid,
            Collection<TaskInstanceStatus> fromStatuses,
            TaskInstanceStatus toStatus,
            LocalDateTime scheduleTime,
            LocalDateTime latestStartTime,
            LocalDateTime latestEndTime,
            LocalDateTime finishTime,
            String errorCause
    );

    int resetForRetry( GUID instanceGuid, int currentRetryCnt, LocalDateTime expectTime, LocalDateTime fireTime, LocalDateTime scheduleTime );

    int resetForSequence(
            GUID instanceGuid,
            int currentSequenceCnt,
            LocalDateTime expectTime,
            LocalDateTime fireTime,
            LocalDateTime scheduleTime,
            String imagePath,
            String execArch,
            int priority,
            int actuallyPriority,
            boolean dryRun,
            Long timeoutSeconds,
            int retryTimes,
            Long retryIntervalSeconds,
            String taskType,
            String designatedProcessor
    );

    TaskInstrument getTaskInstrument();

    InstanceEntry makeInstanceEntry( GUID taskGuid, @Nullable String insName, @Nullable LocalDateTime bizTime );

    default InstanceEntry makeInstanceEntry( GUID taskGuid ) {
        return this.makeInstanceEntry( taskGuid, null, null );
    }

    void removeInstance( GUID insGuid );

    InstanceEntry findLastExecuted( GUID taskGuid, String bizTime );





    TableIndexMeta querySchedulableIdRange( Collection<TaskInstanceStatus> runStatuses, LocalDateTime targetTime );

    List<InstanceEntry> fetchSchedulableInstances(
            long idMin, long idMax, Collection<TaskInstanceStatus> runStatuses, LocalDateTime targetTime
    );

    TableIndexMeta querySchedulableIdRange( Collection<TaskInstanceStatus> runStatuses, LocalDateTime targetTime, short actuallyPriority );

    List<InstanceEntry> fetchSchedulableInstances(
            long idMin, long idMax, Collection<TaskInstanceStatus> runStatuses, LocalDateTime targetTime, short actuallyPriority
    );

    TableIndexMeta queryRetryableTerminalIdRange( Collection<TaskInstanceStatus> runStatuses, LocalDateTime targetTime );

    List<InstanceEntry> fetchRetryableTerminalInstances(
            long idMin, long idMax, Collection<TaskInstanceStatus> runStatuses, LocalDateTime targetTime
    );

    TableIndexMeta queryTimedOutRunningIdRange( LocalDateTime targetTime );

    List<InstanceEntry> fetchTimedOutRunningInstances( long idMin, long idMax, LocalDateTime targetTime );

}
