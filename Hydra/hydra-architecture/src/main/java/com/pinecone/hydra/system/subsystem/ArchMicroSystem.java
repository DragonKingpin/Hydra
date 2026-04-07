package com.pinecone.hydra.system.subsystem;

import com.pinecone.framework.util.config.PatriarchalConfig;
import com.pinecone.hydra.system.Hydrogen;

public abstract class ArchMicroSystem implements MicroSystem {
    protected String            mszName;
    protected Hydrogen          mSystem;

    protected PatriarchalConfig mSubsystemConfig;

    public ArchMicroSystem( String name, Hydrogen system, PatriarchalConfig config ) {
        this.mszName           = name;
        this.mSystem           = system;
        this.mSubsystemConfig  = config;
    }

    @Override
    public String getName() {
        return this.mszName;
    }

    @Override
    public Hydrogen getMasterSystem(){
        return this.mSystem;
    }

    @Override
    public PatriarchalConfig getSubsystemConfig() {
        return this.mSubsystemConfig;
    }

    protected abstract void traceWelcomeInfo();
}
