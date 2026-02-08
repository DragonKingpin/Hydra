package com.pinecone.hydra.system.subsystem;

import com.pinecone.framework.system.RuntimeSystem;
import com.pinecone.framework.system.executum.Systema;

public interface MicroSystem extends Systema {
    void release();

    RuntimeSystem getMasterSystem();
}
