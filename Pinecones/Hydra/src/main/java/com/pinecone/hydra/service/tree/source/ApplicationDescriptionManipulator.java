package com.pinecone.hydra.service.tree.source;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.tree.GenericApplicationNodeMeta;

public interface ApplicationDescriptionManipulator {
    //ApplicationDescription的CRUD
    void insertApplicationDescription(GenericApplicationNodeMeta applicationDescription);

    void deleteApplicationDescription(GUID guid);

    GenericApplicationNodeMeta getApplicationDescription(GUID guid);

    void updateApplicationDescription(GenericApplicationNodeMeta applicationDescription);
}
