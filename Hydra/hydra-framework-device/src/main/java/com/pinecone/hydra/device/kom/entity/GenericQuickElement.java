package com.pinecone.hydra.device.kom.entity;

import com.pinecone.framework.util.json.JSON;
import com.pinecone.framework.util.json.JSONObject;
import com.pinecone.framework.util.json.homotype.BeanMapDecoder;
import com.pinecone.hydra.device.DeviceExtraMeta;
import com.pinecone.hydra.device.kom.DeviceInstrument;
import com.pinecone.hydra.device.kom.source.QuickElementManipulator;

import java.util.Map;

public class GenericQuickElement extends ArchElementNode implements QuickElement {

    protected String typeName;

    protected boolean enable;

    protected  DeviceExtraMeta extraMeta;

    protected QuickElementManipulator quickElementManipulator;


    private void initSelf( Map<String, Object > joEntity ) {
        BeanMapDecoder.BasicDecoder.decode( this, joEntity );
        if ( this.szElementaryConfig != null ) {
            this.elementaryConfig = (JSONObject) JSON.parse( this.szElementaryConfig );
        }
    }

    public GenericQuickElement(DeviceInstrument deviceInstrument, QuickElementManipulator quickElementManipulator) {
        super(deviceInstrument);
        this.quickElementManipulator = quickElementManipulator;
    }

    public GenericQuickElement( Map<String, Object > joEntity, DeviceInstrument deviceInstrument ) {
        super( joEntity, deviceInstrument);
        this.initSelf( joEntity );
    }

    public GenericQuickElement( Map<String, Object > joEntity ) {
        super( joEntity );
        this.initSelf( joEntity );
    }
    public GenericQuickElement( DeviceInstrument deviceInstrument ) {
        super(deviceInstrument);
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
    public DeviceExtraMeta getExtraMeta() {
        return this.extraMeta;
    }

    @Override
    public void setExtraMeta(DeviceExtraMeta extraMeta) {
              this.extraMeta = extraMeta;
    }

    @Override
    public DeviceElement getAffiliateDevicement() {
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
