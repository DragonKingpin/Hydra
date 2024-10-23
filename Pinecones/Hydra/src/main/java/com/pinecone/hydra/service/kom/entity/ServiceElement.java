package com.pinecone.hydra.service.kom.entity;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.kom.ServiceFamilyNode;
import com.pinecone.hydra.service.kom.meta.GenericServiceNodeMeta;
import com.pinecone.hydra.unit.udtt.GUIDDistributedTrieNode;

public interface ServiceElement extends ServiceTreeNode, ServiceFamilyNode {
    long getEnumId();
    void setEnumId(long id);

    @Override
    default ServiceElement evinceTreeNode() {
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

}