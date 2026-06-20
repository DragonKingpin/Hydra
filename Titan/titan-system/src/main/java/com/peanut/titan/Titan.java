package com.peanut.titan;

import com.peanut.titan.storage.TitanStorageRegiment;
import com.peanut.titan.storage.conduct.TitanCollectiveStorageRegiment;
import com.peanut.titan.system.TitanSubsystem;
import com.pinecone.framework.system.executum.Processum;
import com.pinecone.framework.util.config.PatriarchalConfig;
import com.pinecone.framework.util.io.Tracer;
import com.pinecone.framework.util.json.JSONObject;
import com.pinecone.framework.util.json.homotype.MapStructure;
import com.pinecone.hydra.file.ibatis.hydranium.FileMappingDriver;
import com.pinecone.hydra.lifecycle.ibatis.hydranium.StorageLifecycleMappingDriver;
import com.pinecone.hydra.storage.file.FileSystemConfig;
import com.pinecone.hydra.storage.file.KernelFileSystemConfig;
import com.pinecone.hydra.storage.file.KOMFileSystem;
import com.pinecone.hydra.storage.file.UniformObjectFileSystem;
import com.pinecone.hydra.storage.lifecycle.GenericStorageLifecycleService;
import com.pinecone.hydra.storage.lifecycle.service.StorageLifecycleService;
import com.pinecone.hydra.storage.lifecycle.source.StorageLifecycleMasterManipulator;
import com.pinecone.hydra.storage.volume.KernelVolumeConfig;
import com.pinecone.hydra.storage.volume.UniformVolumeManager;
import com.pinecone.hydra.storage.volume.VolumeConfig;
import com.pinecone.hydra.storage.volume.VolumeManager;
import com.pinecone.hydra.system.ArchModularizedSubsystem;
import com.pinecone.hydra.system.Hydrogen;
import com.pinecone.hydra.system.component.LogStatuses;
import com.pinecone.hydra.volume.ibatis.hydranium.VolumeMappingDriver;
import com.pinecone.slime.jelly.source.ibatis.IbatisClient;
import com.pinecone.tritium.system.TritiumSystem;

import java.util.Map;

public class Titan extends ArchModularizedSubsystem implements TitanSubsystem {

    protected TitanStorageRegiment          mStorageRegiment;

    protected VolumeManager                 mVolumeManager;

    protected KOMFileSystem                 mFileSystem;

    protected StorageLifecycleService       mStorageLifecycleService;

    @MapStructure("metaDependent.storageInstrument")
    private String                          mszStorageInstrumentKey;

    @MapStructure("metaDependent.processManager")
    private String                          mszProcessManagerKey;

    @MapStructure("kernelConfig.enableStorageRegiment")
    private Boolean                         mbEnableStorageRegiment;

    @MapStructure("kernelConfig.enableVolumeManager")
    private Boolean                         mbEnableVolumeManager;

    @MapStructure("kernelConfig.enableUofs")
    private Boolean                         mbEnableUofs;

    @MapStructure("kernelConfig.enableJournalRecovery")
    private Boolean                         mbEnableJournalRecovery;

    public Titan( Hydrogen primarySystem, String name, PatriarchalConfig config ) {
        super( primarySystem, name, config );

        TritiumSystem sys = (TritiumSystem) this.parentSystem();
        sys.getPrimaryConfigScope().autoInject( Titan.class, config, this );
    }

    @Override
    protected void traceWelcomeInfo() {
        Tracer console = this.mPrimarySystem.console();
        console.getOut().print( "---------------------------------------------------------------\n" );
        console.getOut().print( "\u001B[31mPeanut Titan Storage Domain\u001B[0m\n" );
        console.getOut().print( "\u001B[31mUniform storage orchestration over Hydra kernel\u001B[0m\n" );
        console.getOut().print( "\u001B[32mCopyright(C) 2008-2028 Bean Nuts Foundation. All rights reserved.\u001B[0m\n" );
        console.getOut().print( "---------------------------------------------------------------\n" );
    }

    protected void init() {
        this.getLogger().info( "<Titan> >>> System Booting..." );

        this.infoLifecycle( "<Titan> Domain Subsystem Initialization", LogStatuses.StatusStart );
        this.traceWelcomeInfo();
        this.prepare_system_skeleton();

        this.infoLifecycle( "<Titan> Welcome to the Titan storage domain!", LogStatuses.StatusReady );
        this.infoLifecycle( "<Titan> Domain Subsystem Initialization", LogStatuses.StatusReady );
    }

    protected void prepare_system_skeleton() {
        this.infoLifecycle( "<Titan> Preparing system skeleton.", LogStatuses.StatusStart );

        this.prepare_volume_manager();
        this.prepare_uofs();
        this.prepare_storage_lifecycle_service();
        this.prepare_instrumentation();

        this.infoLifecycle( "<Titan> Preparing system skeleton.", LogStatuses.StatusDone );
    }

