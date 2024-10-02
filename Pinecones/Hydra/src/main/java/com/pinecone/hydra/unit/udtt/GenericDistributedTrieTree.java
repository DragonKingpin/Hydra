package com.pinecone.hydra.unit.udtt;


import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.udtt.source.TireOwnerManipulator;
import com.pinecone.hydra.unit.udtt.source.TriePathCacheManipulator;
import com.pinecone.hydra.unit.udtt.source.TrieTreeManipulator;
import com.pinecone.hydra.unit.udtt.source.TreeMasterManipulator;
import com.pinecone.ulf.util.id.GUID72;

import java.util.List;

/**
 * 提供服务树的相应方法
 */
public class GenericDistributedTrieTree implements UniDistributedTrieTree {
    private TrieTreeManipulator  trieTreeManipulator;

    private TireOwnerManipulator tireOwnerManipulator;

    private TriePathCacheManipulator triePathCacheManipulator;

    public GenericDistributedTrieTree( TreeMasterManipulator masterManipulator ){
        this.trieTreeManipulator  =  masterManipulator.getTrieTreeManipulator();
        this.tireOwnerManipulator =  masterManipulator.getTireOwnerManipulator();
        this.triePathCacheManipulator =  masterManipulator.getTriePathCacheManipulator();
    }


    @Override
    public void insert( DistributedTreeNode node ) {
        this.trieTreeManipulator.insert( this.tireOwnerManipulator, (GUIDDistributedTrieNode) node );
    }

    //获取路径信息
    @Override
    public String getPath( GUID guid ){
        return this.triePathCacheManipulator.getPath(guid);
    }

    @Override
    public void insertOwnedNode(GUID nodeGUID,GUID parentGUID){
        GUID owner = this.tireOwnerManipulator.getOwner(nodeGUID);
        if ( owner == null ){
            this.tireOwnerManipulator.remove(nodeGUID, owner);
            this.tireOwnerManipulator.insertOwnedNode(nodeGUID,parentGUID);
        }
        else {
            this.tireOwnerManipulator.insertOwnedNode(nodeGUID,parentGUID);
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
    public void put( GUID guid, GUIDDistributedTrieNode distributedTreeNode ){
        this.trieTreeManipulator.insertNode( guid, distributedTreeNode );
    }

    @Override
    public boolean containsKey(GUID key) {
        GUIDDistributedTrieNode guidDistributedTrieNode = this.trieTreeManipulator.getNode(key);
        return guidDistributedTrieNode ==null;
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
            if ( exist > 0 ){
                this.tireOwnerManipulator.insertOwnedNode( sourceGuid, targetGuid );
            }
            else {
                this.tireOwnerManipulator.setOwned(sourceGuid,targetGuid);
            }
        }
        else {
            this.tireOwnerManipulator.remove( sourceGuid, owner );
            this.tireOwnerManipulator.insertOwnedNode( sourceGuid, targetGuid );
        }
    }

    @Override
    public void removePath(GUID guid) {
        this.triePathCacheManipulator.remove( guid );
    }

    @Override
    public GUID getOwner(GUID guid) {
        return this.tireOwnerManipulator.getOwner(guid);
    }

    @Override
    public void moveTo( GUID sourceGuid, GUID destinationGuid ) {
        GUID owner = this.tireOwnerManipulator.getOwner( sourceGuid );
        this.tireOwnerManipulator.remove( sourceGuid, owner );
        this.tireOwnerManipulator.insertOwnedNode( sourceGuid, destinationGuid );
    }

    @Override
    public void setReparse( GUID sourceGuid, GUID targetGuid ) {
        long count = this.trieTreeManipulator.countNode( sourceGuid, targetGuid );
        if ( count > 0 ){
            this.tireOwnerManipulator.insertHardLinkedNode( sourceGuid,targetGuid );
        }
        else {
            Debug.trace("the relationship is exist!!!");
        }
    }

    @Override
    public List<GUID> getSubordinates(GUID guid) {
        return this.tireOwnerManipulator.getSubordinates(guid);
    }

    @Override
    public void insertPath(GUID guid, String path) {
        this.triePathCacheManipulator.insert(guid,path);
    }

    @Override
    public List<GUID> listRoot() {
        return this.trieTreeManipulator.listRoot();
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
