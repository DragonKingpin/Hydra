package com.pinecone.hydra.unit.udsn;


import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.uoi.UOI;
import com.pinecone.hydra.service.tree.nodes.ServiceTreeNode;
import com.pinecone.hydra.service.tree.operator.MetaNodeOperator;
import com.pinecone.hydra.service.tree.operator.MetaNodeOperatorProxy;
import com.pinecone.hydra.service.tree.source.DefaultMetaNodeManipulators;
import com.pinecone.hydra.unit.udsn.source.ScopeOwnerManipulator;
import com.pinecone.hydra.unit.udsn.source.ScopePathManipulator;
import com.pinecone.hydra.unit.udsn.source.ScopeTreeManipulator;
import com.pinecone.hydra.unit.udsn.source.TreeManipulatorSharer;
import com.pinecone.ulf.util.id.GUID72;

import java.util.List;

/**
 * 提供服务树的相应方法
 */
public class GenericDistributedScopeTree implements UniDistributedScopeTree {
    private ScopeTreeManipulator            scopeTreeManipulator;

    private ScopeOwnerManipulator           scopeOwnerManipulator;

    private ScopePathManipulator            scopePathManipulator;

    public GenericDistributedScopeTree(TreeManipulatorSharer treeManipulatorSharer){
        this.scopeTreeManipulator   =  treeManipulatorSharer.getScopeTreeManipulator();
        this.scopeOwnerManipulator  =  treeManipulatorSharer.getScopeOwnerManipulator();
        this.scopePathManipulator   =  treeManipulatorSharer.getScopePathManipulator();
    }


    @Override
    public void insert(DistributedTreeNode distributedConfTreeNode) {
        this.scopeTreeManipulator.insert((GUIDDistributedScopeNode) distributedConfTreeNode);
    }

    //获取路径信息
    @Override
    public String getPath( GUID guid ){
        return this.scopePathManipulator.getPath(guid);
    }

    @Override
    public void insertNodeToParent(GUID nodeGUID,GUID parentGUID){
        //todo 添加一个插入的条件判断
        this.scopeTreeManipulator.insertNodeToParent(nodeGUID,parentGUID);
    }

    @Override
    public GUIDDistributedScopeNode getNode(GUID guid){
        return this.scopeTreeManipulator.getNode(guid);
    }


    @Override
    public void  remove( GUID guid ){
        this.scopeTreeManipulator.remove(guid);
    }

    @Override
    public void put( GUID guid, GUIDDistributedScopeNode distributedTreeNode ){
        this.scopeTreeManipulator.putNode(guid,distributedTreeNode);
    }

    @Override
    public boolean isEmpty(){
        long size = this.scopeTreeManipulator.size();
        return size == 0;
    }

    @Override
    public long size(){
        return this.scopeTreeManipulator.size();
    }

    @Override
    public boolean containsKey(GUID key) {
        GUIDDistributedScopeNode guidDistributedScopeNode = this.scopeTreeManipulator.getNode(key);
        return guidDistributedScopeNode==null;
    }

    @Override
    public GUID parsePath(String path) {
        return this.scopeTreeManipulator.parsePath(path);
    }

    @Override
    public List<GUIDDistributedScopeNode> getChildNode(GUID guid) {
        return this.scopeTreeManipulator.getChild(guid);
    }

    @Override
    public List<GUID> getParentNodes(GUID guid) {
        return this.scopeTreeManipulator.getParentNodes(guid);
    }

    @Override
    public void removeInheritance(GUID childGuid, GUID parentGuid) {
        this.scopeTreeManipulator.removeInheritance(childGuid,parentGuid);
    }

    @Override
    public void removePath(GUID guid) {
        this.scopeTreeManipulator.removePath(guid);
    }

    @Override
    public GUID getOwner(GUID guid) {
        return this.scopeOwnerManipulator.getOwner(guid);
    }

    @Override
    public List<GUID> getSubordinates(GUID guid) {
        return this.scopeOwnerManipulator.getSubordinates(guid);
    }

    @Override
    public void insertPath(GUID guid, String path) {
        this.scopePathManipulator.insert(guid,path);
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
