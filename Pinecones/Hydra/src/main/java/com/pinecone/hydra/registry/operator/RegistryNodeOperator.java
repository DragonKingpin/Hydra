package com.pinecone.hydra.registry.operator;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.registry.entity.RegistryTreeNode;
import com.pinecone.hydra.unit.udtt.entity.TreeNode;
import com.pinecone.hydra.unit.udtt.operator.TreeNodeOperator;

public interface RegistryNodeOperator extends TreeNodeOperator {
    @Override
    RegistryTreeNode get( GUID guid );

    @Override
    RegistryTreeNode getWithoutInheritance( GUID guid );
}
