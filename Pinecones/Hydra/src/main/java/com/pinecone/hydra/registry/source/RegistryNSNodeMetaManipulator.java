package com.pinecone.hydra.registry.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.registry.entity.NamespaceNodeMeta;

public interface RegistryNSNodeMetaManipulator extends Pinenut {
    void insert( NamespaceNodeMeta namespaceNodeMeta );

    void remove( GUID guid );

    NamespaceNodeMeta getNamespaceNodeMeta( GUID guid );

    void update( NamespaceNodeMeta namespaceNodeMeta );
}
