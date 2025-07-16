package com.pinecone.hydra.deploy.kom.entity;

import com.pinecone.framework.util.json.JSON;
import com.pinecone.framework.util.json.JSONObject;
import com.pinecone.framework.util.json.homotype.BeanMapDecoder;
import com.pinecone.hydra.deploy.DeployExtraMeta;
import com.pinecone.hydra.deploy.kom.DeployInstrument;
import com.pinecone.hydra.deploy.kom.source.QuickElementManipulator;

import java.util.Map;

public class GenericQuickElement extends ArchElementNode implements QuickElement {

    protected String typeName;

    protected boolean enable;

    protected  DeployExtraMeta extraMeta;

    protected QuickElementManipulator quickElementManipulator;


    private void initSelf( Map<String, Object > joEntity ) {
        BeanMapDecoder.BasicDecoder.decode( this, joEntity );
        if ( this.szElementaryConfig != null ) {
            this.elementaryConfig = (JSONObject) JSON.parse( this.szElementaryConfig );
        }
    }

    public GenericQuickElement(DeployInstrument deployInstrument, QuickElementManipulator quickElementManipulator) {
        super(deployInstrument);
        this.quickElementManipulator = quickElementManipulator;
    }

    public GenericQuickElement( Map<String, Object > joEntity, DeployInstrument deployInstrument ) {
        super( joEntity, deployInstrument);
        this.initSelf( joEntity );
    }

    public GenericQuickElement( Map<String, Object > joEntity ) {
        super( joEntity );
        this.initSelf( joEntity );
    }
    public GenericQuickElement( DeployInstrument deployInstrument ) {
        super(deployInstrument);
    }


    public GenericQuickElement() {
        super();
    }

    @Override
    public String getTypeName() {
        return this.typeName;
    }

    @Override
    public void setTypeName(String typeName) {
        this.typeName = typeName;
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
    public void addChild(ElementNode child) {
        super.addChild( child );
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
