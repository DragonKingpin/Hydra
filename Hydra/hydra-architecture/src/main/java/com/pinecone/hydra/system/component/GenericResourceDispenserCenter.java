package com.pinecone.hydra.system.component;

import com.pinecone.framework.system.construction.StructureInstanceDispenser;
import com.pinecone.framework.system.construction.UnifyCentralInstanceDispenser;
import com.pinecone.framework.util.name.Namespace;
import com.pinecone.hydra.system.ArchSystemCascadeComponent;
import com.pinecone.hydra.system.HyComponent;
import com.pinecone.hydra.system.Hydrogen;

public class GenericResourceDispenserCenter extends ArchSystemCascadeComponent implements ResourceDispenserCenter {
    protected StructureInstanceDispenser mInstanceDispenser;

    public GenericResourceDispenserCenter(Namespace name, Hydrogen system, HyComponent parent ) {
        super( name, system, system.getComponentManager(), parent );

        this.mInstanceDispenser = new UnifyCentralInstanceDispenser();
    }

    public GenericResourceDispenserCenter(Hydrogen system, HyComponent parent ) {
        this( null, system, parent );
    }

    public GenericResourceDispenserCenter( Hydrogen system ) {
        this( system, null );
    }

    @Override
    public StructureInstanceDispenser getInstanceDispenser() {
        return this.mInstanceDispenser;
    }

    @Override
    public Hydrogen getSystem() {
        return super.getSystem();
    }
}