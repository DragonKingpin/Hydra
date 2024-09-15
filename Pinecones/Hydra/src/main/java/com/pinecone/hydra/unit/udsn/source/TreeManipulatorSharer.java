package com.pinecone.hydra.unit.udsn.source;

public interface TreeManipulatorSharer {
    ScopeOwnerManipulator   getScopeOwnerManipulator();

    ScopeTreeManipulator    getScopeTreeManipulator();

    ScopePathManipulator    getScopePathManipulator();
}
