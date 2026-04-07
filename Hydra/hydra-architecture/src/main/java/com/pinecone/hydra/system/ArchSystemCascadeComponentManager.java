package com.pinecone.hydra.system;

import com.pinecone.framework.system.architecture.ArchCascadeComponentManager;

public abstract class ArchSystemCascadeComponentManager extends ArchCascadeComponentManager implements SystemCascadeComponentManager {
    protected Hydrogen mSystem;

    protected ArchSystemCascadeComponentManager( Hydrogen system ){
        super();
        this.mSystem = system;
    }

    @Override
    public Hydrogen getSystem() {
        return this.mSystem;
    }

    @Override
    public SystemCascadeComponent getRootComponentByFullName(String fullName) {
        return (SystemCascadeComponent)super.getRootComponentByFullName(fullName);
    }

    @Override
    public SystemCascadeComponent getComponentByFullName(String fullName) {
        return (SystemCascadeComponent)super.getComponentByFullName(fullName);
    }
}
