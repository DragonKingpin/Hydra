package com.walnut.redstone.ether.red.model;

import com.pinecone.framework.system.prototype.Pinenut;
import com.walnut.redstone.ether.object.ObjectMetadata;

public class RedMetadataEnvelope implements Pinenut {
    protected String uri;
    protected ObjectMetadata metadata;

    public String getUri() {
        return this.uri;
    }

    public void setUri( String uri ) {
        this.uri = uri;
    }

    public ObjectMetadata getMetadata() {
        return this.metadata;
    }

    public void setMetadata( ObjectMetadata metadata ) {
        this.metadata = metadata;
    }
}

