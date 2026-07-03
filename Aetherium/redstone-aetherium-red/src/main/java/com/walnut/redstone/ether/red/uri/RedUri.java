package com.walnut.redstone.ether.red.uri;

import com.pinecone.framework.system.prototype.Pinenut;

public class RedUri implements Pinenut {
    protected String raw;
    protected String bucket;
    protected String path;
    protected RedNamespace namespace;

    public String getRaw() {
        return this.raw;
    }

    public void setRaw( String raw ) {
        this.raw = raw;
    }

    public String getBucket() {
        return this.bucket;
    }

    public void setBucket( String bucket ) {
        this.bucket = bucket;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath( String path ) {
        this.path = path;
    }

    public RedNamespace getNamespace() {
        return this.namespace;
    }

    public void setNamespace( RedNamespace namespace ) {
        this.namespace = namespace;
    }
}

