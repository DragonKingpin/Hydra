package com.pinecone.hydra.service.tree.nodes;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.tree.ClassificationRules;
import com.pinecone.hydra.service.tree.GenericClassificationRules;
import com.pinecone.hydra.service.tree.GenericNodeCommonData;
import com.pinecone.hydra.service.tree.NodeCommonData;
import com.pinecone.hydra.unit.udsn.DistributedTreeNode;
import com.pinecone.hydra.unit.udsn.GUIDDistributedScopeNode;

public interface ClassificationNode extends ServiceTreeNode {
    long getEnumId();
    void setEnumId(long id);

    GUID getGuid();
    void setGuid(GUID guid);

    String getName();
    void setName(String name);

    GUID getRulesGUID();
    void setRulesGUID(GUID rulesGUID);

    GenericClassificationRules getClassificationRules();
    void setClassificationRules(GenericClassificationRules classificationRules);

    GUIDDistributedScopeNode getDistributedTreeNode();
    void setDistributedTreeNode(GUIDDistributedScopeNode distributedTreeNode);

    GenericNodeCommonData getNodeCommonData();
    void setNodeCommonData(GenericNodeCommonData nodeCommonData);

}
