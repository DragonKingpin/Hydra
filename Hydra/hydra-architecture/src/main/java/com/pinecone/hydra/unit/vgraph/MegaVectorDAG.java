package com.pinecone.hydra.unit.vgraph;

import com.pinecone.framework.system.prototype.PineUnit;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.vgraph.entity.GraphNode;
import com.pinecone.hydra.unit.vgraph.source.VectorGraphMasterManipulator;

import java.util.List;

public interface MegaVectorDAG extends VectorDAG, PineUnit {

    VectorGraphMasterManipulator getMasterManipulator();

    GUID put( GraphNode graphNode );

    void put( GUID parentGuid, GraphNode graphNode );

    void putCachePath( String path, GUID guid );

    GraphNode get( GUID guid );

    GUID getGuidByCachePath( String path );

    List<String> getCachePath( GUID guid );

    void remove( GUID guid );

    void removeCache( GUID guid );

    void removeCache( String path );

    List<GraphNode> getChildren( GUID guid );

    List<GUID> fetchChildrenIds(GUID guid);

    List<GUID> fetchParentIds(GUID guid);

}
