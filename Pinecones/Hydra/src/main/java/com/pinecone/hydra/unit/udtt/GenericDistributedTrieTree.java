package com.pinecone.hydra.unit.udtt;


import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.uoi.UOI;
import com.pinecone.hydra.unit.udtt.source.TireOwnerManipulator;
import com.pinecone.hydra.unit.udtt.source.TriePathManipulator;
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

    private TriePathManipulator  triePathManipulator;

    public GenericDistributedTrieTree( TreeMasterManipulator masterManipulator ){
        this.trieTreeManipulator  =  masterManipulator.getTrieTreeManipulator();
        this.tireOwnerManipulator =  masterManipulator.getTireOwnerManipulator();
        this.triePathManipulator  =  masterManipulator.getTriePathManipulator();
    }


    @Override
    public void insert(DistributedTreeNode distributedConfTreeNode) {
        this.trieTreeManipulator.insert((GUIDDistributedTrieNode) distributedConfTreeNode);
    }

    //获取路径信息
    @Override
    public String getPath( GUID guid ){
        return this.triePathManipulator.getPath(guid);
    }

    @Override
    public void insertNodeToParent(GUID nodeGUID,GUID parentGUID){
        //todo 添加一个插入的条件判断
        this.trieTreeManipulator.insertNodeToParent(nodeGUID,parentGUID);
    }

    @Override
    public GUIDDistributedTrieNode getNode(GUID guid){
        return this.trieTreeManipulator.getNode(guid);
    }


    @Override
    public void  remove( GUID guid ){
        this.trieTreeManipulator.remove(guid);
    }

    @Override
    public void put( GUID guid, GUIDDistributedTrieNode distributedTreeNode ){
        this.trieTreeManipulator.putNode(guid,distributedTreeNode);
    }

    @Override
    public boolean isEmpty(){
        long size = this.trieTreeManipulator.size();
        return size == 0;
    }

    @Override
    public long size(){
        return this.trieTreeManipulator.size();
    }

    @Override
    public boolean containsKey(GUID key) {
        GUIDDistributedTrieNode guidDistributedTrieNode = this.trieTreeManipulator.getNode(key);
        return guidDistributedTrieNode ==null;
    }

    @Override
    public GUID queryGUIDByPath( String path ) {
        return this.trieTreeManipulator.getGUIDByPath( path );
    }

    @Override
    public List<GUIDDistributedTrieNode> getChildNode(GUID guid) {
        return this.trieTreeManipulator.getChild(guid);
    }

    @Override
    public List<GUID> getParentNodes(GUID guid) {
        return this.trieTreeManipulator.getParentNodes(guid);
    }

    @Override
    public void removeInheritance(GUID childGuid, GUID parentGuid) {
        this.trieTreeManipulator.removeInheritance(childGuid,parentGuid);
    }

    @Override
    public void removePath(GUID guid) {
        this.trieTreeManipulator.removePath(guid);
    }

    @Override
    public GUID getOwner(GUID guid) {
        return this.tireOwnerManipulator.getOwner(guid);
    }

    @Override
    public List<GUID> getSubordinates(GUID guid) {
        return this.tireOwnerManipulator.getSubordinates(guid);
    }

    @Override
    public void insertPath(GUID guid, String path) {
        this.triePathManipulator.insert(guid,path);
    }

    @Override
    public void updateType(UOI type,GUID guid) {

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
