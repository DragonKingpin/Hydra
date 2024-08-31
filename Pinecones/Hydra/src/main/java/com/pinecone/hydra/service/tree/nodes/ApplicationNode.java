package com.pinecone.hydra.service.tree.nodes;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.tree.GenericNodeCommonData;
import com.pinecone.hydra.service.tree.NodeCommonData;
import com.pinecone.hydra.service.tree.meta.ApplicationNodeMeta;
import com.pinecone.hydra.service.tree.meta.GenericApplicationNodeMeta;
import com.pinecone.hydra.unit.udsn.DistributedTreeNode;
import com.pinecone.hydra.unit.udsn.GUIDDistributedScopeNode;

public interface ApplicationNode extends ServiceTreeNode {
    long getEnumId();
    void setEnumId(long id);

    GUID getGuid();
    void setGuid(GUID guid);

    String getName();
    void setName(String name);

    GUIDDistributedScopeNode getDistributedTreeNode();
    void setDistributedTreeNode(GUIDDistributedScopeNode distributedTreeNode);

    GenericApplicationNodeMeta getApplicationNodeMeta();
    void setApplicationNodeMeta(GenericApplicationNodeMeta applicationNodeMeta);

    GenericNodeCommonData getNodeCommonData();
    void setNodeCommonData(GenericNodeCommonData nodeCommonData);
}
