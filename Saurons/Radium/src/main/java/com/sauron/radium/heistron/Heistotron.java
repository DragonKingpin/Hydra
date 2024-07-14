package com.sauron.radium.heistron;

import com.pinecone.hydra.config.MapConfigReinterpreter;
import com.pinecone.hydra.servgram.Gram;
import com.pinecone.hydra.servgram.OrchestrateInterruptException;
import com.sauron.radium.heistron.orchestration.LocalHeistumOrchestrator;
import com.sauron.radium.heistron.orchestration.HeistletOrchestrator;
import com.pinecone.framework.util.config.JSONConfig;
import com.pinecone.hydra.config.ConfigSource;
import com.pinecone.hydra.config.LocalConfigSource;
import com.pinecone.framework.system.executum.Processum;
import com.pinecone.hydra.servgram.ArchServgramium;
import com.pinecone.framework.util.json.JSONMaptron;
import com.pinecone.framework.util.json.JSONObject;
import com.sauron.radium.system.RadiumSystem;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

@Gram( "Heist" )
public class Heistotron extends ArchServgramium implements Heistgram {
    protected boolean              mbEnableCmdCall    = true;

    protected ConfigSource         mUnifyConfigSource;
    protected JSONConfig           mLocalHeistsConfigList;
    protected JSONConfig           mTemplateHeistSchemeConfig;
    protected JSONConfig           mComponents;

    protected HeistletOrchestrator mHeistletOrchestrator;

    public Heistotron( String szName, Processum parent ) {
        super( szName, parent );

        this.mHeistletOrchestrator = new LocalHeistumOrchestrator( this, this.getConfig()  );
        this.loadHeistronScopeConfig();
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
        MapConfigReinterpreter reinterpreter  = this.getSystem().getPrimaryConfigScope().newMapConfigReinterpreter();
        reinterpreter.addExcludeKey( HeistConfigConstants.KeyTemplatedConfig );
        reinterpreter.reinterpretByLineage( this.getConfig(), null );

        this.mUnifyConfigSource               = new LocalConfigSource( this, joLocalConfigs, joLocalHeistsConfigList ); // TODO, to implements UnifyConfigSource

        this.prepareTemplateHeistScheme();
    }

    @Override
    public JSONConfig getConfig() {
        return (JSONConfig)this.mServgramConf;
    }

    @Override
    public RadiumSystem getSystem() {
        return (RadiumSystem) super.getSystem();
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
        Map<String, String[] > map = this.getSystem().getStartupCommandMap();
        String[] heists = map.get( "heist" );
        if( heists != null && heists.length > 0 ) {
            return heists[ 0 ];
        }
        return "";
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

    protected void dispatch() throws OrchestrateInterruptException {
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
            this.mHeistletOrchestrator.orchestrate();
        }
    }

    @Override
    public void execute() throws OrchestrateInterruptException  {
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
            else if( annotation instanceof com.sauron.radium.heistron.orchestration.Heistlet ) {
                return ( (com.sauron.radium.heistron.orchestration.Heistlet) annotation ).value();
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

}
