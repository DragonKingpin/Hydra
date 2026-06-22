package com.walnut.odin.formation;

import com.pinecone.framework.system.prototype.Pinenut;

public interface FormationStrategyRuntime extends Pinenut {
    long pageSize();

    long frameSize();

    long windowSize();

    long inflightLimit();

    long inflightCount();

    long productsSum();

    long producedCount();

    long consumedCount();

    boolean isFinished();
}
