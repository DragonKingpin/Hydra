package com.pinecone.hydra.unit.udtt.operator;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.udtt.entity.TreeNode;

public interface TreeNodeOperator extends Pinenut {
    GUID insert( TreeNode treeNode );

    void purge( GUID guid );

    TreeNode get( GUID guid );

    TreeNode getWithoutInheritance( GUID guid );

    void update( TreeNode treeNode );

    void updateName(GUID guid ,String name);
}
