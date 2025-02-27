package com.pinecone.hydra.scenario.entity;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public interface NamespaceNodeMeta extends Pinenut {
    long getEnumId();
    void setEnumId(long enumId);

    GUID getGuid();
    void setGuid(GUID guid);
}
