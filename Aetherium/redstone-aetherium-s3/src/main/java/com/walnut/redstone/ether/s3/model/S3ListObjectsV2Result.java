package com.walnut.redstone.ether.s3.model;

public class S3ListObjectsV2Result extends S3ListBucketResult {
    protected String nextContinuationToken;

    public String getNextContinuationToken() {
        return this.nextContinuationToken;
    }

    public void setNextContinuationToken( String nextContinuationToken ) {
        this.nextContinuationToken = nextContinuationToken;
    }
}

