package com.pinecone.framework.system.construction;

import com.pinecone.framework.system.PineRuntimeException;

public class StructureResolutionException extends PineRuntimeException {
    public StructureResolutionException( String szMessage ) {
        super( szMessage );
    }

    public StructureResolutionException( String szMessage, Throwable cause ) {
        super( szMessage, cause );
    }
}
