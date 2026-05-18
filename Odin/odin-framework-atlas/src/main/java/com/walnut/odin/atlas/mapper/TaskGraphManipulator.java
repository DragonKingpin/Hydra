package com.walnut.odin.atlas.mapper;

import java.util.List;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.vgraph.entity.GraphNode;
import com.pinecone.hydra.unit.vgraph.source.VectorGraphManipulator;
import com.pinecone.slime.meta.TableIndex64Meta;

import com.walnut.odin.atlas.graph.entity.TaskGraphNode;

public interface TaskGraphManipulator extends VectorGraphManipulator {

    @Override
    TaskGraphNode queryNode( GUID guid );

    TaskGraphNode getNodeByTaskGuid( GUID taskGuid );

    GUID queryTaskGuidByNodeId( GUID nodeId );

    List<GraphNode> fetchSourceNodes( long offset, long limit );

    List<GraphNode> fetchSourceNodesById( long idStart, long idEnd );

    TableIndex64Meta selectSourceNodeIndexMeta();

}
