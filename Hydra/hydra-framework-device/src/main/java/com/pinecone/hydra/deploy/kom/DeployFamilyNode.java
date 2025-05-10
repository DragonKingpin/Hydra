package com.pinecone.hydra.deploy.kom;

import java.util.Map;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.Identification;
import com.pinecone.hydra.deploy.DeployFamilyMeta;

public interface DeployFamilyNode extends DeployFamilyMeta {
    long getEnumId();

    void setEnumId(long id);

    void setName(String name);

    GUID getGuid();

    void setGuid(GUID guid);

    @Override
    default Identification getId() {
        return this.getGuid();
    }

    String getExtraInformation();

    void setExtraInformation(String extraInformation);

    String getDescription();

    void setDescription(String description);

    DeployFamilyNode apply(Map<String, Object> joEntity) ;
}