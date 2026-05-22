package com.pinecone.framework.system.construction;

public class StructureProviderException extends RuntimeException {
    public StructureProviderException( String szMessage ) {
        super( szMessage );
    }

    public StructureProviderException( String szMessage, Throwable cause ) {
        super( szMessage, cause );
    }
}
