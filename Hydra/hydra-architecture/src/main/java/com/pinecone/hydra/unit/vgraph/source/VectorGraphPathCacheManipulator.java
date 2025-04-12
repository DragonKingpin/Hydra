package com.pinecone.hydra.unit.vgraph.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

import java.util.List;

public interface VectorGraphPathCacheManipulator extends Pinenut {
    void insert(String path, GUID guid);

    void insertLongPath( GUID guid, String path, String longPath );

    void remove ( GUID guid );

    void removeByPath( String path );

    List<String> getPath (GUID guid );

    GUID getNode ( String path );

    GUID queryGUIDByPath( String path );
}
