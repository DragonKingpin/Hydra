package com.pinecone.hydra.system.minister;

import com.pinecone.framework.system.RuntimeSystem;
import com.pinecone.framework.system.executum.Systema;

public interface MicroSystem extends Systema {
    void release();

    RuntimeSystem getMasterSystem();
}
