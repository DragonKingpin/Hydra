package com.pinecone.framework.system.construction;

import com.pinecone.framework.system.PineRuntimeException;

public class StructureProviderException extends PineRuntimeException {
    public StructureProviderException( String szMessage ) {
        super( szMessage );
    }

    public StructureProviderException( String szMessage, Throwable cause ) {
        super( szMessage, cause );
    }
}
