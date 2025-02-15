package com.pinecone.hydra.storage.file.entity;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.file.source.RemoteClusterManipulator;

public class GenericRemoteCluster extends ArchCluster implements RemoteCluster {
    private GUID                    deviceGuid;
    private RemoteClusterManipulator  frameManipulator;

    public GenericRemoteCluster() {
    }

    public GenericRemoteCluster(GUID deviceGuid) {
        this.deviceGuid = deviceGuid;
    }

    public GenericRemoteCluster(RemoteClusterManipulator remoteClusterManipulator ) {
        this.frameManipulator = remoteClusterManipulator;

    }

    public GUID getDeviceGuid() {
        return deviceGuid;
    }


    public void setDeviceGuid(GUID deviceGuid) {
        this.deviceGuid = deviceGuid;
    }

    @Override
    public void setRemoteClusterManipulator(RemoteClusterManipulator remoteClusterManipulator) {
        this.frameManipulator = remoteClusterManipulator;
    }

    public String toString() {
        return "GenericRemoteCluster{deviceGuid = " + deviceGuid + "}";
    }

    @Override
    public void save() {
        this.frameManipulator.insert(this);
    }
    @Override
    public void remove() {
        this.frameManipulator.remove( this.getSegGuid() );
    }
}
