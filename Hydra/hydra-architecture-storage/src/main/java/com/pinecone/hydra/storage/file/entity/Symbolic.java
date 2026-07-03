package com.pinecone.hydra.storage.file.entity;

public interface Symbolic extends InternalSymbolic {
    @Override
    default Symbolic evinceSymbolic() {
        return this;
    }
}
