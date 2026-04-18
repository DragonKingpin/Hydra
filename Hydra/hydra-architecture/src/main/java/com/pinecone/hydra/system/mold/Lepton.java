package com.pinecone.hydra.system.mold;

import java.nio.file.Path;

import com.pinecone.framework.util.lang.DynamicFactory;
import com.pinecone.hydra.system.component.Slf4jTraceable;

public interface Lepton extends DrakeHypha, Slf4jTraceable {

    Path getPrimaryConfigsPath();

    Path getPrimaryConfigPath();

    DynamicFactory getShardDynamicFactory();

}
