package com.pinecone.hydra.unit.vgraph.layer.source;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.imperium.source.TriePathCacheManipulator;

public interface LayerPathCacheManipulator extends TriePathCacheManipulator {
    void insert (GUID guid, String path );

    void insertLongPath( GUID guid, String path, String longPath );

    void remove ( GUID guid );

    String getPath ( GUID guid );

    GUID getNode ( String path );

    GUID queryGUIDByPath( String path );
}
