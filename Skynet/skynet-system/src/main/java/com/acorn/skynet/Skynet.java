package com.acorn.skynet;

import com.acorn.skynet.system.SkynetSubsystem;
import com.pinecone.framework.util.config.PatriarchalConfig;
import com.pinecone.framework.util.io.Tracer;
import com.pinecone.hydra.system.ArchModularizedSubsystem;
import com.pinecone.hydra.system.Hydrogen;
import com.pinecone.hydra.system.component.LogStatuses;

public class Skynet extends ArchModularizedSubsystem implements SkynetSubsystem {

    public Skynet( Hydrogen primarySystem, String name, PatriarchalConfig config ) {
        super( primarySystem, name, config );
    }

    @Override
    protected void traceWelcomeInfo() {
        Tracer console = this.mPrimarySystem.console();
        console.getOut().print( "---------------------------------------------------------------\n" );
        console.getOut().print( "\u001B[31mBean Nuts Acorn Skynet\u001B[0m\n" );
        console.getOut().print( "\u001B[31mSkynet cloud computing infrastructure \u001B[0m\n" );
        console.getOut().print( "\u001B[32mCopyright(C) 2008-2028 Bean Nuts Foundation. All rights reserved.\u001B[0m\n" );
        console.getOut().print( "---------------------------------------------------------------\n" );
    }

    protected void init() {
        this.getLogger().info( "<Skynet> >>> System Booting..." );

        this.infoLifecycle( "<Skynet> Domain Subsystem Initialization", LogStatuses.StatusStart );
        this.traceWelcomeInfo();
        this.prepare_system_skeleton();

        this.infoLifecycle( "<Skynet> Welcome to the Skynet cloud computing!", LogStatuses.StatusReady );
        this.infoLifecycle( "<Skynet> Domain Subsystem Initialization", LogStatuses.StatusReady );
    }

    protected void prepare_system_skeleton() {

    }

    @Override
    public void vitalize() {
        this.init();
    }

    @Override
    public void terminate() {

    }
}
