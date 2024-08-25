package com.pinecone.hydra.service.tree.operator;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.tree.nodes.ServiceTreeNode;

public interface MetaNodeOperator extends Pinenut {
    GUID insert(ServiceTreeNode nodeWideData);

    void remove(GUID guid);

    ServiceTreeNode get(GUID guid);

    void update(ServiceTreeNode nodeWideData);
}
