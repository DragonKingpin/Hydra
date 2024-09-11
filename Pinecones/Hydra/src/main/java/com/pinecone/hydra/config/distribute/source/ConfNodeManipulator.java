package com.pinecone.hydra.config.distribute.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.config.distribute.entity.ConfNode;

import java.util.List;

public interface ConfNodeManipulator extends Pinenut {
    void insert(ConfNode confNode);

    void remove(GUID guid);

    ConfNode getConfigurationNode(GUID guid);

    void update(ConfNode confNode);

    List<GUID> getNodeByName(String name);
}
