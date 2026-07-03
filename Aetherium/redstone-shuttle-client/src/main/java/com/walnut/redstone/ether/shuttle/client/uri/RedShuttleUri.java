package com.walnut.redstone.ether.shuttle.client.uri;

import com.pinecone.framework.system.prototype.Pinenut;

public class RedShuttleUri implements Pinenut {
    protected String raw;
    protected String scheme;
    protected String endpoint;
    protected String bucket;
    protected String key;
    protected String path;
    protected boolean explicitEndpoint;
    protected RedShuttleResourceType resourceType = RedShuttleResourceType.Object;

    public String getRaw() {
        return this.raw;
    }

    public void setRaw( String raw ) {
        this.raw = raw;
    }

    public String getScheme() {
        return this.scheme;
    }

    public void setScheme( String scheme ) {
        this.scheme = scheme;
    }

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

    public String getPath() {
        return this.path;
    }

    public void setPath( String path ) {
        this.path = path;
    }

    public boolean isExplicitEndpoint() {
        return this.explicitEndpoint;
    }

    public void setExplicitEndpoint( boolean explicitEndpoint ) {
        this.explicitEndpoint = explicitEndpoint;
    }

    public RedShuttleResourceType getResourceType() {
        return this.resourceType;
    }

    public void setResourceType( RedShuttleResourceType resourceType ) {
        this.resourceType = resourceType;
    }
}
