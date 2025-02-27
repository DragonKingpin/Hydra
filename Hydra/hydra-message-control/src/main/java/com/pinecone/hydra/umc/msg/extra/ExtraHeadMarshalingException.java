package com.pinecone.hydra.umc.msg.extra;

import com.pinecone.framework.system.PineRuntimeException;

public class ExtraHeadMarshalingException extends PineRuntimeException {
    public ExtraHeadMarshalingException() {
        super();
    }

    public ExtraHeadMarshalingException( String message ) {
        super( message );
    }

    public ExtraHeadMarshalingException( String message, Throwable cause ) {
        super( message, cause );
    }

    public ExtraHeadMarshalingException( Throwable cause ) {
        super(cause);
    }
}
