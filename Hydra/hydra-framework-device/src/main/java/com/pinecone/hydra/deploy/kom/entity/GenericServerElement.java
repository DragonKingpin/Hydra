package com.pinecone.hydra.deploy.kom.entity;

import com.pinecone.framework.util.json.JSON;
import com.pinecone.framework.util.json.JSONObject;
import com.pinecone.framework.util.json.homotype.BeanMapDecoder;
import com.pinecone.hydra.deploy.DeployExtraMeta;
import com.pinecone.hydra.deploy.kom.DeployInstrument;

import java.util.Map;

public class GenericServerElement extends ArchElementNode implements ServerElement{

    protected String ipAddress;

    protected String localDomain;

    protected String wideDomain;

    protected boolean  isEnable;

    protected DeployExtraMeta extraMeta;

    protected  DeployElement affiliateDeployment;

    private void initSelf( Map<String, Object > joEntity ) {
        BeanMapDecoder.BasicDecoder.decode( this, joEntity );
        if ( this.szElementaryConfig != null ) {
            this.elementaryConfig = (JSONObject) JSON.parse( this.szElementaryConfig );
        }
    }
    public GenericServerElement( Map<String, Object > joEntity ) {
        super( joEntity );
        this.initSelf( joEntity );
    }

    public GenericServerElement( Map<String, Object > joEntity, DeployInstrument deployInstrument ) {
        super( joEntity, deployInstrument);
        this.initSelf( joEntity );
    }

    public GenericServerElement( DeployInstrument deployInstrument ) {
        super(deployInstrument);
    }


    public GenericServerElement() {
        super();
    }

    @Override
    public String getIpAddress() {
        return this.ipAddress;
    }

    @Override
    public void setIpAddress(String ipAddress) {
       this.ipAddress = ipAddress;
    }

    @Override
    public String getLocalDomain() {
        return this.localDomain;
    }

    @Override
    public void setLocalDomain(String localDomain) {
        this.localDomain = localDomain;
    }

    @Override
    public String getWideDomain() {
        return this.wideDomain;
    }

    @Override
    public void setWideDomain(String wideDomain) {
        this.wideDomain = wideDomain;
    }

    @Override
    public boolean isEnable() {
        return this.isEnable;
    }

    @Override
    public void setEnable(boolean enable) {
        this.isEnable = enable;
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
        return this.affiliateDeployment;
    }



}
