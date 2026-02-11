package com.walnut.sparta.ucdn.console.infrastructure;

import com.pinecone.framework.system.executum.Processum;
import com.pinecone.framework.system.functions.Executor;
import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.config.JSONConfig;
import com.pinecone.hydra.bucket.ibatis.hydranium.BucketMappingDriver;
import com.pinecone.hydra.file.ibatis.hydranium.FileMappingDriver;
import com.pinecone.hydra.servgram.Servgram;
import com.pinecone.hydra.service.ibatis.hydranium.ServiceMappingDriver;
import com.pinecone.hydra.service.kom.ServiceInstrument;
import com.pinecone.hydra.service.kom.UniformServiceInstrument;
import com.pinecone.hydra.service.registry.server.UniformServiceManager;
import com.pinecone.hydra.service.registry.ulf.HuskyServiceAppointServer;
import com.pinecone.hydra.storage.bucket.TitanBucketInstrument;
import com.pinecone.hydra.storage.file.FileSystemConfig;
import com.pinecone.hydra.storage.file.KOMFileSystem;
import com.pinecone.hydra.storage.file.KernelFileSystemConfig;
import com.pinecone.hydra.storage.file.UniformObjectFileSystem;
import com.pinecone.hydra.storage.version.TitanVersionManage;
import com.pinecone.hydra.storage.version.VersionManage;
import com.pinecone.hydra.storage.volume.KernelVolumeConfig;
import com.pinecone.hydra.storage.volume.UniformVolumeManager;
import com.pinecone.hydra.storage.volume.VolumeConfig;
import com.pinecone.hydra.system.component.ComponentInitializationException;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.uma.DuplexAppointClient;
import com.pinecone.hydra.version.ibatis.hydranium.VersionMappingDriver;
import com.pinecone.hydra.volume.ibatis.hydranium.VolumeMappingDriver;
import com.pinecone.tritium.Tritium;
import com.pinecone.slime.jelly.source.ibatis.IbatisClient;
import com.pinecone.summer.spring.Springron;
import com.walnut.archcraft.redstone.messge.PrimaryMessageWareStone;
import com.walnut.sparta.ucdn.console.SpartaBoot;
import com.walnut.sparta.ucdn.console.ufm.UCFMConfig;
import com.walnut.sparta.ucdn.console.ufm.UFMConfig;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import java.io.IOException;
import java.nio.file.Path;

public class SpartaUCDNService extends Springron implements UCDNService {
    protected KOIMappingDriver koiMappingDriver;

    protected KOIMappingDriver koiFileMappingDriver;

    protected KOIMappingDriver koiBucketMappingDriver;

    protected KOIMappingDriver koiVersionMappingDriver;

    protected KOIMappingDriver koiServiceMappingDriver;


    protected KOMFileSystem fileSystem;

    protected UniformVolumeManager volumeTree;

    protected TitanBucketInstrument bucketInstrument;

    protected TitanVersionManage versionManage;

    protected ServiceInstrument serviceInstrument;


    protected PrimaryMessageWareStone  primaryMessageWareStone;

    protected UniformServiceManager    serviceManager;

    protected UFMConfig                clusterFileSynchronizationConfig;

    protected void initKOMSubsystem() throws ComponentInitializationException {
        this.koiMappingDriver = new VolumeMappingDriver(
                this, (IbatisClient)this.parentSystem().getMiddlewareDirector().getRDBManager().getRDBClientByName( "MySQLKingHydranium" ), this.parentSystem().getDispenserCenter()
        );
        this.koiFileMappingDriver = new FileMappingDriver(
                this, (IbatisClient)this.parentSystem().getMiddlewareDirector().getRDBManager().getRDBClientByName( "MySQLKingHydranium" ), this.parentSystem().getDispenserCenter()
        );
        this.koiBucketMappingDriver = new BucketMappingDriver(
                this, (IbatisClient)this.parentSystem().getMiddlewareDirector().getRDBManager().getRDBClientByName( "MySQLKingHydranium" ), this.parentSystem().getDispenserCenter()
        );
        this.koiVersionMappingDriver = new VersionMappingDriver(
                this, (IbatisClient)this.parentSystem().getMiddlewareDirector().getRDBManager().getRDBClientByName( "MySQLKingHydranium" ), this.parentSystem().getDispenserCenter()
        );
        this.koiServiceMappingDriver = new ServiceMappingDriver(
                this, (IbatisClient)this.parentSystem().getMiddlewareDirector().getRDBManager().getRDBClientByName( "MySQLKingHydranium" ), this.parentSystem().getDispenserCenter()
        );

        JSONConfig selfConfig = (JSONConfig) this.getConfig();
        FileSystemConfig fileSystemConfig = new KernelFileSystemConfig( selfConfig.queryJSONObject( "service.PrimaryUniformFileSystem" ) );
        this.fileSystem         = new UniformObjectFileSystem( this.koiFileMappingDriver, fileSystemConfig );

        VolumeConfig volumeConfig = new KernelVolumeConfig( selfConfig.queryJSONObject( "service.PrimaryUniformVolumeManager" ) );
        this.volumeTree         = new UniformVolumeManager( this.koiMappingDriver, volumeConfig );
        this.bucketInstrument   = new TitanBucketInstrument( this.koiBucketMappingDriver );
        this.versionManage      = new TitanVersionManage( this.koiVersionMappingDriver );
        this.serviceInstrument = new UniformServiceInstrument( this.koiServiceMappingDriver );
    }

