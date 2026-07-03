package com.walnut.redstone.ether.shuttle.error;

public class ShuttleException extends RuntimeException {
    protected final ShuttleErrorCode errorCode;

    public ShuttleException( ShuttleErrorCode errorCode, String message ) {
        this( errorCode, message, null );
    }

    public ShuttleException( ShuttleErrorCode errorCode, String message, Throwable cause ) {
        super( message, cause );
        this.errorCode = errorCode == null ? ShuttleErrorCode.ClientError : errorCode;
    }

    public ShuttleErrorCode getErrorCode() {
        return this.errorCode;
    }
}
