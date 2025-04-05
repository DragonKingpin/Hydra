package com.pinecone.hydra.atlas.runtime.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.atlas.source.RuntimeGraphCachePathManipulator;
import com.pinecone.hydra.unit.vgraph.source.VectorGraphPathCacheManipulator;

public interface RuntimeVectorGraphPathCacheMapper extends RuntimeGraphCachePathManipulator {
    void insert(String path, GUID guid);

    void insertLongPath( GUID guid, String path, String longPath );

    void remove ( GUID guid );

    String getPath ( GUID guid );

    GUID getNode ( String path );

    GUID queryGUIDByPath( String path );
}
