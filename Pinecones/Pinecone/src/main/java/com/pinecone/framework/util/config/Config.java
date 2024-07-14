package com.pinecone.framework.util.config;

import com.pinecone.framework.system.prototype.Pinenut;

public interface Config extends Pinenut {
    Object getProtoConfig();

    Object get( Object key );

    Object getOrDefault( Object key, Object def );
}
