package com.pinecone.hydra.deploy;

public interface PhysicalHost extends Server {
    String getHardwareSpecs();

    void setHardwareSpecs(String hardwareSpecs);
}
