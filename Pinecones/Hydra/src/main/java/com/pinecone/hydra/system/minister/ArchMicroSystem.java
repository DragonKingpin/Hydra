package com.pinecone.hydra.system.minister;

import com.pinecone.Pinecone;
import com.pinecone.hydra.system.Hydrarum;
import com.pinecone.hydra.system.component.LogStatuses;

public abstract class ArchMicroSystem implements MicroSystem {
    protected String      mszName;
    protected Hydrarum    mSystem;

    public ArchMicroSystem( String name, Hydrarum system ) {
        this.mszName = name;
        this.mSystem = system;
    }

    @Override
    public String getName() {
        return this.mszName;
    }

    @Override
    public Hydrarum getMasterSystem(){
        return this.mSystem;
    }

    protected abstract void traceWelcomeInfo();
}
