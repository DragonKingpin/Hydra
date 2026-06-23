package com.pinecone.hydra.task.ibatis;

import com.pinecone.framework.system.Nullable;
import com.pinecone.framework.util.CollectionUtils;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.task.TaskInstanceStatus;
import com.pinecone.hydra.task.kom.TaskInstrument;
import com.pinecone.hydra.task.kom.instance.GenericInstanceEntry;
import com.pinecone.hydra.task.kom.instance.InstanceEntry;
import com.pinecone.hydra.task.kom.instance.TaskInstanceQuery;
import com.pinecone.hydra.task.kom.instance.source.InstanceNodeManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import com.pinecone.slime.meta.TableIndex64Meta;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Mapper
@IbatisDataAccessObject
public interface InstanceNodeMapper extends InstanceNodeManipulator {

    @Override
    void insert( InstanceEntry instance );

    void update( InstanceEntry instance );

    GenericInstanceEntry queryByGuid0( GUID guid );

    @Override
    default InstanceEntry queryByGuid( GUID guid, TaskInstrument instrument ) {
        GenericInstanceEntry entry = this.queryByGuid0( guid );
        if ( entry == null ) {
            return null;
        }
        entry.apply( instrument );
        return entry;
    }



    int countInstance();

    long countInstanceByName( String name );

    long countInstances0( @Param( "query" ) TaskInstanceQuery query );

    @Override
    default long countInstances( TaskInstanceQuery query ) {
        return this.countInstances0( query );
    }

    List<GenericInstanceEntry> fetchInstancesByQuery0( @Param( "query" ) TaskInstanceQuery query );

    @Override
    @SuppressWarnings( "unchecked" )
    default List<InstanceEntry> fetchInstances( TaskInstrument instrument, TaskInstanceQuery query ) {
        List<GenericInstanceEntry> list = this.fetchInstancesByQuery0( query );
        for ( GenericInstanceEntry entry : list ) {
            entry.apply( instrument );
        }
        return (List) list;
    }

    @Override
    @SuppressWarnings( "unchecked" )
    default List<InstanceEntry> fetchInstanceDigests( TaskInstanceQuery query ) {
        return (List) this.fetchInstancesByQuery0( query );
    }

    List<GenericInstanceEntry> fetchInstances0( @Param("offset") long offset, @Param("pageSize") long pageSize );

    @Override
    @SuppressWarnings( "unchecked" )
    default List<InstanceEntry> fetchInstances( TaskInstrument instrument, long offset, long pageSize ) {
        List<GenericInstanceEntry> list = this.fetchInstances0( offset, pageSize );
        for ( GenericInstanceEntry entry : list ) {
            entry.apply( instrument );
        }
        return (List) list;
    }

    List<GenericInstanceEntry> queryByTaskGuid0( @Param("taskGuid") GUID taskGuid, @Param("offset") long offset, @Param("pageSize") long pageSize );

    @Override
    @SuppressWarnings( "unchecked" )
    default List<InstanceEntry> queryByTaskGuid( TaskInstrument instrument, GUID taskGuid, long offset, long pageSize ) {
        List<GenericInstanceEntry> list = this.queryByTaskGuid0( taskGuid, offset, pageSize );
        for ( GenericInstanceEntry entry : list ) {
            entry.apply( instrument );
        }
        return (List) list;
    }

    GenericInstanceEntry queryByTaskGuidAndExpectTime0(
            @Param("taskGuid") GUID taskGuid,
            @Param("expectTime") LocalDateTime expectTime
    );

    @Override
    default InstanceEntry queryByTaskGuidAndExpectTime( TaskInstrument instrument, GUID taskGuid, LocalDateTime expectTime ) {
        GenericInstanceEntry entry = this.queryByTaskGuidAndExpectTime0( taskGuid, expectTime );
        if ( entry == null ) {
            return null;
        }
        entry.apply( instrument );
        return entry;
    }

    GenericInstanceEntry queryByTaskGuidAndBusinessTime0(
            @Param("taskGuid") GUID taskGuid,
            @Param("businessTime") LocalDateTime businessTime
    );

    @Override
    default InstanceEntry queryByTaskGuidAndBusinessTime( TaskInstrument instrument, GUID taskGuid, LocalDateTime businessTime ) {
        GenericInstanceEntry entry = this.queryByTaskGuidAndBusinessTime0( taskGuid, businessTime );
        if ( entry == null ) {
            return null;
        }
        entry.apply( instrument );
        return entry;
    }

