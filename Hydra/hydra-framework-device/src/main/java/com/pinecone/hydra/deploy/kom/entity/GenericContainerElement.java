package com.pinecone.hydra.deploy.kom.entity;

import com.pinecone.framework.util.json.JSON;
import com.pinecone.framework.util.json.JSONObject;
import com.pinecone.framework.util.json.homotype.BeanMapDecoder;
import com.pinecone.hydra.deploy.DeployExtraMeta;
import com.pinecone.hydra.deploy.kom.DeployInstrument;
import com.pinecone.hydra.deploy.kom.source.ContainerElementManipulator;

import java.util.Map;

public class GenericContainerElement extends ArchElementNode implements ContainerElement{

    protected String status;

    protected boolean  enable;

    protected DeployExtraMeta extraMeta;


    protected ContainerElementManipulator ContainerElementManipulator;


    private void initSelf( Map<String, Object > joEntity ) {
        BeanMapDecoder.BasicDecoder.decode( this, joEntity );
        if ( this.szElementaryConfig != null ) {
            this.elementaryConfig = (JSONObject) JSON.parse( this.szElementaryConfig );
        }
    }

    public GenericContainerElement(DeployInstrument deployInstrument, ContainerElementManipulator containerElementManipulator) {
        super(deployInstrument);
        this.ContainerElementManipulator = containerElementManipulator;
    }

    public GenericContainerElement( Map<String, Object > joEntity, DeployInstrument deployInstrument ) {
        super( joEntity, deployInstrument);
        this.initSelf( joEntity );
    }

    public GenericContainerElement( Map<String, Object > joEntity ) {
        super( joEntity );
        this.initSelf( joEntity );
    }
    public GenericContainerElement( DeployInstrument deployInstrument ) {
        super(deployInstrument);
    }


    public GenericContainerElement() {
        super();
    }
    
    
    
    @Override
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String getStatus() {
        return this.status;
    }

    @Override
    public boolean isEnable() {
        return this.enable;
    }

    @Override
    public void setEnable(boolean enable) {
            this.enable = enable;
    }

    @Override
    public DeployExtraMeta getExtraMeta() {
        return this.extraMeta;
    }

    @Override
    public void setExtraMeta(DeployExtraMeta extraMeta) {
                this.extraMeta = extraMeta;
    }

    @Override
    public DeployElement getAffiliateDeployment() {
        return null;
    }
    @Override
    public String getIpAddress() {
        return this.ipAddress;
    }

    @Override
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

}
