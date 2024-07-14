package com.pinecone.hydra.system;

import com.pinecone.framework.system.architecture.SystemComponentManager;
import com.pinecone.framework.system.prototype.Pinenut;

public interface MultiComponentSystem extends Pinenut {
    SystemComponentManager getComponentManager();

    MultiComponentSystem apply( SystemComponentManager manager );
}