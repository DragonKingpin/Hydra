package com.pinecone.hydra.config.distribute.source;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.config.distribute.entity.ConfNode;
import com.pinecone.hydra.config.distribute.entity.ConfNodeMeta;

public interface ConfigNodeMetaManipulator {
    void insert(ConfNodeMeta confNodeMeta);

    void remove(GUID guid);

    ConfNodeMeta getConfNodeMeta(GUID guid);

    void update(ConfNodeMeta confNodeMeta);
}
