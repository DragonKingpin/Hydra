package com.pinecone.hydra.service.tree.source;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.tree.meta.GenericApplicationNodeMeta;

public interface ApplicationMetaManipulator {
    //ApplicationDescription的CRUD
    void insert(GenericApplicationNodeMeta applicationMeta);

    void remove(GUID guid);

    GenericApplicationNodeMeta getApplicationMeta(GUID guid);

    void update(GenericApplicationNodeMeta applicationMeta);
}
