package com.pinecone.hydra.storage.file.transmit.address;

import com.pinecone.framework.system.prototype.Pinenut;

public final class UofsAddress implements Pinenut {
    protected final String mUserIdentifier;
    protected final String mBucketName;
    protected final String mKey;
    protected final String mMountPath;

    public UofsAddress( String userIdentifier, String bucketName, String key, String mountPath ) {
        this.mUserIdentifier = userIdentifier;
        this.mBucketName     = bucketName;
        this.mKey            = key;
        this.mMountPath      = mountPath;
    }

    public String getUserIdentifier() {
        return this.mUserIdentifier;
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
