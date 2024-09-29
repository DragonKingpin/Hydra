package com.pinecone.hydra.registry.source;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.registry.entity.NamespaceNode;
import com.pinecone.hydra.system.ko.dao.GUIDNameManipulator;

import java.util.List;

public interface RegistryNSNodeManipulator extends GUIDNameManipulator {
    void insert(NamespaceNode namespaceNode);

    void remove(GUID guid);

    NamespaceNode getNamespaceMeta(GUID guid);

    void update(NamespaceNode namespaceNode);

    List<GUID> getGuidsByName(String name);

    List<GUID> getAll();
}
