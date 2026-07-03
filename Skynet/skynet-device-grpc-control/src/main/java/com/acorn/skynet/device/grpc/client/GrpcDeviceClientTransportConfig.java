package com.acorn.skynet.device.grpc.client;

import com.pinecone.framework.system.prototype.Pinenut;

public class GrpcDeviceClientTransportConfig implements Pinenut {

    protected long lifecycleSyncTimeoutMillis = 5000L;

    protected long commandTimeoutMillis = 5000L;

    public long getLifecycleSyncTimeoutMillis() {
        return this.lifecycleSyncTimeoutMillis;
    }

    public void setLifecycleSyncTimeoutMillis( long lifecycleSyncTimeoutMillis ) {
        this.lifecycleSyncTimeoutMillis = lifecycleSyncTimeoutMillis;
    }

    public long getCommandTimeoutMillis() {
        return this.commandTimeoutMillis;
    }

    public void setCommandTimeoutMillis( long commandTimeoutMillis ) {
        this.commandTimeoutMillis = commandTimeoutMillis;
    }
}
