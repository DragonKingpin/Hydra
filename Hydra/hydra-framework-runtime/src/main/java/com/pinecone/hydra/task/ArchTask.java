package com.pinecone.hydra.task;

import java.util.Map;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.Identification;
import com.pinecone.framework.util.json.homotype.BeanJSONEncoder;
import com.pinecone.hydra.task.kom.entity.TaskElement;

public abstract class ArchTask implements Task {

    protected Identification            mServiceId;

    protected TaskElement               mTaskElement;

    protected Map<String, Object >      mMetaDataScope;

    public ArchTask( Identification serviceId, TaskElement serviceElement, Map<String, Object > metaDataScope ){
        this.mServiceId     = serviceId;
        this.mTaskElement   = serviceElement;
        this.mMetaDataScope = metaDataScope;
    }

    public ArchTask( Identification serviceId, TaskElement serviceElement ){
       this( serviceId, serviceElement, null );
    }


    public TaskElement getTaskElement() {
        return this.mTaskElement;
    }

    @Override
    public String getName() {
        return this.mTaskElement.getName();
    }

    @Override
    public String getDisplayName() {
        return this.mTaskElement.getName();
    }

    @Override
    public String getFullName() {
        return this.mTaskElement.getKomPath();
    }


    public GUID getGuid() {
        return this.mTaskElement.getGuid();
    }

    @Override
    public Identification getId() {
        return this.getGuid();
    }

    @Override
    public String getScenario() {
        return this.mTaskElement.getScenario();
    }

    @Override
    public String getMarshallingArchitecture() {
        return this.mTaskElement.getMarshallingArchitecture();
    }

    @Override
    public String getExtraInformation() {
        return this.mTaskElement.getExtraInformation();
    }

    @Override
    public short getPriority() {
        return this.mTaskElement.getPriority();
    }

    @Override
    public short getActuallyPriority() {
        return this.mTaskElement.getActuallyPriority();
    }

    @Override
    public boolean isDryRun() {
        return this.mTaskElement.isDryRun();
    }

    @Override
    public int getScheduleTypeCode() {
        return this.mTaskElement.getScheduleTypeCode();
    }

    @Override
    public boolean isEnable() {
        return this.mTaskElement.isEnable();
    }

    @Override
    public String getDescription() {
        return this.mTaskElement.getDescription();
    }

    @Override
    public Map<String, Object> getMetaDataScope() {
        return this.mMetaDataScope;
    }

    @Override
    public String toString() {
        return this.toJSONString();
    }

    @Override
    public String toJSONString() {
        return BeanJSONEncoder.BasicEncoder.encode( this );
    }
}
