package com.pinecone.hydra.unit.udsn;


import com.pinecone.framework.system.ProxyProvokeHandleException;
import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.lang.GenericDynamicFactory;
import com.pinecone.framework.util.uoi.UOI;
import com.pinecone.hydra.service.tree.nodes.ServiceTreeNode;
import com.pinecone.hydra.service.tree.operator.MetaNodeOperator;
import com.pinecone.hydra.service.tree.operator.MetaNodeOperatorProxy;
import com.pinecone.hydra.unit.udsn.source.ScopeTreeManipulator;
import com.pinecone.hydra.service.tree.source.ApplicationNodeManipulator;
import com.pinecone.hydra.service.tree.source.ClassifNodeManipulator;
import com.pinecone.hydra.service.tree.source.ServiceNodeManipulator;
import com.pinecone.ulf.util.id.GUID72;

import java.lang.reflect.InvocationTargetException;

/**
 * 提供服务树的相应方法
 */
public class GenericDistributedScopeTree implements UniDistributedScopeTree {
    private ScopeTreeManipulator scopeTreeManipulator;

    private ApplicationNodeManipulator applicationNodeManipulator;

    private ServiceNodeManipulator serviceNodeManipulator;

    private ClassifNodeManipulator classifNodeManipulator;

    private MetaNodeOperatorProxy metaNodeOperatorProxy;

    public GenericDistributedScopeTree(ScopeTreeManipulator scopeTreeManipulator, ApplicationNodeManipulator applicationNodeManipulator,
                                       ServiceNodeManipulator serviceNodeManipulator, ClassifNodeManipulator classifNodeManipulator,
                                       MetaNodeOperatorProxy functionalNodeFactory){
        this.scopeTreeManipulator=scopeTreeManipulator;
        this.applicationNodeManipulator=applicationNodeManipulator;
        this.serviceNodeManipulator = serviceNodeManipulator;
        this.classifNodeManipulator=classifNodeManipulator;
        this.metaNodeOperatorProxy=functionalNodeFactory;
    }

    private final static String ApplicationNode="com.walnut.sparta.pojo.ApplicationFunctionalNodeInformation";

    private final static String ServiceNode="com.walnut.sparta.pojo.ServiceFunctionalNodeInformation";

    private final static String ClassifNode="com.walnut.sparta.pojo.ClassifFunctionalNodeInformation";


    //打印路径信息
    public String getPath(GUID guid){
        String path = this.scopeTreeManipulator.selectPath(guid);
        System.out.println("查找到路径：" + path);
        //若不存在path信息则更新缓存表
        if ( path == null ){
            GUIDDistributedScopeNode node = this.scopeTreeManipulator.selectNode(guid);
            String nodeName = getNodeName(node);
            String pathString = "";
            pathString=pathString+nodeName;
            while (node.getParentGUID() != null){
                node = this.scopeTreeManipulator.selectNode(node.getParentGUID());
                nodeName = getNodeName(node);
                pathString = nodeName + "." + pathString;
            }
            this.scopeTreeManipulator.savePath(pathString,guid);
            return pathString;
        }
        return path;

    }

    public void addNodeToParent(GUID nodeGUID,GUID parentGUID){
        this.scopeTreeManipulator.addNodeToParent(nodeGUID,parentGUID);
    }

    public GUIDDistributedScopeNode getNode(GUID guid){
        return this.scopeTreeManipulator.selectNode(guid);
    }

    private String getNodeName( GUIDDistributedScopeNode node ){
        UOI type = node.getType();
        GenericDynamicFactory genericDynamicFactory = new GenericDynamicFactory();
        try {
            Object nodeInformation = genericDynamicFactory.loadInstance(type.getObjectName(), null, null);
            Class<?> nodeInformationClass = nodeInformation.getClass();
            MetaNodeOperator nodeOperation = this.metaNodeOperatorProxy.getNodeOperation(nodeInformationClass.getName());
            ServiceTreeNode nodeWideData = nodeOperation.get(node.getGuid());
            Debug.trace(nodeWideData);
            return nodeWideData.getName();
        }
        catch ( ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException e ) {
            throw new ProxyProvokeHandleException(e);
        }
    }


    // DistributedTreeNode getNode

    // DistributedTreeNode remove( GUID guid )

    // void put( GUID guid, DistributedTreeNode )

    // bool isEmpty

    // long size


    public boolean containsKey(GUID key) {
        return false;
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
