package com.pinecone.hydra.config.distribute.tree;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.udsn.DistributedScopeTree;
import com.pinecone.hydra.unit.udsn.DistributedTreeNode;

public interface DistributedConfTree extends Pinenut {

    void insert(DistributedTreeNode distributedConfTreeNode);

    DistributedTreeNode getNode(GUID guid);

    DistributedTreeNode getParentNode(GUID guid);

    void remove(GUID guid);

    GUID getOwner(GUID guid);

    void insertOwner(GUID subordinateGuid,GUID ownerGuid);

    void updateOwner(GUID subordinateGuid,GUID ownerGuid);

    void removeOwner(GUID subordinateGuid,GUID ownerGuid);

    void removeOwnerBySubordinateGuid(GUID subordinateGuid);
}
