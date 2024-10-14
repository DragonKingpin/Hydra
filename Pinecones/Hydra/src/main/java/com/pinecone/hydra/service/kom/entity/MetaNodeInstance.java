package com.pinecone.hydra.service.kom.entity;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public interface MetaNodeInstance extends Pinenut {
    MetaNodeWideEntity get(GUID guid);

    void put(GUID guid, MetaNodeWideEntity metaNodeWideEntity);

    void remove(GUID guid);
}
