package com.walnut.odin.task.troll;

import com.pinecone.framework.system.prototype.Pinenut;

public class ElevationException extends Exception implements Pinenut {

    public ElevationException() {
        super();
    }

    public ElevationException( String message ) {
        super(message);
    }

    public ElevationException( String message, Throwable cause ) {
        super(message, cause);
    }

    public ElevationException( Throwable cause ) {
        super(cause);
    }

}