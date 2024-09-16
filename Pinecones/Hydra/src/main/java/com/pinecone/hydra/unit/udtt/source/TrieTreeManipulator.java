package com.pinecone.hydra.unit.udtt.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.udtt.GUIDDistributedTrieNode;

import java.util.List;

public interface TrieTreeManipulator extends Pinenut {
    void insert(GUIDDistributedTrieNode node);

    GUIDDistributedTrieNode getNode(GUID guid);

    void remove(GUID guid);

    String getPath(GUID guid);

    void updatePath( GUID guid, String path);

    GUID parsePath(String path);

    void insertNodeToParent(GUID nodeGUID,GUID parentGUID);

    List<GUIDDistributedTrieNode> getChild(GUID guid);

    void removePath(GUID guid);

    void putNode(GUID guid, GUIDDistributedTrieNode distributedTreeNode);

    long size();

    List<GUID> getParentNodes(GUID guid);

    void removeInheritance(GUID childNode, GUID parentGUID);

}
