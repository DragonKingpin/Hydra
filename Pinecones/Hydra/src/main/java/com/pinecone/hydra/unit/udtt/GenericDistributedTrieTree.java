package com.pinecone.hydra.unit.udtt;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.system.ko.KOMInstrument;
import com.pinecone.hydra.unit.udtt.entity.ReparseLinkNode;
import com.pinecone.hydra.unit.udtt.source.TireOwnerManipulator;
import com.pinecone.hydra.unit.udtt.source.TriePathCacheManipulator;
import com.pinecone.hydra.unit.udtt.source.TrieTreeManipulator;
import com.pinecone.hydra.unit.udtt.source.TreeMasterManipulator;
import com.pinecone.ulf.util.id.GUID72;
import com.pinecone.ulf.util.id.GuidAllocator;

import java.util.List;

public class GenericDistributedTrieTree implements UniDistributedTrieTree {
    private TrieTreeManipulator      trieTreeManipulator;

    private TireOwnerManipulator     tireOwnerManipulator;

    private TriePathCacheManipulator triePathCacheManipulator;

    public GenericDistributedTrieTree( TreeMasterManipulator masterManipulator ){
        this.trieTreeManipulator      =  masterManipulator.getTrieTreeManipulator();
        this.tireOwnerManipulator     =  masterManipulator.getTireOwnerManipulator();
        this.triePathCacheManipulator =  masterManipulator.getTriePathCacheManipulator();
    }


    @Override
    public void insert( DistributedTreeNode node ) {
        this.trieTreeManipulator.insert( this.tireOwnerManipulator, (GUIDDistributedTrieNode) node );
    }

    @Override
    public void affirmOwnedNode( GUID nodeGUID, GUID parentGUID ){
        GUID owner = this.tireOwnerManipulator.getOwner( nodeGUID );
        if ( owner == null ){
            this.tireOwnerManipulator.remove( nodeGUID, owner );
            this.tireOwnerManipulator.insertOwnedNode( nodeGUID, parentGUID );
        }
        else {
            this.tireOwnerManipulator.insertOwnedNode( nodeGUID, parentGUID );
        }
    }

    @Override
    public GUIDDistributedTrieNode getNode( GUID guid ){
        return this.trieTreeManipulator.getNode( guid );
    }


    @Override
    public void purge( GUID guid ){
        this.trieTreeManipulator.purge( guid );
    }

    @Override
    public void removeTreeNodeOnly( GUID guid ) {
        this.trieTreeManipulator.removeTreeNode( guid );
    }

    @Override
    public void put( GUID guid, GUIDDistributedTrieNode distributedTreeNode ){
        this.trieTreeManipulator.insertNode( guid, distributedTreeNode );
    }

    @Override
    public boolean containsKey( GUID key ) {
        GUIDDistributedTrieNode guidDistributedTrieNode = this.trieTreeManipulator.getNode(key);
        return guidDistributedTrieNode == null;
    }

    @Override
    public GUID queryGUIDByPath( String path ) {
        return this.triePathCacheManipulator.queryGUIDByPath( path );
    }

    @Override
    public List<GUIDDistributedTrieNode > getChildren( GUID guid ) {
        return this.trieTreeManipulator.getChildren(guid);
    }

    @Override
    public List<GUID > getChildrenGuids( GUID parentGuid ) {
        return this.trieTreeManipulator.getChildrenGuids( parentGuid );
    }

    @Override
    public List<GUID > getParentGuids( GUID guid ) {
        return this.trieTreeManipulator.getParentGuids(guid);
    }

    @Override
    public void removeInheritance( GUID childGuid, GUID parentGuid ) {
        this.trieTreeManipulator.removeInheritance(childGuid,parentGuid);
    }

    @Override
    public void setOwner( GUID sourceGuid, GUID targetGuid ) {
        GUID owner = this.tireOwnerManipulator.getOwner(sourceGuid);
        if ( owner == null ){
            long exist = this.trieTreeManipulator.countNode( sourceGuid, targetGuid );
            if ( exist <= 0 ){
                this.tireOwnerManipulator.insertOwnedNode( sourceGuid, targetGuid );
            }
            else {
                this.tireOwnerManipulator.setOwned(sourceGuid, targetGuid);
            }
        }
        else {
            this.tireOwnerManipulator.remove( sourceGuid, owner );
            this.tireOwnerManipulator.insertOwnedNode( sourceGuid, targetGuid );
        }
    }

    @Override
    public void setGuidLineage( GUID sourceGuid, GUID targetGuid ) {
        this.tireOwnerManipulator.updateParentGuid( sourceGuid, targetGuid );
    }

