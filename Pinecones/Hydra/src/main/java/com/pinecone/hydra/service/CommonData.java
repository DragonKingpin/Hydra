package com.pinecone.hydra.service;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public interface CommonData extends Pinenut  {
    void setEnumId(long id);

    void setGuid(GUID guid);

    void setScenario(String scenario);

    void setPrimaryImplLang(String primaryImplLang);

    void setExtraInformation(String extraInformation);

    void setLevel(String level);


    void setDescription(String description);
}
