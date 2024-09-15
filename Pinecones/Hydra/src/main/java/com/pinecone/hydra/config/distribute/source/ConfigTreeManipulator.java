package com.pinecone.hydra.config.distribute.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.udsn.DistributedScopeTree;
import com.pinecone.hydra.unit.udsn.DistributedTreeNode;
import com.pinecone.hydra.unit.udsn.GUIDDistributedScopeNode;

import java.util.List;

public interface ConfigTreeManipulator extends Pinenut {
    void insert (DistributedTreeNode distributedConfTreeNode);
    GUIDDistributedScopeNode getNode(GUID guid);

    void remove(GUID guid);

    void removeInheritance(GUID childGuid, GUID parentGuid);

    List<GUID> getParentNodes(GUID guid);

    List<GUIDDistributedScopeNode> getChild(GUID guid);
}
