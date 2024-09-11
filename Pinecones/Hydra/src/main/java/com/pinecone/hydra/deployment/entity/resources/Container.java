package com.pinecone.hydra.deployment.entity.resources;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public interface Container extends Pinenut {
    int getEnumId();
    void setEnumId(int enumId);

    GUID getGuid();
    void setGuid(GUID guid);

    String getName();
    void setName(String name);

   String getImageName();
   void setImageName(String imageName);

   GUID getServerGuid();
   void setServerGuid(GUID serverGuid);
}
