package com.pinecone.hydra.service.tree.source;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.tree.meta.GenericServiceNodeMeta;

public interface ServiceMetaManipulator {
    //ServiceDescription的CRUD
    void insert(GenericServiceNodeMeta genericServiceDescription);
    void remove(GUID guid);
    void update(GenericServiceNodeMeta serviceDescription);
    GenericServiceNodeMeta getServiceMeta(GUID guid);
}
