package com.pinecone.framework.util.config;

import java.util.Map;

public interface Configson extends MappedConfig, PatriarchalConfig {
    Map<String, Object > getProtoConfig();
}
