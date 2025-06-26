package com.acorn.redqueen;

import com.acorn.redqueen.system.RedQueenSubsystem;
import com.pinecone.framework.util.io.Tracer;
import com.pinecone.hydra.system.ArchModularizedSubsystem;
import com.pinecone.hydra.system.Hydrogen;
import com.pinecone.hydra.system.component.LogStatuses;

public class RedQueen extends ArchModularizedSubsystem implements RedQueenSubsystem {

    public RedQueen( Hydrogen primarySystem, String name ) {
        super( primarySystem, name );
    }

    @Override
    protected void traceWelcomeInfo() {
        Tracer console = this.mPrimarySystem.console();
        console.getOut().print( "---------------------------------------------------------------\n" );
        console.getOut().print( "\u001B[31mBean Nuts Acorn Red Queen\u001B[0m\n" );
        console.getOut().print( "\u001B[31mMassive Parallel Computing Orchestration System \u001B[0m\n" );
        console.getOut().print( "\u001B[32mCopyright(C) 2008-2028 Bean Nuts Foundation. All rights reserved.\u001B[0m\n" );
        console.getOut().print( "---------------------------------------------------------------\n" );
    }

    protected void init() {
        this.getLogger().info( "<RedQueen> >>> System Booting..." );

        this.infoLifecycle( "<RedQueen> Domain Subsystem Initialization", LogStatuses.StatusStart );
        this.traceWelcomeInfo();
        this.prepare_system_skeleton();

        this.infoLifecycle( "<RedQueen> Welcome to the Red Queen super computing!", LogStatuses.StatusReady );
        this.infoLifecycle( "<RedQueen> Domain Subsystem Initialization", LogStatuses.StatusReady );
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
