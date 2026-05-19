package com.pinecone.hydra.storage.file.entity;

import com.pinecone.hydra.storage.file.source.ExternalSymbolicManipulator;

public interface ExternalSymbolic extends ReparseSemanticNode {
    void create();

    void remove();

    void apply( ExternalSymbolicManipulator externalSymbolicManipulator );
}
