package com.walnut.redstone.ether.shuttle.client.object;

import com.pinecone.framework.system.prototype.Pinenut;
import com.walnut.redstone.ether.shuttle.client.uri.RedShuttleResourceType;

public class RedObjectRef implements Pinenut {
    protected String endpoint;
    protected String bucket;
    protected String key;
    protected RedShuttleResourceType resourceType = RedShuttleResourceType.Object;

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

    public String getKey() {
        return this.key;
    }

    public void setKey( String key ) {
        this.key = key;
    }

    public RedShuttleResourceType getResourceType() {
        return this.resourceType;
    }

    public void setResourceType( RedShuttleResourceType resourceType ) {
        this.resourceType = resourceType;
    }
}
