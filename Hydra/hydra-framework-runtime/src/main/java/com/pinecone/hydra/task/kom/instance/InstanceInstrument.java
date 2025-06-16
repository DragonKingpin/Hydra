package com.pinecone.hydra.task.kom.instance;

import com.pinecone.framework.system.Nullable;
import com.pinecone.framework.system.regime.Instrument;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.task.kom.TaskInstrument;

import java.time.LocalDateTime;
import java.util.List;

public interface InstanceInstrument extends Instrument {


    void addInstance( InstanceEntry instanceEntry );

    void addInstance( GUID taskGuid, InstanceEntry instanceEntry );

    void updateInstance( InstanceEntry instanceEntry );

    List<InstanceEntry> queryInstances( String taskTreePath, long offset, long pageSize );

    long countInstanceByGuid( GUID taskGuid );

    default List<InstanceEntry> queryInstances( String taskTreePath ) {
        return this.queryInstances( this.getTaskInstrument().queryGUIDByPath( taskTreePath ) );
    }

    default List<InstanceEntry> queryInstances( GUID taskGuid ) {
        return this.queryInstances( taskGuid, 0, this.countInstanceByGuid( taskGuid ) );
    }

    List<InstanceEntry> queryInstances( GUID taskGuid, long offset, long pageSize );

    TaskInstrument getTaskInstrument();

    InstanceEntry makeInstanceEntry( GUID taskGuid, @Nullable String insName, @Nullable LocalDateTime bizTime );

    default InstanceEntry makeInstanceEntry( GUID taskGuid ) {
        return this.makeInstanceEntry( taskGuid, null, null );
    }

    void removeInstance( GUID insGuid );


}
