package com.walnut.archcraft.ender;

import com.pinecone.framework.system.CascadeSystem;
import com.pinecone.framework.system.architecture.Component;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.proc.ProcessManager;
import com.pinecone.hydra.proc.UProcess;
import com.pinecone.hydra.proc.UniformProcessManager;
import com.pinecone.hydra.proc.image.ImageLoader;
import com.pinecone.hydra.proc.image.UniformImageLoader;
import com.pinecone.hydra.system.component.LogStatuses;

import com.pinecone.tritium.Tritium;
import com.pinecone.ulf.util.guid.GUIDs;
import com.walnut.archcraft.ender.system.HydraEmpire;
import com.walnut.archcraft.ender.system.Hydroxy;

public class EnderHydra extends Tritium implements HydraEmpire {

    protected GuidAllocator  mSystemGuidAllocator;
    protected ImageLoader    mSystemImageLoader;
    protected ProcessManager mSystemProcessManager;
    protected UProcess       mProxiedRootSystemProcess;

    public EnderHydra( String[] args, CascadeSystem parent ) {
        this( args, null, parent );
    }

    public EnderHydra( String[] args, String szName, CascadeSystem parent ){
        super( args, szName, parent );
    }

    @Override
    protected void prepare_system_skeleton() {
        super.prepare_system_skeleton();
        this.prepare_uniform_system();
    }

    protected void prepare_uniform_system() {
        this.infoLifecycle( "<Hydra Empire> Uniform Operation System", LogStatuses.StatusStart );

        this.mSystemGuidAllocator  = GUIDs.newGuidAllocator();
        this.infoLifecycle(
                "<Uniform Hydra> System GUIDAllocator Initialization [Type: `" + this.mSystemGuidAllocator.getClass().getName() + "`]",
                LogStatuses.StatusDone
        );

        this.mSystemProcessManager = new UniformProcessManager(
                this, null, "UniformProcessManager", "", null
        );
        this.infoLifecycle( "<Uniform Hydra> System ProcessManager Initialization", LogStatuses.StatusDone );

        this.mSystemImageLoader        = new UniformImageLoader( this );
        this.mProxiedRootSystemProcess = new Hydroxy( this );
        this.mSystemProcessManager.applyRootUProcess( this.mProxiedRootSystemProcess );
        this.mSystemProcessManager.register( this.mProxiedRootSystemProcess );

        this.init_process_kernel_subsystem();

        this.infoLifecycle( "<Hydra Empire> Uniform Operation System", LogStatuses.StatusReady );
    }

    protected void init_process_kernel_subsystem() {
        this.infoLifecycle( "Uniform Process Subsystem", LogStatuses.StatusStart );

        this.infoLifecycle( "Uniform Process Subsystem", LogStatuses.StatusDone );
    }

    @Override
    protected void traceWelcomeInfo() {
        this.pout().print( "---------------------------------------------------------------\n" );
        this.pout().print( "\u001B[31mBean Nuts Pinecone Ursus for Java\u001B[0m\n" );
        this.pout().print( "\u001B[31mHydra Kingdom Framework (Ender Hydra) \u001B[0m\n" );
        this.pout().print( "\u001B[32mCopyright(C) 2008-2028 Bean Nuts Foundation. All rights reserved.\u001B[0m\n" );
        this.pout().print( "---------------------------------------------------------------\n" );
        this.pout().print( "\u001B[31mDragon King\u001B[0m\n" );
        this.pout().print( "\u001B[32mWebsit: https://www.dragonking.cn/ \u001B[0m\n" );

        this.traceSubsystemWelcomeInfo();
        this.prepare_system_log4j_logger();
        this.infoLifecycle( "Initialization", LogStatuses.StatusStart );
    }

    @Override
    public GuidAllocator getSystemGuidAllocator() {
        return this.mSystemGuidAllocator;
    }

    @Override
    public ProcessManager processManager() {
        return this.mSystemProcessManager;
    }

    @Override
    public ImageLoader imageLoader() {
        return this.mSystemImageLoader;
    }

}
