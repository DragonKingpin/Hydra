package com.pinecone.hydra.unit.vgraph.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.vgraph.entity.GraphNode;

import java.util.List;

public interface VectorGraphManipulator extends Pinenut {
    void insertHandleNode( GraphNode graphNode );

    void insertNodeByEdge( GUID parentGuid, GraphNode graphNode );


    void removeNode( GUID guid );

    GraphNode queryNode( GUID guid );

    List<GUID> fetchParentIds(GUID guid );

    List<GraphNode> fetchChildNodes( GUID guid );

    List<GraphNode> fetchRootNodes();

    List<GUID> fetchChildNodeIds(GUID guid );

    List<GraphNode> fetchNodesByName( String name );

    void updateNode( GraphNode graphNode );

    List<GUID> fetchHandleGuids(long offset, long limit);

    long countHandleNodes();

    List<GUID> fetchDownstreamNodeGuid(GUID nodeGuid, long offset, long limit);

    List<GUID> fetchUpstreamNodeGuid(GUID nodeGuid, long offset, long limit);

    long queryInDegree(GUID nodeGuid);

    long queryOutDegree(GUID nodeGuid);

}
