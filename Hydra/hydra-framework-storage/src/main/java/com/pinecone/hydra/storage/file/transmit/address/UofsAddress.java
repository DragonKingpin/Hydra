package com.pinecone.hydra.storage.file.transmit.address;

import com.pinecone.framework.system.prototype.Pinenut;

public final class UofsAddress implements Pinenut {
    protected final String mOwnerName;
    protected final String mBucketName;
    protected final String mKey;
    protected final String mMountPath;

    public UofsAddress( String ownerName, String bucketName, String key, String mountPath ) {
        this.mOwnerName  = ownerName;
        this.mBucketName = bucketName;
        this.mKey        = key;
        this.mMountPath  = mountPath;
    }

    public String getOwnerName() {
        return this.mOwnerName;
    }

    public String getBucketName() {
        return this.mBucketName;
    }

    public String getKey() {
        return this.mKey;
    }

    public String getMountPath() {
        return this.mMountPath;
    }
}
