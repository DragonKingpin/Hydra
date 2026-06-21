package com.pinecone.hydra.storage.file.remove;

import com.pinecone.framework.system.prototype.Pinenut;

import java.util.ArrayList;
import java.util.List;

public class UofsRemovePlan implements Pinenut {
    protected final List<String> mSourcePaths = new ArrayList<>();
    protected long mnTotalCount;
    protected boolean mbDangerous;

    public List<String> getSourcePaths() {
        return this.mSourcePaths;
    }

    public void setSourcePaths( List<String> sourcePaths ) {
        this.mSourcePaths.clear();
        if ( sourcePaths != null ) {
            this.mSourcePaths.addAll( sourcePaths );
        }
    }

    public long getTotalCount() {
        return this.mnTotalCount;
    }

    public void setTotalCount( long totalCount ) {
        this.mnTotalCount = totalCount;
    }

    public boolean isDangerous() {
        return this.mbDangerous;
    }

    public void setDangerous( boolean dangerous ) {
        this.mbDangerous = dangerous;
    }
}
