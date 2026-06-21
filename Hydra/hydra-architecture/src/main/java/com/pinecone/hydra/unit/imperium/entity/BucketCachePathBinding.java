package com.pinecone.hydra.unit.imperium.entity;

import com.pinecone.framework.util.id.GUID;

public class BucketCachePathBinding extends CachePathBinding {
    protected GUID bucketGuid;

    public GUID getBucketGuid() {
        return this.bucketGuid;
    }

    public void setBucketGuid( GUID bucketGuid ) {
        this.bucketGuid = bucketGuid;
    }
}
