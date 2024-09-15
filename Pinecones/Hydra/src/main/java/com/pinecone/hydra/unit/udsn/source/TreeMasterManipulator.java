package com.pinecone.hydra.unit.udsn.source;

public interface TreeMasterManipulator {
    ScopeOwnerManipulator   getScopeOwnerManipulator();

    ScopeTreeManipulator    getScopeTreeManipulator();

    ScopePathManipulator    getScopePathManipulator();
}
