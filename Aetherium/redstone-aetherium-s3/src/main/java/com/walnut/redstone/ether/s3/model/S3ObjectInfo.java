package com.walnut.redstone.ether.s3.model;

import java.time.LocalDateTime;

import com.pinecone.framework.system.prototype.Pinenut;

public class S3ObjectInfo implements Pinenut {
    protected String key;
    protected String name;
    protected Long size;
    protected String etag;
    protected String contentType;
    protected LocalDateTime lastModified;
    protected boolean folder;

    public String getKey() {
        return this.key;
    }

    public void setKey( String key ) {
        this.key = key;
    }

    public String getName() {
        return this.name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public Long getSize() {
        return this.size;
    }

    public void setSize( Long size ) {
        this.size = size;
    }

    public String getEtag() {
        return this.etag;
    }

    public void setEtag( String etag ) {
        this.etag = etag;
    }

    public String getContentType() {
        return this.contentType;
    }

    public void setContentType( String contentType ) {
        this.contentType = contentType;
    }

    public LocalDateTime getLastModified() {
        return this.lastModified;
    }

    public void setLastModified( LocalDateTime lastModified ) {
        this.lastModified = lastModified;
    }

    public boolean isFolder() {
        return this.folder;
    }

    public void setFolder( boolean folder ) {
        this.folder = folder;
    }
}


