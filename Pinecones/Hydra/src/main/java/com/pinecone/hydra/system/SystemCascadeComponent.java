package com.pinecone.hydra.system;

import com.pinecone.framework.system.architecture.CascadeComponent;
import com.pinecone.framework.system.architecture.SystemComponent;

public interface SystemCascadeComponent extends CascadeComponent, SystemComponent {
    @Override
    SystemCascadeComponentManager getComponentManager();

    default Hydrarum getSystem() {
        return this.getComponentManager().getSystem();
    }
}
