package com.pinecone.hydra.system;

import com.pinecone.framework.system.prototype.Pinenut;

public interface HyHierarchy extends Pinenut {
    String getName();

    boolean isDominantClass();

    boolean isWorkerClass();
}
