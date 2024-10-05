package com.pinecone.hydra.registry.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.registry.entity.NamespaceMeta;

public interface RegistryNSNodeMetaManipulator extends Pinenut {
    void insert( NamespaceMeta namespaceMeta);

    void remove( GUID guid );

    NamespaceMeta getNamespaceNodeMeta(GUID guid );

    void update( NamespaceMeta namespaceMeta);
}
