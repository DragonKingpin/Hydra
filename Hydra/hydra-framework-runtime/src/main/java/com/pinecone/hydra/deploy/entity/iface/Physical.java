package com.pinecone.hydra.deploy.entity.iface;

public interface Physical extends Server{
    String getHardwareSpecs();
    void setHardwareSpecs(String hardwareSpecs);
}
