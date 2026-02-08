package com.pinecone.hydra.system.subsystem;

import com.pinecone.framework.system.regime.arch.Director;
import com.pinecone.framework.util.config.PatriarchalConfig;
import com.pinecone.hydra.system.HyComponent;

public interface SubsystemDirector extends Director, HyComponent {

    PatriarchalConfig getSubsystemConfig();

    PatriarchalConfig getSegmentConfig();

}
