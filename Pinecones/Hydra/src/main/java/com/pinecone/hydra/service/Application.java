package com.pinecone.hydra.service;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.kom.meta.GenericApplicationNodeMeta;
import com.pinecone.hydra.service.kom.nodes.ApplicationNode;
import com.pinecone.hydra.unit.udtt.GUIDDistributedTrieNode;

public interface Application extends CommonData {
    long getEnumId();

    GUID getGuid();

    String getName();

    GUIDDistributedTrieNode getDistributedTreeNode();

    GenericApplicationNodeMeta getApplicationNodeMeta();
}
