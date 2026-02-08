package com.pinecone.framework.system;

public interface ModularizedSubsystem extends Subsystem {

    RuntimeSystem parentSystem();

    void vitalize();

    void terminate();

    void release();

}
