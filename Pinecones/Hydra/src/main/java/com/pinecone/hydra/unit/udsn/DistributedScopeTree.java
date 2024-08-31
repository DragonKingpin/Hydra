package com.pinecone.hydra.unit.udsn;

import com.pinecone.framework.system.prototype.PineUnit;
import com.pinecone.framework.util.id.GUID;

public interface DistributedScopeTree extends PineUnit {
    String getPath( GUID guid );

    void insertNodeToParent( GUID nodeGUID, GUID parentGUID );

    GUIDDistributedScopeNode getNode( GUID guid );

    void remove( GUID guid );

    void put( GUID guid, GUIDDistributedScopeNode distributedTreeNode );

    boolean isEmpty();

    long size();

    boolean containsKey( GUID key );
}
