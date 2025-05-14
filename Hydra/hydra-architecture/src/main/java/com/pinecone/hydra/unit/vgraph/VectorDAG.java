package com.pinecone.hydra.unit.vgraph;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.vgraph.entity.GraphNode;
import com.pinecone.hydra.unit.vgraph.layer.Layer;
import com.pinecone.hydra.unit.vgraph.layer.LayerInstrument;

import java.util.List;

public interface VectorDAG extends Pinenut {
    GUID getGuid();

    Layer getAffiliateLayer();

    default boolean isPersistenceGraph() {
        return this.getAffiliateLayer() != null;
    }

    List<GUID> fetchHandleGuids( long offset, long limit );

    List<GUID> fetchHandleGuidsByTaskPriority( long offset, long limit );

    long countHandleNodes();

    List<GUID> fetchDownstreamNodeGuid( GUID nodeGuid, long offset, long limit );

    List<GUID> fetchUpstreamNodeGuid( GUID nodeGuid, long offset, long limit );

    long queryInDegree( GUID nodeGuid );

    long queryOutDegree( GUID nodeGuid );

    void addHandleNodeGuid( GUID handleNodeGuid );

    VectorGraphConfig getConfig();

    Layer persistenceAsLayer( LayerInstrument layerInstrument, String name );

    List<GraphNode> fetchChildNodes( GUID guid );

    List<GUID> fetchChildNodeGuids( GUID guid );

    List<GUID> fetchChildNodeGuids( long offset, long limit, GUID guid );

    long countChildNodeNum( GUID guid );

    GraphNode get( GUID guid );

    void removeNode( GUID guid );

    long getPriorityByInDegree( GUID guid );

    void addChild( GUID parentGuid, GUID childGuid );
}
