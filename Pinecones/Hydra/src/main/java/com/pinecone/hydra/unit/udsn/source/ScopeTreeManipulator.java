package com.pinecone.hydra.unit.udsn.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.udsn.GUIDDistributedScopeNode;

import java.util.List;

public interface ScopeTreeManipulator extends Pinenut {
    void insert(GUIDDistributedScopeNode node);

    GUIDDistributedScopeNode getNode(GUID guid);

    void remove(GUID guid);

    String getPath(GUID guid);

    void updatePath( GUID guid, String path);

    GUID parsePath(String path);

    void insertNodeToParent(GUID nodeGUID,GUID parentGUID);

    List<GUIDDistributedScopeNode> getChild(GUID guid);

    void removePath(GUID guid);

    void putNode(GUID guid,GUIDDistributedScopeNode distributedTreeNode);

    long size();

    List<GUID> getParentNodes(GUID guid);

    void removeInheritance(GUID childNode, GUID parentGUID);

}
