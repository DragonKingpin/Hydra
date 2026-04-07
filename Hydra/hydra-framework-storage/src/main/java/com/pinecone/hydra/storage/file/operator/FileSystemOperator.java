package com.pinecone.hydra.storage.file.operator;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.file.entity.FileTreeNode;
import com.pinecone.hydra.unit.imperium.operator.TreeNodeOperator;

public interface FileSystemOperator extends TreeNodeOperator {
    @Override
    FileTreeNode get(GUID guid );

    FileTreeNode get( GUID guid, int depth );

    void rename( GUID fileGuid, String newName );

    @Override
    FileTreeNode getAsRootDepth( GUID guid );
}
