package com.pinecone.hydra.storage.lifecycle;

public class StorageLifecycleAction extends StorageLifecycleBlocker {
    public StorageLifecycleAction() {
    }

    public StorageLifecycleAction( String code, String message, long count ) {
        super( code, message, count );
    }
}
