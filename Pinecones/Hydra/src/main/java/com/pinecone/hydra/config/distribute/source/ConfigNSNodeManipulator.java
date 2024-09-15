package com.pinecone.hydra.config.distribute.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.config.distribute.entity.NamespaceNode;
import org.apache.catalina.LifecycleState;

import java.util.List;

public interface ConfigNSNodeManipulator extends Pinenut {
    void insert(NamespaceNode namespaceNode);

    void remove(GUID guid);

    NamespaceNode getNamespaceMeta(GUID guid);

    void update(NamespaceNode namespaceNode);

    List<GUID> getNodeByName(String name);
}
