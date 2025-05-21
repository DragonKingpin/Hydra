package com.pinecone.hydra.task.kom.instance;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.task.kom.TaskInstrument;
import com.pinecone.hydra.task.kom.instance.source.InstanceNodeManipulator;

import java.util.List;

public class KernelInstanceInstrument implements InstanceInstrument {

    protected InstanceNodeManipulator instanceManipulator;

    protected TaskInstrument taskInstrument;

    public KernelInstanceInstrument( TaskInstrument instrument, InstanceNodeManipulator manipulator ) {
        this.taskInstrument      = instrument;
        this.instanceManipulator = manipulator;
    }

    @Override
    public TaskInstrument getTaskInstrument() {
        return this.taskInstrument;
    }

    @Override
    public void addInstance( InstanceEntry instanceEntry ) {
        this.instanceManipulator.insert( instanceEntry );
    }

    @Override
    public void addInstance( GUID taskGuid, InstanceEntry instanceEntry ) {
        instanceEntry.setAffiliatedTaskGuid( taskGuid );
        if ( instanceEntry.getGuid() == null ) {
            instanceEntry.setGuid( this.taskInstrument.getGuidAllocator().nextGUID() );
        }
        this.addInstance( instanceEntry );
    }

    @Override
    public void updateInstance( InstanceEntry instanceEntry ) {
        this.instanceManipulator.update( instanceEntry );
    }

    @Override
    public List<InstanceEntry> queryInstances( String taskPath, long offset, long pageSize ) {
        GUID guid = this.taskInstrument.queryGUIDByPath( taskPath );
        if ( guid == null ) {
            return null;
        }
        return this.instanceManipulator.queryByTaskGuid( this.taskInstrument, guid, offset, pageSize );
    }

    @Override
    public List<InstanceEntry> queryInstances( GUID taskGuid, long offset, long pageSize ) {
        return this.instanceManipulator.queryByTaskGuid( this.taskInstrument, taskGuid, offset, pageSize );
    }

    @Override
    public long countInstanceByGuid( GUID taskGuid ) {
        return this.instanceManipulator.countInstanceByTaskGuid( taskGuid );
    }




}
