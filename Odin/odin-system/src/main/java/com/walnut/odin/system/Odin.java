package com.walnut.odin.system;

import com.pinecone.framework.util.config.PatriarchalConfig;
import com.pinecone.framework.util.io.Tracer;
import com.pinecone.hydra.system.ArchModularizedSubsystem;
import com.pinecone.hydra.system.Hydrogen;
import com.pinecone.hydra.system.component.LogStatuses;

public class Odin extends ArchModularizedSubsystem implements TaskCentralControl {

    public Odin( Hydrogen primarySystem, String name, PatriarchalConfig config ) {
        super( primarySystem, name, config );
    }

    @Override
    protected void traceWelcomeInfo() {
        Tracer console = this.mPrimarySystem.console();
        console.getOut().print( "---------------------------------------------------------------\n" );
        console.getOut().print( "\u001B[31mBean Nuts Acorn Odin\u001B[0m\n" );
        console.getOut().print( "\u001B[31mMassive Task Orchestration System \u001B[0m\n" );
        console.getOut().print( "\u001B[32mCopyright(C) 2008-2028 Bean Nuts Foundation. All rights reserved.\u001B[0m\n" );
        console.getOut().print( "---------------------------------------------------------------\n" );
    }

    protected void init() {
        this.getLogger().info( "<Odin> >>> System Booting..." );

        this.infoLifecycle( "<Odin> Domain Subsystem Initialization", LogStatuses.StatusStart );
        this.traceWelcomeInfo();
        this.prepare_system_skeleton();

        this.infoLifecycle( "<Odin> Welcome to the Odin task central control!", LogStatuses.StatusReady );
        this.infoLifecycle( "<Odin> Domain Subsystem Initialization", LogStatuses.StatusReady );
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