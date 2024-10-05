package com.pinecone.hydra.file.entity;

import com.pinecone.framework.util.id.GUID;

public class GenericRemoteCluster extends ArchCluster implements RemoteCluster{
    private GUID deviceGuid;

    public GenericRemoteCluster() {
    }

    public GenericRemoteCluster(GUID deviceGuid) {
        this.deviceGuid = deviceGuid;
    }


    public GUID getDeviceGuid() {
        return deviceGuid;
    }


    public void setDeviceGuid(GUID deviceGuid) {
        this.deviceGuid = deviceGuid;
    }

    public String toString() {
        return "GenericRemoteCluster{deviceGuid = " + deviceGuid + "}";
    }
}
