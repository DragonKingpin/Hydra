package com.pinecone.hydra.service.tree.source;

import com.pinecone.framework.util.id.GUID;

public interface ServiceFamilyTreeManipulator {
    void insert(GUID childGUID,GUID parentGUID);

    void removeByChildGUID(GUID childGUID);

    void removeByParentGUID(GUID parentGUID);

    void remove(GUID childGUID,GUID parentGUID);

    GUID getParentByChildGUID(GUID childGUID);

    GUID getChildByParentGUID(GUID parentGUID);
}
