package com.pinecone.framework.util.config;

import java.util.Map;

public interface MappedConfig extends Config {
    Map getProtoConfig();

    @Override
    default Object get( Object key ) {
        return this.getProtoConfig().get( key );
    }
}
