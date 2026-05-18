package com.pinecone.hydra.storage.file.reparse;

import com.pinecone.framework.system.prototype.Pinenut;

public class UofsSymbolicResolveConfig implements Pinenut {
    public static final int DefaultMaxDepth = 16;

    protected int mnMaxDepth = DefaultMaxDepth;

    public UofsSymbolicResolveConfig() {
    }

    public UofsSymbolicResolveConfig( int maxDepth ) {
        this.mnMaxDepth = maxDepth;
    }

    public int getMaxDepth() {
        return this.mnMaxDepth;
    }
}
