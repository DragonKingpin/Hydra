package com.walnut.sailor.stream.fm.protocol;

import com.pinecone.framework.system.prototype.Pinenut;

public class UFMCFileMeta64 implements Pinenut {
    protected String sourceName;

    public UFMCFileMeta64(){}

    public UFMCFileMeta64( String sourceName ){
        this.sourceName = sourceName;
    }

    public String getSourceName(){
        return this.sourceName;
    }

    public void setSourceName( String sourceName ){
        this.sourceName = sourceName;
    }
}
