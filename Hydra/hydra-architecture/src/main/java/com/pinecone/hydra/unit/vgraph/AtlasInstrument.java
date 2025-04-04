package com.pinecone.hydra.unit.vgraph;

import com.pinecone.framework.system.executum.Processum;
import com.pinecone.framework.system.regime.Instrument;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;
import com.pinecone.hydra.unit.vgraph.entity.GraphNode;

import java.util.List;

public interface AtlasInstrument extends Instrument {
    AtlasInstrument parent();

    Processum getSuperiorProcess();

    void setParent( AtlasInstrument atlasInstrument );

    String getPath( GUID guid );

    GUID queryGUIDByPath( String path );

    default GUID assertPath( String path, String pathType ) throws IllegalArgumentException {
        GUID guid      = this.queryGUIDByPath( path );
        if( guid == null ) {
            throw new IllegalArgumentException( "Undefined " + pathType + " '" + path + "'" );
        }

        return guid;
    }

    default GUID assertPath( String path ) throws IllegalArgumentException {
        return this.assertPath( path, "path" );
    }

    boolean contains( GUID nodeGuid );

    GUID put( GraphNode graphNode );

    GraphNode get( GUID guid );

    GUID queryGUIDByNS( String path, String szBadSep, String szTargetSep );

    TreeNode get(GUID guid, int depth );

    TreeNode getSelf( GUID guid );

    void remove( GUID guid );

    void remove( String path );

    List<GraphNode> getChildren( GUID guid );

    List<GUID > fetchChildrenGuids( GUID guid );

    void rename( GUID guid, String name );
}
