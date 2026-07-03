package com.walnut.redstone.ether.shuttle.client.object;

import com.pinecone.framework.system.prototype.Pinenut;

public class RedBucketRef implements Pinenut {
    protected String endpoint;
    protected String bucket;
    protected String prefix;

    public String getEndpoint() {
        return this.endpoint;
    }

    public void setEndpoint( String endpoint ) {
        this.endpoint = endpoint;
    }

    public String getBucket() {
        return this.bucket;
    }

    public void setBucket( String bucket ) {
        this.bucket = bucket;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public void setPrefix( String prefix ) {
        this.prefix = prefix;
    }
}
