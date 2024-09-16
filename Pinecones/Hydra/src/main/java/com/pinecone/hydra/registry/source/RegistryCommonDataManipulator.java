package com.pinecone.hydra.registry.source;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.registry.entity.NodeCommonData;

public interface RegistryCommonDataManipulator {
    void insert(NodeCommonData nodeCommonData);

    void remove(GUID guid);

    NodeCommonData getNodeCommonData(GUID guid);

    void update(NodeCommonData nodeCommonData);
}
