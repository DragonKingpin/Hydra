package com.pinecone.hydra.system;

import org.slf4j.Logger;

import com.pinecone.framework.system.ModularizedSubsystem;
import com.pinecone.framework.util.config.PatriarchalConfig;

public abstract class ArchModularizedSubsystem implements ModularizedSubsystem {

    protected Hydrogen             mPrimarySystem;

    protected String               mszName;

    protected Logger               mLogger;
    protected PatriarchalConfig    mSubsystemConfig;

    public ArchModularizedSubsystem( Hydrogen primarySystem, String name, PatriarchalConfig config ) {
        this.mPrimarySystem    = primarySystem;
        this.mszName           = name;
        this.mLogger           = primarySystem.getTracerScope().newLogger( name );
        this.mSubsystemConfig  = config;
    }

    @Override
    public PatriarchalConfig getSubsystemConfig() {
        return this.mSubsystemConfig;
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
