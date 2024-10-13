package com.pinecone.hydra.file.entity;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.file.source.RemoteFrameManipulator;

public interface RemoteFrame extends Frame {
    GUID getDeviceGuid();
    void setDeviceGuid(GUID deviceGuid);
    void setRemoteFrameManipulator(RemoteFrameManipulator remoteFrameManipulator);

    @Override
    default RemoteFrame evinceRemoteFrame() {
        return this;
    }
}
