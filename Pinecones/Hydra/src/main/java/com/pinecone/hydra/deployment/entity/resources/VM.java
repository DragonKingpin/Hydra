package com.pinecone.hydra.deployment.entity.resources;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public interface VM extends Pinenut {
    int getEnumId();
    void setEnumId(int enumId);

    GUID getGuid();
    void setGuid(GUID guid);

    String getHostName();
    void setHostName(String hostName);

    String getIpAddress();
    void setIpAddress(String ipAddress);

    String getMacAddress();
    void setMacAddress(String macAddress);

    GUID getServerGuid();
    void setServerGuid(GUID serverGuid);
}
