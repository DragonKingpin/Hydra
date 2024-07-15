package com.pinecone.hydra.system.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface Slf4jTracerScope extends TracerScope {
    @Override
    default Logger newLogger( String name ){
        return LoggerFactory.getLogger( this.getLoggerName( name ) );
    }
}
