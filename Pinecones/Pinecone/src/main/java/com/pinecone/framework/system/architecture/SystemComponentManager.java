package com.pinecone.framework.system.architecture;

import com.pinecone.framework.system.architecture.ComponentManager;
import com.pinecone.framework.system.architecture.SystemComponent;

public interface SystemComponentManager extends ComponentManager {
    @Override
    SystemComponent getComponentByFullName( String fullName );
}
