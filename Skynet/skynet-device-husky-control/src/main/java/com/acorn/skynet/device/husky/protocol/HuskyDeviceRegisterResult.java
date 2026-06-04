package com.acorn.skynet.device.husky.protocol;

import com.pinecone.framework.system.prototype.Pinenut;

public class HuskyDeviceRegisterResult implements Pinenut {

    protected String deviceGuid;

    protected String instanceGuid;

    protected long clientId;

    public String getDeviceGuid() {
        return this.deviceGuid;
    }

    public void setDeviceGuid( String deviceGuid ) {
        this.deviceGuid = deviceGuid;
    }

    public String getInstanceGuid() {
        return this.instanceGuid;
    }

    public void setInstanceGuid( String instanceGuid ) {
        this.instanceGuid = instanceGuid;
    }

    public long getClientId() {
        return this.clientId;
    }

    public void setClientId( long clientId ) {
        this.clientId = clientId;
    }
}
