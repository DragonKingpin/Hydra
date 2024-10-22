package com.pinecone.hydra.service;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.Identification;
import com.pinecone.framework.util.json.hometype.BeanJSONEncoder;

public abstract class ArchServiceFamilyObject implements ServiceFamilyObject {
    protected GUID guid;

    protected String scenario;

    protected String primaryImplLang;

    protected String extraInformation;

    protected String level;

    protected String description;


    public ArchServiceFamilyObject() {
    }

    public GUID getGuid() {
        return this.guid;
    }

    @Override
    public Identification getId() {
        return this.getGuid();
    }

    @Override
    public String getScenario() {
        return this.scenario;
    }

    @Override
    public String getPrimaryImplLang() {
        return this.primaryImplLang;
    }

    @Override
    public String getExtraInformation() {
        return this.extraInformation;
    }

    @Override
    public String getLevel() {
        return this.level;
    }

    @Override
    public String getDescription() {
        return this.description;
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
