package com.pinecone.hydra.storage.file.entity;

public interface InternalSymbolic extends ReparseSemanticNode {
    void create();

    void remove();
}
