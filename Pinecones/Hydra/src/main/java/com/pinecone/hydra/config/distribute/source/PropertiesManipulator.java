package com.pinecone.hydra.config.distribute.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.config.distribute.entity.GenericProperties;
import com.pinecone.hydra.config.distribute.entity.Properties;

import java.util.List;

public interface PropertiesManipulator extends Pinenut {
    void insert(Properties properties);

    void remove(GUID guid);

    List<GenericProperties> getProperties(GUID guid);

    void update(Properties properties);
}
