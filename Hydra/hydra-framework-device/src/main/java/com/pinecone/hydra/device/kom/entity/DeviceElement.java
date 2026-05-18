package com.pinecone.hydra.device.kom.entity;

import com.pinecone.hydra.device.DeviceExtraMeta;

public interface DeviceElement extends ElementNode {

    @Override
    default DeviceElement evinceDeviceElement() {
        return this;
    }

    boolean isEnable() ;

    void setEnable( boolean enable ) ;

    DeviceExtraMeta getExtraMeta();

    void setExtraMeta(DeviceExtraMeta extraMeta);

    DeviceElement getAffiliateDevicement();

}