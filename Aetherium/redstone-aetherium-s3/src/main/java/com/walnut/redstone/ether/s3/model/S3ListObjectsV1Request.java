package com.walnut.redstone.ether.s3.model;

import com.pinecone.framework.system.prototype.Pinenut;

public class S3ListObjectsV1Request implements Pinenut {
    protected String prefix;
    protected String marker;
    protected String delimiter;
    protected String encodingType;
    protected Integer maxKeys;

    public String getPrefix() {
        return this.prefix;
    }

    public void setPrefix( String prefix ) {
        this.prefix = prefix;
    }

    public String getMarker() {
        return this.marker;
    }

    public void setMarker( String marker ) {
        this.marker = marker;
    }

    public String getDelimiter() {
        return this.delimiter;
    }

    public void setDelimiter( String delimiter ) {
        this.delimiter = delimiter;
    }

    public String getEncodingType() {
        return this.encodingType;
    }

    public void setEncodingType( String encodingType ) {
        this.encodingType = encodingType;
    }

    public Integer getMaxKeys() {
        return this.maxKeys;
    }

    public void setMaxKeys( Integer maxKeys ) {
        this.maxKeys = maxKeys;
    }
}
