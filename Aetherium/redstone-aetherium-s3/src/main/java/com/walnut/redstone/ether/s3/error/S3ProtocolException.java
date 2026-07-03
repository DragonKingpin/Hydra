package com.walnut.redstone.ether.s3.error;

public class S3ProtocolException extends RuntimeException {
    protected final S3ErrorCode errorCode;
    protected final String resource;

    public S3ProtocolException( S3ErrorCode errorCode, String message ) {
        this( errorCode, message, null, null );
    }

    public S3ProtocolException( S3ErrorCode errorCode, String message, String resource ) {
        this( errorCode, message, resource, null );
    }

    public S3ProtocolException( S3ErrorCode errorCode, String message, String resource, Throwable cause ) {
        super( message, cause );
        this.errorCode = errorCode == null ? S3ErrorCode.INTERNAL_ERROR : errorCode;
        this.resource = resource;
    }

    public S3ErrorCode getErrorCode() {
        return this.errorCode;
    }

    public String getResource() {
        return this.resource;
    }
}


