package com.pinecone.hydra.file.entity;

import com.pinecone.framework.util.id.GUID;

public interface RemoteCluster extends Cluster{
    GUID getDeviceGuid();
    void setDeviceGuid(GUID deviceGuid);
}
