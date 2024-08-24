package com.pinecone.hydra.service.tree.source;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.tree.GenericApplicationNodeMetadata;

public interface ApplicationDescriptionManipulator {
    //ApplicationDescription的CRUD
    void insertApplicationDescription(GenericApplicationNodeMetadata applicationDescription);

    void deleteApplicationDescription(GUID guid);

    GenericApplicationNodeMetadata getApplicationDescription(GUID guid);

    void updateApplicationDescription(GenericApplicationNodeMetadata applicationDescription);
}
