package com.walnut.odin.task.troll;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.framework.util.id.Identification;
import com.pinecone.hydra.task.ArchTask;
import com.pinecone.hydra.task.TaskInstanceStatus;
import com.pinecone.hydra.task.kom.entity.TaskElement;
import com.pinecone.hydra.task.kom.instance.GenericInstanceEntry;
import com.pinecone.hydra.task.kom.instance.InstanceEntry;
import com.pinecone.hydra.task.kom.instance.InstanceInstrument;
import com.walnut.odin.task.CentralizedTaskInstrument;
import com.walnut.odin.task.RavenTaskInstance;
import com.walnut.odin.task.RavenTask;

import java.time.LocalDateTime;
import java.util.Map;

public class GenericRavenTask extends ArchTask implements RavenTask {
    protected CentralizedTaskInstrument  mTaskInstrument;

    protected InstanceInstrument         mInstanceInstrument;

    protected GuidAllocator              mGuidAllocator;

    public GenericRavenTask( CentralizedTaskInstrument taskInstrument, Identification serviceId, TaskElement serviceElement, Map<String, Object> metaDataScope ) {
        super( serviceId, serviceElement, metaDataScope );
        this.mTaskInstrument       = taskInstrument;
        this.mInstanceInstrument   = taskInstrument.getInstanceInstrument();
        this.mGuidAllocator        = taskInstrument.getGuidAllocator();
    }

    public GenericRavenTask( CentralizedTaskInstrument taskInstrument,Identification serviceId, TaskElement serviceElement ) {
        this( taskInstrument, serviceId, serviceElement, null );
    }

    @Override
    public RavenTaskInstance createInstance() {
        GUID guid = this.mGuidAllocator.nextGUID();
        GenericInstanceEntry entry = new GenericInstanceEntry();
        entry.setGuid( guid );
        entry.setActuallyPriority( this.mTaskElement.getActuallyPriority() );
        entry.setAffiliatedTaskGuid( this.mTaskElement.getGuid());
        entry.setCreateTime( LocalDateTime.now() );
        entry.setKernelScheduleCycle( this.mTaskElement.getScheduleCycle() );
        entry.setKernelScheduleCycleCode( this.mTaskElement.getScheduleCycleCode() );
        entry.setKernelScheduleType( this.mTaskElement.getScheduleType() );
        entry.setKernelScheduleTypeCode( this.mTaskElement.getScheduleTypeCode() );
        entry.setRunCount( 0 );
        entry.setTaskType( this.mTaskElement.getType() );
        entry.setInstanceStatus( TaskInstanceStatus.New );

        GenericRavenTaskInstance instance = new GenericRavenTaskInstance(entry);

        this.mTaskInstrument.getInstanceInstrument().addInstance( entry );
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

    public void updateInstanceMeta( RavenTaskInstance instance ) {
        this.updateTaskMeta( instance.getInstanceEntry() );
    }

    public void updateTaskMeta( InstanceEntry instanceEntry ) {
        this.mInstanceInstrument.updateInstance( instanceEntry );
    }

}
