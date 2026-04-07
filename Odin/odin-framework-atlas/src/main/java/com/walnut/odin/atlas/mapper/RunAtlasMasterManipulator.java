package com.walnut.odin.atlas.mapper;

import com.pinecone.hydra.unit.vgraph.source.AtlasMasterManipulator;

public interface RunAtlasMasterManipulator extends AtlasMasterManipulator {

    QueueStratumManipulator      getQueueStratumManipulator();

}
