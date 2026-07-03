package com.pinecone.hydra.device.kom.entity;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.uoi.UOI;

public class DeviceNodeOwnershipEntry implements Pinenut {

    protected GUID guid;

    protected GUID ownerDeviceGuid;

    protected GUID baseDataGuid;

    protected GUID nodeMetadataGuid;

    protected boolean deviceNode;

    protected UOI type;

    public GUID getGuid() {
        return this.guid;
    }

    public void setGuid( GUID guid ) {
        this.guid = guid;
    }

    public GUID getOwnerDeviceGuid() {
        return this.ownerDeviceGuid;
    }

    public void setOwnerDeviceGuid( GUID ownerDeviceGuid ) {
        this.ownerDeviceGuid = ownerDeviceGuid;
    }

    public GUID getBaseDataGuid() {
        return this.baseDataGuid;
    }

    public void setBaseDataGuid( GUID baseDataGuid ) {
        this.baseDataGuid = baseDataGuid;
    }

    public GUID getNodeMetadataGuid() {
        return this.nodeMetadataGuid;
    }

    public void setNodeMetadataGuid( GUID nodeMetadataGuid ) {
        this.nodeMetadataGuid = nodeMetadataGuid;
    }

    public boolean isDeviceNode() {
        return this.deviceNode;
    }

    public void setDeviceNode( boolean deviceNode ) {
        this.deviceNode = deviceNode;
    }

    public UOI getType() {
        return this.type;
    }

    public void setType( UOI type ) {
        this.type = type;
    }
}
