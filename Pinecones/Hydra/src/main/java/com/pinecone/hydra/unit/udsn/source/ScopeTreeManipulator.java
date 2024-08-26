package com.pinecone.hydra.unit.udsn.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.udsn.DistributedTreeNode;
import com.pinecone.hydra.unit.udsn.GUIDDistributedScopeNode;

import java.util.List;

public interface ScopeTreeManipulator extends Pinenut {
    void saveNode(GUIDDistributedScopeNode node);

    GUIDDistributedScopeNode getNode(GUID guid);

    void removeNode(GUID guid);

    void updateParentGUID(GUIDDistributedScopeNode node);

    String selectPath(GUID guid);

    void savePath( String path, GUID guid);

    void updatePath( GUID guid, String path);

    List<GUIDDistributedScopeNode> selectChildNode(GUID guid);

    GUID parsePath(String path);

    void addNodeToParent(GUID nodeGUID,GUID parentGUID);

    //方法放在这是否有问题

    String getClassifNodeClassif(GUID classifNodeGUID);

    List<GUIDDistributedScopeNode> getChildNode(GUID guid);

    void removePath(GUID guid);

    void putNode(GUID guid,GUIDDistributedScopeNode distributedTreeNode);
    long size();
}
