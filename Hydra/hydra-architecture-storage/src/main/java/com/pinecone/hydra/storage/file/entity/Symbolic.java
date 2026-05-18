package com.pinecone.hydra.storage.file.entity;

public interface Symbolic extends ReparseSemanticNode {
    void create();

    void remove();

    @Override
    default Symbolic evinceSymbolic() {
        return this;
    }
}
