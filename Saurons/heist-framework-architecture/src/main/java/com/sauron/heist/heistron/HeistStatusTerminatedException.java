package com.sauron.heist.heistron;

import com.pinecone.framework.system.prototype.Pinenut;

public class HeistStatusTerminatedException extends IllegalStateException implements Pinenut {

    public HeistStatusTerminatedException    () {
        super();
    }

    public HeistStatusTerminatedException    ( String message ) {
        super(message);
    }

    public HeistStatusTerminatedException    ( String message, Throwable cause ) {
        super(message, cause);
    }

    public HeistStatusTerminatedException    ( Throwable cause ) {
        super(cause);
    }

}
