package com.pinecone.hydra.device.kom.entity;

import com.pinecone.framework.util.json.JSON;
import com.pinecone.framework.util.json.JSONObject;
import com.pinecone.framework.util.json.homotype.BeanMapDecoder;
import com.pinecone.hydra.device.DeviceExtraMeta;
import com.pinecone.hydra.device.kom.DeviceInstrument;

import java.util.Map;

public abstract class ArchServerElement extends ArchElementNode implements ServerElement {



    protected String localDomain;

    protected String wideDomain;

    protected boolean  enable;

    protected DeviceExtraMeta extraMeta;

    protected  DeviceElement affiliateDevicement;

    private void initSelf( Map<String, Object > joEntity ) {
        BeanMapDecoder.BasicDecoder.decode( this, joEntity );
        if ( this.szElementaryConfig != null ) {
            this.elementaryConfig = (JSONObject) JSON.parse( this.szElementaryConfig );
        }
    }

    public ArchServerElement(Map<String, Object > joEntity ) {
        super( joEntity );
        this.initSelf( joEntity );
    }

    public ArchServerElement(Map<String, Object > joEntity, DeviceInstrument deviceInstrument ) {
        super( joEntity, deviceInstrument);
        this.initSelf( joEntity );
    }

    public ArchServerElement(DeviceInstrument deviceInstrument ) {
        super(deviceInstrument);
    }


    public ArchServerElement() {
        super();
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
        return this.enable;
    }

    @Override
    public void setEnable( boolean enable ) {
        this.enable = enable;
    }

    @Override
    public DeviceExtraMeta getExtraMeta() {
        return this.extraMeta;
    }

    @Override
    public void setExtraMeta(DeviceExtraMeta extraMeta) {
          this.extraMeta = extraMeta;
    }

    @Override
    public DeviceElement getAffiliateDevicement() {
        return this.affiliateDevicement;
    }


}
