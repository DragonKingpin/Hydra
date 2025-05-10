package com.pinecone.hydra.deploy.kom.entity;

public interface ServerElement extends DeployElement {

    String getIpAddress();

    void setIpAddress( String ipAddress );

    String getLocalDomain();

    void setLocalDomain( String localDomain );

    String getWideDomain();

    void setWideDomain( String wideDomain );

}
