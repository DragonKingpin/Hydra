package com.acorn.skynet.device.husky.client;

import com.pinecone.framework.system.prototype.Pinenut;

public class HuskyDeviceClientTransportConfig implements Pinenut {

    protected long controlSyncTimeoutMillis = 5000L;

    public long getControlSyncTimeoutMillis() {
        return this.controlSyncTimeoutMillis;
    }

    public void setControlSyncTimeoutMillis( long controlSyncTimeoutMillis ) {
        this.controlSyncTimeoutMillis = controlSyncTimeoutMillis;
    }
}
