package com.pinecone.hydra.storage.lifecycle;

public class StorageLifecycleWarning extends StorageLifecycleBlocker {
    public StorageLifecycleWarning() {
    }

    public StorageLifecycleWarning( String code, String message, long count ) {
        super( code, message, count );
    }
}
