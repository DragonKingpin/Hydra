package com.pinecone.hydra.task.kom.instance.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.task.kom.TaskInstrument;
import com.pinecone.hydra.task.kom.instance.GenericInstanceEntry;
import com.pinecone.hydra.task.kom.instance.InstanceEntry;

import java.time.LocalDateTime;
import java.util.List;

public interface InstanceNodeManipulator extends Pinenut {

    void insert( InstanceEntry instanceEntry );

    void update( InstanceEntry instanceEntry );

    InstanceEntry queryByGuid( GUID guid, TaskInstrument instrument );

    int countInstance();

    long countInstanceByName( String name );

    List<InstanceEntry> fetchInstances( TaskInstrument instrument, long offset, long pageSize );

    default List<InstanceEntry> fetchInstances( TaskInstrument instrument ) {
        return this.fetchInstances( instrument, 0, this.countInstance() );
    }

    List<InstanceEntry> queryByTaskGuid( TaskInstrument instrument, GUID taskGuid, long offset, long pageSize );

    long countInstanceByTaskGuid( GUID taskGuid );

    void remove( GUID guid );

    InstanceEntry findLastExecuted( GUID taskGuid, TaskInstrument instrument, String bizTime );


    List<InstanceEntry> fetchExecutableInstances(TaskInstrument instrument, String runStatus, LocalDateTime targetTime, int limit );
}
