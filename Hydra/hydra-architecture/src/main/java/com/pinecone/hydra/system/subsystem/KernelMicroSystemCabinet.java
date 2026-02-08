package com.pinecone.hydra.system.subsystem;

import java.util.Map;
import java.util.Set;

import com.pinecone.hydra.system.HyComponent;

public interface KernelMicroSystemCabinet extends SubsystemDirector, Cabinet {
    String KeyMainClass = "MainClass";

    void register( String name, MicroSystem system );

    void deregister( String name );

    MicroSystem get( String name );

    void clearCabinet() ;

    Set<Map.Entry<String, MicroSystem > > entrySet();

    int size();
}
