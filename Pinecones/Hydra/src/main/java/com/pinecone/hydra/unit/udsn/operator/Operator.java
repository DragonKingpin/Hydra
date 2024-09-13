package com.pinecone.hydra.unit.udsn.operator;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.config.distribute.entity.TreeNode;
import com.pinecone.hydra.service.tree.nodes.ServiceTreeNode;

public interface Operator extends Pinenut {
    GUID insert(ServiceTreeNode nodeWideData);

    void remove(GUID guid);

    ServiceTreeNode get(GUID guid);

    void update(ServiceTreeNode nodeWideData);

    TreeNode getWithoutInheritance(GUID guid);
}
