package com.walnut.sparta.uis.console.infrastructure;

import com.pinecone.framework.system.executum.Processum;
import com.pinecone.framework.system.functions.Executor;
import com.pinecone.hydra.deploy.kom.UniformDeployInstrument;
import com.pinecone.hydra.file.ibatis.hydranium.FileMappingDriver;
import com.pinecone.hydra.servgram.Servgram;
import com.pinecone.hydra.service.ibatis.hydranium.ServiceMappingDriver;
import com.pinecone.hydra.service.kom.UniformServiceInstrument;
import com.pinecone.hydra.storage.file.UniformObjectFileSystem;
import com.pinecone.hydra.system.component.ComponentInitializationException;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.slime.jelly.source.ibatis.IbatisClient;
import com.pinecone.summer.spring.Springron;
import com.pinecone.tritium.Tritium;
import com.walnut.sparta.uis.console.SpartaBoot;
import org.slf4j.Logger;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import java.io.IOException;
import java.nio.file.Path;

public class SpartaUISService extends Springron implements UISService{

    protected KOIMappingDriver koiFileMappingDriver;

    protected KOIMappingDriver koiTaskMappingDriver;

    public SpartaUISService(String szName, Processum parent, String[] springbootArgs) throws ComponentInitializationException {
        super(szName, parent, springbootArgs);
        this.mSpringKernel.setPrimarySources( SpartaBoot.class );
        this.initSubsystem();
    }


    public SpartaUISService( String szName, Processum parent ) throws ComponentInitializationException {
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

    protected void initKOMSubsystem() throws ComponentInitializationException {

        this.koiFileMappingDriver = new FileMappingDriver(
                this, (IbatisClient)this.parentSystem().getMiddlewareDirector().getRDBManager().getRDBClientByName( "MySQLKingHydranium" ), this.parentSystem().getDispenserCenter()
        );

        this.koiTaskMappingDriver  = new ServiceMappingDriver(
                this, (IbatisClient)this.parentSystem().getMiddlewareDirector().getRDBManager().getRDBClientByName( "MySQLKingHydranium" ), this.parentSystem().getDispenserCenter()
        );


    }

    protected void initSpringBeanFactorySubsystem() throws ComponentInitializationException {
        this.setPrimarySources( SpartaBoot.class );
        this.setInitializer(new Executor() {
            @Override
            public void execute() throws Exception {
                SpartaUISService.this.getSpringApplication().addInitializers(new ApplicationContextInitializer<ConfigurableApplicationContext>() {
                    @Override
                    public void initialize( ConfigurableApplicationContext applicationContext ) {
                        GenericApplicationContext genericApplicationContext = (GenericApplicationContext) applicationContext;
                    }
                });
            }
        });
    }
    @Override
    public Logger getLogger() {
        return  super.mLogger;
    }
}
