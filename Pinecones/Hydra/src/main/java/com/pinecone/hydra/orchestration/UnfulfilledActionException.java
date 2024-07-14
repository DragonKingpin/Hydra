package com.pinecone.hydra.orchestration;

import com.pinecone.framework.system.PineRuntimeException;

public class UnfulfilledActionException extends PineRuntimeException {
    protected Exertion exertion;

    public UnfulfilledActionException( Exertion exertion ) {
        this( null, "", exertion );
    }

    public UnfulfilledActionException( Throwable cause, String message, Exertion exertion ) {
        super( message, cause );
        this.exertion = exertion;
    }

    public UnfulfilledActionException( String message, Exertion exertion ) {
        this( null, message, exertion );
    }

    public UnfulfilledActionException ( Throwable cause ) {
        super(cause);
    }

    public Exertion getExertion() {
        return this.exertion;
    }
}
