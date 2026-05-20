package com.walnut.redstone.ether.error;

public class EtherException extends RuntimeException {
    protected final EtherErrorCode errorCode;

    public EtherException( EtherErrorCode errorCode, String message ) {
        super( message );
        this.errorCode = errorCode == null ? EtherErrorCode.InternalError : errorCode;
    }

    public EtherErrorCode getErrorCode() {
        return this.errorCode;
    }
}

