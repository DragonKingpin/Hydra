package com.walnut.sparta.utask.console.infrastructure;

import com.pinecone.framework.system.executum.Processum;
import com.pinecone.framework.system.functions.Executor;
import com.pinecone.framework.util.config.JSONConfig;
import com.pinecone.hydra.bucket.ibatis.hydranium.BucketMappingDriver;
import com.pinecone.hydra.file.ibatis.hydranium.FileMappingDriver;
import com.pinecone.hydra.servgram.Servgram;
import com.pinecone.hydra.service.ibatis.hydranium.ServiceMappingDriver;
import com.pinecone.hydra.service.kom.ServiceInstrument;
import com.pinecone.hydra.service.kom.UniformServiceInstrument;
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
import com.pinecone.hydra.task.ibatis.hydranium.TaskMappingDriver;
import com.pinecone.hydra.task.kom.TaskInstrument;
import com.pinecone.hydra.task.kom.UniformTaskInstrument;
import com.pinecone.hydra.version.ibatis.hydranium.VersionMappingDriver;
import com.pinecone.hydra.volume.ibatis.hydranium.VolumeMappingDriver;
import com.pinecone.slime.jelly.source.ibatis.IbatisClient;
import com.pinecone.summer.spring.Springron;
import com.pinecone.tritium.Tritium;
import com.walnut.odin.task.CentralizedTaskInstrument;
import com.walnut.odin.task.GenericRavenTaskConfig;
import com.walnut.odin.task.RavenTaskInstrument;
import com.walnut.odin.task.mapper.OdinUniformTaskMappingDriver;
import com.walnut.sparta.utask.console.SpartaBoot;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import java.io.IOException;
import java.nio.file.Path;

public class SpartaUTASKService extends Springron implements TaskService{

    protected KOIMappingDriver koiFileMappingDriver;


    protected KOIMappingDriver koiTaskMappingDriver;

    protected KOMFileSystem fileSystem;

    protected CentralizedTaskInstrument mUniformTaskInstrument;

    protected void initKOMSubsystem() throws ComponentInitializationException {

        this.koiFileMappingDriver = new FileMappingDriver(
                this, (IbatisClient)this.parentSystem().getMiddlewareDirector().getRDBManager().getRDBClientByName( "MySQLKingHydranium" ), this.parentSystem().getDispenserCenter()
        );

        this.koiTaskMappingDriver  = new OdinUniformTaskMappingDriver(
                this, (IbatisClient)this.parentSystem().getMiddlewareDirector().getRDBManager().getRDBClientByName( "MySQLKingHydranium" ), this.parentSystem().getDispenserCenter()
        );

        this.mUniformTaskInstrument = new RavenTaskInstrument( this.koiTaskMappingDriver, new GenericRavenTaskConfig() );

    }

    protected void initSpringBeanFactorySubsystem() throws ComponentInitializationException {
        this.setPrimarySources( SpartaBoot.class );
        this.setInitializer(new Executor() {
            @Override
            public void execute() throws Exception {
                SpartaUTASKService.this.getSpringApplication().addInitializers(new ApplicationContextInitializer<ConfigurableApplicationContext>() {
                    @Override
                    public void initialize( ConfigurableApplicationContext applicationContext ) {
                        GenericApplicationContext genericApplicationContext = (GenericApplicationContext) applicationContext;
                        genericApplicationContext.registerBean("primaryFileSystem", UniformObjectFileSystem.class, () -> (UniformObjectFileSystem) fileSystem);
                        genericApplicationContext.registerBean("primaryTask", CentralizedTaskInstrument.class, () -> (CentralizedTaskInstrument) mUniformTaskInstrument);
                    }
                });
            }
        });
    }


    public SpartaUTASKService(String szName, Processum parent, String[] springbootArgs ) throws ComponentInitializationException {
        super( szName, parent, springbootArgs );
        this.mSpringKernel.setPrimarySources( SpartaBoot.class );

        this.initSubsystem();
    }
    public SpartaUTASKService( String szName, Processum parent ) throws ComponentInitializationException {
        this( szName, parent, new String[0] );
    }

    protected void initSubsystem() throws ComponentInitializationException {
        this.initKOMSubsystem();
        this.initSpringBeanFactorySubsystem();
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

}
