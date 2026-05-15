package com.pinecone.hydra.storage.file.transmit.address;

import com.pinecone.framework.system.prototype.Pinenut;

public class UofsResolveContext implements Pinenut {
    protected String mDefaultOwnerName  = "root";
    protected String mDefaultBucketName = "default";
    protected String mHydraMountPath;

    public String getDefaultOwnerName() {
        return this.mDefaultOwnerName;
    }

    public void setDefaultOwnerName( String defaultOwnerName ) {
        this.mDefaultOwnerName = defaultOwnerName;
    }

    public String getDefaultBucketName() {
        return this.mDefaultBucketName;
    }

    public void setDefaultBucketName( String defaultBucketName ) {
        this.mDefaultBucketName = defaultBucketName;
    }

    public String getHydraMountPath() {
        return this.mHydraMountPath;
    }

    public void setHydraMountPath( String hydraMountPath ) {
        this.mHydraMountPath = hydraMountPath;
    }
}
