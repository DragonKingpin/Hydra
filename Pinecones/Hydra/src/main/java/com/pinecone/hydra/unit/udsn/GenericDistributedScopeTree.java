package com.pinecone.hydra.unit.udsn;


import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.uoi.UOI;
import com.pinecone.hydra.service.tree.nodes.ServiceTreeNode;
import com.pinecone.hydra.service.tree.operator.MetaNodeOperator;
import com.pinecone.hydra.service.tree.operator.MetaNodeOperatorProxy;
import com.pinecone.hydra.service.tree.source.DefaultMetaNodeManipulators;
import com.pinecone.hydra.service.tree.source.ServiceNodeOwnerManipulator;
import com.pinecone.hydra.unit.udsn.source.ScopeTreeManipulator;
import com.pinecone.hydra.service.tree.source.ApplicationNodeManipulator;
import com.pinecone.hydra.service.tree.source.ClassifNodeManipulator;
import com.pinecone.hydra.service.tree.source.ServiceNodeManipulator;
import com.pinecone.ulf.util.id.GUID72;

import java.util.List;

/**
 * 提供服务树的相应方法
 */
public class GenericDistributedScopeTree implements UniDistributedScopeTree {
    private ScopeTreeManipulator            scopeTreeManipulator;

    private ApplicationNodeManipulator      applicationNodeManipulator;

    private ServiceNodeManipulator          serviceNodeManipulator;

    private ClassifNodeManipulator          classifNodeManipulator;

    private MetaNodeOperatorProxy           metaNodeOperatorProxy;

    private DefaultMetaNodeManipulators     defaultMetaNodeManipulators;

    private ServiceNodeOwnerManipulator     serviceNodeOwnerManipulator;

    public GenericDistributedScopeTree( DefaultMetaNodeManipulators defaultMetaNodeManipulators){
        this.scopeTreeManipulator        =   defaultMetaNodeManipulators.getScopeTreeManipulator();
        this.applicationNodeManipulator  =   defaultMetaNodeManipulators.getApplicationNodeManipulator();
        this.serviceNodeManipulator      =   defaultMetaNodeManipulators.getServiceNodeManipulator();
        this.classifNodeManipulator      =   defaultMetaNodeManipulators.getClassifNodeManipulator();
        this.metaNodeOperatorProxy       =   new MetaNodeOperatorProxy(defaultMetaNodeManipulators);
        this.serviceNodeOwnerManipulator =   defaultMetaNodeManipulators.getServiceNodeOwnerManipulator();
    }


    @Override
    public DistributedTreeNode getParentNode(GUID guid) {
        return null;
    }

    @Override
    public void insert(DistributedTreeNode distributedConfTreeNode) {

    }

    //获取路径信息
    @Override
    public String getPath( GUID guid ){
        String cachePath = this.scopeTreeManipulator.getPath( guid );
        Debug.trace( "查找到路径：" + cachePath );
        //若不存在path信息则更新缓存表
        if ( cachePath == null ){
            GUIDDistributedScopeNode node = this.scopeTreeManipulator.getNode( guid );
            //查看是否具有拥有关系
            GUID owner = this.serviceNodeOwnerManipulator.getOwner(node.getGuid());
            if (owner==null){
                String nodeName = this.getNodeName(node);

                // Assemble new path, if cache path dose not exist.
                String assemblePath = nodeName;
                while ( !node.getParentGUIDs().isEmpty() ){
                    Debug.trace("获取到了节点" + node);
                    List<GUID> parentGUIDs = node.getParentGUIDs();
                        node = this.scopeTreeManipulator.getNode(parentGUIDs.get(0));
                        nodeName = this.getNodeName(node);
                        assemblePath = nodeName + "." + assemblePath;
                }
                this.scopeTreeManipulator.putPath( assemblePath, guid );
                return assemblePath;
            }
            else {
                String nodeName = this.getNodeName(node);

                // Assemble new path, if cache path dose not exist.
                String assemblePath = nodeName;
                while ( !node.getParentGUIDs().isEmpty() ){
                    Debug.trace("获取到了节点" + node);
                    node = this.scopeTreeManipulator.getNode(owner);
                    nodeName = this.getNodeName(node);
                    assemblePath = nodeName + "." + assemblePath;
                }
                this.scopeTreeManipulator.putPath( assemblePath, guid );
                return assemblePath;
            }

        }
        return cachePath;
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

    private String getNodeName(GUIDDistributedScopeNode node){
        Debug.trace("查找节点名"+node);
        UOI type = node.getType();
        ServiceTreeNode newInstance = (ServiceTreeNode)type.newInstance();
        MetaNodeOperator operator = this.metaNodeOperatorProxy.getOperator(newInstance.getMetaType());
        ServiceTreeNode serviceTreeNode = operator.get(node.getGuid());

        return serviceTreeNode.getName();
    }


    @Override
    public void  remove( GUID guid ){
        this.scopeTreeManipulator.removeNode(guid);
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
        return this.scopeTreeManipulator.getChildNode(guid);
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
        return null;
    }

    @Override
    public List<GUID> getSubordinates(GUID guid) {
        return null;
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
