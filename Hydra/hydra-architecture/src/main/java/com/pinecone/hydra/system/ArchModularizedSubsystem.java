package com.pinecone.hydra.system;

import org.slf4j.Logger;

import com.pinecone.framework.system.ModularizedSubsystem;

public abstract class ArchModularizedSubsystem implements ModularizedSubsystem {

    protected Hydrogen mPrimarySystem;

    protected String   mszName;

    protected Logger   mLogger;

    public ArchModularizedSubsystem( Hydrogen primarySystem, String name ) {
        this.mPrimarySystem = primarySystem;
        this.mszName        = name;
        this.mLogger        = primarySystem.getTracerScope().newLogger( name );
    }

    @Override
    public Hydrogen parentSystem() {
        return this.mPrimarySystem;
    }

    @Override
    public String getName() {
        return this.mszName;
    }

    public Logger getLogger() {
        return this.mLogger;
    }

    protected abstract void traceWelcomeInfo() ;

    @Override
    public void release() {

    }

}
