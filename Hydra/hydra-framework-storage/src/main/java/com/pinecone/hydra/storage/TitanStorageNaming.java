package com.pinecone.hydra.storage;

public class TitanStorageNaming implements StorageNaming{
    @Override
    public String naming( String objectName, String identity ) {
        return String.format( "%s_%s.storage", objectName, identity ); // TODO! CONST
    }
}
