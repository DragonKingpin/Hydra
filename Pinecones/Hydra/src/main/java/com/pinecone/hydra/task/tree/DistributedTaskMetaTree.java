package com.pinecone.hydra.task.tree;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.udtt.entity.TreeNode;

public interface DistributedTaskMetaTree extends Pinenut {
    String getPath(GUID guid);

    GUID insert(TreeNode treeNode);

    TreeNode get(GUID guid);

    TreeNode parsePath(String path);

    void remove(GUID guid);

    TreeNode getSelf(GUID guid);
}
