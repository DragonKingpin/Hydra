package com.pinecone.hydra.unit.udsn;


import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.uoi.UOI;
import com.pinecone.hydra.service.tree.nodes.ServiceNode;
import com.pinecone.hydra.service.tree.nodes.ServiceTreeNode;
import com.pinecone.hydra.service.tree.operator.MetaNodeOperator;
import com.pinecone.hydra.service.tree.operator.MetaNodeOperatorProxy;
import com.pinecone.hydra.service.tree.source.DefaultMetaNodeManipulator;
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
    private ScopeTreeManipulator scopeTreeManipulator;

    private ApplicationNodeManipulator applicationNodeManipulator;

    private ServiceNodeManipulator serviceNodeManipulator;

    private ClassifNodeManipulator classifNodeManipulator;

    private MetaNodeOperatorProxy metaNodeOperatorProxy;

    private DefaultMetaNodeManipulator defaultMetaNodeManipulator;

    public GenericDistributedScopeTree(DefaultMetaNodeManipulator defaultMetaNodeManipulator){
        this.scopeTreeManipulator       =   defaultMetaNodeManipulator.getScopeTreeManipulator();
        this.applicationNodeManipulator =   defaultMetaNodeManipulator.getApplicationNodeManipulator();
        this.serviceNodeManipulator     =   defaultMetaNodeManipulator.getServiceNodeManipulator();
        this.classifNodeManipulator     =   defaultMetaNodeManipulator.getClassifNodeManipulator();
        this.metaNodeOperatorProxy      =   new MetaNodeOperatorProxy(defaultMetaNodeManipulator);
    }


    //打印路径信息
    public String getPath(GUID guid){
        String path = this.scopeTreeManipulator.selectPath(guid);
        System.out.println("查找到路径：" + path);
        //若不存在path信息则更新缓存表
        if ( path == null ){
            GUIDDistributedScopeNode node = this.scopeTreeManipulator.getNode(guid);
            String nodeName = getNodeName(node);
            String pathString = "";
            pathString=pathString+nodeName;
            while (node.getParentGUID() != null){
                node = this.scopeTreeManipulator.getNode(node.getParentGUID());
                nodeName = getNodeName(node);
                pathString = nodeName + "." + pathString;
            }
            this.scopeTreeManipulator.savePath(pathString,guid);
            return pathString;
        }
        return path;

    }

    public void insertNodeToParent(GUID nodeGUID,GUID parentGUID){
        this.scopeTreeManipulator.addNodeToParent(nodeGUID,parentGUID);
    }

    public GUIDDistributedScopeNode getNode(GUID guid){
        return this.scopeTreeManipulator.getNode(guid);
    }

    private String getNodeName(GUIDDistributedScopeNode node){
        UOI type = node.getType();
        ServiceTreeNode newInstance = (ServiceTreeNode)type.newInstance();
        MetaNodeOperator operator = metaNodeOperatorProxy.getOperator(newInstance.getMetaType());
        ServiceTreeNode serviceTreeNode = operator.get(node.getGuid());
        Debug.trace("获取到了节点"+serviceTreeNode);
        return serviceTreeNode.getName();
    }



    public void  remove( GUID guid ){
        removeNode(guid);
    }

    public void put( GUID guid, GUIDDistributedScopeNode distributedTreeNode ){
        this.scopeTreeManipulator.putNode(guid,distributedTreeNode);
    }

    public boolean isEmpty(){
        long size = this.scopeTreeManipulator.size();
        return size == 0;
    }

    public long size(){
        return this.scopeTreeManipulator.size();
    }


    public boolean containsKey(GUID key) {
        GUIDDistributedScopeNode guidDistributedScopeNode = this.scopeTreeManipulator.getNode(key);
        return guidDistributedScopeNode==null;
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
    private void removeNode(GUID guid){
        List<GUIDDistributedScopeNode> childNodes = this.scopeTreeManipulator.getChildNode(guid);
        this.scopeTreeManipulator.removeNode(guid);
        for(GUIDDistributedScopeNode guidDistributedScopeNode : childNodes){
            removeNode(guidDistributedScopeNode.getGuid());
        }
    }
}
