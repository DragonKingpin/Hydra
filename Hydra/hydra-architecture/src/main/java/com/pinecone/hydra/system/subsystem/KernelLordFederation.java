package com.pinecone.hydra.system.subsystem;

import java.util.Map;
import java.util.Set;

import com.pinecone.framework.system.regime.arch.Lord;

public interface KernelLordFederation extends SubsystemDirector, Federation {

    String KeyMainClass = "MainClass";

    void register( String name, Lord system );

    void deregister( String name );

    Lord get( String name );

    void clearLords() ;

    Set<Map.Entry<String, Lord > > entrySet();

    int size();

    Lord instantiate( String fullName );

}
