package com.pinecone.hydra.system;

import com.pinecone.framework.system.architecture.ArchCascadeComponentManager;
import com.pinecone.framework.system.architecture.Component;

public class ArchSystemCascadeComponentManager extends ArchCascadeComponentManager implements SystemCascadeComponentManager {
    protected Hydrarum      mSystem;

    protected ArchSystemCascadeComponentManager( Hydrarum system ){
        super();
        this.mSystem = system;
    }

    @Override
    public Hydrarum getSystem() {
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
