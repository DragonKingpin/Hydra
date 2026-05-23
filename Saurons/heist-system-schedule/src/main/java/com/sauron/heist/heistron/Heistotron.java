package com.sauron.heist.heistron;

import com.pinecone.hydra.config.MapConfigReinterpreter;
import com.pinecone.hydra.servgram.Gram;
import com.pinecone.hydra.servgram.OrchestrateInterruptException;
import com.pinecone.hydra.task.TaskInstanceStatus;
import com.sauron.heist.heistron.event.HeistLifecycleEventInterceptor;
import com.sauron.heist.heistron.orchestration.Heistlet;
import com.pinecone.tritium.system.TritiumSystem;
import com.sauron.heist.heistron.orchestration.Hierarchy;
import com.sauron.heist.heistron.orchestration.LocalHeistumOrchestrator;
import com.sauron.heist.heistron.orchestration.HeistletOrchestrator;
import com.pinecone.framework.util.config.JSONConfig;
import com.pinecone.hydra.config.ConfigSource;
import com.pinecone.hydra.config.LocalConfigSource;
import com.pinecone.framework.system.executum.Processum;
import com.pinecone.hydra.servgram.ArchServgramium;
import com.pinecone.framework.util.json.JSONMaptron;
import com.pinecone.framework.util.json.JSONObject;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 *  Bean Nuts Hazelnut Sauron Heistotron
 *  Author: Harald.E / JH.W (DragonKing)
 *  Copyright © 2008 - 2028 Bean Nuts Foundation All rights reserved.
 *  *****************************************************************************************
 *  Heistotron Spider for PB-Level Automatic Crawler
 *
 *  *****************************************************************************************
 *  DragonKing.cn of Harald
 */
@Gram( "Heist" )
public class Heistotron extends ArchServgramium implements Heistgram {
    protected boolean                                      mbEnableCmdCall    = true;

    protected ConfigSource                                 mUnifyConfigSource;
    protected JSONConfig                                   mLocalHeistsConfigList;
    protected JSONConfig                                   mTemplateHeistSchemeConfig;
    protected JSONConfig                                   mComponents;

    protected HeistletOrchestrator                         mHeistletOrchestrator;

    protected Collection<HeistLifecycleEventInterceptor>   mLifecycleEventInterceptors;

    public Heistotron( String szName, Processum parent ) {
        super( szName, parent );

        this.mHeistletOrchestrator = new LocalHeistumOrchestrator( this, this.getConfig()  );
        this.loadHeistronScopeConfig();
        this.mLifecycleEventInterceptors = new ArrayList<>();
    }

    protected void prepareTemplateHeistScheme() {
        JSONConfig tc = this.getConfig().getChild( HeistConfigConstants.KeyTemplatedConfig ) ;
        if( tc == null ){
            this.getConfig().put( HeistConfigConstants.KeyTemplatedConfig, new JSONMaptron() );
            tc = this.getConfig().getChild( HeistConfigConstants.KeyTemplatedConfig );
        }
        this.mTemplateHeistSchemeConfig       = tc;

        this.mTemplateHeistSchemeConfig.put( HeistConfigConstants.KeyHttpBrowser, this.getComponentsConfig().opt( HeistConfigConstants.KeyHttpBrowser ) );
    }

    protected void loadHeistronScopeConfig() {
        this.mbEnableCmdCall                  = this.getConfig().optBoolean( "EnableCmdCall" );
        this.mLocalHeistsConfigList           = this.getConfig().getChild( HeistConfigConstants.KeyHeistsTable );
        this.mComponents                      = this.getConfig().getChild( HeistConfigConstants.KeyComponents );

        JSONConfig joLocalHeistsConfigList    = this.getLocalHeistsConfigList();
        JSONConfig joLocalConfigs             = this.getConfig().getChild( HeistConfigConstants.KeyConfigScope ).getChild( HeistConfigConstants.KeyLocalConfigs );
        MapConfigReinterpreter reinterpreter  = this.parentSystem().getPrimaryConfigScope().newMapConfigReinterpreter();
        reinterpreter.addExcludeKey( HeistConfigConstants.KeyTemplatedConfig );
        reinterpreter.reinterpretByLineage( this.getConfig(), null );

        this.mUnifyConfigSource               = new LocalConfigSource( this, joLocalConfigs, joLocalHeistsConfigList ); // TODO, to implements UnifyConfigSource

        this.prepareTemplateHeistScheme();
    }

