package com.pinecone.hydra.registry.entity;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public interface ConfNodeMeta extends Pinenut {
    int getEnumId();
    void setEnumId(int id);

    GUID getGuid();
    void setGuid(GUID guid);
}
