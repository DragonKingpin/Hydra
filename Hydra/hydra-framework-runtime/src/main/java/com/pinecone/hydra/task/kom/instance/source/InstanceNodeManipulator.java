package com.pinecone.hydra.task.kom.instance.source;

import com.pinecone.framework.system.Nullable;
import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.task.TaskInstanceStatus;
import com.pinecone.hydra.task.kom.TaskInstrument;
import com.pinecone.hydra.task.kom.instance.InstanceEntry;
import com.pinecone.hydra.task.kom.instance.TaskInstanceQuery;
import com.pinecone.slime.meta.TableIndexMeta;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface InstanceNodeManipulator extends Pinenut {

    void insert( InstanceEntry instanceEntry );

    void update( InstanceEntry instanceEntry );

    InstanceEntry queryByGuid( GUID guid, TaskInstrument instrument );

    int countInstance();

    long countInstanceByName( String name );

    long countInstances( TaskInstanceQuery query );

    List<InstanceEntry> fetchInstances( TaskInstrument instrument, TaskInstanceQuery query );

    List<InstanceEntry> fetchInstanceDigests( TaskInstanceQuery query );

    List<InstanceEntry> fetchInstances( TaskInstrument instrument, long offset, long pageSize );

    default List<InstanceEntry> fetchInstances( TaskInstrument instrument ) {
        return this.fetchInstances( instrument, 0, this.countInstance() );
    }

    List<InstanceEntry> queryByTaskGuid( TaskInstrument instrument, GUID taskGuid, long offset, long pageSize );

    InstanceEntry queryByTaskGuidAndExpectTime( TaskInstrument instrument, GUID taskGuid, LocalDateTime expectTime );

    InstanceEntry queryByTaskGuidAndBusinessTime( TaskInstrument instrument, GUID taskGuid, LocalDateTime businessTime );

    int transitStatus( GUID instanceGuid, TaskInstanceStatus fromStatus, TaskInstanceStatus toStatus );

    int transitStatusWithScheduleTime(
            GUID instanceGuid, TaskInstanceStatus fromStatus, TaskInstanceStatus toStatus, LocalDateTime scheduleTime
    );

    int transitStatusIn( GUID instanceGuid, Collection<TaskInstanceStatus> fromStatuses, TaskInstanceStatus toStatus );

    long countInstanceByTaskGuid( GUID taskGuid );

    void remove( GUID guid );

    InstanceEntry findLastExecuted( GUID taskGuid, TaskInstrument instrument, String bizTime );


    TableIndexMeta selectSchedulableIdRange( Collection<TaskInstanceStatus> runStatuses, LocalDateTime targetTime, @Nullable Short actuallyPriority );

    List<InstanceEntry> fetchSchedulableInstances(
            TaskInstrument instrument,
            long idMin, long idMax, Collection<TaskInstanceStatus> runStatuses, LocalDateTime targetTime, @Nullable Short actuallyPriority
    );
}
