package com.walnut.odin.conduct;

import com.pinecone.framework.system.prototype.Pinenut;

public class RegimentException extends Exception implements Pinenut {

    public RegimentException() {
        super();
    }

    public RegimentException( String message ) {
        super(message);
    }

    public RegimentException( String message, Throwable cause ) {
        super(message, cause);
    }

    public RegimentException( Throwable cause ) {
        super(cause);
    }

}