    protected void initMessageWares() throws ComponentInitializationException {
        this.primaryMessageWareStone = new WolfKingMessageWareStone( this );
    }

    protected void initModules() throws ComponentInitializationException {
        this.serviceManager = new UniformServiceManager( this.serviceInstrument );
        this.serviceManager.hookAppointServer( new HuskyServiceAppointServer( this.primaryMessageWareStone.getWolfKingAppointServer() ) );

        JSONConfig selfConfig = (JSONConfig) this.getConfig();
        this.clusterFileSynchronizationConfig = new UCFMConfig( selfConfig.queryJSONObject( "service.ClusterFileSynchronizationConfig" ) );
    }

    protected void startGlobalMiddlewares() throws ComponentInitializationException {
        try {
            this.getPrimaryMessageMiddlewareDirector().getWolfKingAppointServer().execute();
            Debug.sleep( 500 );
            this.getPrimaryMessageMiddlewareDirector().getWolfAppointClient().execute();
        }
        catch ( Exception e ) {
            throw new ComponentInitializationException( e );
        }
    }

    protected void initSpringBeanFactorySubsystem() throws ComponentInitializationException {
        this.setPrimarySources( SpartaBoot.class );
        this.setInitializer(new Executor() {
            @Override
            public void execute() throws Exception {
                SpartaUCDNService.this.getSpringApplication().addInitializers(new ApplicationContextInitializer<ConfigurableApplicationContext>() {
                    @Override
                    public void initialize( ConfigurableApplicationContext applicationContext ) {
                        GenericApplicationContext genericApplicationContext = (GenericApplicationContext) applicationContext;
                        genericApplicationContext.registerBean("primaryFileSystem", UniformObjectFileSystem.class, () -> (UniformObjectFileSystem) fileSystem);
                        genericApplicationContext.registerBean("primaryVolume", UniformVolumeManager.class, () -> (UniformVolumeManager) volumeTree);
                        genericApplicationContext.registerBean("primaryBucket", TitanBucketInstrument.class, () -> (TitanBucketInstrument) bucketInstrument);
                        genericApplicationContext.registerBean("primaryVersion", VersionManage.class, () -> (VersionManage) versionManage);
                        genericApplicationContext.registerBean("primaryService", ServiceInstrument.class, () -> serviceInstrument);
                        genericApplicationContext.registerBean("primaryWolfDuplexAppointClient", DuplexAppointClient.class, () ->  primaryMessageWareStone.getWolfAppointClient());
                        genericApplicationContext.registerBean("uofsContentDelivery", UCDNContentDelivery.class, () -> (UCDNContentDelivery) SpartaUCDNService.this.parentSystem());
                    }
                });
            }
        });
    }

    protected void initSubsystem() throws ComponentInitializationException {
        this.initKOMSubsystem();
        this.initMessageWares();
        this.initModules();
        this.startGlobalMiddlewares();
        this.initSpringBeanFactorySubsystem();
    }

    public SpartaUCDNService( String szName, Processum parent, String[] springbootArgs ) throws ComponentInitializationException {
        super( szName, parent, springbootArgs );
        this.mSpringKernel.setPrimarySources( SpartaBoot.class );

        this.initSubsystem();
    }

    public SpartaUCDNService( String szName, Processum parent ) throws ComponentInitializationException {
        this( szName, parent, new String[0] );
    }

    @Override
    protected void loadConfig() {
        this.mServgramList     = this.getAttachedOrchestrator().getSectionConfig().getChild( Servgram.ConfigServgramsKey );
        Object dyServgramConf  = this.mServgramList.get( this.gramName() );
        if( dyServgramConf instanceof String ) {
            try{
                this.mServgramConf = this.mServgramList.getChildFromPath( Path.of((String) dyServgramConf) );
            }
            catch ( IOException ignore ) {
                this.getLogger().info( "[Notice] Spring will use the default config `application.yaml`." );
            }
        }
        else {
            this.mServgramConf = this.mServgramList.getChild( this.gramName() );
        }
    }

    @Override
    public Tritium parentSystem() {
        return (Tritium)super.parentSystem();
    }

    @Override
    public KOMFileSystem getKOMFileSystem() {
        return this.fileSystem;
    }

    @Override
    public UniformVolumeManager getUniformVolumeManager() {
        return this.volumeTree;
    }

    @Override
    public TitanBucketInstrument getTitanBucketInstrument() {
        return this.bucketInstrument;
    }

    @Override
    public TitanVersionManage getTitanVersionManage() {
        return this.versionManage;
    }

    @Override
    public ServiceInstrument getServiceInstrument() {
        return this.serviceInstrument;
    }

    @Override
    public PrimaryMessageWareStone getPrimaryMessageMiddlewareDirector() {
        return this.primaryMessageWareStone;
    }

    @Override
    public UniformServiceManager getUniformServiceManager() {
        return this.serviceManager;
    }

    @Override
    public UFMConfig getClusterFileSynchronizationConfig() {
        return this.clusterFileSynchronizationConfig;
    }
}
