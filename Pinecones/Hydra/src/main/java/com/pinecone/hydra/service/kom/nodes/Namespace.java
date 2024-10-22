package com.pinecone.hydra.service.kom.nodes;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.kom.GenericNamespaceRules;
import com.pinecone.hydra.service.kom.ArchServiceFamilyNode;
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

    GenericNamespaceRules getClassificationRules();
    void setClassificationRules(GenericNamespaceRules classificationRules);

    GUIDDistributedTrieNode getDistributedTreeNode();
    void setDistributedTreeNode(GUIDDistributedTrieNode distributedTreeNode);

    ArchServiceFamilyNode getAttributes();
    void setNodeCommonData(ArchServiceFamilyNode nodeAttributes);

}
