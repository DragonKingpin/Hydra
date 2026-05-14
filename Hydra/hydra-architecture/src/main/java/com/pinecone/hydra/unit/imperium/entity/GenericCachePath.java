package com.pinecone.hydra.unit.imperium.entity;

public class GenericCachePath implements CachePath {
    protected String path;

    protected String longPath;

    @Override
    public String getPath() {
        return this.path;
    }

    public void setPath( String path ) {
        this.path = path;
    }

    @Override
    public String getLongPath() {
        return this.longPath;
    }

    public void setLongPath( String longPath ) {
        this.longPath = longPath;
    }
}
