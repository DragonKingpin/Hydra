package com.pinecone.hydra.registry.source;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.registry.entity.NodeAttribute;

public interface RegistryCommonDataManipulator {
    void insert(NodeAttribute nodeAttribute);

    void remove(GUID guid);

    NodeAttribute getNodeCommonData(GUID guid);

    void update(NodeAttribute nodeAttribute);
}
