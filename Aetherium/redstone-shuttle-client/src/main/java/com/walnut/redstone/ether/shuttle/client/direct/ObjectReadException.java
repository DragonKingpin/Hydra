package com.walnut.redstone.ether.shuttle.client.direct;

public class ObjectReadException extends RuntimeException {
    public ObjectReadException( String message ) {
        super( message );
    }

    public ObjectReadException( String message, Throwable cause ) {
        super( message, cause );
    }
}