    int transitStatus0(
            @Param( "instanceGuid" ) GUID instanceGuid,
            @Param( "fromStatus" ) String szFromStatus,
            @Param( "toStatus" ) String szToStatus
    );

    @Override
    default int transitStatus( GUID instanceGuid, TaskInstanceStatus fromStatus, TaskInstanceStatus toStatus ) {
        return this.transitStatus0( instanceGuid, fromStatus.getName(), toStatus.getName() );
    }

    int transitStatusWithScheduleTime0(
            @Param( "instanceGuid" ) GUID instanceGuid,
            @Param( "fromStatus" ) String szFromStatus,
            @Param( "toStatus" ) String szToStatus,
            @Param( "scheduleTime" ) LocalDateTime scheduleTime
    );

    @Override
    default int transitStatusWithScheduleTime(
            GUID instanceGuid, TaskInstanceStatus fromStatus, TaskInstanceStatus toStatus, LocalDateTime scheduleTime
    ) {
        return this.transitStatusWithScheduleTime0( instanceGuid, fromStatus.getName(), toStatus.getName(), scheduleTime );
    }

    int transitStatusIn0(
            @Param( "instanceGuid" ) GUID instanceGuid,
            @Param( "fromStatuses" ) Collection<String> fromStatuses,
            @Param( "toStatus" ) String szToStatus
    );

    @Override
    default int transitStatusIn( GUID instanceGuid, Collection<TaskInstanceStatus> fromStatuses, TaskInstanceStatus toStatus ) {
        Collection<String> szFromStatuses = fromStatuses.stream().map( TaskInstanceStatus::getName ).collect( Collectors.toList() );
        return this.transitStatusIn0( instanceGuid, szFromStatuses, toStatus.getName() );
    }

    int transitStatusInMonotonic0(
            @Param( "instanceGuid" ) GUID instanceGuid,
            @Param( "fromStatuses" ) Collection<String> fromStatuses,
            @Param( "toStatus" ) String szToStatus
    );

    @Override
    default int transitStatusInMonotonic( GUID instanceGuid, Collection<TaskInstanceStatus> fromStatuses, TaskInstanceStatus toStatus ) {
        Collection<String> szFromStatuses = fromStatuses.stream().map( TaskInstanceStatus::getName ).collect( Collectors.toList() );
        return this.transitStatusInMonotonic0( instanceGuid, szFromStatuses, toStatus.getName() );
    }

    int transitStatusInMonotonicWithFields0(
            @Param( "instanceGuid" ) GUID instanceGuid,
            @Param( "fromStatuses" ) Collection<String> fromStatuses,
            @Param( "toStatus" ) String szToStatus,
            @Param( "scheduleTime" ) LocalDateTime scheduleTime,
            @Param( "latestStartTime" ) LocalDateTime latestStartTime,
            @Param( "latestEndTime" ) LocalDateTime latestEndTime,
            @Param( "finishTime" ) LocalDateTime finishTime,
            @Param( "errorCause" ) String szErrorCause
    );

    @Override
    default int transitStatusInMonotonicWithFields(
            GUID instanceGuid,
            Collection<TaskInstanceStatus> fromStatuses,
            TaskInstanceStatus toStatus,
            LocalDateTime scheduleTime,
            LocalDateTime latestStartTime,
            LocalDateTime latestEndTime,
            LocalDateTime finishTime,
            String errorCause
    ) {
        Collection<String> szFromStatuses = fromStatuses.stream().map( TaskInstanceStatus::getName ).collect( Collectors.toList() );
        return this.transitStatusInMonotonicWithFields0(
                instanceGuid, szFromStatuses, toStatus.getName(), scheduleTime, latestStartTime, latestEndTime, finishTime, errorCause
        );
    }

    int resetForRetry0(
            @Param( "instanceGuid" ) GUID instanceGuid,
            @Param( "currentRetryCnt" ) int nCurrentRetryCnt,
            @Param( "expectTime" ) LocalDateTime expectTime,
            @Param( "fireTime" ) LocalDateTime fireTime,
            @Param( "scheduleTime" ) LocalDateTime scheduleTime
    );

    @Override
    default int resetForRetry(
            GUID instanceGuid, int currentRetryCnt, LocalDateTime expectTime, LocalDateTime fireTime, LocalDateTime scheduleTime
    ) {
        return this.resetForRetry0( instanceGuid, currentRetryCnt, expectTime, fireTime, scheduleTime );
    }

