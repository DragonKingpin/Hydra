package com.pinecone.hydra.deploy.kom.entity;

import com.pinecone.framework.util.json.JSON;
import com.pinecone.framework.util.json.JSONObject;
import com.pinecone.framework.util.json.homotype.BeanMapDecoder;
import com.pinecone.hydra.deploy.DeployExtraMeta;
import com.pinecone.hydra.deploy.kom.DeployInstrument;
import com.pinecone.hydra.deploy.kom.source.PhysicalHostManipulator;
import com.pinecone.hydra.deploy.kom.source.QuickElementManipulator;

import java.util.Map;

public class GenericPhysicalHostElement extends GenericServerElement implements PhysicalHostElement{

    protected String hardwareSpecs;

    protected String status;
    private void initSelf( Map<String, Object > joEntity ) {
        BeanMapDecoder.BasicDecoder.decode( this, joEntity );
        if ( this.szElementaryConfig != null ) {
            this.elementaryConfig = (JSONObject) JSON.parse( this.szElementaryConfig );
        }
    }

    public GenericPhysicalHostElement(Map<String, Object> joEntity) {
        super(joEntity);
    }

    public GenericPhysicalHostElement(DeployInstrument deployInstrument) {
        super(deployInstrument);
    }

    public GenericPhysicalHostElement() {
        super();
    }
    public GenericPhysicalHostElement( Map<String, Object > joEntity, DeployInstrument deployInstrument ) {
        super( joEntity, deployInstrument);
        this.initSelf( joEntity );
    }


    @Override
    public void setHardwareSpecs(String hardwareSpecs) {
        this.hardwareSpecs = hardwareSpecs;
    }

    @Override
    public String getHardwareSpecs() {
        return this.hardwareSpecs;
    }

    @Override
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String getStatus() {
        return this.status;
    }
}
