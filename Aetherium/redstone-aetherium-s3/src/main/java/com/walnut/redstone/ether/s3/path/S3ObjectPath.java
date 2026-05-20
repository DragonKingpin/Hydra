package com.walnut.redstone.ether.s3.path;

import com.pinecone.framework.system.prototype.Pinenut;

public class S3ObjectPath implements Pinenut {
    protected String bucket;
    protected String key;

    public String getBucket() {
        return this.bucket;
    }

    public void setBucket( String bucket ) {
        this.bucket = bucket;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey( String key ) {
        this.key = key;
    }
}

