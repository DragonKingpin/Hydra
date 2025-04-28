package com.pinecone.hydra.unit.vgraph;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.vgraph.entity.GraphNode;
import com.pinecone.hydra.unit.vgraph.layer.LayerManager;

import java.util.List;

public interface VectorDAG extends Pinenut {

    List<GUID> fetchHandleGuids( long offset, long limit );

    long countHandleNodes();

    List<GUID> fetchDownstreamNodeGuid( GUID nodeGuid, long offset, long limit );

    List<GUID> fetchUpstreamNodeGuid( GUID nodeGuid, long offset, long limit );

    long queryInDegree( GUID nodeGuid );

    long queryOutDegree( GUID nodeGuid );

    void saveVectorDAG( VectorDAG vectorDAG );

    void addHandleNodeGuid( GUID handleNodeGuid );

    VectorGraphConfig getConfig();

    void save( LayerManager layerManager, String name );

    List<GraphNode> nextNodes( GUID guid );
}
