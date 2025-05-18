package com.pinecone.hydra.deploy.kom.entity;

public interface PhysicalHostElement extends ServerElement {

    void setHardwareSpecs( String hardwareSpecs );
    String getHardwareSpecs();

    void setStatus( String status );
    String getStatus();

    default PhysicalHostElement evincePhysicalHostElement() {
        return this;
    }

}
