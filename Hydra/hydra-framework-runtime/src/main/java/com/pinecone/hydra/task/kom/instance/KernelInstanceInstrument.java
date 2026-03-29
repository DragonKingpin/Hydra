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
    public TableIndexMeta querySchedulableIdRange( TaskInstanceStatus runStatus, LocalDateTime targetTime ) {
        return this.mInstanceManipulator.selectSchedulableIdRange( runStatus, targetTime, null );
    }

    @Override
    public List<InstanceEntry> fetchSchedulableInstances(
            long idMin, long idMax, TaskInstanceStatus runStatus, LocalDateTime targetTime
    ) {
        return this.mInstanceManipulator.fetchSchedulableInstances( this.mTaskInstrument, idMin, idMax, runStatus, targetTime, null );
    }

    @Override
    public TableIndexMeta querySchedulableIdRange( TaskInstanceStatus runStatus, LocalDateTime targetTime, short actuallyPriority ) {
        return this.mInstanceManipulator.selectSchedulableIdRange( runStatus, targetTime, actuallyPriority );
    }

    @Override
    public List<InstanceEntry> fetchSchedulableInstances(
            long idMin, long idMax, TaskInstanceStatus runStatus, LocalDateTime targetTime, short actuallyPriority
    ) {
        return this.mInstanceManipulator.fetchSchedulableInstances( this.mTaskInstrument, idMin, idMax, runStatus, targetTime, actuallyPriority );
    }


}
