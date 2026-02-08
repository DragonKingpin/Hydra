package com.pinecone.hydra.system.subsystem;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.pinecone.framework.system.ProxyProvokeHandleException;
import com.pinecone.framework.system.regime.arch.Director;
import com.pinecone.framework.util.ClassUtils;
import com.pinecone.framework.util.config.PatriarchalConfig;
import com.pinecone.framework.util.json.JSONObject;
import com.pinecone.framework.util.lang.DynamicFactory;
import com.pinecone.framework.util.lang.GenericDynamicFactory;
import com.pinecone.framework.util.name.Namespace;
import com.pinecone.hydra.system.ArchSystemCascadeComponent;
import com.pinecone.hydra.system.HyComponent;
import com.pinecone.hydra.system.Hydrogen;

public abstract class ArchSubsystemDirector extends ArchSystemCascadeComponent implements SubsystemDirector {
    protected DynamicFactory                          mDynamicFactory;
    protected PatriarchalConfig                       mSubsystemConfig;
    protected PatriarchalConfig                       mSegmentConfig;
    protected boolean                                 mSegmentEnabled;

    public ArchSubsystemDirector( Namespace name, Hydrogen system, HyComponent parent ) {
        super( name, system, system.getComponentManager(), parent );

        this.mDynamicFactory    = new GenericDynamicFactory( this.getSystem().getTaskManager().getClassLoader() );
        this.mSubsystemConfig   = this.getSystem().getSystemConfig().getChild( "Subsystem" );
        this.prepare_segment();
        this.mSegmentEnabled    = (boolean) this.mSegmentConfig.get( "Enable" );
    }

    public ArchSubsystemDirector( Hydrogen system, HyComponent parent ) {
        this( null, system, parent );
    }

    public ArchSubsystemDirector( Hydrogen system ) {
        this( system, null );
    }

    protected abstract void prepare_segment();

    protected abstract void prepare_each_sub( String key, Object dy );

    @SuppressWarnings( "unchecked" )
    protected void prepare_init_subsystem_config( PatriarchalConfig seg ) {
        if ( seg instanceof Map ) {
            Map<String, Object> cms = (Map<String, Object>) seg;
            for ( Map.Entry<String, Object> kv : cms.entrySet() ) {
                Object dy = kv.getValue();
                if( dy instanceof String ) {
                    try {
                        PatriarchalConfig sysConfig = seg.getChildFromPath( Path.of((String) dy) );
                        cms.put( kv.getKey(), sysConfig );
                        dy = sysConfig;
                    }
                    catch ( IOException e ) {
                        throw new ProxyProvokeHandleException( e );
                    }
                }
                else if( dy.getClass().isPrimitive() || ClassUtils.isPrimitiveWrapper( dy.getClass() ) ) {
                    continue;
                }

                this.prepare_each_sub( kv.getKey(), dy );
            }
        }
    }

    @Override
    public PatriarchalConfig getSubsystemConfig() {
        return this.mSubsystemConfig;
    }

    @Override
    public PatriarchalConfig getSegmentConfig() {
        return this.mSegmentConfig;
    }

}
