package com.walnut.redstone.ether.red.model;

import com.pinecone.framework.system.prototype.Pinenut;

public class RedMountView implements Pinenut {
    protected String mountPath;
    protected String backend;

    public String getMountPath() {
        return this.mountPath;
    }

    public void setMountPath( String mountPath ) {
        this.mountPath = mountPath;
    }

    public String getBackend() {
        return this.backend;
    }

    public void setBackend( String backend ) {
        this.backend = backend;
    }
}

