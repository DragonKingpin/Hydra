package com.pinecone.framework.system.architecture;

public interface SystemComponentManager extends ComponentManager {
    @Override
    SystemComponent getComponentByFullName( String fullName );
}
