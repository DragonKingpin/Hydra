package com.pinecone.slime.chunk.orchestration;

import com.pinecone.slime.cluster.SequentialChunkGroup;
import com.pinecone.framework.system.prototype.Pinenut;

public interface PartitionDividerStrategy extends Pinenut {
    SequentialChunkGroup assignment(SequentialChunkGroup group );
}
