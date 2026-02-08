package com.pinecone.hydra.system.subsystem;

import com.pinecone.hydra.system.Hydrogen;

public abstract class ArchMicroSystem implements MicroSystem {
    protected String      mszName;
    protected Hydrogen mSystem;

    public ArchMicroSystem( String name, Hydrogen system ) {
        this.mszName = name;
        this.mSystem = system;
    }

    @Override
    public String getName() {
        return this.mszName;
    }

    @Override
    public Hydrogen getMasterSystem(){
        return this.mSystem;
    }

    protected abstract void traceWelcomeInfo();
}
