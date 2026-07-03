package com.walnut.redstone.ether.s3.model;

import java.time.LocalDateTime;

import com.pinecone.framework.system.prototype.Pinenut;

public class S3BucketInfo implements Pinenut {
    protected String bucketName;
    protected LocalDateTime creationDate;

    public String getBucketName() {
        return this.bucketName;
    }

    public void setBucketName( String bucketName ) {
        this.bucketName = bucketName;
    }

    public LocalDateTime getCreationDate() {
        return this.creationDate;
    }

    public void setCreationDate( LocalDateTime creationDate ) {
        this.creationDate = creationDate;
    }
}