    protected void prepare_volume_manager() {
        this.infoLifecycle( "<Titan> Constructing component `VolumeManager`.", LogStatuses.StatusStart );

        if ( !this.isEnabled( this.mbEnableVolumeManager ) ) {
            this.infoLifecycle( "<Titan> Component `VolumeManager` disabled by config.", LogStatuses.StatusDone );
            return;
        }
        if ( this.mVolumeManager != null ) {
            this.infoLifecycle( "<Titan> Component `VolumeManager` already prepared.", LogStatuses.StatusDone );
            return;
        }

        VolumeMappingDriver driver = new VolumeMappingDriver(
                this.superiorProcess(),
                this.storageIbatisClient(),
                this.tritiumSystem().getDispenserCenter()
        );
        VolumeConfig volumeConfig = new KernelVolumeConfig( this.configMap( "storage", "volume" ) );
        this.mVolumeManager = new UniformVolumeManager( driver, volumeConfig );

        this.infoLifecycle( "<Titan> Constructing component `VolumeManager`.", LogStatuses.StatusDone );
    }

    protected void prepare_uofs() {
        this.infoLifecycle( "<Titan> Constructing component `UOFS`.", LogStatuses.StatusStart );

        if ( !this.isEnabled( this.mbEnableUofs ) ) {
            this.infoLifecycle( "<Titan> Component `UOFS` disabled by config.", LogStatuses.StatusDone );
            return;
        }
        if ( this.mFileSystem != null ) {
            this.infoLifecycle( "<Titan> Component `UOFS` already prepared.", LogStatuses.StatusDone );
            return;
        }

        FileMappingDriver driver = new FileMappingDriver(
                this.superiorProcess(),
                this.storageIbatisClient(),
                this.tritiumSystem().getDispenserCenter()
        );
        FileSystemConfig fileSystemConfig = new KernelFileSystemConfig( this.configMap( "storage", "uofs" ) );
        this.mFileSystem = new UniformObjectFileSystem( driver, fileSystemConfig );

        this.infoLifecycle( "<Titan> Constructing component `UOFS`.", LogStatuses.StatusDone );
    }

    protected void prepare_storage_lifecycle_service() {
        this.infoLifecycle( "<Titan> Constructing component `StorageLifecycleService`.", LogStatuses.StatusStart );

        if ( this.mStorageLifecycleService != null ) {
            this.infoLifecycle( "<Titan> Component `StorageLifecycleService` already prepared.", LogStatuses.StatusDone );
            return;
        }
        if ( this.mVolumeManager == null || this.mFileSystem == null ) {
            this.infoLifecycle( "<Titan> Component `StorageLifecycleService` skipped because storage kernel is incomplete.", LogStatuses.StatusDone );
            return;
        }

        StorageLifecycleMappingDriver driver = new StorageLifecycleMappingDriver(
                this.superiorProcess(),
                this.storageIbatisClient(),
                this.tritiumSystem().getDispenserCenter()
        );
        StorageLifecycleMasterManipulator masterManipulator =
                (StorageLifecycleMasterManipulator) driver.getMasterManipulator();
        this.mStorageLifecycleService = new GenericStorageLifecycleService(
                this.mFileSystem,
                this.mVolumeManager,
                masterManipulator.getTaskManipulator()
        );

        this.infoLifecycle( "<Titan> Constructing component `StorageLifecycleService`.", LogStatuses.StatusDone );
    }


    protected void prepare_instrumentation() {
        this.infoLifecycle( "<Titan> Constructing components `Instrumentation`.", LogStatuses.StatusStart );

        if ( !this.isEnabled( this.mbEnableStorageRegiment ) ) {
            this.infoLifecycle( "<Titan> Component `StorageRegiment` disabled by config.", LogStatuses.StatusDone );
            return;
        }

        this.mStorageRegiment = new TitanCollectiveStorageRegiment( this.mVolumeManager, this.mFileSystem );
        this.infoLifecycle( "<Titan> Constructing component `StorageRegiment`.", LogStatuses.StatusDone );

        this.infoLifecycle( "<Titan> Constructing components `Instrumentation`.", LogStatuses.StatusDone );
    }

    @Override
    public void vitalize() {
        this.init();
    }

    @Override
    public void terminate() {

    }

    @Override
    public TitanStorageRegiment storageRegiment() {
        return this.mStorageRegiment;
    }

    @Override
    public VolumeManager volumeManager() {
        return this.mVolumeManager;
    }

    @Override
    public KOMFileSystem fileSystem() {
        return this.mFileSystem;
    }

    @Override
    public StorageLifecycleService storageLifecycleService() {
        return this.mStorageLifecycleService;
    }

    protected TritiumSystem tritiumSystem() {
        return (TritiumSystem) this.parentSystem();
    }

    protected Processum superiorProcess() {
        return (Processum) this.parentSystem();
    }

    protected IbatisClient storageIbatisClient() {
        String storageInstrumentKey = this.mszStorageInstrumentKey == null || this.mszStorageInstrumentKey.isBlank()
                ? "MySQLKingHydranium"
                : this.mszStorageInstrumentKey;
        return (IbatisClient) this.tritiumSystem()
                .getMiddlewareDirector()
                .getRDBManager()
                .getRDBClientByName( storageInstrumentKey );
    }

    protected Map<String, Object> configMap( String... paths ) {
        PatriarchalConfig config = this.mSubsystemConfig;
        for ( String path : paths ) {
            if ( config == null ) {
                return Map.of();
            }
            config = config.getChild( path );
        }
        if ( config instanceof JSONObject ) {
            return ( (JSONObject) config ).getMap();
        }
        return Map.of();
    }

    protected boolean isEnabled( Boolean value ) {
        return value == null || value;
    }
}
