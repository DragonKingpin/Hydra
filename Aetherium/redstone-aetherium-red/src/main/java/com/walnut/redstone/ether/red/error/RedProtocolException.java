package com.walnut.redstone.ether.red.error;

public class RedProtocolException extends RuntimeException {
    protected final RedErrorCode errorCode;

    public RedProtocolException( RedErrorCode errorCode, String message ) {
        super( message );
        this.errorCode = errorCode == null ? RedErrorCode.InternalError : errorCode;
    }

    public RedErrorCode getErrorCode() {
        return this.errorCode;
    }
}

