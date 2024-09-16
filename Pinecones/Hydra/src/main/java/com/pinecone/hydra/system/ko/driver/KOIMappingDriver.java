package com.pinecone.hydra.system.ko.driver;

import java.util.Map;

import com.pinecone.framework.system.ProxyProvokeHandleException;
import com.pinecone.framework.system.construction.UnifyStructureInjector;
import com.pinecone.framework.system.hometype.StereotypicInjector;
import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.system.Hydrarum;

public interface KOIMappingDriver extends Pinenut {
    String getVersionSignature();

    Hydrarum getSystem();

    KOIMasterManipulator getMasterManipulator();

    // Temp, TODO
    StereotypicInjector autoConstruct( Class<?> stereotype, Map config, Object instance );
}
