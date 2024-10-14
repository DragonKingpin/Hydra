package com.pinecone.hydra.service.kom.nodes;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.kom.GenericNodeCommonData;
import com.pinecone.hydra.service.kom.meta.GenericApplicationNodeMeta;
import com.pinecone.hydra.unit.udtt.GUIDDistributedTrieNode;

public interface ApplicationNode extends ServiceTreeNode {
    long getEnumId();
    void setEnumId(long id);

    @Override
    default ApplicationNode evinceTreeNode() {
        return this;
    }

    GUID getGuid();
    void setGuid(GUID guid);

    String getName();
    void setName(String name);

    GUIDDistributedTrieNode getDistributedTreeNode();
    void setDistributedTreeNode(GUIDDistributedTrieNode distributedTreeNode);

    GenericApplicationNodeMeta getApplicationNodeMeta();
    void setApplicationNodeMeta(GenericApplicationNodeMeta applicationNodeMeta);

    GenericNodeCommonData getAttributes();
    void setNodeCommonData(GenericNodeCommonData nodeAttributes);
}
