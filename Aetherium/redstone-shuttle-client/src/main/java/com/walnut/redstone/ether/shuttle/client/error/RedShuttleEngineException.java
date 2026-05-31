package com.walnut.redstone.ether.shuttle.client.error;

public class RedShuttleEngineException extends RedShuttleClientException {
    public RedShuttleEngineException( String szMessage ) {
        super( szMessage );
    }

    public RedShuttleEngineException( String szMessage, Throwable cause ) {
        super( szMessage, cause );
    }
}
