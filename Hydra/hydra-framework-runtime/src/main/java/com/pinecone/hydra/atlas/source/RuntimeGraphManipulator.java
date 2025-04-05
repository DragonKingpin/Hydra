package com.pinecone.hydra.atlas.source;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.vgraph.entity.GraphNode;
import com.pinecone.hydra.unit.vgraph.source.VectorGraphManipulator;

import java.util.List;

public interface RuntimeGraphManipulator extends VectorGraphManipulator {
    void insertStartNode( GraphNode graphNode );

    void insertNode(GUID parentGuid, GraphNode graphNode);


    void removeNode( GUID guid );

    GraphNode queryNode( GUID guid );

    List<GUID> fetchParentIds(GUID guid );

    List<GraphNode> fetchChildNodes( GUID guid );

    List<GraphNode> fetchRootNodes();

    List<GUID> fetchChildNodeIds(GUID guid );

    List<GraphNode> fetchNodesByName( String name );

    void updateNode( GraphNode graphNode );
}
