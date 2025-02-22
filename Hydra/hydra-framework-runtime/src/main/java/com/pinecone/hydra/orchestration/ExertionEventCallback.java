package com.pinecone.hydra.orchestration;

import com.pinecone.framework.system.prototype.Pinenut;

public interface ExertionEventCallback extends Pinenut {
    void callback( Exertion exertion );
}
