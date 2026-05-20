package com.walnut.redstone.ether.shuttle.config;

import com.pinecone.framework.system.prototype.Pinenut;

public class ShuttleTimeoutConfig implements Pinenut {
    protected long connectTimeoutMillis;
    protected long connectionRequestTimeoutMillis;
    protected long responseTimeoutMillis;

    public long getConnectTimeoutMillis() {
        return this.connectTimeoutMillis;
    }

    public void setConnectTimeoutMillis( long connectTimeoutMillis ) {
        this.connectTimeoutMillis = connectTimeoutMillis;
    }

    public long getConnectionRequestTimeoutMillis() {
        return this.connectionRequestTimeoutMillis;
    }

    public void setConnectionRequestTimeoutMillis( long connectionRequestTimeoutMillis ) {
        this.connectionRequestTimeoutMillis = connectionRequestTimeoutMillis;
    }

    public long getResponseTimeoutMillis() {
        return this.responseTimeoutMillis;
    }

    public void setResponseTimeoutMillis( long responseTimeoutMillis ) {
        this.responseTimeoutMillis = responseTimeoutMillis;
    }
}
