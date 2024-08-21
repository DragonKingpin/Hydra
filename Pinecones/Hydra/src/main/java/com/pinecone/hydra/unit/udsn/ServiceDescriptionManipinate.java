package com.pinecone.hydra.unit.udsn;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.GenericServiceDescription;

public interface ServiceDescriptionManipinate {
    //ServiceDescription的CRUD
    void saveServiceDescription(GenericServiceDescription UUID);
    void deleteServiceDescription(GUID UUID);
    void updateServiceDescription(GenericServiceDescription serviceDescription);
    GenericServiceDescription selectServiceDescription(GUID UUID);
}
