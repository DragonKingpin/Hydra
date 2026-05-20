package com.walnut.redstone.ether.object;

import com.pinecone.framework.system.prototype.Pinenut;

public class ObjectListRequest implements Pinenut {
    protected String prefix;
    protected String marker;
    protected String continuationToken;
    protected Integer maxKeys;

    public String getPrefix() {
        return this.prefix;
    }

    public void setPrefix( String prefix ) {
        this.prefix = prefix;
    }

    public String getMarker() {
        return this.marker;
    }

    public void setMarker( String marker ) {
        this.marker = marker;
    }

    public String getContinuationToken() {
        return this.continuationToken;
    }

    public void setContinuationToken( String continuationToken ) {
        this.continuationToken = continuationToken;
    }

    public Integer getMaxKeys() {
        return this.maxKeys;
    }

    public void setMaxKeys( Integer maxKeys ) {
        this.maxKeys = maxKeys;
    }
}

