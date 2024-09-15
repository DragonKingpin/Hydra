package com.pinecone.hydra.deployment.entity;

import com.pinecone.hydra.deployment.entity.iface.Container;

public class GenericContainer implements Container {
    @Override
    public String getStatus() {
        return null;
    }

    @Override
    public void setStatus(String status) {

    }
}
