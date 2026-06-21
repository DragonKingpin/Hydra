package com.pinecone.hydra.unit.imperium.entity;

import com.pinecone.framework.util.id.GUID;

public class CachePathBinding extends GenericCachePath {
    protected Long id;

    protected GUID guid;

    protected String pathHash;

    protected Integer hashSlot;

    public Long getId() {
        return this.id;
    }

    public void setId( Long id ) {
        this.id = id;
    }

    public GUID getGuid() {
        return this.guid;
    }

    public void setGuid( GUID guid ) {
        this.guid = guid;
    }

    public String getPathHash() {
        return this.pathHash;
    }

    public void setPathHash( String pathHash ) {
        this.pathHash = pathHash;
    }

    public Integer getHashSlot() {
        return this.hashSlot;
    }

    public void setHashSlot( Integer hashSlot ) {
        this.hashSlot = hashSlot;
    }
}
