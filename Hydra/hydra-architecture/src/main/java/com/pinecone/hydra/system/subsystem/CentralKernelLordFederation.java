package com.pinecone.hydra.system.subsystem;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pinecone.framework.system.ProxyProvokeHandleException;
import com.pinecone.framework.system.regime.arch.Lord;
import com.pinecone.framework.util.config.JSONConfig;
import com.pinecone.framework.util.name.Namespace;
import com.pinecone.hydra.system.HyComponent;
import com.pinecone.hydra.system.Hydrogen;

public class CentralKernelLordFederation extends ArchSubsystemDirector implements KernelLordFederation {
    protected Logger log = LoggerFactory.getLogger( "CentralKernelLordFederation" );

    protected Map<String, Lord> mEmpireLords;   // Domain subsystem.

    public CentralKernelLordFederation( Namespace name, Hydrogen system, HyComponent parent ) {
        super( name, system, parent );

        this.mEmpireLords = new ConcurrentHashMap<>();
        this.prepare_init_subsystem_config( this.mSegmentConfig );

        this.log.info( "[Lifecycle] LordFederation prepared, ready to start. <Done>" );
    }

    public CentralKernelLordFederation( Hydrogen system, HyComponent parent ) {
        this( null, system, parent );
    }

    public CentralKernelLordFederation( Hydrogen system ) {
        this( system, null );
    }

    @Override
    protected void prepare_segment() {
        this.mSegmentConfig     = this.mSubsystemConfig.getChild( "SystemFederation" );
    }

    @Override
    protected void prepare_each_sub( String key, Object dy ) {
        if ( !this.mSegmentEnabled ) {
            return;
        }

        if( dy instanceof Map ) {
            try {
                Map tm = (Map) dy;
                String name = (String) tm.get( "Name" );
                if( name == null ) {
                    name = key;
                }

                Lord lord = this.instantiate( tm, name );
                this.register( name, lord );

                if( lord == null ) {
                    throw new IllegalArgumentException( "Instancing Lord compromised with illegal arguments." );
                }
            }
            catch ( ClassNotFoundException e ) {
                throw new ProxyProvokeHandleException( e );
            }
        }
        else {
            throw new IllegalArgumentException( "Lord config should be map or json format." );
        }
    }

    @Override
    @SuppressWarnings( "unchecked" )
    protected Lord instantiate( Map config, String name ) throws ClassNotFoundException {
        Class<? > clazz = this.mDynamicFactory.getClassLoader().loadClass( (String)config.get( KernelLordFederation.KeyMainClass ) );

        JSONConfig p = null;
        if ( this.mSegmentConfig instanceof JSONConfig ) {
            p = (JSONConfig) this.mSegmentConfig;
        }
        Object      ins = this.mDynamicFactory.optNewInstance( clazz, new Object[] {
                this.getSystem(), name, new JSONConfig( (Map<String, Object>) config, p )
        } );
        return (Lord) ins;
    }

    @Override
    public Lord instantiate( String fullName ) {
        Lord ms = (Lord) super.instantiate( fullName );
        this.register( fullName, ms );
        return ms;
    }


    @Override
    public void register( String name, Lord system ) {
        this.mEmpireLords.put( name, system );
    }

    @Override
    public void deregister( String name ) {
        this.mEmpireLords.remove( name );
    }

    @Override
    public Lord get( String name ) {
        return this.mEmpireLords.get( name );
    }

    @Override
    public void clearLords() {
        for ( Lord system : this.mEmpireLords.values() ) {
            system.release();
        }

        this.mEmpireLords.clear();
    }

    @Override
    public Set<Map.Entry<String, Lord > > entrySet() {
        return this.mEmpireLords.entrySet();
    }

    @Override
    public int size() {
        return this.mEmpireLords.size();
    }


    @Override
    public Hydrogen getSystem() {
        return super.getSystem();
    }
}