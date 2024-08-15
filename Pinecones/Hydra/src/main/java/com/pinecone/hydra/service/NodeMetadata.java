package com.pinecone.hydra.service;

import com.pinecone.framework.system.prototype.Pinenut;

public interface NodeMetadata extends Pinenut {
    String getId();
    void setId(String id);

    String getUUID();
    void setUUID(String UUID);

    String getScenario();
    void setScenario(String scenario);

    String getPrimaryImplLang();
    void setPrimaryImplLang(String primaryImplLang);

    String getExtraInformation();
    void setExtraInformation(String extraInformation);

    String getLevel();
    void setLevel(String level);

    String getDescription();
    void setDescription(String description);
}