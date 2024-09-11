package com.pinecone.hydra.scenario.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.config.distribute.entity.GenericProperties;
import com.pinecone.hydra.config.distribute.entity.Properties;
import com.pinecone.hydra.scenario.entity.NamespaceNode;

import java.util.List;

public interface NamespaceNodeManipulator extends Pinenut {
    void insert(NamespaceNode namespaceNode);

    void remove(GUID guid);

    NamespaceNode getNamespaceNode(GUID guid);

    void update(NamespaceNode namespaceNode);
}
