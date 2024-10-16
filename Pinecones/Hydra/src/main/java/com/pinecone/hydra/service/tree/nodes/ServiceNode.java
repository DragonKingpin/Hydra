package com.pinecone.hydra.service.tree.nodes;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.tree.GenericNodeCommonData;
import com.pinecone.hydra.service.tree.meta.GenericServiceNodeMeta;
import com.pinecone.hydra.unit.udtt.GUIDDistributedTrieNode;

public interface ServiceNode extends ServiceTreeNode {
    long getEnumId();
    void setEnumId(long id);

    @Override
    default ServiceNode evinceTreeNode() {
        return this;
    }

    GUID getGuid();
    void setGuid(GUID guid);

    String getName();
    void setName(String name);

    GUIDDistributedTrieNode getDistributedTreeNode();
    void setDistributedTreeNode(GUIDDistributedTrieNode distributedTreeNode);

    GenericServiceNodeMeta getServiceNodeMetadata();
    void setServiceNodeMetadata(GenericServiceNodeMeta serviceNodeMetadata);

    GenericNodeCommonData getAttributes();
    void setNodeCommonData(GenericNodeCommonData nodeAttributes);
}