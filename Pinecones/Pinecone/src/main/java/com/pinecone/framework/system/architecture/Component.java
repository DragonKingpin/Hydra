package com.pinecone.framework.system.architecture;

import com.pinecone.framework.system.prototype.Pinenut;

public interface Component extends Pinenut {
    String getSimpleName();

    String getFullName();

    ComponentManager getComponentManager();

    void setComponentManager( ComponentManager componentManager );
}
