package com.walnut.redstone.ether.resource;

import com.pinecone.framework.system.prototype.Pinenut;

public class ResourceRoute implements Pinenut {
    protected ResourceNamespace namespace;
    protected String backend;
    protected String routeMode;
    protected ResourcePath resourcePath;

    public ResourceNamespace getNamespace() {
        return this.namespace;
    }

    public void setNamespace( ResourceNamespace namespace ) {
        this.namespace = namespace;
    }

    public String getBackend() {
        return this.backend;
    }

    public void setBackend( String backend ) {
        this.backend = backend;
    }

    public String getRouteMode() {
        return this.routeMode;
    }

    public void setRouteMode( String routeMode ) {
        this.routeMode = routeMode;
    }

    public ResourcePath getResourcePath() {
        return this.resourcePath;
    }

    public void setResourcePath( ResourcePath resourcePath ) {
        this.resourcePath = resourcePath;
    }
}

