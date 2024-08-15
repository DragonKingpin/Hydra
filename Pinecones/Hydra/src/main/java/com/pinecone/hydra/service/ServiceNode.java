package com.pinecone.hydra.service;

import com.pinecone.framework.system.prototype.Pinenut;

public interface ServiceNode extends Pinenut {
    String getId();
    void setId(String id);

    String getUUID();
    void setUUID(String UUID);

    String getName();
    void setName(String name);
}