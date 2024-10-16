package com.pinecone.hydra.registry.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public interface RegistryNodePathManipulator extends Pinenut {
    void insert( GUID guid, String path );

    void remove( GUID guid );

    String getPath( GUID guid );

    GUID getNode( String path );
}
