package com.pinecone.hydra.deploy;

import java.util.Map;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.Identification;
import com.pinecone.framework.util.json.homotype.BeanJSONEncoder;
import com.pinecone.framework.util.json.homotype.BeanMapDecoder;
import com.pinecone.ulf.util.guid.GUIDs;

public abstract class ArchDeployFamilyMeta implements DeployFamilyMeta {
    protected GUID   guid;

    protected String name;

    protected String extraInformation;

    protected String ipAddress;

    protected String szElementaryConfig;

    protected Map<String, Object > elementaryConfig;


    protected String description;

    public ArchDeployFamilyMeta() {
    }

    public ArchDeployFamilyMeta(Map<String, Object > joEntity ) {
        this.apply( joEntity );
    }

    protected ArchDeployFamilyMeta apply(Map<String, Object > joEntity ) {
        String szGuid = (String) joEntity.get( "guid" );
        if( szGuid != null ) {
            this.guid = GUIDs.GUID128( (String) joEntity.get( "guid" ) );
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
    public String getExtraInformation() {
        return this.extraInformation;
    }
    @Override
    public String getIpAddress() {
        return this.ipAddress;
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
