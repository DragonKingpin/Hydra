package com.pinecone.hydra.registry.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.registry.entity.NamespaceNode;

import java.util.List;

public interface RegistryNSNodeManipulator extends Pinenut {
    void insert(NamespaceNode namespaceNode);

    void remove(GUID guid);

    NamespaceNode getNamespaceMeta(GUID guid);

    void update(NamespaceNode namespaceNode);

    List<GUID> getNodeByName(String name);

    List<GUID> getAll();
}
