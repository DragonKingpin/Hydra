package com.walnut.redstone.ether.resource;

import com.pinecone.framework.system.prototype.Pinenut;

public class ResourcePath implements Pinenut {
    protected String bucket;
    protected String path;
    protected ResourceNamespace namespace;

    public ResourcePath() {
    }

    public ResourcePath( ResourceNamespace namespace, String bucket, String path ) {
        this.namespace = namespace;
        this.bucket = bucket;
        this.path = path;
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

    public ResourceNamespace getNamespace() {
        return this.namespace;
    }

    public void setNamespace( ResourceNamespace namespace ) {
        this.namespace = namespace;
    }
}

