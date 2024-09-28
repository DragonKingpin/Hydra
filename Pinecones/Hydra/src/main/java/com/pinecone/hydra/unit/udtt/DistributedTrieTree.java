package com.pinecone.hydra.unit.udtt;

import com.pinecone.framework.system.prototype.PineUnit;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.uoi.UOI;

import java.util.List;

public interface DistributedTrieTree extends PineUnit {

    void insert(DistributedTreeNode distributedConfTreeNode);

    String getPath( GUID guid );

    void insertNodeToParent( GUID nodeGUID, GUID parentGUID );

    GUIDDistributedTrieNode getNode(GUID guid );

    void remove( GUID guid );

    void put( GUID guid, GUIDDistributedTrieNode distributedTreeNode );

    boolean isEmpty();

    long size();

    boolean containsKey( GUID key );

    GUID queryGUIDByPath( String path );

    List<GUIDDistributedTrieNode> getChildNode(GUID guid);

    List<GUID> getParentNodes(GUID guid);

    void removeInheritance(GUID childGuid,GUID parentGuid);

    void removePath(GUID guid);

    GUID getOwner(GUID guid);

    List<GUID> getSubordinates(GUID guid);

    void insertPath(GUID guid,String path);

    void updateType(UOI type,GUID guid);
}
