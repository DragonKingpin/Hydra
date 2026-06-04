package com.pinecone.hydra.device.registry.client.entity;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public class DeviceClientDeregisterResult implements Pinenut {

    protected GUID instanceGuid;

    public GUID getInstanceGuid() {
        return this.instanceGuid;
    }

    public void setInstanceGuid( GUID instanceGuid ) {
        this.instanceGuid = instanceGuid;
    }
}
