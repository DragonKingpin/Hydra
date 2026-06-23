package com.walnut.odin.task.troll;

import com.pinecone.framework.system.Nullable;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.framework.util.id.Identification;
import com.pinecone.hydra.system.ko.MetaPersistenceException;
import com.pinecone.hydra.task.TaskInstanceStatus;
import com.pinecone.hydra.task.kom.entity.ArchElementNode;
import com.pinecone.hydra.task.kom.entity.TaskElement;
import com.pinecone.hydra.task.kom.instance.GenericInstanceEntry;
import com.pinecone.hydra.task.kom.instance.InstanceEntry;
import com.pinecone.hydra.task.kom.instance.InstanceInstrument;
import com.walnut.odin.task.CentralizedTaskInstrument;
import com.walnut.odin.task.RavenTaskInstance;
import com.walnut.odin.task.RavenTask;

import java.time.LocalDateTime;
import java.util.Map;

public class GenericRavenTask extends ArchRavenTask implements RavenTask {
    protected CentralizedTaskInstrument  mTaskInstrument;

    protected InstanceInstrument         mInstanceInstrument;

    protected GuidAllocator              mGuidAllocator;

    public GenericRavenTask(CentralizedTaskInstrument taskInstrument, @Nullable Identification serviceId, TaskElement serviceElement, Map<String, Object> metaDataScope ) {
        super( serviceId, serviceElement, metaDataScope );
        this.mTaskInstrument       = taskInstrument;
        this.mInstanceInstrument   = taskInstrument.getInstanceInstrument();
        this.mGuidAllocator        = taskInstrument.getGuidAllocator();
        if ( this.mTaskElement instanceof ArchElementNode ) {
            ( (ArchElementNode) this.mTaskElement ).apply( taskInstrument );
        }
    }

    public GenericRavenTask( CentralizedTaskInstrument taskInstrument, @Nullable Identification serviceId, TaskElement serviceElement ) {
        this( taskInstrument, serviceId, serviceElement, null );
    }

    public GenericRavenTask( CentralizedTaskInstrument taskInstrument, TaskElement serviceElement ) {
        this( taskInstrument, null, serviceElement );
    }



    @Override
    public RavenTaskInstance createInstance() {
        GUID guid = this.mGuidAllocator.nextGUID();
        GenericInstanceEntry entry = new GenericInstanceEntry( this.mTaskInstrument, this.mTaskElement );
        entry.setGuid( guid );
        entry.setActuallyPriority( this.mTaskElement.getActuallyPriority() );
        entry.setImagePath( this.mTaskElement.getImagePath() );
        entry.setExecArch( this.mTaskElement.getExecArch() );
        entry.setTaskGuid( this.mTaskElement.getGuid());
        entry.setCreateTime( LocalDateTime.now() );
        entry.setScheduleCycle( this.mTaskElement.getScheduleCycle() );
        entry.setScheduleType( this.mTaskElement.getScheduleType() );
        entry.setRunCount( 1 );
        entry.setSequenceCnt( 1 );
        entry.setRetryCnt( 0 );
        entry.setTaskType( this.mTaskElement.getType() );
        entry.setInstanceStatus( TaskInstanceStatus.New );
        entry.setTaskName( this.mTaskElement.getName() );
        entry.setAffinityProcessor( this.mTaskElement.getProcessorName() );

        GenericRavenTaskInstance instance = new GenericRavenTaskInstance( entry, this );
        return instance;
    }

    public RavenTaskInstance constructInstance( InstanceEntry instanceEntry ) {
        return this.constructInstance( instanceEntry, null );
    }

    public RavenTaskInstance constructInstance( InstanceEntry instanceEntry, Identification serviceId ) {
        if ( serviceId == null ) {
            //serviceId = taskElement.
        }

        return null;
    }

    public RavenTask createInstance( InstanceEntry instanceEntry, Identification serviceId ) {
//        RavenTaskInstance task = this.constructInstance( taskElement, serviceId );
//
//
//        return task;
        return null;
    }

    public void removeInstance( GUID insGuid ) {
       this.mInstanceInstrument.removeInstance( insGuid );
    }

    public void updateInstanceMeta( RavenTaskInstance instance ) throws MetaPersistenceException {
        this.updateTaskMeta( instance.getInstanceEntry() );
    }

    public void updateTaskMeta( InstanceEntry instanceEntry ) throws MetaPersistenceException {
        this.mInstanceInstrument.updateInstance( instanceEntry );
    }

}
