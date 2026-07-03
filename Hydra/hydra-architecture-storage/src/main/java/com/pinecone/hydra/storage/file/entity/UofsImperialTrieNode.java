package com.pinecone.hydra.storage.file.entity;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.imperium.GUIDImperialTrieNode;

public class UofsImperialTrieNode extends GUIDImperialTrieNode {
    protected GUID mBucketGuid;

    public GUID getBucketGuid() {
        return this.mBucketGuid;
    }

    public void setBucketGuid( GUID bucketGuid ) {
        this.mBucketGuid = bucketGuid;
    }
}
