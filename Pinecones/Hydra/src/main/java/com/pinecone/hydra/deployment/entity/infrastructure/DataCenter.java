package com.pinecone.hydra.deployment.entity.infrastructure;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public interface DataCenter extends Pinenut {
    int getEnumId();
    void setEnumId(int id);

    GUID getGuid();
    void setGuid(GUID guid);

    String getName();
    void setName(String name);

    String getLocation();
    void setLocation(String location);

    String getAddress();
    void setAddress(String address);


}
