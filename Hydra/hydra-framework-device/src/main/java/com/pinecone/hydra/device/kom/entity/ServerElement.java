package com.pinecone.hydra.device.kom.entity;

public interface ServerElement extends DeviceElement {

    String getLocalDomain();

    void setLocalDomain( String localDomain );

    String getWideDomain();

    void setWideDomain( String wideDomain );

}
