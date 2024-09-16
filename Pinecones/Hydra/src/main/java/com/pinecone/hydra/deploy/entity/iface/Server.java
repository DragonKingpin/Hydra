package com.pinecone.hydra.deploy.entity.iface;

public interface Server extends Deploy{
    String getName();
    void setName(String name);

    String getIpAddress();
    void setIpAddress(String ipAddress);
}
