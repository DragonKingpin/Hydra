package com.sauron.radium.system;

import com.pinecone.framework.system.construction.InstanceDispenser;
import com.pinecone.framework.system.construction.UnifyCentralInstanceDispenser;
import com.pinecone.framework.util.name.Namespace;
import com.pinecone.hydra.system.ArchSystemCascadeComponent;
import com.pinecone.hydra.system.HyComponent;
import com.pinecone.hydra.system.Hydrarum;

public class ResourceDispenserCenter extends ArchSystemCascadeComponent implements Saunut, HyComponent {
    protected InstanceDispenser mInstanceDispenser;

    public ResourceDispenserCenter( Namespace name, Hydrarum system, HyComponent parent ) {
        super( name, system, system.getComponentManager(), parent );

        this.mInstanceDispenser = new UnifyCentralInstanceDispenser();
    }

    public ResourceDispenserCenter( Hydrarum system, HyComponent parent ) {
        this( null, system, parent );
    }

    public ResourceDispenserCenter( Hydrarum system ) {
        this( system, null );
    }

    public InstanceDispenser getInstanceDispenser() {
        return this.mInstanceDispenser;
    }

    @Override
    public RadiumSystem getSystem() {
        return ( RadiumSystem ) super.getSystem();
    }
}