package com.pinecone.hydra.business.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.business.entity.GenericNodeCachePath;
import com.pinecone.hydra.unit.imperium.source.TriePathCacheManipulator;

public interface PathManipulator extends TriePathCacheManipulator, Pinenut {

    @Override
    void insert( GUID guid, String szPath );

    @Override
    void insertLongPath( GUID guid, String szPath, String szLongPath );

    @Override
    void remove( GUID guid );

    GenericNodeCachePath getPath0( GUID guid );

    default String getPath( GUID guid ) {
        GenericNodeCachePath cachePath = this.getPath0( guid );
        if ( cachePath == null ) {
            return null;
        }

        return cachePath.getResolvedPath();
    }

    GUID queryGuidByPath( String szPath );

    @Override
    default GUID getNode( String szPath ) {
        return this.queryGuidByPath( szPath );
    }

    @Override
    default GUID queryGUIDByPath( String szPath ) {
        return this.queryGuidByPath( szPath );
    }
}
