package com.pinecone.hydra.task.kom.instance;

import com.pinecone.framework.system.Nullable;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.system.ko.MetaPersistenceException;
import com.pinecone.hydra.task.TaskInstanceStatus;
import com.pinecone.hydra.task.kom.TaskInstrument;
import com.pinecone.hydra.task.kom.entity.TaskElement;
import com.pinecone.hydra.task.kom.instance.source.InstanceNodeManipulator;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;
import com.pinecone.slime.meta.TableIndexMeta;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public class KernelInstanceInstrument implements InstanceInstrument {

    protected InstanceNodeManipulator   mInstanceManipulator;

    protected TaskInstrument            mTaskInstrument;


    public KernelInstanceInstrument( TaskInstrument instrument, InstanceNodeManipulator manipulator ) {
        this.mTaskInstrument = instrument;
        this.mInstanceManipulator = manipulator;
    }

    @Override
    public TaskInstrument getTaskInstrument() {
        return this.mTaskInstrument;
    }

    @Override
    public void addInstance( InstanceEntry instanceEntry ) {
        this.mInstanceManipulator.insert( instanceEntry );
    }

    @Override
    public void addInstance( GUID taskGuid, InstanceEntry instanceEntry ) {
        instanceEntry.setTaskGuid( taskGuid );
        if ( instanceEntry.getGuid() == null ) {
            instanceEntry.setGuid( this.mTaskInstrument.getGuidAllocator().nextGUID() );
        }
        this.addInstance( instanceEntry );
    }

    @Override
    public void updateInstance( InstanceEntry instanceEntry ) throws MetaPersistenceException {
        try {
            this.mInstanceManipulator.update( instanceEntry );
        }
        catch ( Exception e ) {
            throw new MetaPersistenceException( e );
        }
    }

    @Override
    public InstanceEntry getInstanceEntry( GUID insGuid ) {
        return this.mInstanceManipulator.queryByGuid( insGuid, this.mTaskInstrument );
    }

    @Override
    public long countInstances( TaskInstanceQuery query ) {
        if ( query == null ) {
            query = new TaskInstanceQuery();
        }
        return this.mInstanceManipulator.countInstances( query );
    }

    @Override
    public List<InstanceEntry> fetchInstances( TaskInstanceQuery query ) {
        if ( query == null ) {
            query = new TaskInstanceQuery();
        }
        return this.mInstanceManipulator.fetchInstances( this.mTaskInstrument, query );
    }

    @Override
    public List<InstanceEntry> fetchInstanceDigests( TaskInstanceQuery query ) {
        if ( query == null ) {
            query = new TaskInstanceQuery();
        }
        return this.mInstanceManipulator.fetchInstanceDigests( query );
    }

    @Override
    public List<InstanceEntry> queryInstances( String taskPath, long offset, long pageSize ) {
        GUID guid = this.mTaskInstrument.queryGUIDByPath( taskPath );
        if ( guid == null ) {
            return null;
        }
        return this.mInstanceManipulator.queryByTaskGuid( this.mTaskInstrument, guid, offset, pageSize );
    }

    @Override
    public List<InstanceEntry> queryInstances( GUID taskGuid, long offset, long pageSize ) {
        return this.mInstanceManipulator.queryByTaskGuid( this.mTaskInstrument, taskGuid, offset, pageSize );
    }

    @Override
    public InstanceEntry queryInstanceByTaskGuidAndExpectTime( GUID taskGuid, LocalDateTime expectTime ) {
        return this.mInstanceManipulator.queryByTaskGuidAndExpectTime( this.mTaskInstrument, taskGuid, expectTime );
    }

    @Override
    public InstanceEntry queryInstanceByTaskGuidAndBusinessTime( GUID taskGuid, LocalDateTime businessTime ) {
        return this.mInstanceManipulator.queryByTaskGuidAndBusinessTime( this.mTaskInstrument, taskGuid, businessTime );
    }

    @Override
    public int transitStatus( GUID instanceGuid, TaskInstanceStatus fromStatus, TaskInstanceStatus toStatus ) {
        return this.mInstanceManipulator.transitStatus( instanceGuid, fromStatus, toStatus );
    }

    @Override
    public int transitStatusWithScheduleTime(
            GUID instanceGuid, TaskInstanceStatus fromStatus, TaskInstanceStatus toStatus, LocalDateTime scheduleTime
    ) {
        return this.mInstanceManipulator.transitStatusWithScheduleTime( instanceGuid, fromStatus, toStatus, scheduleTime );
    }

    @Override
    public int transitStatusIn( GUID instanceGuid, Collection<TaskInstanceStatus> fromStatuses, TaskInstanceStatus toStatus ) {
        return this.mInstanceManipulator.transitStatusIn( instanceGuid, fromStatuses, toStatus );
    }

    @Override
    public int transitStatusInMonotonic( GUID instanceGuid, Collection<TaskInstanceStatus> fromStatuses, TaskInstanceStatus toStatus ) {
        return this.mInstanceManipulator.transitStatusInMonotonic( instanceGuid, fromStatuses, toStatus );
    }

    @Override
    public int transitStatusInMonotonicWithFields(
            GUID instanceGuid,
            Collection<TaskInstanceStatus> fromStatuses,
            TaskInstanceStatus toStatus,
            LocalDateTime scheduleTime,
            LocalDateTime latestStartTime,
            LocalDateTime latestEndTime,
            LocalDateTime finishTime,
            String errorCause
    ) {
        return this.mInstanceManipulator.transitStatusInMonotonicWithFields(
                instanceGuid, fromStatuses, toStatus, scheduleTime, latestStartTime, latestEndTime, finishTime, errorCause
        );
    }

    @Override
    public int resetForRetry(
            GUID instanceGuid, int currentRetryCnt, LocalDateTime expectTime, LocalDateTime fireTime, LocalDateTime scheduleTime
    ) {
        return this.mInstanceManipulator.resetForRetry( instanceGuid, currentRetryCnt, expectTime, fireTime, scheduleTime );
    }

    @Override
    public int resetForSequence(
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
    ) {
        return this.mInstanceManipulator.resetForSequence(
                instanceGuid,
                currentSequenceCnt,
                expectTime,
                fireTime,
                scheduleTime,
                imagePath,
                execArch,
                priority,
                actuallyPriority,
                dryRun,
                timeoutSeconds,
                retryTimes,
                retryIntervalSeconds,
                taskType,
                designatedProcessor
        );
    }

    @Override
    public long countInstanceByGuid( GUID taskGuid ) {
        return this.mInstanceManipulator.countInstanceByTaskGuid( taskGuid );
    }

    @Override
    public InstanceEntry makeInstanceEntry( GUID taskGuid, @Nullable String insName, @Nullable LocalDateTime bizTime ) {
        TreeNode tn = this.mTaskInstrument.get( taskGuid );
        if ( tn instanceof TaskElement ) {
            TaskElement taskElement = (TaskElement) tn;
            InstanceEntry instanceEntry = new GenericInstanceEntry( this.mTaskInstrument, taskElement );
            instanceEntry.setTaskGuid( taskGuid );
            instanceEntry.setGuid( this.mTaskInstrument.getGuidAllocator().nextGUID() );
            instanceEntry.setPriority( taskElement.getPriority() );
            instanceEntry.setActuallyPriority( taskElement.getPriority() );
            instanceEntry.setTaskType( taskElement.getType() );
//            instanceEntry.setInstanceName( taskElement.getName() );
//            instanceEntry.setBusinessTime( taskElement.getBusinessTime() );
//            instanceEntry.setScheduleCycleCode( taskElement.getScheduleCycleCode() );
            instanceEntry.setScheduleCycle( taskElement.getScheduleCycle() );
            instanceEntry.setScheduleType( taskElement.getScheduleType() );
            instanceEntry.setRunCount( 0 );
            instanceEntry.setDryRun( taskElement.isDryRun() );
            instanceEntry.setTimeoutSeconds( taskElement.getTimeoutSeconds() );
            instanceEntry.setRetryTimes( taskElement.getRetryTimes() );
            instanceEntry.setRetryIntervalSeconds( taskElement.getRetryIntervalSeconds() );
            instanceEntry.setInstanceStatus( TaskInstanceStatus.New );
            return instanceEntry;
        }
        return null;
    }

    @Override
    public void removeInstance( GUID insGuid ) {
        this.mInstanceManipulator.remove( insGuid );
    }

    @Override
    public InstanceEntry findLastExecuted( GUID taskGuid, String bizTime ) {
        return this.mInstanceManipulator.findLastExecuted( taskGuid, this.mTaskInstrument, bizTime );
    }





    @Override
    public TableIndexMeta querySchedulableIdRange( Collection<TaskInstanceStatus> runStatuses, LocalDateTime targetTime ) {
        return this.mInstanceManipulator.selectSchedulableIdRange( runStatuses, targetTime, null );
    }

    @Override
    public List<InstanceEntry> fetchSchedulableInstances(
            long idMin, long idMax, Collection<TaskInstanceStatus> runStatuses, LocalDateTime targetTime
    ) {
        return this.mInstanceManipulator.fetchSchedulableInstances( this.mTaskInstrument, idMin, idMax, runStatuses, targetTime, null );
    }

    @Override
    public TableIndexMeta querySchedulableIdRange( Collection<TaskInstanceStatus> runStatuses, LocalDateTime targetTime, short actuallyPriority ) {
        return this.mInstanceManipulator.selectSchedulableIdRange( runStatuses, targetTime, actuallyPriority );
    }

    @Override
    public List<InstanceEntry> fetchSchedulableInstances(
            long idMin, long idMax, Collection<TaskInstanceStatus> runStatuses, LocalDateTime targetTime, short actuallyPriority
    ) {
        return this.mInstanceManipulator.fetchSchedulableInstances( this.mTaskInstrument, idMin, idMax, runStatuses, targetTime, actuallyPriority );
    }

    @Override
    public TableIndexMeta queryRetryableTerminalIdRange( Collection<TaskInstanceStatus> runStatuses, LocalDateTime targetTime ) {
        return this.mInstanceManipulator.selectRetryableTerminalIdRange( runStatuses, targetTime );
    }

    @Override
    public List<InstanceEntry> fetchRetryableTerminalInstances(
            long idMin, long idMax, Collection<TaskInstanceStatus> runStatuses, LocalDateTime targetTime
    ) {
        return this.mInstanceManipulator.fetchRetryableTerminalInstances( this.mTaskInstrument, idMin, idMax, runStatuses, targetTime );
    }

    @Override
    public TableIndexMeta queryTimedOutRunningIdRange( LocalDateTime targetTime ) {
        return this.mInstanceManipulator.selectTimedOutRunningIdRange( targetTime );
    }

    @Override
    public List<InstanceEntry> fetchTimedOutRunningInstances( long idMin, long idMax, LocalDateTime targetTime ) {
        return this.mInstanceManipulator.fetchTimedOutRunningInstances( this.mTaskInstrument, idMin, idMax, targetTime );
    }


}
