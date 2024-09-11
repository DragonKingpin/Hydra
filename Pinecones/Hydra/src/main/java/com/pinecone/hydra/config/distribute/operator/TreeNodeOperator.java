package com.pinecone.hydra.config.distribute.operator;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.config.distribute.entity.TreeNode;

public interface TreeNodeOperator extends Pinenut {
    GUID insert(TreeNode treeNode);

    void remove(GUID guid);

    TreeNode get(GUID guid);

    TreeNode getWithoutInheritance(GUID guid);
}
