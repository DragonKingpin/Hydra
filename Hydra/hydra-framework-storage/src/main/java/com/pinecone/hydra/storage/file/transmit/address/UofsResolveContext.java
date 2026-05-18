package com.pinecone.hydra.storage.file.transmit.address;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.storage.StorageConstants;

public class UofsResolveContext implements Pinenut {
    protected String mDefaultUserIdentifier = "root";
    protected String mDefaultBucketName = "default";
    protected String mHydraMountPath;
    protected String mPathNameSeparator = StorageConstants.PathSeparator;

    public String getDefaultUserIdentifier() {
        return this.mDefaultUserIdentifier;
    }

    public void setDefaultUserIdentifier( String defaultUserIdentifier ) {
        this.mDefaultUserIdentifier = defaultUserIdentifier;
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

    public String getPathNameSeparator() {
        return this.mPathNameSeparator == null || this.mPathNameSeparator.isEmpty()
                ? StorageConstants.PathSeparator
                : this.mPathNameSeparator;
    }

    public void setPathNameSeparator( String pathNameSeparator ) {
        this.mPathNameSeparator = pathNameSeparator;
    }
}
