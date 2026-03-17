package com.pinecone.hydra.unit.vgraph;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.vgraph.entity.GraphNode;
import com.pinecone.hydra.unit.vgraph.layer.Layer;
import com.pinecone.hydra.unit.vgraph.layer.LayerInstrument;

import java.util.List;

public interface VectorDAG extends Pinenut {
    GUID getGraphGuid();

    GUID getAffiliateLayerGuid();

    Layer getAffiliateLayer();

    boolean isPersistenceGraph();

    List<GUID> fetchSourceGuids( long offset, long limit );

    List<GUID> fetchSourceGuidsByTaskPriority( long offset, long limit );

    long countSourceNodes();

    List<GUID> fetchDownstreamNodeGuid( GUID nodeGuid, long offset, long limit );

    List<GUID> fetchUpstreamNodeGuid( GUID nodeGuid, long offset, long limit );

    long queryInDegree( GUID nodeGuid );

    long queryOutDegree( GUID nodeGuid );

    VectorGraphConfig getConfig();

    List<GraphNode> fetchChildNodes( GUID guid );

    List<GUID> fetchChildNodeGuids( GUID guid );

    List<GUID> fetchChildNodeGuids( long offset, long limit, GUID guid );

    long countChildNodeNum( GUID guid );

    GraphNode get( GUID guid );

    void removeNode( GUID guid );

    long getPriorityByInDegree( GUID guid );

    void addChild( GUID parentGuid, GUID childGuid );
}
