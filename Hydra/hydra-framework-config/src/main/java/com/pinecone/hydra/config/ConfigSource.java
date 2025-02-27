package com.pinecone.hydra.config;

import com.pinecone.framework.system.RuntimeSystem;
import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.config.PatriarchalConfig;

import java.io.IOException;
import java.net.URI;

public interface ConfigSource extends Pinenut {
    PatriarchalConfig getSearchScopeConfig();

    RuntimeSystem getSystem();

    PatriarchalConfig loadConfig( URI path ) throws IOException;

    PatriarchalConfig loadConfig( Object dyPath ) throws IOException;

    PatriarchalConfig loadConfigBySegmentName ( String szSegName ) throws IOException;
}
