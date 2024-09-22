package com.pinecone.hydra.system.minister;

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
import com.pinecone.hydra.system.Hydrarum;

public class CentralMicroSystemCabinet extends ArchSystemCascadeComponent implements KernelMicroSystemCabinet {
    protected ConcurrentHashMap<String, MicroSystem > mRegistry;
    protected PatriarchalConfig                       mConfSystemCabinet;
    protected PatriarchalConfig                       mConfMicroSystems;
    protected DynamicFactory                          mDynamicFactory;

    public CentralMicroSystemCabinet( Namespace name, Hydrarum system, HyComponent parent ) {
        super( name, system, system.getComponentManager(), parent );

        this.mDynamicFactory    = new GenericDynamicFactory( this.getSystem().getTaskManager().getClassLoader() );
        this.mRegistry          = new ConcurrentHashMap<>();
        this.mConfSystemCabinet = this.getSystem().getSystemConfig().getChild( "SystemCabinet" );
        this.mConfMicroSystems  = this.mConfSystemCabinet.getChild( "MicroSystems" );
        this.loadConfigEtSystem();
    }

    public CentralMicroSystemCabinet( Hydrarum system, HyComponent parent ) {
        this( null, system, parent );
    }

    public CentralMicroSystemCabinet( Hydrarum system ) {
        this( system, null );
    }

    protected void loadConfigEtSystem() {
        JSONObject cms = (JSONObject)this.mConfMicroSystems;
        for ( Map.Entry<String, Object > kv : cms.entrySet() ) {
            Object dy = kv.getValue();
            if( dy instanceof String ) {
                try{
                    PatriarchalConfig sysConfig = this.mConfMicroSystems.getChildFromPath( Path.of((String) dy) );
                    cms.put( kv.getKey(), sysConfig );
                    dy = sysConfig;
                }
                catch ( IOException e ) {
                    throw new ProxyProvokeHandleException( e );
                }
            }

            if( dy instanceof Map ) {
                try{
                    Map tm = (Map) dy;
                    String name = (String) tm.get( "Name" );
                    if( name == null ) {
                        name = kv.getKey();
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
    public int cabinetSize() {
        return this.mRegistry.size();
    }


    @Override
    public Hydrarum getSystem() {
        return super.getSystem();
    }
}