    int resetForSequence0(
            @Param( "instanceGuid" ) GUID instanceGuid,
            @Param( "currentSequenceCnt" ) int nCurrentSequenceCnt,
            @Param( "expectTime" ) LocalDateTime expectTime,
            @Param( "fireTime" ) LocalDateTime fireTime,
            @Param( "scheduleTime" ) LocalDateTime scheduleTime,
            @Param( "imagePath" ) String szImagePath,
            @Param( "execArch" ) String szExecArch,
            @Param( "priority" ) int nPriority,
            @Param( "actuallyPriority" ) int nActuallyPriority,
            @Param( "dryRun" ) boolean bDryRun,
            @Param( "timeoutSeconds" ) Long nTimeoutSeconds,
            @Param( "retryTimes" ) int nRetryTimes,
            @Param( "retryIntervalSeconds" ) Long nRetryIntervalSeconds,
            @Param( "taskType" ) String szTaskType,
            @Param( "designatedProcessor" ) String szDesignatedProcessor
    );

    @Override
    default int resetForSequence(
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
        return this.resetForSequence0(
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

    long countInstanceByTaskGuid( GUID taskGuid );

    GenericInstanceEntry findLastExecuted0( @Param("taskGuid") GUID taskGuid, @Param("bizTime") String bizTime );

    @Override
    default InstanceEntry findLastExecuted( GUID taskGuid, TaskInstrument instrument, String bizTime ) {
        GenericInstanceEntry entry = this.findLastExecuted0( taskGuid, bizTime );
        if ( entry == null ) {
            return null;
        }
        entry.apply( instrument );
        return entry;
    }





    @Override
    TableIndex64Meta selectSchedulableIdRange(
            @Param("runStatuses") Collection<TaskInstanceStatus> runStatuses, @Param("targetTime") LocalDateTime targetTime,
            @Param( "actuallyPriority" ) @Nullable Short actuallyPriority
    );

    List<GenericInstanceEntry> fetchSchedulableInstances0(
            @Param( "idMin" ) long idMin, @Param( "idMax" ) long idMax,
            @Param("runStatuses") Collection<TaskInstanceStatus> runStatuses, @Param( "targetTime" ) LocalDateTime targetTime,
            @Param( "actuallyPriority" ) @Nullable Short actuallyPriority
    );

    @Override
    default List<InstanceEntry> fetchSchedulableInstances(
            TaskInstrument instrument,
            long idMin, long idMax, Collection<TaskInstanceStatus> runStatuses, LocalDateTime targetTime, @Nullable Short actuallyPriority
    ) {
        List<GenericInstanceEntry> list = this.fetchSchedulableInstances0( idMin, idMax, runStatuses, targetTime, actuallyPriority );
        for ( GenericInstanceEntry entry : list ) {
            entry.apply( instrument );
        }
        return CollectionUtils.genericConvert( list );
    }

    @Override
    TableIndex64Meta selectRetryableTerminalIdRange(
            @Param("runStatuses") Collection<TaskInstanceStatus> runStatuses, @Param("targetTime") LocalDateTime targetTime
    );

    List<GenericInstanceEntry> fetchRetryableTerminalInstances0(
            @Param( "idMin" ) long idMin, @Param( "idMax" ) long idMax,
            @Param("runStatuses") Collection<TaskInstanceStatus> runStatuses, @Param( "targetTime" ) LocalDateTime targetTime
    );

    @Override
    default List<InstanceEntry> fetchRetryableTerminalInstances(
            TaskInstrument instrument,
            long idMin, long idMax, Collection<TaskInstanceStatus> runStatuses, LocalDateTime targetTime
    ) {
        List<GenericInstanceEntry> list = this.fetchRetryableTerminalInstances0( idMin, idMax, runStatuses, targetTime );
        for ( GenericInstanceEntry entry : list ) {
            entry.apply( instrument );
        }
        return CollectionUtils.genericConvert( list );
    }

    @Override
    TableIndex64Meta selectTimedOutRunningIdRange( @Param("targetTime") LocalDateTime targetTime );

    List<GenericInstanceEntry> fetchTimedOutRunningInstances0(
            @Param( "idMin" ) long idMin, @Param( "idMax" ) long idMax, @Param( "targetTime" ) LocalDateTime targetTime
    );

    @Override
    default List<InstanceEntry> fetchTimedOutRunningInstances(
            TaskInstrument instrument, long idMin, long idMax, LocalDateTime targetTime
    ) {
        List<GenericInstanceEntry> list = this.fetchTimedOutRunningInstances0( idMin, idMax, targetTime );
        for ( GenericInstanceEntry entry : list ) {
            entry.apply( instrument );
        }
        return CollectionUtils.genericConvert( list );
    }
}
