package com.pinecone.hydra.task.kom.instance;

import com.pinecone.framework.system.Nullable;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.task.TaskInstanceStatus;
import com.pinecone.hydra.task.kom.TaskInstrument;
import com.pinecone.hydra.task.kom.entity.TaskElement;
import com.pinecone.hydra.task.kom.instance.source.InstanceNodeManipulator;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;

import java.time.LocalDateTime;
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
        instanceEntry.setAffiliatedTaskGuid( taskGuid );
        if ( instanceEntry.getGuid() == null ) {
            instanceEntry.setGuid( this.mTaskInstrument.getGuidAllocator().nextGUID() );
        }
        this.addInstance( instanceEntry );
    }

    @Override
    public void updateInstance( InstanceEntry instanceEntry ) {
        this.mInstanceManipulator.update( instanceEntry );
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
    public long countInstanceByGuid( GUID taskGuid ) {
        return this.mInstanceManipulator.countInstanceByTaskGuid( taskGuid );
    }

    @Override
    public InstanceEntry makeInstanceEntry( GUID taskGuid, @Nullable String insName, @Nullable LocalDateTime bizTime ) {
        TreeNode tn = this.mTaskInstrument.get( taskGuid );
        if ( tn instanceof TaskElement ) {
            TaskElement taskElement = (TaskElement) tn;
            InstanceEntry instanceEntry = new GenericInstanceEntry( this.mTaskInstrument);
            instanceEntry.setAffiliatedTaskGuid( taskGuid );
            instanceEntry.setGuid( this.mTaskInstrument.getGuidAllocator().nextGUID() );
            instanceEntry.setPriority( taskElement.getPriority() );
            instanceEntry.setActuallyPriority( taskElement.getPriority() );
            instanceEntry.setTaskType( taskElement.getType() );
//            instanceEntry.setInstanceName( taskElement.getName() );
//            instanceEntry.setBusinessTime( taskElement.getBusinessTime() );
//            instanceEntry.setScheduleCycleCode( taskElement.getScheduleCycleCode() );
            instanceEntry.setKernelScheduleCycle( taskElement.getScheduleCycle() );
            instanceEntry.setKernelScheduleType( taskElement.getScheduleType() );
            instanceEntry.setRunCount( 0 );
            instanceEntry.setDryRun( taskElement.isDryRun() );
            instanceEntry.setInstanceStatus( TaskInstanceStatus.New );
            return instanceEntry;
        }
        return null;
    }

    @Override
    public void removeInstance(GUID insGuid) {
        this.mInstanceManipulator.remove( insGuid );
    }
}
