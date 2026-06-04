package com.pinecone.hydra.device.registry.client.entity;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public class DeviceClientRegisterResult implements Pinenut {

    protected GUID deviceGuid;

    protected GUID instanceGuid;

    protected long clientId;

    public GUID getDeviceGuid() {
        return this.deviceGuid;
    }

    public void setDeviceGuid( GUID deviceGuid ) {
        this.deviceGuid = deviceGuid;
    }

    public GUID getInstanceGuid() {
        return this.instanceGuid;
    }

    public void setInstanceGuid( GUID instanceGuid ) {
        this.instanceGuid = instanceGuid;
    }

    public long getClientId() {
        return this.clientId;
    }

    public void setClientId( long clientId ) {
        this.clientId = clientId;
    }
}
