package com.pinecone.hydra.service;

import com.pinecone.framework.system.prototype.Pinenut;

public interface ClassificationRules extends Pinenut {
    String getId();
    void setId(String id);

    String getUUID();
    void setUUID(String UUID);

    String getScope();
    void setScope(String scope);

    String getName();
    void setName(String name);

    String getDescription();
    void setDescription(String description);
}