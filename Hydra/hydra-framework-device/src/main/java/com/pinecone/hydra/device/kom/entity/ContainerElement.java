package com.pinecone.hydra.device.kom.entity;

import com.pinecone.framework.util.id.GUID;

public interface ContainerElement extends DeviceElement {

    void setStatus( String status );

    String getStatus();

    GUID getAffiliateHostGuid();
    void setAffiliateHostGuid( GUID affiliateHostGuid );

    String getRuntime();
    void setRuntime( String runtime );

    String getImageName();
    void setImageName( String imageName );

    String getContainerId();
    void setContainerId( String containerId );

    @Override
    default ContainerElement evinceContainerElement() {
        return this;
    }
}
