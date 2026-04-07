package com.pinecone.framework.system;

import com.pinecone.framework.util.config.PatriarchalConfig;

public interface ModularizedSubsystem extends Subsystem {

    RuntimeSystem parentSystem();

    void vitalize();

    void terminate();

    void release();

    PatriarchalConfig getSubsystemConfig();

}
