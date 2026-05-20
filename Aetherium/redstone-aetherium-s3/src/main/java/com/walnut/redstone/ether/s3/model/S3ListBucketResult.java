package com.walnut.redstone.ether.s3.model;

import java.util.ArrayList;
import java.util.List;

import com.pinecone.framework.system.prototype.Pinenut;

public class S3ListBucketResult implements Pinenut {
    protected String bucketName;
    protected String prefix;
    protected String marker;
    protected String delimiter;
    protected String encodingType;
    protected int maxKeys;
    protected boolean truncated;
    protected List<S3ObjectInfo> objects = new ArrayList<>();

    public String getBucketName() {
        return this.bucketName;
    }

    public void setBucketName( String bucketName ) {
        this.bucketName = bucketName;
    }

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

    public int getMaxKeys() {
        return this.maxKeys;
    }

    public void setMaxKeys( int maxKeys ) {
        this.maxKeys = maxKeys;
    }

    public boolean isTruncated() {
        return this.truncated;
    }

    public void setTruncated( boolean truncated ) {
        this.truncated = truncated;
    }

    public List<S3ObjectInfo> getObjects() {
        return this.objects;
    }

    public void setObjects( List<S3ObjectInfo> objects ) {
        this.objects = objects == null ? new ArrayList<>() : new ArrayList<>( objects );
    }
}

