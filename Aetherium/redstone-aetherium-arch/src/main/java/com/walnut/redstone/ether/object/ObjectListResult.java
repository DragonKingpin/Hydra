package com.walnut.redstone.ether.object;

import java.util.ArrayList;
import java.util.List;

import com.pinecone.framework.system.prototype.Pinenut;

public class ObjectListResult implements Pinenut {
    protected String bucket;
    protected String prefix;
    protected int maxKeys;
    protected boolean truncated;
    protected String nextContinuationToken;
    protected List<ObjectEntry> objects = new ArrayList<>();

    public String getBucket() {
        return this.bucket;
    }

    public void setBucket( String bucket ) {
        this.bucket = bucket;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public void setPrefix( String prefix ) {
        this.prefix = prefix;
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

    public String getNextContinuationToken() {
        return this.nextContinuationToken;
    }

    public void setNextContinuationToken( String nextContinuationToken ) {
        this.nextContinuationToken = nextContinuationToken;
    }

    public List<ObjectEntry> getObjects() {
        return this.objects;
    }

    public void setObjects( List<ObjectEntry> objects ) {
        this.objects = objects == null ? new ArrayList<>() : new ArrayList<>( objects );
    }
}

