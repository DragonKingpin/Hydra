package com.pinecone.hydra.system.component;

import com.pinecone.hydra.system.HyComponent;
import com.pinecone.hydra.system.Hydrogen;


public interface TracerScope extends HyComponent {
    @Override
    Hydrogen getSystem();

    String getLoggerName( String name );

    Object newLogger( String name );
}
