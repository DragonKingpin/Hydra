package com.walnut.sparta;

import com.pinecone.framework.system.CascadeSystem;
import com.pinecone.framework.unit.MultiScopeMap;
import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.config.JSONSystemConfig;
import com.pinecone.framework.util.io.Tracerson;
import com.pinecone.hydra.Hydradom;
import com.pinecone.hydra.system.HyHierarchy;
import com.pinecone.hydra.system.component.Slf4jTraceable;
import com.pinecone.hydra.system.component.Slf4jTracerScope;
import com.pinecone.hydra.system.types.HydraKingdom;
import com.pinecone.summer.spring.Springron;
import org.slf4j.Logger;

public class Sparta extends Hydradom implements HydraKingdom, Slf4jTraceable {
    public Sparta( CascadeSystem parent ) {
        this( parent.getStartupCommand(), null, parent );
    }

    public Sparta( String[] args, CascadeSystem parent ) {
        this( args, null, parent );
    }

    public Sparta( String[] args, String szName, CascadeSystem parent ){
        super( args, szName, parent );
    }

    protected void loadTracer() {
        this.mConsole = new Tracerson();
    }

    @Override
    protected void loadConfig() {
        this.mjoGlobalConfig   = (JSONSystemConfig) this.parentExecutum().getGlobalConfig();
    }

    public void vitalize () throws Exception {
        Springron springron = new Springron( "Springron", this );
        springron.execute();

        Thread shutdowner = new Thread(()->{
            Debug.sleep( 5000 );
            springron.terminate();
        });
        //shutdowner.start();

        this.getTaskManager().add( springron );
        this.getTaskManager().syncWaitingTerminated();
    }

    @Override
    public Logger getLogger() {
        return null;
    }

    @Override
    public Slf4jTracerScope getTracerScope() {
        return null;
    }

    @Override
    public HyHierarchy getServiceArch() {
        return null;
    }

    @Override
    public boolean isTopmostArchy() {
        return false;
    }

    @Override
    public HyHierarchy getTopmostArchy() {
        return null;
    }

    @Override
    public boolean isBottommostArchy() {
        return false;
    }

    @Override
    public HyHierarchy getBottommostArchy() {
        return null;
    }

    @Override
    public MultiScopeMap<String, Object> getGlobalConfigScope() {
        return null;
    }
}
