package com.pinecone.hydra.registry.source;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.registry.entity.ConfNodeMeta;

public interface RegistryNodeMetaManipulator {
    void insert(ConfNodeMeta confNodeMeta);

    void remove(GUID guid);

    ConfNodeMeta getConfNodeMeta(GUID guid);

    void update(ConfNodeMeta confNodeMeta);
}
