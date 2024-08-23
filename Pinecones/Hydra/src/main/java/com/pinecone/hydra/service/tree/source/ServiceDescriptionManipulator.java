package com.pinecone.hydra.service.tree.source;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.tree.GenericServiceDescription;

public interface ServiceDescriptionManipulator {
    //ServiceDescription的CRUD
    void saveServiceDescription(GenericServiceDescription UUID);
    void deleteServiceDescription(GUID UUID);
    void updateServiceDescription(GenericServiceDescription serviceDescription);
    GenericServiceDescription selectServiceDescription(GUID UUID);
}
