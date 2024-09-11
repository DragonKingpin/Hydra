package com.pinecone.hydra.config.distribute.source;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.config.distribute.entity.NamespaceNodeMeta;
import com.pinecone.hydra.config.distribute.entity.NodeCommonData;

public interface NodeCommonDataManipulator {
    void insert(NodeCommonData nodeCommonData);

    void remove(GUID guid);

    NodeCommonData getNodeCommonData(GUID guid);

    void update(NodeCommonData nodeCommonData);
}
