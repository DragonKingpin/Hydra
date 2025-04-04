package com.pinecone.hydra.unit.vgraph.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.vgraph.entity.GraphNode;

import java.util.List;

public interface VectorGraphManipulator extends Pinenut {
    void insertStartNode( GraphNode graphNode );

    void insertIntermediateNode(GUID parentGuid, GraphNode graphNode);

    void removeNode( GUID guid );

    GraphNode queryNode( GUID guid );

    List<GraphNode> fetchChildNodes( GUID guid );

    List<GUID> fetchChildNodeGuids( GUID guid );

    void updateNode( GraphNode graphNode );


}
