package com.pinecone.framework.system.architecture;

import com.pinecone.framework.unit.LinkedTreeMap;

import java.util.Collection;
import java.util.Map;

public abstract class ArchComponentManager implements ComponentManager {
    private Map<String, Component > mRootComponents;

    protected ArchComponentManager( Map<String, Component > components ) {
        this.mRootComponents = components;
    }

    protected ArchComponentManager() {
        this( new LinkedTreeMap<>() );
    }

    protected Map<String, Component > rootComponents() {
        return this.mRootComponents;
    }

    @Override
    public void addComponent ( Component component ) {
        this.mRootComponents.put( component.getFullName(), component );
    }

    @Override
    public void removeComponent ( String fullName ){
        this.mRootComponents.remove( fullName );
    }

    @Override
    public Component getComponentByFullName ( String fullName ) {
        return this.mRootComponents.get( fullName );
    }

    @Override
    public int componentSize() {
        return this.mRootComponents.size();
    }

    @Override
    public Collection<Component > getComponents() {
        return this.rootComponents().values();
    }
}
