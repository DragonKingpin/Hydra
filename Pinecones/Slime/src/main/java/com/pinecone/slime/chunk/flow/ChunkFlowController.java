package com.pinecone.slime.chunk.flow;

import com.pinecone.framework.system.prototype.Pinenut;

public interface ChunkFlowController extends Pinenut {
    void start();

    void suspend();

    void resume();

    void stop();
}
