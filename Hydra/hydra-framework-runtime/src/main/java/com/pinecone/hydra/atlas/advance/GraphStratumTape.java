package com.pinecone.hydra.atlas.advance;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.iqueue.MegaDeflectPriorityQueue;
import com.pinecone.hydra.unit.vgraph.entity.GraphNode;

import java.util.List;

public interface GraphStratumTape extends Pinenut {

    GraphNode queryNodeByIndex( long index );

    GUID queryNodeGuidByIndex( long index );

    List<GraphNode> fetchNodes( List<GUID> guids );

    List<GraphNode> fetchNodes( long offset, long limit );

    List<GraphNode> fetchNodes( long queuePriority, long offset, long limit );


    List<GUID> fetchGuids( long offset, long limit );

    List<GUID> fetchGuids( long queryPriority, long offset, long limit );

    int countStratum();

    MegaDeflectPriorityQueue query( int stratumId, short runtimePriority );

    MegaDeflectPriorityQueue getExecutionPriorityQueue();

}

