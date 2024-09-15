package com.pinecone.hydra.scenario.entity;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.udsn.entity.TreeNode;

public interface NamespaceNode extends TreeNode {
    int getEnumId();
    void setEnumId(int id);

    GUID getGuid();
    void setGuid(GUID guid);

    String getName();
    void setName(String name);

    GenericNamespaceNodeMeta getNamespaceNodeMeta();
    void setNamespaceNodeMeta(GenericNamespaceNodeMeta namespaceNodeMeta);

    GenericScenarioCommonData getScenarioCommonData();
    void setScenarioCommonData(GenericScenarioCommonData scenarioCommonData);
}
