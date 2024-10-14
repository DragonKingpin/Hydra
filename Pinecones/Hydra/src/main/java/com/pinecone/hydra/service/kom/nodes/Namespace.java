package com.pinecone.hydra.service.kom.nodes;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.kom.GenericClassificationRules;
import com.pinecone.hydra.service.kom.GenericNodeCommonData;
import com.pinecone.hydra.unit.udtt.GUIDDistributedTrieNode;

public interface Namespace extends ServiceTreeNode {
    long getEnumId();

    @Override
    default Namespace evinceTreeNode() {
        return this;
    }

    void setEnumId(long id);

    GUID getGuid();
    void setGuid(GUID guid);

    String getName();
    void setName(String name);

    GUID getRulesGUID();
    void setRulesGUID(GUID rulesGUID);

    GenericClassificationRules getClassificationRules();
    void setClassificationRules(GenericClassificationRules classificationRules);

    GUIDDistributedTrieNode getDistributedTreeNode();
    void setDistributedTreeNode(GUIDDistributedTrieNode distributedTreeNode);

    GenericNodeCommonData getAttributes();
    void setNodeCommonData(GenericNodeCommonData nodeAttributes);

}
