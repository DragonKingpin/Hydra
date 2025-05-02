package com.pinecone.hydra.task;

import java.util.Map;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.Identification;
import com.pinecone.framework.util.json.homotype.BeanJSONEncoder;
import com.pinecone.framework.util.json.homotype.BeanMapDecoder;
import com.pinecone.ulf.util.guid.GUIDs;

public abstract class ArchTaskFamilyMeta implements TaskFamilyMeta {
    protected GUID   guid;

    protected String name;

    protected String scenario;

    protected String marshallingArchitecture;

    protected String extraInformation;

    protected String szElementaryConfig;

    protected Map<String, Object > elementaryConfig;


    protected String description;

    public ArchTaskFamilyMeta() {
    }

    public ArchTaskFamilyMeta(Map<String, Object > joEntity ) {
        this.apply( joEntity );
    }

    protected ArchTaskFamilyMeta apply( Map<String, Object > joEntity ) {
        String szGuid = (String) joEntity.get( "guid" );
        if( szGuid != null ) {
            this.guid = GUIDs.GUID72( (String) joEntity.get( "guid" ) );
        }
        BeanMapDecoder.BasicDecoder.decode( this, joEntity );

        return this;
    }


    public GUID getGuid() {
        return this.guid;
    }

    @Override
    public Identification getId() {
        return this.getGuid();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getScenario() {
        return this.scenario;
    }

    @Override
    public String getMarshallingArchitecture() {
        return this.marshallingArchitecture;
    }

    @Override
    public String getExtraInformation() {
        return this.extraInformation;
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
