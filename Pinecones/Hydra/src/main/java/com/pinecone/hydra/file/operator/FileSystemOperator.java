package com.pinecone.hydra.file.operator;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.file.entity.FileTreeNode;
import com.pinecone.hydra.unit.udtt.operator.TreeNodeOperator;

public interface FileSystemOperator extends TreeNodeOperator {
    @Override
    FileTreeNode get(GUID guid );

    FileTreeNode get( GUID guid, int depth );

    @Override
    FileTreeNode getSelf( GUID guid );
}
