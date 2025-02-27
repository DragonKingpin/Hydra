package com.pinecone.radium.system;

import java.util.Map;

import com.pinecone.framework.system.homotype.StereotypicInjector;
import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.unit.MultiScopeMap;
import com.pinecone.framework.util.config.JSONConfig;
import com.pinecone.hydra.config.MapConfigReinterpreter;
import com.pinecone.hydra.system.HyComponent;

public interface ConfigScope extends Pinenut, HyComponent {
    String KeyGlobal = "Global" + RadiumConfigScope.class.getSimpleName();

    JSONConfig getProtoConfig();

    MultiScopeMap<String, Object > getScopeMap();

    MapConfigReinterpreter getMapConfigReinterpreter();

     MapConfigReinterpreter newMapConfigReinterpreter() ;


    StereotypicInjector autoInject(Class<?> stereotype, Object config, Object instance ) ;

    StereotypicInjector autoInject(Class<?> stereotype, Map config, Object instance ) ;

    StereotypicInjector autoConstruct( Class<?> stereotype, Object config, Object instance ) ;

    StereotypicInjector autoConstruct( Class<?> stereotype, Map config, Object instance ) ;
}
