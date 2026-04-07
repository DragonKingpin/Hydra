package com.pinecone.hydra.system;

import com.pinecone.framework.system.architecture.ArchCascadeComponent;
import com.pinecone.framework.system.architecture.CascadeComponent;
import com.pinecone.framework.util.name.Namespace;

public abstract class ArchSystemCascadeComponent extends ArchCascadeComponent implements HyComponent {
    private Hydrogen mSystem;

    protected ArchSystemCascadeComponent( Namespace name, Hydrogen system, SystemCascadeComponentManager manager, CascadeComponent parent ) {
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
    public Hydrogen getSystem() {
        return this.mSystem;
    }
}
