package com.pinecone.hydra.service.tree.nodes;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.tree.GenericClassificationRules;
import com.pinecone.hydra.service.tree.GenericNodeCommonData;
import com.pinecone.hydra.unit.udtt.GUIDDistributedTrieNode;

public interface ClassificationNode extends ServiceTreeNode {
    long getEnumId();

    @Override
    default ClassificationNode evinceTreeNode() {
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

    GenericNodeCommonData getNodeCommonData();
    void setNodeCommonData(GenericNodeCommonData nodeCommonData);

}
