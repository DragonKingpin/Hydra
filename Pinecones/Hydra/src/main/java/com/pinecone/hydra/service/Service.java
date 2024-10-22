package com.pinecone.hydra.service;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.kom.meta.GenericServiceNodeMeta;
import com.pinecone.hydra.service.kom.nodes.ServiceNode;
import com.pinecone.hydra.unit.udtt.GUIDDistributedTrieNode;

public interface Service extends CommonData {
    Object getProcessImageObject();
    long getEnumId();

    GUID getGuid();

    String getName();

    GUIDDistributedTrieNode getDistributedTreeNode();

    GenericServiceNodeMeta getServiceNodeMetadata();
}
