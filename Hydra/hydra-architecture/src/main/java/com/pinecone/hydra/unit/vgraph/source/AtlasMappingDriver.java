package com.pinecone.hydra.unit.vgraph.source;

import com.pinecone.framework.system.executum.Processum;
import com.pinecone.framework.system.homotype.StereotypicInjector;
import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.system.Hydrarum;
import com.pinecone.hydra.system.ko.driver.KOIMasterManipulator;

import java.util.Map;

public interface AtlasMappingDriver extends Pinenut {
    String getVersionSignature();

    Hydrarum getSystem();

    Processum getSuperiorProcess();

    AtlasMasterManipulator getMasterManipulator();

    // Temp, TODO
    StereotypicInjector autoConstruct(Class<?> stereotype, Map config, Object instance );
}
