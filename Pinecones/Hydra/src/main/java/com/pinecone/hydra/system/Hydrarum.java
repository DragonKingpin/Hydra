package com.pinecone.hydra.system;

import com.pinecone.framework.system.Pinecore;
import com.pinecone.hydra.system.component.Log4jTracerScope;

import java.nio.file.Path;

public interface Hydrarum extends Pinecore, ScopedSystem, MultiComponentSystem {

    @Override
    HySkeleton getComponentManager();

    String getServiceID();

    Path getWorkingPath();

    boolean isDebugMode();

    Log4jTracerScope getTracerScope();

}
