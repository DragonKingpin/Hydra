package com.pinecone.hydra.atlas.graph.source;

import com.pinecone.hydra.unit.vgraph.source.AtlasMasterManipulator;

public interface RuntimeMasterManipulator extends AtlasMasterManipulator {

    QueueStratumManipulator      getQueueStratumManipulator();

}
