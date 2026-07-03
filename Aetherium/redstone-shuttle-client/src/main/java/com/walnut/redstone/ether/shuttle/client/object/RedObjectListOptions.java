package com.walnut.redstone.ether.shuttle.client.object;

import com.pinecone.framework.system.prototype.Pinenut;

public class RedObjectListOptions implements Pinenut {
    protected String prefix;
    protected boolean recursive = true;

    public static RedObjectListOptions empty() {
        return new RedObjectListOptions();
    }

    public String getPrefix() {
        return this.prefix;
    }

    public void setPrefix( String prefix ) {
        this.prefix = prefix;
    }

    public boolean isRecursive() {
        return this.recursive;
    }

    public void setRecursive( boolean recursive ) {
        this.recursive = recursive;
    }
}
