package com.pinecone.framework.system.construction;

public class StructureResolutionException extends RuntimeException {
    public StructureResolutionException( String szMessage ) {
        super( szMessage );
    }

    public StructureResolutionException( String szMessage, Throwable cause ) {
        super( szMessage, cause );
    }
}
