package com.pinecone.hydra.umc.io;

import com.pinecone.framework.system.prototype.Strategy;

public interface IOLoadBalanceStrategy extends Strategy {
    boolean readPriorityMatched();

    boolean writePriorityMatched();
}
