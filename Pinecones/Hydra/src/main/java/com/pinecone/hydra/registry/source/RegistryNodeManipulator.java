package com.pinecone.hydra.registry.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.registry.entity.ConfNode;

import java.util.List;

public interface RegistryNodeManipulator extends Pinenut {
    void insert(ConfNode confNode);

    void remove(GUID guid);

    ConfNode getConfigurationNode(GUID guid);

    void update(ConfNode confNode);

    List<GUID> getNodeByName(String name);
}
