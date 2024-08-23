package com.pinecone.hydra.service.tree;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

import java.util.UUID;

public interface NodeMetadata extends Pinenut {
    long getEnumId();
    void setEnumId(long id);

    GUID getUUID();
    void setUUID(GUID UUID);

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