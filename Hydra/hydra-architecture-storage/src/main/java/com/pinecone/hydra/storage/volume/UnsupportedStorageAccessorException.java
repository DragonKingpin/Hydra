package com.pinecone.hydra.storage.volume;

public class UnsupportedStorageAccessorException extends IllegalArgumentException {
    public UnsupportedStorageAccessorException( String message ) {
        super( message );
    }
}
