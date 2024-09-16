package com.pinecone.hydra.scenario.entity;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public interface NamespaceNodeMeta extends Pinenut {
    int getEnumId();
    void setEnumId(int enumId);

    GUID getGuid();
    void setGuid(GUID guid);
}
