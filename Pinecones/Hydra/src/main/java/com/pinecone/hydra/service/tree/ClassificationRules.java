package com.pinecone.hydra.service.tree;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

import java.util.UUID;

public interface ClassificationRules extends Pinenut {
    long getEnumId();
    void setEnumId(long id);

    GUID getUUID();
    void setUUID(GUID UUID);

    String getScope();
    void setScope(String scope);

    String getName();
    void setName(String name);

    String getDescription();
    void setDescription(String description);
}