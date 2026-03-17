package com.walnut.odin.atlas.mapper;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.vgraph.source.VectorGraphManipulator;

import com.walnut.odin.atlas.graph.entity.TaskGraphNode;

public interface TaskGraphManipulator extends VectorGraphManipulator {

    @Override
    TaskGraphNode queryNode( GUID guid );

    TaskGraphNode getNodeByTaskGuid( GUID taskGuid );

    GUID queryTaskGuidByNodeId( GUID nodeId );

}
