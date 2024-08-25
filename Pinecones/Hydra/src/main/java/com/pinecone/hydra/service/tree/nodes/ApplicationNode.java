package com.pinecone.hydra.service.tree.nodes;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.tree.meta.ApplicationNodeMeta;
import com.pinecone.hydra.unit.udsn.DistributedTreeNode;

public interface ApplicationNode extends ServiceTreeNode {
    long getEnumId();
    void setEnumId(long id);

    GUID getGuid();
    void setGuid(GUID guid);

    String getName();
    void setName(String name);

    DistributedTreeNode getDistributedTreeNode();
    void setDistributedTreeNode(DistributedTreeNode distributedTreeNode);

    ApplicationNodeMeta getApplicationNodeMeta();
    void setApplicationNodeMeta(ApplicationNodeMeta applicationNodeMeta);
}
