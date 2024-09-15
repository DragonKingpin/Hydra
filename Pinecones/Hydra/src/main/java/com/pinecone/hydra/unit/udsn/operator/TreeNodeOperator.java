package com.pinecone.hydra.unit.udsn.operator;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.udsn.entity.TreeNode;

public interface TreeNodeOperator extends Pinenut {
    GUID insert(TreeNode treeNode);

    void remove(GUID guid);

    TreeNode get(GUID guid);

    TreeNode getWithoutInheritance(GUID guid);
}
