package com.pinecone.hydra.registry.source;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.registry.entity.ConfigNodeMeta;

public interface RegistryNodeMetaManipulator {
    void insert(ConfigNodeMeta configNodeMeta);

    void remove(GUID guid);

    ConfigNodeMeta getConfigNodeMeta(GUID guid);

    void update(ConfigNodeMeta configNodeMeta);
}
