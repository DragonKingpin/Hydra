package com.pinecone.hydra.atlas.advance;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.orchestration.GraphNode;
import com.pinecone.hydra.unit.iqueue.entity.QueueElement;
import com.pinecone.hydra.unit.vgraph.VectorDAG;

import java.util.List;

public interface GraphAdvancer extends Pinenut {
    void traverse( VectorDAG vectorDAG );

    List<QueueElement> fetchExecuteNode(long offset, long limit );
}
