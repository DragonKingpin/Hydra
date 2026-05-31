package com.walnut.redstone.ether.shuttle.client.error;

import com.pinecone.framework.system.prototype.Pinenut;

public class RedShuttleClientException extends RuntimeException implements Pinenut {
    public RedShuttleClientException( String szMessage ) {
        super( szMessage );
    }

    public RedShuttleClientException( String szMessage, Throwable cause ) {
        super( szMessage, cause );
    }
}
