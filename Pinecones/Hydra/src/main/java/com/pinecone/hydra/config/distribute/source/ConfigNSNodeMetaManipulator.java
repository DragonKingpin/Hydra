package com.pinecone.hydra.config.distribute.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.config.distribute.entity.ConfNode;
import com.pinecone.hydra.config.distribute.entity.NamespaceNodeMeta;

public interface ConfigNSNodeMetaManipulator extends Pinenut {
    void insert(NamespaceNodeMeta namespaceNodeMeta);

    void remove(GUID guid);

    NamespaceNodeMeta getNamespaceNodeMeta(GUID guid);

    void update(NamespaceNodeMeta namespaceNodeMeta);
}
