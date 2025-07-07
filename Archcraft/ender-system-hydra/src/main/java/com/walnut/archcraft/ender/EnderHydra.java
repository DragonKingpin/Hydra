package com.walnut.archcraft.ender;

import java.util.HashMap;
import java.util.Map;

import com.acorn.redqueen.RedQueen;
import com.acorn.redqueen.system.ServiceCentralControl;
import com.acorn.skynet.Skynet;
import com.acorn.skynet.system.SkynetSubsystem;
import com.pinecone.framework.system.CascadeSystem;
import com.pinecone.framework.system.executum.Processum;
import com.pinecone.framework.system.regime.arch.Lord;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.framework.util.name.UniNamespace;
import com.pinecone.hydra.proc.ProcessManager;
import com.pinecone.hydra.proc.UProcess;
import com.pinecone.hydra.proc.UniformProcessManager;
import com.pinecone.hydra.proc.image.FileSystemMappingImageLoader;
import com.pinecone.hydra.proc.image.ImageLoader;
import com.pinecone.hydra.proc.image.UniformMultiScopeImageLoader;
import com.pinecone.hydra.proc.image.kom.VirtualExeImageInstrument;
import com.pinecone.hydra.proc.image.kom.VirtualMappingExeImageInstrument;
import com.pinecone.hydra.reign.UnixInstitutionalizedMetaImperiumPrivy;
import com.pinecone.hydra.system.component.LogStatuses;

import com.pinecone.hydra.system.imperium.ImperiumPrivy;
import com.pinecone.hydra.system.imperium.KernelObjectRootMountPoint;
import com.pinecone.hydra.system.ko.KernelObjectConfig;
import com.pinecone.hydra.system.ko.runtime.GenericRuntimeInstrumentConfig;
import com.pinecone.tritium.Tritium;
import com.pinecone.ulf.util.guid.GUIDs;
import com.pinecone.ulf.util.guid.i64.GuidAllocator72;
import com.pinecone.ulf.util.guid.i64.GuidAllocator72V2;
import com.walnut.archcraft.ender.system.HydraEmpire;
import com.walnut.archcraft.ender.system.Hydroxy;

public class EnderHydra extends Tritium implements HydraEmpire {

    protected GuidAllocator             mSystemGuidAllocator;
    protected GuidAllocator72           mSystemGuidAllocator72;
    protected ImageLoader               mSystemImageLoader;
    protected ProcessManager            mSystemProcessManager;
    protected UProcess                  mProxiedRootSystemProcess;
    protected KernelObjectConfig        mFundamentalKernelObjectConfig;
    protected VirtualExeImageInstrument mVirtualExeImageInstrument;
    protected ImperiumPrivy             mImperiumPrivy;


    protected Map<String, Lord>        mEmpireLords;   // Domain subsystem.
    protected SkynetSubsystem          mSkynetSubsystem;
    protected ServiceCentralControl mServiceCentralControl;


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

    protected void prepare_uniform_system_process_task_subsystem() {
        this.mVirtualExeImageInstrument = new VirtualMappingExeImageInstrument( this, "" );
        this.infoLifecycle( "<Uniform Hydra> ProcessSubsystem[1] System VirtualExeImageInstrument Initialization", LogStatuses.StatusDone );


        ImageLoader localMappingImageLoader = new FileSystemMappingImageLoader( this, this.mVirtualExeImageInstrument );
        this.infoLifecycle( "<Uniform Hydra> ProcessSubsystem[2] System Scope LocalMappingImageLoader Initialization", LogStatuses.StatusDone );
        this.mSystemImageLoader         = new UniformMultiScopeImageLoader( this, localMappingImageLoader );
        this.infoLifecycle( "<Uniform Hydra> ProcessSubsystem[3] System Scope UniformMultiScopeImageLoader Initialization", LogStatuses.StatusDone );


        this.mSystemProcessManager = new UniformProcessManager(
                this, null, "SystemUniformProcessManager", "", null
        );
        this.infoLifecycle( "<Uniform Hydra> ProcessSubsystem[4] System ProcessManager Initialization", LogStatuses.StatusDone );


        this.mProxiedRootSystemProcess  = new Hydroxy( this );
        this.mSystemProcessManager.applyRootUProcess( this.mProxiedRootSystemProcess );
        this.mSystemProcessManager.register( this.mProxiedRootSystemProcess );
        this.infoLifecycle( "<Uniform Hydra> ProcessSubsystem[5] System Hydroxy Initialization", LogStatuses.StatusDone );

        this.infoLifecycle( "<Uniform Hydra> Uniform System Process/Task Subsystem", LogStatuses.StatusDone );
    }

