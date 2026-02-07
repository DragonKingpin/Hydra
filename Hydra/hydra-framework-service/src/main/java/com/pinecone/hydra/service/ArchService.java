package com.pinecone.hydra.service;

import java.util.Map;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.Identification;
import com.pinecone.framework.util.json.homotype.BeanJSONEncoder;
import com.pinecone.hydra.service.kom.entity.ServiceElement;

public abstract class ArchService implements Service {

    protected Identification            mServiceId;

    protected ServiceElement            mServiceMetaData;

    protected Map<String, Object >      mMetaDataScope;

    public ArchService( Identification serviceId, ServiceElement serviceElement, Map<String, Object > metaDataScope ){
        this.mServiceId = serviceId;
        this.mServiceMetaData = serviceElement;
        this.mMetaDataScope = metaDataScope;
    }

    public ArchService( Identification serviceId, ServiceElement serviceElement ){
       this( serviceId, serviceElement, null );
    }


    @Override
    public String getName() {
        return this.mServiceMetaData.getName();
    }

    @Override
    public String getType() {
        return this.mServiceMetaData.getType();
    }

    @Override
    public String getDisplayName() {
        return this.mServiceMetaData.getName();
    }

    @Override
    public String getFullName() {
        return this.mServiceMetaData.getPath();
    }


    public GUID getGuid() {
        return this.mServiceMetaData.getGuid();
    }

    @Override
    public Identification getId() {
        return this.getGuid();
    }

    @Override
    public String getScenario() {
        return this.mServiceMetaData.getScenario();
    }

    @Override
    public String getPrimaryImplLang() {
        return this.mServiceMetaData.getPrimaryImplLang();
    }

    @Override
    public String getExtraInformation() {
        return this.mServiceMetaData.getExtraInformation();
    }

    @Override
    public String getLevel() {
        return this.mServiceMetaData.getLevel();
    }

    @Override
    public String getDescription() {
        return this.mServiceMetaData.getDescription();
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
