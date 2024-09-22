package com.pinecone.hydra.system.component;

import com.pinecone.framework.util.name.Namespace;
import com.pinecone.hydra.system.ArchSystemCascadeComponent;
import com.pinecone.hydra.system.HyComponent;
import com.pinecone.hydra.system.Hydrarum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenericTracerScope extends ArchSystemCascadeComponent implements Slf4jTracerScope {
    public GenericTracerScope( Namespace name, Hydrarum system, HyComponent parent ) {
        super( name, system, system.getComponentManager(), parent );
    }

    public GenericTracerScope( Hydrarum system, HyComponent parent ) {
        this( null, system, parent );
    }

    public GenericTracerScope( Hydrarum system ) {
        this( system, null );
    }

    @Override
    public Hydrarum getSystem() {
        return super.getSystem();
    }

    @Override
    public String getLoggerName( String name ){
        return String.format( "%s<%s>", this.getSystem().className(), name );
    }

    @Override
    public Logger newLogger( String name ){
        return LoggerFactory.getLogger( this.getLoggerName( name ) );
    }

}