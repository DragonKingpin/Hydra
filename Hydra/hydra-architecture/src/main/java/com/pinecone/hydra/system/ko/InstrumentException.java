package com.pinecone.hydra.system.ko;

import com.pinecone.framework.system.prototype.Pinenut;

public class InstrumentException extends Exception implements Pinenut {

    public InstrumentException() {
        super();
    }

    public InstrumentException( String message ) {
        super(message);
    }

    public InstrumentException( String message, Throwable cause ) {
        super(message, cause);
    }

    public InstrumentException( Throwable cause ) {
        super(cause);
    }

}