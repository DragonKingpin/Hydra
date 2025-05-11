package com.pinecone.hydra.deploy.kom.entity;

import java.util.Map;

import com.pinecone.framework.util.json.JSON;
import com.pinecone.framework.util.json.JSONObject;
import com.pinecone.framework.util.json.homotype.BeanMapDecoder;
import com.pinecone.hydra.deploy.DeployExtraMeta;
import com.pinecone.hydra.deploy.kom.DeployInstrument;

public class GenericDeployElement extends ArchElementNode implements DeployElement {

    protected boolean       enable;
    protected DeployExtraMeta extraMeta;


    private void initSelf( Map<String, Object > joEntity ) {
        BeanMapDecoder.BasicDecoder.decode( this, joEntity );
        if ( this.szElementaryConfig != null ) {
            this.elementaryConfig = (JSONObject)JSON.parse( this.szElementaryConfig );
        }
    }

    public GenericDeployElement() {
        super();
    }

    public GenericDeployElement(Map<String, Object > joEntity ) {
        super( joEntity );
        this.initSelf( joEntity );
    }

    public GenericDeployElement(Map<String, Object > joEntity, DeployInstrument deployInstrument) {
        super( joEntity, deployInstrument);
        this.initSelf( joEntity );
    }

    public GenericDeployElement(DeployInstrument deployInstrument) {
        super(deployInstrument);
    }

    @Override
    public boolean isEnable() {
        return this.enable;
    }

    @Override
    public void setEnable( boolean enable ) {
        this.enable = enable;
    }


    @Override
    public DeployExtraMeta getExtraMeta() {
        return this.extraMeta;
    }

    @Override
    public void setExtraMeta( DeployExtraMeta extraMeta ) {
        this.extraMeta = extraMeta;
    }

    @Override
    public DeployElement getAffiliateDeployment() {
        return null;
    }
}