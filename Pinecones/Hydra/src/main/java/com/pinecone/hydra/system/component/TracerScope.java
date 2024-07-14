package com.pinecone.hydra.system.component;

import com.pinecone.hydra.system.HyComponent;
import com.pinecone.hydra.system.Hydrarum;


public interface TracerScope extends HyComponent {
    @Override
    Hydrarum getSystem();

    String getLoggerName( String name );

    Object newLogger( String name );
}
