package com.pinecone.hydra.service;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

import java.util.UUID;

public interface ServiceNode extends Pinenut {
    long getEnumId();
    void setEnumId(long id);

    GUID getUUID();
    void setUUID(GUID UUID);

    String getName();
    void setName(String name);
}