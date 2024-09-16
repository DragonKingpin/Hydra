package com.pinecone.hydra.registry.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.registry.entity.GenericProperties;
import com.pinecone.hydra.registry.entity.Properties;

import java.util.List;

public interface RegistryPropertiesManipulator extends Pinenut {
    void insert(Properties properties);

    void remove(GUID guid);

    List<GenericProperties> getProperties(GUID guid);

    void update(Properties properties);
}
