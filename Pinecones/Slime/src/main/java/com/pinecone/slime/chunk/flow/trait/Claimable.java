package com.pinecone.slime.chunk.flow.trait;

import com.pinecone.framework.system.prototype.Pinenut;

public interface Claimable extends Pinenut {
    boolean claim();
}
