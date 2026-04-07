package com.sauron.heist.heistron;

import com.pinecone.framework.system.prototype.Pinenut;

public class HeistException extends Exception implements Pinenut {

    public HeistException    () {
        super();
    }

    public HeistException    ( String message ) {
        super(message);
    }

    public HeistException    ( String message, Throwable cause ) {
        super(message, cause);
    }

    public HeistException    ( Throwable cause ) {
        super(cause);
    }

}
