package com.walnut.redstone.ether.resource;

import com.pinecone.framework.system.prototype.Pinenut;

public class ResourceUri implements Pinenut {
    protected String scheme;
    protected String authority;
    protected String path;
    protected ResourceNamespace namespace;

    public String getScheme() {
        return this.scheme;
    }

    public void setScheme( String scheme ) {
        this.scheme = scheme;
    }

    public String getAuthority() {
        return this.authority;
    }

    public void setAuthority( String authority ) {
        this.authority = authority;
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

