package com.pinecone.hydra.service.tree.nodes;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.tree.ClassificationRules;
import com.pinecone.hydra.unit.udsn.DistributedTreeNode;

public interface ClassificationNode extends ServiceTreeNode {
    long getEnumId();
    void setEnumId(long id);

    GUID getGuid();
    void setGuid(GUID guid);

    String getName();
    void setName(String name);

    GUID getRulesGUID();
    void setRulesGUID(GUID rulesGUID);

    ClassificationRules getClassificationRules();
    void setClassificationRules(ClassificationRules classificationRules);

    DistributedTreeNode getDistributedTreeNode();
    void setDistributedTreeNode(DistributedTreeNode distributedTreeNode);

}
