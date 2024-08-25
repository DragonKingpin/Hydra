package com.pinecone.hydra.service.tree.source;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.tree.GenericServiceNodeMeta;

public interface ServiceMetaManipulator {
    //ServiceDescription的CRUD
    void insert(GenericServiceNodeMeta genericServiceDescription);
    void delete(GUID guid);
    void update(GenericServiceNodeMeta serviceDescription);
    GenericServiceNodeMeta getServiceMeta(GUID guid);
}
