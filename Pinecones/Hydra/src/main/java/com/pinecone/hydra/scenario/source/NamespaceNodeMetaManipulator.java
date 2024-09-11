package com.pinecone.hydra.scenario.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.scenario.entity.NamespaceNode;
import com.pinecone.hydra.scenario.entity.NamespaceNodeMeta;

public interface NamespaceNodeMetaManipulator extends Pinenut {
    void insert(NamespaceNodeMeta namespaceNodeMeta);

    void remove(GUID guid);

    NamespaceNodeMeta getNamespaceNodeMeta(GUID guid);

    void update(NamespaceNodeMeta namespaceNodeMeta);
}
