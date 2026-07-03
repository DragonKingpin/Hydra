package com.walnut.redstone.ether.s3.model;

public class S3ListObjectsV2Request extends S3ListObjectsV1Request {
    protected String continuationToken;
    protected String startAfter;
    protected Integer fetchOwner;

    public String getContinuationToken() {
        return this.continuationToken;
    }

    public void setContinuationToken( String continuationToken ) {
        this.continuationToken = continuationToken;
    }

    public String getStartAfter() {
        return this.startAfter;
    }

    public void setStartAfter( String startAfter ) {
        this.startAfter = startAfter;
    }

    public Integer getFetchOwner() {
        return this.fetchOwner;
    }

    public void setFetchOwner( Integer fetchOwner ) {
        this.fetchOwner = fetchOwner;
    }
}
