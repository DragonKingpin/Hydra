package com.walnut.redstone.ether.shuttle.config;

import com.pinecone.framework.system.prototype.Pinenut;

public class ShuttleHttpClientConfig implements Pinenut {
    protected String type;
    protected int ioThreadCount;
    protected String userAgent;

    public String getType() {
        return this.type;
    }

    public void setType( String type ) {
        this.type = type;
    }

    public int getIoThreadCount() {
        return this.ioThreadCount;
    }

    public void setIoThreadCount( int ioThreadCount ) {
        this.ioThreadCount = ioThreadCount;
    }

    public String getUserAgent() {
        return this.userAgent;
    }

    public void setUserAgent( String userAgent ) {
        this.userAgent = userAgent;
    }
}
