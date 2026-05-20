package com.walnut.redstone.ether.s3;

import com.pinecone.framework.system.prototype.Pinenut;

public class S3CompatibilityProfile implements Pinenut {
    protected boolean pathStyleAccess = true;
    protected boolean listObjectsV2 = true;

    public boolean isPathStyleAccess() {
        return this.pathStyleAccess;
    }

    public void setPathStyleAccess( boolean pathStyleAccess ) {
        this.pathStyleAccess = pathStyleAccess;
    }

    public boolean isListObjectsV2() {
        return this.listObjectsV2;
    }

    public void setListObjectsV2( boolean listObjectsV2 ) {
        this.listObjectsV2 = listObjectsV2;
    }
}

