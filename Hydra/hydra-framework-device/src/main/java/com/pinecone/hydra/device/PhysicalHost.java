package com.pinecone.hydra.device;

public interface PhysicalHost extends Server {

    String getHardwareSpecs();

    void setHardwareSpecs( String hardwareSpecs );
}