    @Override
    public String getCachePath( GUID guid ){
        return this.triePathCacheManipulator.getPath(guid);
    }

    @Override
    public void removeCachePath( GUID guid ) {
        this.triePathCacheManipulator.remove( guid );
    }

    @Override
    public GUID getOwner( GUID guid ) {
        return this.tireOwnerManipulator.getOwner(guid);
    }



    @Override
    public List<GUID> getSubordinates(GUID guid) {
        return this.tireOwnerManipulator.getSubordinates(guid);
    }

    @Override
    public void insertCachePath( GUID guid, String path ) {
        this.triePathCacheManipulator.insert( guid, path );
    }

    @Override
    public List<GUID> listRoot() {
        return this.trieTreeManipulator.listRoot();
    }

    @Override
    public boolean isRoot( GUID guid ) {
        return this.trieTreeManipulator.isRoot( guid );
    }

    @Override
    public long queryLinkedCount( GUID guid, LinkedType linkedType ) {
        return this.trieTreeManipulator.queryLinkedCount( guid, linkedType );
    }

    @Override
    public long queryAllLinkedCount( GUID guid ) {
        return this.trieTreeManipulator.queryAllLinkedCount( guid );
    }

    @Override
    public long queryStrongLinkedCount( GUID guid ) {
        return this.trieTreeManipulator.queryStrongLinkedCount( guid );
    }

    @Override
    public long queryWeakLinkedCount( GUID guid ) {
        return this.trieTreeManipulator.queryWeakLinkedCount( guid );
    }

    @Override
    public void newHardLink( GUID sourceGuid, GUID targetGuid ) {
        long count = this.trieTreeManipulator.countNode( sourceGuid, targetGuid );
        if ( count <= 0 ){
            this.tireOwnerManipulator.insertHardLinkedNode( sourceGuid,targetGuid );
        }
    }

    @Override
    public void moveTo( GUID sourceGuid, GUID destinationGuid ) {
        this.removeCachePath( sourceGuid );
        this.tireOwnerManipulator.updateParentGuid( sourceGuid, destinationGuid );
    }

    @Override
    public void newLinkTag( GUID originalGuid, GUID dirGuid, String tagName, KOMInstrument instrument ) {
        GuidAllocator guidAllocator = instrument.getGuidAllocator();
        GUID tagGuid = guidAllocator.nextGUID72();
        this.trieTreeManipulator.newLinkTag( originalGuid, dirGuid, tagName, tagGuid );
    }

    @Override
    public void updateLinkTagName( GUID tagGuid, String tagName ) {
        this.trieTreeManipulator.updateLinkTagName( tagGuid,tagName );
    }

    @Override
    public boolean isTagGuid(GUID guid) {
        return this.trieTreeManipulator.isTagGuid( guid );
    }

    @Override
    public GUID getOriginalGuid( String tagName, GUID parentDirGUID ) {
        return this.trieTreeManipulator.getOriginalGuid( tagName, parentDirGUID );
    }

    @Override
    public GUID getOriginalGuidByNodeGuid( String tagName, GUID nodeGUID ) {
        return this.trieTreeManipulator.getOriginalGuidByNodeGuid( tagName, nodeGUID );
    }

    @Override
    public List<GUID > fetchOriginalGuid( String tagName ) {
        return this.trieTreeManipulator.fetchOriginalGuid( tagName );
    }

    @Override
    public List<GUID > fetchOriginalGuidRoot( String tagName ) {
        return this.trieTreeManipulator.fetchOriginalGuidRoot( tagName );
    }

    @Override
    public ReparseLinkNode getReparseLinkNodeByNodeGuid( String tagName, GUID nodeGUID ) {
        return this.trieTreeManipulator.getReparseLinkNodeByNodeGuid( tagName, nodeGUID );
    }

    @Override
    public ReparseLinkNode getReparseLinkNode( String tagName, GUID parentDirGuid ) {
        return this.trieTreeManipulator.getReparseLinkNode( tagName, parentDirGuid );
    }

    @Override
    public GUID getOriginalGuid( GUID tagGuid ) {
        return this.trieTreeManipulator.getOriginalGuidByTagGuid( tagGuid );
    }

    @Override
    public void removeReparseLink( GUID guid ) {
        this.trieTreeManipulator.removeReparseLink( guid );
    }

    @Override
    public boolean hasOwnProperty(Object key) {
        return this.containsKey( key );
    }

    @Override
    public boolean containsKey(Object key) {
        if( key instanceof GUID ) {
            return this.containsKey((GUID) key );
        }
        else if( key instanceof String ) {
            return this.containsKey( (new GUID72((String)key)) );
        }
        return false;
    }

}
