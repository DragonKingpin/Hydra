package com.pinecone.hydra.service.tree.source;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.tree.GenericApplicationDescription;

public interface ApplicationDescriptionManipulator {
    //ApplicationDescription的CRUD
    void saveApplicationDescription(GenericApplicationDescription applicationDescription);

    void deleteApplicationDescription(GUID UUID);

    GenericApplicationDescription selectApplicationDescription(GUID UUID);

    void updateApplicationDescription(GenericApplicationDescription applicationDescription);
}
