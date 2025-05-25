package com.pinecone.hydra.deploy.kom.entity;

public interface ContainerElement extends DeployElement {

    void setStatus( String status );

    String getStatus();

    @Override
    default ContainerElement evinceContainerElement() {
        return this;
    }
}
