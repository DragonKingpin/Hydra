package com.pinecone.hydra.unit.udsn;


import com.pinecone.framework.system.ProxyProvokeHandleException;
import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.lang.GenericDynamicFactory;
import com.pinecone.framework.util.uoi.UOI;
import com.pinecone.hydra.service.tree.FunctionalNodeMeta;
import com.pinecone.hydra.service.tree.MetaNodeOperator;
import com.pinecone.hydra.service.tree.MetaNodeOperatorProxy;
import com.pinecone.hydra.service.tree.ServiceTreeMapper;
import com.pinecone.hydra.service.tree.source.ApplicationNodeManipulator;
import com.pinecone.hydra.service.tree.source.ClassifNodeManipulator;
import com.pinecone.hydra.service.tree.source.ServiceNodeManipulator;

import java.lang.reflect.InvocationTargetException;

/**
 * 提供服务树的相应方法
 */
public class GenericDistributedScopeTree implements UniDistributedScopeTree {
    private ServiceTreeMapper serviceTreeMapper;

    private ApplicationNodeManipulator applicationNodeManipulator;

    private ServiceNodeManipulator serviceNodeManipulator;

    private ClassifNodeManipulator classifNodeManipulator;

    private MetaNodeOperatorProxy metaNodeOperatorProxy;

    public GenericDistributedScopeTree(ServiceTreeMapper serviceTreeMapper, ApplicationNodeManipulator applicationNodeManipulator,
                                       ServiceNodeManipulator serviceNodeManipulator, ClassifNodeManipulator classifNodeManipulator,
                                       MetaNodeOperatorProxy functionalNodeFactory){
        this.serviceTreeMapper=serviceTreeMapper;
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
        String path = this.serviceTreeMapper.selectPath(guid);
        System.out.println("查找到路径："+path);
        //若不存在path信息则更新缓存表
        if ( path == null ){
            GUIDDistributedScopeNode node = this.serviceTreeMapper.selectNode(guid);
            String nodeName = getNodeName(node);
            String pathString="";
            pathString=pathString+nodeName;
            while (node.getParentGUID() != null){
                node=this.serviceTreeMapper.selectNode(node.getParentGUID());
                nodeName = getNodeName(node);
                pathString=nodeName + "." + pathString;
            }
            this.serviceTreeMapper.savePath(pathString,guid);
            return pathString;
        }
        return path;

    }

    public void addNodeToParent(GUID nodeGUID,GUID parentGUID){
        this.serviceTreeMapper.addNodeToParent(nodeGUID,parentGUID);
    }

    public GUIDDistributedScopeNode getNode(GUID guid){
        return this.serviceTreeMapper.selectNode(guid);
    }

    private String getNodeName( GUIDDistributedScopeNode node ){
        UOI type = node.getType();
        GenericDynamicFactory genericDynamicFactory = new GenericDynamicFactory();
        try {
            Object nodeInformation = genericDynamicFactory.loadInstance(type.getObjectName(), null, null);
            Class<?> nodeInformationClass = nodeInformation.getClass();
            MetaNodeOperator nodeOperation = this.metaNodeOperatorProxy.getNodeOperation(nodeInformationClass.getName());
            FunctionalNodeMeta functionalNodeMeta = nodeOperation.get(node.getGuid());
            Debug.trace(functionalNodeMeta);
            return functionalNodeMeta.getName();
        }
        catch (ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new ProxyProvokeHandleException(e);
        }
    }

    @Override
    public boolean hasOwnProperty(Object elm) {
        return false;
    }

    @Override
    public boolean containsKey(Object key) {
        return false;
    }
}
