package com.pinecone.hydra.system.subsystem;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.pinecone.framework.system.ProxyProvokeHandleException;
import com.pinecone.framework.util.config.PatriarchalConfig;
import com.pinecone.framework.util.json.JSONObject;
import com.pinecone.framework.util.lang.DynamicFactory;
import com.pinecone.framework.util.lang.GenericDynamicFactory;
import com.pinecone.framework.util.name.Namespace;
import com.pinecone.hydra.system.ArchSystemCascadeComponent;
import com.pinecone.hydra.system.HyComponent;
import com.pinecone.hydra.system.Hydrogen;

public class CentralMicroSystemCabinet extends ArchSubsystemDirector implements KernelMicroSystemCabinet {
    protected ConcurrentHashMap<String, MicroSystem > mRegistry;
    protected PatriarchalConfig                       mConfMicroSystems;


    public CentralMicroSystemCabinet( Namespace name, Hydrogen system, HyComponent parent ) {
        super( name, system, parent );

        this.mRegistry          = new ConcurrentHashMap<>();
        this.prepare_init_subsystem_config( this.mConfMicroSystems );
    }

    public CentralMicroSystemCabinet( Hydrogen system, HyComponent parent ) {
        this( null, system, parent );
    }

    public CentralMicroSystemCabinet( Hydrogen system ) {
        this( system, null );
    }

    @Override
    protected void prepare_segment() {
        this.mSegmentConfig     = this.mSubsystemConfig.getChild( "SystemCabinet" );
        this.mConfMicroSystems  = this.mSegmentConfig.getChild( "MicroSystems" );
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

                Class<? > clazz = this.mDynamicFactory.getClassLoader().loadClass( (String)tm.get( KernelMicroSystemCabinet.KeyMainClass ) );
                Object      ins = this.mDynamicFactory.optNewInstance( clazz, new Object[] { name, this.getSystem() } );

                this.register( name, (MicroSystem)ins );

                if( ins == null ) {
                    throw new IllegalArgumentException( "Instancing MicroSystem compromised with illegal arguments." );
                }
            }
            catch ( ClassNotFoundException e ) {
                throw new ProxyProvokeHandleException( e );
            }
        }
        else {
            throw new IllegalArgumentException( "MicroSystem config should be map or json format." );
        }
    }

    @Override
    public void register( String name, MicroSystem system ) {
        this.mRegistry.put( name, system );
    }

    @Override
    public void deregister( String name ) {
        this.mRegistry.remove( name );
    }

    @Override
    public MicroSystem get( String name ) {
        return this.mRegistry.get( name );
    }

    @Override
    public void clearCabinet() {
        for( MicroSystem system : this.mRegistry.values() ) {
            system.release();
        }

        this.mRegistry.clear();
    }

    @Override
    public Set<Map.Entry<String, MicroSystem > > entrySet() {
        return this.mRegistry.entrySet();
    }

    @Override
    public int size() {
        return this.mRegistry.size();
    }


    @Override
    public Hydrogen getSystem() {
        return super.getSystem();
    }
}
