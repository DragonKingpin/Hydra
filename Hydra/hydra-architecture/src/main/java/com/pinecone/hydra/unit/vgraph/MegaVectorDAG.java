package com.pinecone.hydra.unit.vgraph;

import com.pinecone.framework.system.prototype.PineUnit;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.vgraph.entity.GraphNode;

import java.util.List;

public interface MegaVectorDAG extends VectorDAG, PineUnit {
    void insertInletNode(GraphNode graphNode );

    void insertNode(GUID parentGuid, GraphNode graphNode );

    void purge(GUID guid );

    GraphNode getGraphNode( GUID guid );

    GraphNode getGraphNode( String path );

    GUID queryIdByPath(String path );

    List<GraphNode> fetchChildren( GUID guid );

    List<GUID> fetchChildrenIds(GUID guid );

    String getCachePath( GUID guid );

    void removeCachePath( GUID guid );

    GraphNode updateGraphNode( GraphNode graphNode );

}
