package com.pinecone.ulf.util.protobuf;

import com.pinecone.framework.system.PineRuntimeException;

public class ProtobufEncodeException extends PineRuntimeException {
    public ProtobufEncodeException    () {
        super();
    }

    public ProtobufEncodeException    ( String message ) {
        super(message);
    }

    public ProtobufEncodeException    ( String message, Throwable cause ) {
        super(message, cause);
    }

    public ProtobufEncodeException    ( Throwable cause ) {
        super(cause);
    }

    protected ProtobufEncodeException ( String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace ) {
        super( message, cause, enableSuppression, writableStackTrace );
    }
}
