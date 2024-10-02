package com.pinecone.hydra.unit.udtt;

import com.pinecone.framework.system.prototype.PineUnit;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.uoi.UOI;

import java.util.List;

public interface DistributedTrieTree extends PineUnit {

    void insert( DistributedTreeNode distributedConfTreeNode );

    String getPath( GUID guid );

    void insertOwnedNode( GUID nodeGUID, GUID parentGUID );

    GUIDDistributedTrieNode getNode(GUID guid );

    void purge( GUID guid );

    void put( GUID guid, GUIDDistributedTrieNode distributedTreeNode );

    boolean containsKey( GUID key );

    GUID queryGUIDByPath( String path );

    List<GUIDDistributedTrieNode> getChildren( GUID guid );

    List<GUID> getParentGuids( GUID guid );

    void removeInheritance( GUID childGuid,GUID parentGuid );

    void removePath(GUID guid);

    GUID getOwner(GUID guid);

    void setOwner(GUID sourceGuid,GUID targetGuid);

    List<GUID> getSubordinates(GUID guid);

    void insertPath(GUID guid,String path);



    List<GUID > listRoot();

    void setReparse( GUID sourceGuid, GUID targetGuid );

    void moveTo( GUID sourceGuid, GUID destinationGuid );
}
