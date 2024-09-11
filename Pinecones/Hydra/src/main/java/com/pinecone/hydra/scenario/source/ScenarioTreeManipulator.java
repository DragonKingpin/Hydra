package com.pinecone.hydra.scenario.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.udsn.DistributedTreeNode;
import com.pinecone.hydra.unit.udsn.GUIDDistributedScopeNode;

import java.util.List;

public interface ScenarioTreeManipulator extends Pinenut {
    void insert (DistributedTreeNode distributedConfTreeNode);
    GUIDDistributedScopeNode getNode(GUID guid);

    void remove(GUID guid);

    List<GUID> getParent(GUID guid);

    void removeInheritance(GUID childGuid, GUID parentGuid);

    List<GUID> getParentNodes(GUID guid);

    List<GUIDDistributedScopeNode> getChild(GUID guid);
}