    @Override
    public Heistgram addLifecycleEventInterceptors( HeistLifecycleEventInterceptor interceptor ) {
        this.mLifecycleEventInterceptors.add( interceptor );
        return this;
    }

    @Override
    public Heistgram removeLifecycleEventInterceptors( HeistLifecycleEventInterceptor interceptor ) {
        this.mLifecycleEventInterceptors.remove( interceptor );
        return this;
    }

    @Override
    public JSONConfig getConfig() {
        return (JSONConfig)this.mServgramConf;
    }

    @Override
    public TritiumSystem parentSystem() {
        return (TritiumSystem) super.parentSystem();
    }

    @Override
    public JSONConfig getTemplateHeistSchemeConfig() {
        return this.mTemplateHeistSchemeConfig;
    }

    public ConfigSource getConfigSource() {
        return this.mUnifyConfigSource;
    }

    @Override
    public JSONConfig getLocalHeistsConfigList() {
        return this.mLocalHeistsConfigList;
    }

    public HeistletOrchestrator getHeistletOrchestrator() {
        return this.mHeistletOrchestrator;
    }

    protected String queryCmdDesignatedHeist() {
        String heist = this.parentSystem().getStartupCommandMap().get( "heist" );
        if ( heist == null ) {
            return "";
        }
        return heist;
    }

    @Override
    public JSONConfig queryHeistConfig ( String szHeistName ) {
        JSONConfig parent = this.getLocalHeistsConfigList();
        Object thisConf   = parent.opt( szHeistName );

        JSONConfig config;
        try{
            if( thisConf instanceof String ) {
                config = (JSONConfig) this.getConfigSource().loadConfig( Path.of( (String) thisConf ) );
            }
            else if( thisConf instanceof JSONObject ) {
                config = parent.getChild( szHeistName );
            }
            else {
                config = (JSONConfig) this.getConfigSource().loadConfigBySegmentName( szHeistName );
            }
        }
        catch ( IOException e ) {
            throw new ConfigNotFoundException( e );
        }

        if( config == null ) {
            throw new ConfigNotFoundException( "Compromised attempts, Heist config `" + szHeistName + "` can be found in nowhere." );
        }
        return config;
    }

    @Override
    public JSONConfig getComponentsConfig() {
        return this.mComponents;
    }

    protected void dispatch() throws HeistException {
        String szDesignatedHeist = this.queryCmdDesignatedHeist();
        //szDesignatedHeist = "Void";
        if( szDesignatedHeist.length() != 0 ) {
            this.infoLifecycle( "Into command-prompt mode" );
            List heists = this.mHeistletOrchestrator.preloads( szDesignatedHeist );
            for( Object o : heists ) {
                ( (Heistum) o ).toHeist();
            }
        }
        else {
            this.infoLifecycle( "Into orchestrator mode" );
            try {
                this.mHeistletOrchestrator.orchestrate();
            }
            catch ( OrchestrateInterruptException e ) {
                throw new HeistOrchestrateException( e );
            }
        }
    }

    @Override
    public void execute() throws HeistException  {
        this.infoLifecycle( "Can do !" );
        this.dispatch();
    }

    @Override
    public String searchHeistName( Heistum that ) {
        Annotation[] annotations = that.getClass().getAnnotations();
        for( Annotation annotation : annotations ) {
            if( annotation instanceof com.pinecone.hydra.servgram.Gram ) {
                return ( (com.pinecone.hydra.servgram.Gram) annotation ).value();
            }
            else if( annotation instanceof Heistlet) {
                return ( (Heistlet) annotation ).value();
            }
        }

        List prefixes      = this.mHeistletOrchestrator.getPreloadPrefixes();
        String szClassName = that.className();
        if( prefixes != null ) {
            for( Object o : prefixes ) {
                szClassName = szClassName.replaceFirst( o.toString(), "" );
            }
        }

        List suffixes      = this.mHeistletOrchestrator.getPreloadSuffixes();
        if( suffixes != null ) {
            for( Object o : suffixes ) {
                szClassName = szClassName.replaceFirst( o.toString(), "" );
            }
        }

        return szClassName;
    }

    @Override
    public void notifyLifecycleEvent( Heistum heist, TaskInstanceStatus instanceStatus, Hierarchy hierarchy ) {
        for ( HeistLifecycleEventInterceptor interceptor : this.mLifecycleEventInterceptors ) {
            interceptor.afterLifecycleEventTriggered( heist.getName(), heist, instanceStatus, hierarchy );
        }
    }

}