    protected void prepare_uniform_system_imperium_privy() {
        this.mImperiumPrivy = new UnixInstitutionalizedMetaImperiumPrivy( new UniNamespace( "SystemUnixInstitutionalizedMetaImperiumPrivy" ), this, null, this.fundamentalKernelObjectConfig() );
        this.infoLifecycle(
                "<Uniform Hydra> System ImperiumPrivy Initialization. (name: `" + this.mImperiumPrivy.getTargetingName() + "`, class: `" + this.mImperiumPrivy.getClass().getName() + "`)",
                LogStatuses.StatusDone
        );
        this.mImperiumPrivy.getExpressInstrument().mount( KernelObjectRootMountPoint.SysImages.getMountPoint(), this.mVirtualExeImageInstrument );
        this.infoLifecycle(
                "<Uniform Hydra::Privy> System VirtualExeImageInstrument Mount. (MountPoint: `/" + KernelObjectRootMountPoint.SysImages.getMountPoint() + "`)",
                LogStatuses.StatusDone
        );


        this.infoLifecycle( "<Uniform Hydra> Uniform Imperium Privy", LogStatuses.StatusDone );
    }

    protected void prepare_uniform_system() {
        this.infoLifecycle( "<Hydra Empire> Uniform Operation System", LogStatuses.StatusStart );

        this.init_uniform_system_configuration();

        this.mSystemGuidAllocator    = GUIDs.newGuidAllocator( 1984 ); // TODO MachineId allocation.
        this.infoLifecycle(
                "<Uniform Hydra> System GUIDAllocator Initialization [Type: `" + this.mSystemGuidAllocator.getClass().getName() + "`]",
                LogStatuses.StatusDone
        );

        this.mSystemGuidAllocator72  = new GuidAllocator72V2();
        this.infoLifecycle(
                "<Uniform Hydra> System GUIDAllocator72 Initialization [Type: `" + this.mSystemGuidAllocator72.getClass().getName() + "`]",
                LogStatuses.StatusDone
        );


        this.prepare_uniform_system_process_task_subsystem();
        this.init_process_kernel_subsystem();

        this.prepare_modularized_subsystem();

        this.infoLifecycle( "<Hydra Empire> Uniform Operation System", LogStatuses.StatusReady );
        this.getLogger().info( "[Welcome] [<Hydra Empire> Welcome to join the imperial army!]" );
    }

    protected void prepare_modularized_subsystem() {
        this.infoLifecycle( "<Hydra Empire> [SummoningLords] Modularized Subsystem Initialization", LogStatuses.StatusStart );
        this.mEmpireLords = new HashMap<>();


        this.mSkynetSubsystem = new Skynet( this, "KernelSkynetLord" );
        this.mSkynetSubsystem.vitalize();
        this.mEmpireLords.put( this.mSkynetSubsystem.getName(), this.mSkynetSubsystem );

        this.mServiceCentralControl = new RedQueen( this, "KernelRedQueenLord" );
        this.mServiceCentralControl.vitalize();
        this.mEmpireLords.put( this.mServiceCentralControl.getName(), this.mServiceCentralControl);

        this.getLogger().info( "[ActionReport] <Hydra Empire> [SummoningLords] Empire now has {} lords.", this.countEmpireLords() );
        this.infoLifecycle( "<Hydra Empire> [SummoningLords] Modularized Subsystem Initialization", LogStatuses.StatusDone );
    }

    protected void init_process_kernel_subsystem() {
        this.infoLifecycle( "Uniform Process Subsystem", LogStatuses.StatusStart );

        this.prepare_uniform_system_imperium_privy();

        this.infoLifecycle( "Uniform Process Subsystem", LogStatuses.StatusDone );
    }

    protected void init_uniform_system_configuration() {
        this.infoLifecycle( "Uniform System Configuration", LogStatuses.StatusStart );

        this.mFundamentalKernelObjectConfig = new GenericRuntimeInstrumentConfig();
        this.infoLifecycle( "<Uniform Hydra> System FundamentalKernelObjectConfig Initialization", LogStatuses.StatusDone );

        this.infoLifecycle( "Uniform System Configuration", LogStatuses.StatusDone );
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
        this.traceSystemBootingInfo();
        this.prepare_system_log4j_logger();
        this.infoLifecycle( "Initialization", LogStatuses.StatusStart );
    }

    @Override
    public GuidAllocator getSystemGuidAllocator() {
        return this.mSystemGuidAllocator;
    }

    @Override
    public GuidAllocator72 getSystemGuidAllocator72() {
        return this.mSystemGuidAllocator72;
    }

    @Override
    public ProcessManager processManager() {
        return this.mSystemProcessManager;
    }

    @Override
    public ImageLoader imageLoader() {
        return this.mSystemImageLoader;
    }

    @Override
    public Processum ownedLocalProcess() {
        return this;
    }

    @Override
    public UProcess ownedUniformProcess() {
        return this.mProxiedRootSystemProcess;
    }

    @Override
    public KernelObjectConfig fundamentalKernelObjectConfig() {
        return this.mFundamentalKernelObjectConfig;
    }

    @Override
    public ImperiumPrivy imperiumPrivy() {
        return this.mImperiumPrivy;
    }

    @Override
    public ServiceCentralControl redQueen() {
        return this.mServiceCentralControl;
    }

    @Override
    public SkynetSubsystem skynet() {
        return this.mSkynetSubsystem;
    }

    @Override
    public Lord getEmpireLordsByName( String lordName ) {
        return this.mEmpireLords.get( lordName );
    }

    @Override
    public int countEmpireLords() {
        return this.mEmpireLords.size();
    }

    @Override
    public VirtualExeImageInstrument virtualExeImageInstrument() {
        return this.mVirtualExeImageInstrument;
    }
}
