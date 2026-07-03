package com.pinecone.ulf.util.protobuf.map;

import com.pinecone.framework.system.PineRuntimeException;

public class MapProtobufException extends PineRuntimeException {
    public MapProtobufException( String message ) {
        super( message );
    }

    public MapProtobufException( String message, Throwable cause ) {
        super( message, cause );
    }
}
