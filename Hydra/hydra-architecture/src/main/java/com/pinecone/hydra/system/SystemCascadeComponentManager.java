package com.pinecone.hydra.system;

import com.pinecone.framework.system.architecture.CascadeComponentManager;
import com.pinecone.framework.system.architecture.SystemComponentManager;

public interface SystemCascadeComponentManager extends SystemComponentManager, CascadeComponentManager {
    Hydrarum getSystem();
}
