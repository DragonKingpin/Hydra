package com.walnut.sparta.ucdn.console.umc.ssfm.protocol;

public class UFMCFileMeta64 {
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
