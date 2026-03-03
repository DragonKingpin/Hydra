package com.pinecone.hydra.atlas.graph.source;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.atlas.graph.entity.TaskGraphNode;
import com.pinecone.hydra.unit.vgraph.source.VectorGraphManipulator;

public interface TaskGraphManipulator extends VectorGraphManipulator {

    @Override
    TaskGraphNode queryNode( GUID guid );

    TaskGraphNode getNodeByTaskGuid( GUID taskGuid );

    GUID queryTaskGuidByNodeId( GUID nodeId );

}
