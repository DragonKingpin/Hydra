package com.pinecone.framework.system.architecture;

import java.util.Collection;

public interface CascadeComponentManager extends ComponentManager {
    int componentScopeSize() ;

    Component getRootComponentByFullName ( String fullName );

    Collection<Component > getComponentsRegisterList();
}
