package com.pinecone.hydra.system;

import com.pinecone.framework.system.Nullable;
import com.pinecone.framework.system.architecture.ArchCascadeComponent;
import com.pinecone.framework.system.architecture.CascadeComponent;
import com.pinecone.framework.util.name.Namespace;

public abstract class ArchSystemCascadeComponent extends ArchCascadeComponent implements HyComponent {
    private Hydrarum   mSystem;

    protected ArchSystemCascadeComponent( Namespace name, Hydrarum system, SystemCascadeComponentManager manager, CascadeComponent parent ) {
        super( name, manager, parent );
        this.mSystem = system;
    }

    protected ArchSystemCascadeComponent( Namespace name, SystemCascadeComponentManager manager, CascadeComponent parent ) {
        this( name, manager.getSystem(), manager, parent );
    }

    protected ArchSystemCascadeComponent( Namespace name, SystemCascadeComponentManager manager ) {
        this( name, manager, null );
    }

    @Override
    public SystemCascadeComponentManager getComponentManager() {
        return (SystemCascadeComponentManager) super.getComponentManager();
    }

    @Override
    public Hydrarum getSystem() {
        return this.mSystem;
    }
}
