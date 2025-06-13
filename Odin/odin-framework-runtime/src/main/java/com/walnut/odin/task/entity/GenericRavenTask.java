package com.walnut.odin.task.entity;

import com.pinecone.framework.system.prototype.TypeIndex;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.framework.util.id.Identification;
import com.pinecone.hydra.task.ArchTask;
import com.pinecone.hydra.task.kom.entity.TaskElement;
import com.pinecone.hydra.task.kom.instance.GenericInstanceEntry;
import com.pinecone.hydra.task.kom.instance.InstanceInstrument;
import com.pinecone.hydra.task.kom.instance.source.InstanceNodeManipulator;
import com.walnut.odin.ups.RavenInstance;
import com.walnut.odin.ups.RavenTask;

import java.time.LocalDateTime;
import java.util.Map;

public class GenericRavenTask extends ArchTask implements RavenTask {
    InstanceInstrument      mInstanceInstrument;

    GuidAllocator           mGuidAllocator;
    public GenericRavenTask(InstanceInstrument instanceInstrument, GuidAllocator guidAllocator, Identification serviceId,
                            TaskElement serviceElement, Map<String, Object> metaDataScope ) {
        super(serviceId, serviceElement, metaDataScope);
        this.mInstanceInstrument   = instanceInstrument;
        this.mGuidAllocator        = guidAllocator;
    }

    public GenericRavenTask(InstanceInstrument instanceInstrument, GuidAllocator guidAllocator,Identification serviceId, TaskElement serviceElement) {
        this(instanceInstrument,guidAllocator,serviceId,serviceElement,null);
    }

    @Override
    public TypeIndex prototype() {
        return super.prototype();
    }

    @Override
    public RavenInstance newInstance() {
        GUID guid = this.mGuidAllocator.nextGUID();
        GenericInstanceEntry instanceEntry = new GenericInstanceEntry();
        instanceEntry.setGuid( guid );
        instanceEntry.setActuallyPriority( this.mTaskElement.getActuallyPriority() );
        instanceEntry.setCreateTime( LocalDateTime.now() );
        instanceEntry.setKernelScheduleCycle( this.mTaskElement.getScheduleCycle() );
        instanceEntry.setKernelScheduleCycleCode( this.mTaskElement.getScheduleCycleCode() );
        instanceEntry.setKernelScheduleType( this.mTaskElement.getScheduleType() );
        instanceEntry.setKernelScheduleTypeCode( this.mTaskElement.getScheduleTypeCode() );
        instanceEntry.setRunCount(0);
        instanceEntry.setTaskType( this.mTaskElement.getType() );

        GenericRavenInstance instance = new GenericRavenInstance(guid, instanceEntry);
        return instance;
    }
}
