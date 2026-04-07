package com.pinecone.hydra.system.subsystem;

import com.pinecone.framework.system.RuntimeSystem;
import com.pinecone.framework.system.executum.Systema;
import com.pinecone.framework.util.config.PatriarchalConfig;

public interface MicroSystem extends Systema {
    void release();

    RuntimeSystem getMasterSystem();

    PatriarchalConfig getSubsystemConfig();
}
