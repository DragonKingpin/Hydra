package com.pinecone.hydra.deploy.entity;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.deploy.entity.iface.Container;

public class GenericContainer implements Container {
    @Override
    public String getStatus() {
        return null;
    }

    @Override
    public GUID getGuid() {
        return null;
    }

    @Override
    public void setStatus(String status) {

    }

    @Override
    public String getName() {
        return null;
    }
}