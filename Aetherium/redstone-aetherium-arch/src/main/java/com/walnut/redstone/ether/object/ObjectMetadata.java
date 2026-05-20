package com.walnut.redstone.ether.object;

import java.time.LocalDateTime;

import com.pinecone.framework.system.prototype.Pinenut;
import com.walnut.redstone.ether.resource.ResourceType;

public class ObjectMetadata implements Pinenut {
    protected String bucket;
    protected String key;
    protected String name;
    protected Long size;
    protected String etag;
    protected String contentType;
    protected LocalDateTime lastModified;
    protected ResourceType type = ResourceType.Object;

    public String getBucket() {
        return this.bucket;
    }

    public void setBucket( String bucket ) {
        this.bucket = bucket;
    }

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

    public ResourceType getType() {
        return this.type;
    }

    public void setType( ResourceType type ) {
        this.type = type;
    }
}

