package com.pinecone.hydra.service.tree.source;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.tree.GenericServiceNodeMeta;

public interface ServiceDescriptionManipulator {
    //ServiceDescription的CRUD
    void insertServiceDescription(GenericServiceNodeMeta genericServiceDescription);
    void deleteServiceDescription(GUID guid);
    void updateServiceDescription(GenericServiceNodeMeta serviceDescription);
    GenericServiceNodeMeta getServiceDescription(GUID guid);
}
