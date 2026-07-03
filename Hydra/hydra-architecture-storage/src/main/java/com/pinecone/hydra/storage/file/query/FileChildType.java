package com.pinecone.hydra.storage.file.query;

import com.pinecone.hydra.storage.file.entity.GenericFileNode;
import com.pinecone.hydra.storage.file.entity.GenericFolder;
import com.pinecone.hydra.storage.file.entity.GenericSymbolic;

public enum FileChildType {
    FOLDER( GenericFolder.class.getName() ),
    FILE( GenericFileNode.class.getName() ),
    REPARSE( GenericSymbolic.class.getName() );

    private final String typeName;

    FileChildType( String typeName ) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return this.typeName;
    }
}
