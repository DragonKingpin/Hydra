package com.walnut.sparta.ucdn.console.umc.ufmc.protocol;

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
