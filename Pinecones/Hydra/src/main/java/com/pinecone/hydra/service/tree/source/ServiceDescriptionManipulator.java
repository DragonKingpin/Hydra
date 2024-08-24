package com.pinecone.hydra.service.tree.source;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.tree.GenericServiceNodeMetadata;

public interface ServiceDescriptionManipulator {
    //ServiceDescription的CRUD
    void insertServiceDescription(GenericServiceNodeMetadata genericServiceDescription);
    void deleteServiceDescription(GUID guid);
    void updateServiceDescription(GenericServiceNodeMetadata serviceDescription);
    GenericServiceNodeMetadata getServiceDescription(GUID guid);
}
