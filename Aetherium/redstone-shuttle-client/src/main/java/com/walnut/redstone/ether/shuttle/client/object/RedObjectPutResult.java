package com.walnut.redstone.ether.shuttle.client.object;

import com.pinecone.framework.system.prototype.Pinenut;

public class RedObjectPutResult implements Pinenut {
    protected String bucket;
    protected String key;
    protected String etag;

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

    public String getEtag() {
        return this.etag;
    }

    public void setEtag( String etag ) {
        this.etag = etag;
    }
}
