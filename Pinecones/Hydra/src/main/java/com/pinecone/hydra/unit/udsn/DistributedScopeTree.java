package com.pinecone.hydra.unit.udsn;

import com.pinecone.framework.system.prototype.PineUnit;
import com.pinecone.framework.util.id.GUID;

import java.util.List;

public interface DistributedScopeTree extends PineUnit {
    DistributedTreeNode getParentNode(GUID guid);

    void insert(DistributedTreeNode distributedConfTreeNode);

    String getPath( GUID guid );

    void insertNodeToParent( GUID nodeGUID, GUID parentGUID );

    GUIDDistributedScopeNode getNode( GUID guid );

    void remove( GUID guid );

    void put( GUID guid, GUIDDistributedScopeNode distributedTreeNode );

    boolean isEmpty();

    long size();

    boolean containsKey( GUID key );

    GUID parsePath(String path);

    List<GUIDDistributedScopeNode> getChildNode(GUID guid);

    List<GUID> getParentNodes(GUID guid);

    void removeInheritance(GUID childGuid,GUID parentGuid);

    void removePath(GUID guid);

    GUID getOwner(GUID guid);

    List<GUID> getSubordinates(GUID guid);
}
