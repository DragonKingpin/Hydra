package com.walnuts.sparta.uofs.console.infrastructure;

import com.pinecone.framework.system.executum.Processum;
import com.pinecone.framework.system.functions.Executor;
import com.pinecone.hydra.bucket.ibatis.hydranium.BucketMappingDriver;
import com.pinecone.hydra.file.ibatis.hydranium.FileMappingDriver;
import com.pinecone.hydra.servgram.Servgram;
import com.pinecone.hydra.service.ibatis.hydranium.ServiceMappingDriver;
import com.pinecone.hydra.service.kom.ServicesInstrument;
import com.pinecone.hydra.service.kom.UniformServicesInstrument;
import com.pinecone.hydra.storage.bucket.TitanBucketInstrument;
import com.pinecone.hydra.storage.file.KOMFileSystem;
import com.pinecone.hydra.storage.file.UniformObjectFileSystem;
import com.pinecone.hydra.storage.version.TitanVersionManage;
import com.pinecone.hydra.storage.version.VersionManage;
import com.pinecone.hydra.storage.volume.UniformVolumeManager;
import com.pinecone.hydra.system.component.ComponentInitializationException;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.uma.DuplexAppointClient;
import com.pinecone.hydra.version.ibatis.hydranium.VersionMappingDriver;
import com.pinecone.hydra.volume.ibatis.hydranium.VolumeMappingDriver;
import com.pinecone.radium.Radium;
import com.pinecone.slime.jelly.source.ibatis.IbatisClient;
import com.pinecone.summer.spring.Springron;
import com.walnuts.sparta.uofs.console.SpartaBoot;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import java.io.IOException;
import java.nio.file.Path;

public class SpartaUOFSService extends Springron implements UOFSService {
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

    protected void initKOMSubsystem() throws ComponentInitializationException {
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

        this.fileSystem         = new UniformObjectFileSystem( this.koiFileMappingDriver );
        this.volumeTree         = new UniformVolumeManager( this.koiMappingDriver );
        this.bucketInstrument   = new TitanBucketInstrument( this.koiBucketMappingDriver );
        this.versionManage      = new TitanVersionManage( this.koiVersionMappingDriver );
        this.servicesInstrument = new UniformServicesInstrument( this.koiServiceMappingDriver );
    }

    protected void initSpringBeanFactorySubsystem() throws ComponentInitializationException {
        this.setPrimarySources( SpartaBoot.class );
        this.setInitializer(new Executor() {
            @Override
            public void execute() throws Exception {
                SpartaUOFSService.this.getSpringApplication().addInitializers(new ApplicationContextInitializer<ConfigurableApplicationContext>() {
                    @Override
                    public void initialize( ConfigurableApplicationContext applicationContext ) {
                        GenericApplicationContext genericApplicationContext = (GenericApplicationContext) applicationContext;
                        genericApplicationContext.registerBean("primaryFileSystem", UniformObjectFileSystem.class, () -> (UniformObjectFileSystem) fileSystem);
                        genericApplicationContext.registerBean("primaryVolume", UniformVolumeManager.class, () -> (UniformVolumeManager) volumeTree);
                        genericApplicationContext.registerBean("primaryBucket", TitanBucketInstrument.class, () -> (TitanBucketInstrument) bucketInstrument);
                        genericApplicationContext.registerBean("primaryVersion", VersionManage.class, () -> (VersionManage) versionManage);
                        genericApplicationContext.registerBean("primaryService", ServicesInstrument.class, () ->  servicesInstrument);
                    }
                });
            }
        });
    }

    protected void initSubsystem() throws ComponentInitializationException {
        this.initKOMSubsystem();
        this.initSpringBeanFactorySubsystem();
    }

    public SpartaUOFSService(String szName, Processum parent, String[] springbootArgs ) throws ComponentInitializationException {
        super( szName, parent, springbootArgs );
        this.mSpringKernel.setPrimarySources( SpartaBoot.class );

        this.initSubsystem();
    }
    public SpartaUOFSService( String szName, Processum parent ) throws ComponentInitializationException {
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
}
