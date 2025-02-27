package com.walnut.sparta.ucdn.console.umc.ufm.protocol;

public class FileMeta64 {

    protected String sourceName;

    protected long size;

    protected long validateVal;

    public FileMeta64() {}

    public FileMeta64( String sourceName, long size, long validateVal ) {
        this.size        = size;
        this.sourceName  = sourceName;
        this.validateVal = validateVal;
    }


    public String getSourceName() {
        return this.sourceName;
    }

    public void setSourceName( String sourceName ) {
        this.sourceName = sourceName;
    }

    public long getSize() {
        return this.size;
    }

    public void setSize( long size ) {
        this.size = size;
    }

    public long getValidateVal() {
        return this.validateVal;
    }

    public void setValidateVal( long validateVal ) {
        this.validateVal = validateVal;
    }

}
