package com.pinecone.hydra.unit.udtt.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.uoi.UOI;
import com.pinecone.hydra.unit.udtt.GUIDDistributedTrieNode;
import com.pinecone.hydra.unit.udtt.LinkedType;

import java.util.List;

public interface TrieTreeManipulator extends Pinenut {
    void insert( TireOwnerManipulator ownerManipulator, GUIDDistributedTrieNode node );

    /** With detail meta data node information. */
    GUIDDistributedTrieNode getNode( GUID guid );

    /** Only with tree node index information. */
    GUIDDistributedTrieNode getTreeNodeOnly( GUID guid, GUID parentGuid );

    long countNode( GUID guid, GUID parentGuid );




    /** Purge */
    void purge         ( GUID guid );

    void removeTreeNode( GUID guid );

    void removeTreeNodeByParentGuid( GUID parentGuid );

    void removeTreeNodeYoke( GUID guid, GUID parentGuid );

    void removeTreeNodeWithLinkedType( GUID guid, LinkedType linkedType );

    void removeNodeMeta( GUID guid );

    default void removeOwnedTreeNode ( GUID guid ) {
        this.removeTreeNodeWithLinkedType( guid, LinkedType.Owned );
    }

    default void removeHardLinkedTreeNode ( GUID guid ) {
        this.removeTreeNodeWithLinkedType( guid, LinkedType.Hard );
    }




    List<GUIDDistributedTrieNode > getChildren( GUID guid );

    List<GUID > getParentGuids( GUID guid );

    void removeInheritance( GUID childNode, GUID parentGUID );

    void updateType       ( UOI type, GUID guid );

    List<GUID > listRoot();



    // TODO
    void insertNode( GUID guid, GUIDDistributedTrieNode distributedTreeNode );

    // TODO
    void updateNode( GUID guid, GUIDDistributedTrieNode distributedTreeNode );

    void newTag(GUID originalGuid, GUID dirGuid, String tagName, GUID tagGuid);
    void updateTage(GUID tagGuid, String tagName);
    GUID getOriginalGuid(String tagName,GUID dirGuid);
    long isTagGuid(GUID guid);
    GUID getOriginalGuidByTagGuid(GUID tagGuid);
}
