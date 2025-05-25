package com.pinecone.tritium.system;

import com.pinecone.framework.system.ProxyProvokeHandleException;
import com.pinecone.framework.system.construction.UnifyStructureInjector;
import com.pinecone.framework.system.homotype.StereotypicInjector;
import com.pinecone.framework.unit.MultiScopeMap;
import com.pinecone.framework.unit.MultiScopeMaptron;
import com.pinecone.framework.util.config.JSONConfig;
import com.pinecone.framework.util.json.JSONMaptron;
import com.pinecone.framework.util.json.homotype.AnnotatedObjectInjector;
import com.pinecone.framework.util.name.Namespace;
import com.pinecone.hydra.config.MapConfigReinterpreter;
import com.pinecone.hydra.config.ScopedMapConfigReinterpreter;
import com.pinecone.hydra.system.ArchSystemCascadeComponent;
import com.pinecone.hydra.system.HyComponent;
import com.pinecone.hydra.system.Hydrogen;

import java.util.Map;

public class TritiumConfigScope extends ArchSystemCascadeComponent implements ConfigScope {
    protected JSONConfig                      mProtoConfig;
    protected MultiScopeMap<String, Object >  mConfigScope;
    protected MapConfigReinterpreter          mConfigReinterpreter;

    public TritiumConfigScope(Namespace name, Hydrogen system, HyComponent parent, JSONConfig config ) {
        super( name, system, system.getComponentManager(), parent );

        this.mConfigScope = new MultiScopeMaptron<>( new JSONMaptron() );
        this.mProtoConfig = config;

        this.reinterpret_conf_default();

        this.mConfigReinterpreter = new ScopedMapConfigReinterpreter( this.getScopeMap() );
    }

    public TritiumConfigScope(Hydrogen system, HyComponent parent, JSONConfig config ) {
        this( (Namespace) null, system, parent, config );
    }

    public TritiumConfigScope(Hydrogen system, JSONConfig config ) {
        this( system, null,config );
    }

    public TritiumConfigScope(String name, Hydrogen system, HyComponent parent, JSONConfig config ) {
        this( system, parent, config );

        this.setTargetingName( name );
    }

    public TritiumConfigScope(String name, Hydrogen system, JSONConfig config ) {
        this( name, system, null, config );
    }

    protected void reinterpret_conf_default() {
        this.mConfigScope.setName( "GlobalConfigScope" );
        for ( Map.Entry<String,Object > kv: this.getProtoConfig().entrySet() ) {
            this.mConfigScope.put( kv.getKey(), kv.getValue() );
        }
    }

    @Override
    public JSONConfig getProtoConfig() {
        return this.mProtoConfig;
    }

    @Override
    public MultiScopeMap<String, Object > getScopeMap() {
        return this.mConfigScope;
    }

    @Override
    public MapConfigReinterpreter getMapConfigReinterpreter() {
        return this.mConfigReinterpreter;
    }

    @Override
    public MapConfigReinterpreter newMapConfigReinterpreter() {
        return new ScopedMapConfigReinterpreter( this.getScopeMap() );
    }


    @Override
    public StereotypicInjector autoInject( Class<?> stereotype, Object config, Object instance ) {
        AnnotatedObjectInjector injector = new AnnotatedObjectInjector( stereotype );
        try{
            injector.inject( config, instance );
            return injector;
        }
        catch ( Exception e ){
            throw new ProxyProvokeHandleException( e );
        }
    }

    @Override
    public StereotypicInjector autoInject( Class<?> stereotype, Map config, Object instance ) {
        AnnotatedObjectInjector injector = new AnnotatedObjectInjector( stereotype );
        try{
            injector.inject( config, instance );
        }
        catch ( Exception e ){
            throw new ProxyProvokeHandleException( e );
        }
        return injector;
    }

    @Override
    public StereotypicInjector autoConstruct( Class<?> stereotype, Object config, Object instance ) {
        UnifyStructureInjector injector = new UnifyStructureInjector( stereotype, ( (TritiumSystem)this.getSystem()).getDispenserCenter().getInstanceDispenser() );
        try{
            injector.inject( config, instance );
            return injector;
        }
        catch ( Exception e ){
            throw new ProxyProvokeHandleException( e );
        }
    }

    @Override
    public StereotypicInjector autoConstruct( Class<?> stereotype, Map config, Object instance ) {
        UnifyStructureInjector injector = new UnifyStructureInjector( stereotype, ( (TritiumSystem)this.getSystem()).getDispenserCenter().getInstanceDispenser() );
        try{
            injector.inject( config, instance );
        }
        catch ( Exception e ){
            throw new ProxyProvokeHandleException( e );
        }
        return injector;
    }

}
