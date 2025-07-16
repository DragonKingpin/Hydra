package com.pinecone.hydra.deploy.kom.entity;

public interface ServerElement extends DeployElement {

    String getLocalDomain();

    void setLocalDomain( String localDomain );

    String getWideDomain();

    void setWideDomain( String wideDomain );

}
