package com.walnut.sparta.ucdn.console.infrastructure;

import com.pinecone.framework.system.executum.Processum;
import com.pinecone.framework.system.functions.Executor;
import com.pinecone.framework.util.json.JSONMaptron;
import com.pinecone.hydra.bucket.ibatis.hydranium.BucketMappingDriver;
import com.pinecone.hydra.file.ibatis.hydranium.FileMappingDriver;
import com.pinecone.hydra.servgram.Servgram;
import com.pinecone.hydra.service.ibatis.hydranium.ServiceMappingDriver;
import com.pinecone.hydra.service.kom.ServicesInstrument;
import com.pinecone.hydra.service.kom.UniformServicesInstrument;
import com.pinecone.hydra.service.registry.ServiceLifecycleIface;
import com.pinecone.hydra.service.registry.ServiceMetaManipulationIface;
import com.pinecone.hydra.service.registry.UniformServiceManager;
import com.pinecone.hydra.storage.bucket.TitanBucketInstrument;
import com.pinecone.hydra.storage.file.KOMFileSystem;
import com.pinecone.hydra.storage.file.UniformObjectFileSystem;
import com.pinecone.hydra.storage.version.TitanVersionManage;
import com.pinecone.hydra.storage.version.VersionManage;
import com.pinecone.hydra.storage.volume.UniformVolumeManager;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.uma.DuplexAppointClient;
import com.pinecone.hydra.uma.HuskyDuplexExpress;
import com.pinecone.hydra.uma.wolf.WolvesAppointClient;
import com.pinecone.hydra.uma.wolf.WolvesAppointServer;
import com.pinecone.hydra.umb.kafka.WolfMCKafkaClient;
import com.pinecone.hydra.umb.rocket.WolfMCRocketClient;
import com.pinecone.hydra.umb.wolf.UlfBroadcastControlNode;
import com.pinecone.hydra.umb.wolf.WolfMCBClient;
import com.pinecone.hydra.umc.msg.UMCServiceException;
import com.pinecone.hydra.umc.wolf.client.WolfMCClient;
import com.pinecone.hydra.umc.wolf.server.WolfMCServer;
import com.pinecone.hydra.umct.WolfMCExpress;
import com.pinecone.hydra.version.ibatis.hydranium.VersionMappingDriver;
import com.pinecone.hydra.volume.ibatis.hydranium.VolumeMappingDriver;
import com.pinecone.radium.Radium;
import com.pinecone.slime.jelly.source.ibatis.IbatisClient;
import com.pinecone.summer.spring.Springron;
import com.walnut.sparta.ucdn.console.SpartaBoot;
import com.walnut.sparta.ucdn.console.umc.ufm.FileMultiDistributionIface;
import com.walnut.sparta.ucdn.console.umc.ufm.SessionValidator;
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

    protected ServicesInstrument servicesInstrument;

    protected DuplexAppointClient wolfClient;

    protected WolfMCServer        wolfKing;

    protected WolvesAppointServer wolfServer;

    protected UlfBroadcastControlNode kafkaClient;

    protected UlfBroadcastControlNode rocketClient;

    protected UniformServiceManager   serviceManager;



    protected void initSubsystem() {
        this.koiMappingDriver = new VolumeMappingDriver(
                this, (IbatisClient)this.getSystem().getMiddlewareDirector().getRDBManager().getRDBClientByName( "MySQLKingHydranium" ), this.getSystem().getDispenserCenter()
        );
        this.koiFileMappingDriver = new FileMappingDriver(
                this, (IbatisClient)this.getSystem().getMiddlewareDirector().getRDBManager().getRDBClientByName( "MySQLKingHydranium" ), this.getSystem().getDispenserCenter()
        );
        this.koiBucketMappingDriver = new BucketMappingDriver(
                this, (IbatisClient)this.getSystem().getMiddlewareDirector().getRDBManager().getRDBClientByName( "MySQLKingHydranium" ), this.getSystem().getDispenserCenter()
        );
        this.koiVersionMappingDriver = new VersionMappingDriver(
                this, (IbatisClient)this.getSystem().getMiddlewareDirector().getRDBManager().getRDBClientByName( "MySQLKingHydranium" ), this.getSystem().getDispenserCenter()
        );
        this.koiServiceMappingDriver = new ServiceMappingDriver(
                this, (IbatisClient)this.getSystem().getMiddlewareDirector().getRDBManager().getRDBClientByName( "MySQLKingHydranium" ), this.getSystem().getDispenserCenter()
        );
        this.wolfClient = new WolvesAppointClient(
                new WolfMCClient( 2048, "", this.getSystem(), this.getSystem().getMiddlewareDirector().getMiddlewareConfig().queryJSONObject( "Messagers.Messagers.WolfMCKingpin" ) )
        );
        this.wolfKing = new WolfMCServer( "", this.getSystem(), new JSONMaptron("{host: \"0.0.0.0\",\n" +
                "port: 5777, SocketTimeout: 800, KeepAliveTimeout: 3600, MaximumConnections: 1e6}") );


        this.fileSystem = new UniformObjectFileSystem( this.koiFileMappingDriver );
        this.volumeTree = new UniformVolumeManager( this.koiMappingDriver );
        this.bucketInstrument = new TitanBucketInstrument( this.koiBucketMappingDriver );
        this.versionManage = new TitanVersionManage( this.koiVersionMappingDriver );
        this.servicesInstrument = new UniformServicesInstrument( koiServiceMappingDriver );
        this.kafkaClient = new WolfMCBClient(new WolfMCKafkaClient(UCDNConstants.KafkaServer), "", this.getSystem(), WolfMCExpress.class);
        this.kafkaClient.compile( FileMultiDistributionIface.class,false );
        this.rocketClient = new WolfMCBClient(new WolfMCRocketClient(UCDNConstants.RocketServer,UCDNConstants.UCDNFileServiceGroup), "", this.getSystem(), WolfMCExpress.class);
        this.rocketClient.compile(SessionValidator.class,false);
        try {
            this.wolfServer = new WolvesAppointServer( this.wolfKing, HuskyDuplexExpress.class );
            this.serviceManager = new UniformServiceManager( servicesInstrument, wolfServer );
            wolfKing.execute();
        } catch (UMCServiceException e) {
            throw new RuntimeException(e);
        }
        try {
            this.wolfClient.execute();
            this.wolfClient.compile( ServiceLifecycleIface.class, false );
            this.wolfClient.compile( ServiceMetaManipulationIface.class, false );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        this.setPrimarySources( SpartaBoot.class );

        this.setInitializer(new Executor() {
            @Override
            public void execute() throws Exception {
                SpartaUCDNService.this.getSpringApplication().addInitializers(new ApplicationContextInitializer<ConfigurableApplicationContext>() {
                    @Override
                    public void initialize( ConfigurableApplicationContext applicationContext ) {
                        GenericApplicationContext genericApplicationContext = (GenericApplicationContext) applicationContext;
                        genericApplicationContext.registerBean("primaryFileSystem", UniformObjectFileSystem.class, () -> (UniformObjectFileSystem)fileSystem);
                        genericApplicationContext.registerBean("primaryVolume", UniformVolumeManager.class, () -> (UniformVolumeManager) volumeTree);
                        genericApplicationContext.registerBean("primaryBucket", TitanBucketInstrument.class, () -> (TitanBucketInstrument) bucketInstrument);
                        genericApplicationContext.registerBean("primaryVersion", VersionManage.class, () -> (VersionManage) versionManage);
                        genericApplicationContext.registerBean("primaryService", ServicesInstrument.class, () ->  servicesInstrument);
                        genericApplicationContext.registerBean("wolfClient", DuplexAppointClient.class, () ->  wolfClient);

                        genericApplicationContext.registerBean("uofsContentDelivery", UOFSContentDelivery.class, () -> (UOFSContentDelivery) SpartaUCDNService.this.getSystem());
                    }
                });
            }
        });
    }

    public SpartaUCDNService( String szName, Processum parent, String[] springbootArgs ) {
        super( szName, parent, springbootArgs );
        this.mSpringKernel.setPrimarySources( SpartaBoot.class );

        this.initSubsystem();
    }

    public SpartaUCDNService( String szName, Processum parent ) {
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
    public Radium getSystem() {
        return (Radium)super.getSystem();
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
    public ServicesInstrument getServicesInstrument() {
        return this.servicesInstrument;
    }

    @Override
    public DuplexAppointClient getWolfClient() {
        return this.wolfClient;
    }

    @Override
    public UlfBroadcastControlNode getKafkaClient() {
        return this.kafkaClient;
    }

    @Override
    public UlfBroadcastControlNode getRocketClient() {
        return this.rocketClient;
    }

    @Override
    public WolfMCServer getWolfMCServer() {
        return this.wolfKing;
    }

    @Override
    public WolvesAppointServer getWolvesAppointServer() {
        return this.wolfServer;
    }

    @Override
    public UniformServiceManager getUniformServiceManager() {
        return this.serviceManager;
    }
}
