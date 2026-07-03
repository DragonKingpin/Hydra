package com.walnut.redstone.ether.s3.error;

public enum S3ErrorCode {
    NO_SUCH_BUCKET( "NoSuchBucket", 404 ),
    NO_SUCH_KEY( "NoSuchKey", 404 ),
    INVALID_ARGUMENT( "InvalidArgument", 400 ),
    MISSING_CONTENT_LENGTH( "MissingContentLength", 411 ),
    INVALID_RANGE( "InvalidRange", 416 ),
    INTERNAL_ERROR( "InternalError", 500 );

    protected final String code;
    protected final int statusCode;

    S3ErrorCode( String code, int statusCode ) {
        this.code = code;
        this.statusCode = statusCode;
    }

    public String getCode() {
        return this.code;
    }

    public int getStatusCode() {
        return this.statusCode;
    }
}

