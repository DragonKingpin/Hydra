package com.pinecone.hydra.service.kom;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public interface NodeCommonData extends Pinenut {
    long getEnumId();

    void setEnumId(long id);

    GUID getGuid();

    void setGuid(GUID guid);

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