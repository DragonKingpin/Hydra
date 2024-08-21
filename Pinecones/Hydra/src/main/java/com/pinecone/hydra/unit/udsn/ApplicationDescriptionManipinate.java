package com.pinecone.hydra.unit.udsn;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.GenericApplicationDescription;

public interface ApplicationDescriptionManipinate {
    //ApplicationDescription的CRUD
    void saveApplicationDescription(GenericApplicationDescription applicationDescription);
    void deleteApplicationDescription(GUID UUID);
    GenericApplicationDescription selectApplicationDescription(GUID UUID);
    void updateApplicationDescription(GenericApplicationDescription applicationDescription);
}
