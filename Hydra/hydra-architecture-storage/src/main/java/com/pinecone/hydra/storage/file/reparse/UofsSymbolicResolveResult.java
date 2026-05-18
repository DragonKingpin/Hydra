package com.pinecone.hydra.storage.file.reparse;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public class UofsSymbolicResolveResult implements Pinenut {
    protected boolean mbChanged;
    protected String  mszOriginalPath;
    protected String  mszResolvedPath;
    protected GUID    mResolvedGuid;

    public UofsSymbolicResolveResult( boolean changed, String originalPath, String resolvedPath, GUID resolvedGuid ) {
        this.mbChanged      = changed;
        this.mszOriginalPath = originalPath;
        this.mszResolvedPath = resolvedPath;
        this.mResolvedGuid   = resolvedGuid;
    }

    public boolean isChanged() {
        return this.mbChanged;
    }

    public String getOriginalPath() {
        return this.mszOriginalPath;
    }

    public String getResolvedPath() {
        return this.mszResolvedPath;
    }

    public GUID getResolvedGuid() {
        return this.mResolvedGuid;
    }
}
