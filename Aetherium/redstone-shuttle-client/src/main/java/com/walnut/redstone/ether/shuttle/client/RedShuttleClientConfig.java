package com.walnut.redstone.ether.shuttle.client;

import com.pinecone.framework.system.prototype.Pinenut;
import com.walnut.redstone.ether.red.ReservedLabels;

public class RedShuttleClientConfig implements Pinenut {
    public static final String DefaultSystemBucket = ReservedLabels.System;
    public static final long DefaultPartSize = 10L * 1024L * 1024L;

    protected String mszDefaultEndpoint;
    protected String mszSystemBucket = DefaultSystemBucket;
    protected String mszAccessKey = "redstone";
    protected String mszSecretKey = "redstone";
    protected boolean mbSecure;
    protected long mnPartSize = DefaultPartSize;

    public String getDefaultEndpoint() {
        return this.mszDefaultEndpoint;
    }

    public void setDefaultEndpoint( String szDefaultEndpoint ) {
        this.mszDefaultEndpoint = szDefaultEndpoint;
    }

    public String getSystemBucket() {
        return this.mszSystemBucket;
    }

    public void setSystemBucket( String szSystemBucket ) {
        if ( szSystemBucket == null || szSystemBucket.trim().isEmpty() ) {
            this.mszSystemBucket = DefaultSystemBucket;
        }
        else {
            this.mszSystemBucket = szSystemBucket;
        }
    }

    public String getAccessKey() {
        return this.mszAccessKey;
    }

    public void setAccessKey( String szAccessKey ) {
        this.mszAccessKey = szAccessKey;
    }

    public String getSecretKey() {
        return this.mszSecretKey;
    }

    public void setSecretKey( String szSecretKey ) {
        this.mszSecretKey = szSecretKey;
    }

    public boolean isSecure() {
        return this.mbSecure;
    }

    public void setSecure( boolean bSecure ) {
        this.mbSecure = bSecure;
    }

    public long getPartSize() {
        return this.mnPartSize;
    }

    public void setPartSize( long nPartSize ) {
        if ( nPartSize <= 0L ) {
            this.mnPartSize = DefaultPartSize;
        }
        else {
            this.mnPartSize = nPartSize;
        }
    }
}
