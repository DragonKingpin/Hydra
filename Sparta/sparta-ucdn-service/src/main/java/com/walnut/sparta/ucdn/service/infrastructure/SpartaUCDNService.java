package com.walnut.sparta.ucdn.service.infrastructure;

import com.pinecone.framework.system.executum.Processum;
import com.pinecone.framework.system.functions.Executor;
import com.pinecone.framework.util.config.JSONConfig;
import com.pinecone.hydra.bucket.ibatis.hydranium.BucketMappingDriver;
import com.pinecone.hydra.file.ibatis.hydranium.FileMappingDriver;
import com.pinecone.hydra.servgram.Servgram;
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
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.version.ibatis.hydranium.VersionMappingDriver;
import com.pinecone.hydra.volume.ibatis.hydranium.VolumeMappingDriver;
import com.pinecone.slime.jelly.source.ibatis.IbatisClient;
import com.pinecone.summer.spring.Springron;
import com.pinecone.tritium.Tritium;
import com.walnut.sparta.ucdn.service.SpartaBoot;

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


    protected KOMFileSystem fileSystem;

    protected UniformVolumeManager volumeTree;

    protected TitanBucketInstrument bucketInstrument;

    protected TitanVersionManage versionManage;


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


        JSONConfig selfConfig = (JSONConfig) this.getConfig();
        FileSystemConfig fileSystemConfig = new KernelFileSystemConfig( selfConfig.queryJSONObject( "service.PrimaryUniformFileSystem" ) );
        this.fileSystem = new UniformObjectFileSystem( this.koiFileMappingDriver, fileSystemConfig );

        VolumeConfig volumeConfig = new KernelVolumeConfig( selfConfig.queryJSONObject( "service.PrimaryUniformVolumeManager" ) );
        this.volumeTree = new UniformVolumeManager( this.koiMappingDriver, volumeConfig );
        this.bucketInstrument = new TitanBucketInstrument( this.koiBucketMappingDriver );
        this.versionManage = new TitanVersionManage( this.koiVersionMappingDriver );


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
    public Tritium getSystem() {
        return (Tritium)super.getSystem();
    }
}
