package com.pinecone.hydra.storage.file.remove;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

import java.util.ArrayList;
import java.util.List;

public class UofsRemoveRequest implements Pinenut {
    protected final List<String> mSourcePaths = new ArrayList<>();
    protected GUID mOperatorGuid;
    protected String mszExtConfig;

    public List<String> getSourcePaths() {
        return this.mSourcePaths;
    }

    public void setSourcePaths( List<String> sourcePaths ) {
        this.mSourcePaths.clear();
        if ( sourcePaths != null ) {
            this.mSourcePaths.addAll( sourcePaths );
        }
    }

    public void addSourcePath( String sourcePath ) {
        if ( sourcePath != null ) {
            this.mSourcePaths.add( sourcePath );
        }
    }

    public GUID getOperatorGuid() {
        return this.mOperatorGuid;
    }

    public void setOperatorGuid( GUID operatorGuid ) {
        this.mOperatorGuid = operatorGuid;
    }

    public String getExtConfig() {
        return this.mszExtConfig;
    }

    public void setExtConfig( String extConfig ) {
        this.mszExtConfig = extConfig;
    }
}
