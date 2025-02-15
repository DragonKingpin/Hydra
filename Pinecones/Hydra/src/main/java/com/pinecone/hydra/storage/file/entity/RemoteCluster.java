package com.pinecone.hydra.storage.file.entity;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.file.source.RemoteClusterManipulator;

public interface RemoteCluster extends Cluster {
    GUID getDeviceGuid();
    void setDeviceGuid(GUID deviceGuid);
    void setRemoteClusterManipulator(RemoteClusterManipulator remoteClusterManipulator);

    @Override
    default RemoteCluster evinceRemoteCluster() {
        return this;
    }
}
