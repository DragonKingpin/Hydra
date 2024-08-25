package com.walnut.sparta.services.pojo;

import com.pinecone.framework.system.ProxyProvokeHandleException;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.lang.GenericDynamicFactory;
import com.pinecone.framework.util.uoi.UOI;
import com.pinecone.hydra.service.tree.ScopeServiceTree;
import com.pinecone.hydra.service.tree.nodes.GenericApplicationNode;
import com.pinecone.hydra.service.tree.nodes.GenericClassificationNode;
import com.pinecone.hydra.service.tree.nodes.GenericServiceNode;
import com.pinecone.hydra.service.tree.nodes.ServiceTreeNode;
import com.pinecone.hydra.service.tree.operator.MetaNodeOperator;
import com.pinecone.hydra.service.tree.operator.ApplicationNodeWideData;
import com.pinecone.hydra.service.tree.operator.ClassificationNodeWideData;
import com.pinecone.hydra.service.tree.operator.ServiceNodeWideData;
import com.pinecone.hydra.service.tree.source.DefaultMetaNodeManipulators;
import com.pinecone.hydra.unit.udsn.source.ScopeTreeManipulator;
import com.pinecone.hydra.service.tree.source.ApplicationNodeManipulator;
import com.pinecone.hydra.service.tree.source.ClassifNodeManipulator;
import com.pinecone.hydra.unit.udsn.GUIDDistributedScopeNode;
import com.pinecone.hydra.service.tree.source.ServiceNodeManipulator;
import com.pinecone.hydra.service.tree.operator.MetaNodeOperatorProxy;
import com.pinecone.hydra.unit.udsn.GenericDistributedScopeTree;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class DistributedScopeServiceTree implements ScopeServiceTree {
    //GenericDistributedScopeTree

    private DefaultMetaNodeManipulators defaultMetaNodeManipulators;
    private MetaNodeOperatorProxy       metaNodeOperatorProxy;

    private ScopeTreeManipulator        scopeTreeManipulator;
    private ApplicationNodeManipulator  applicationNodeManipulator;
    private ServiceNodeManipulator      serviceNodeManipulator;
    private ClassifNodeManipulator      classifNodeManipulator;



    public DistributedScopeServiceTree( DefaultMetaNodeManipulators manipulators ){
        this.defaultMetaNodeManipulators = manipulators;
        this.scopeTreeManipulator        = manipulators.getScopeTreeManipulator();
        this.applicationNodeManipulator  = manipulators.getApplicationNodeManipulator();
        this.serviceNodeManipulator      = manipulators.getServiceNodeManipulator();
        this.classifNodeManipulator      = manipulators.getClassifNodeManipulator();
        this.metaNodeOperatorProxy       = new MetaNodeOperatorProxy( this.defaultMetaNodeManipulators );
    }

    //保存节点
    //这里有个问题，将这个移入Operator中但是现在这里的逻辑就是在获取Operator,要不要传入szClassFullName
    public GUID saveApplicationNode(ApplicationNodeWideData applicationNodeInformation){
        GenericDynamicFactory genericDynamicFactory = new GenericDynamicFactory();
        try {
            Object nodeInformation = genericDynamicFactory.loadInstance("com.walnut.sparta.pojo.ApplicationFunctionalNodeInformation", null, null);
            Class<?> nodeInformationClass = nodeInformation.getClass();
            MetaNodeOperator nodeOperation = this.metaNodeOperatorProxy.getNodeOperation(nodeInformationClass.getName());
            return nodeOperation.insert(applicationNodeInformation);
        }
        catch (ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new ProxyProvokeHandleException(e);
        }
    }

    public GUID saveServiceNode(ServiceNodeWideData serviceNodeInformation){
        GenericDynamicFactory genericDynamicFactory = new GenericDynamicFactory();
        try {
            Object nodeInformation = genericDynamicFactory.loadInstance("com.walnut.sparta.pojo.ServiceFunctionalNodeInformation", null, null);
            Class<?> nodeInformationClass = nodeInformation.getClass();
            MetaNodeOperator nodeOperation = this.metaNodeOperatorProxy.getNodeOperation(nodeInformationClass.getName());
            return nodeOperation.insert(serviceNodeInformation);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public GUID saveClassifNode(ClassificationNodeWideData classifNodeInformation){
        GenericDynamicFactory genericDynamicFactory = new GenericDynamicFactory();
        try {
            Object nodeInformation = genericDynamicFactory.loadInstance("com.walnut.sparta.pojo.ClassifFunctionalNodeInformation", null, null);
            Class<?> nodeInformationClass = nodeInformation.getClass();
            MetaNodeOperator nodeOperation = this.metaNodeOperatorProxy.getNodeOperation(nodeInformationClass.getName());
            return nodeOperation.insert(classifNodeInformation);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }


    //删除节点
    public void deleteNode(GUID UUID){
        GUIDDistributedScopeNode node = this.scopeTreeManipulator.selectNode(UUID);
        UOI type = node.getType();
        GenericDynamicFactory genericDynamicFactory = new GenericDynamicFactory();
        try {
            Object nodeInformation = genericDynamicFactory.loadInstance(type.getObjectName(), null, null);
            Class<?> nodeInformationClass = nodeInformation.getClass();
            MetaNodeOperator nodeOperation = this.metaNodeOperatorProxy.getNodeOperation(nodeInformationClass.getName());
            nodeOperation.remove(UUID);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    //查找节点信息
    public ServiceTreeNode selectNode(GUID guid){
        //先查看缓存表中是否存在路径信息，不存在则补齐
        String path = this.scopeTreeManipulator.selectPath(guid);
        if (path==null){
            GUIDDistributedScopeNode node = this.scopeTreeManipulator.selectNode(guid);
            String nodeName = getNodeName(node);
            String pathString="";
            pathString=pathString+nodeName;
            while (node.getParentGUID() != null){
                node=this.scopeTreeManipulator.selectNode(node.getParentGUID());
                nodeName = getNodeName(node);
                pathString=nodeName + "." + pathString;
            }
            this.scopeTreeManipulator.savePath(pathString,guid);
        }
        GUIDDistributedScopeNode node = this.scopeTreeManipulator.selectNode(guid);
        UOI type = node.getType();
        GenericDynamicFactory genericDynamicFactory = new GenericDynamicFactory();
        try {
            Object nodeInformation = genericDynamicFactory.loadInstance(type.getObjectName(), null, null);
            Class<?> nodeInformationClass = nodeInformation.getClass();
            MetaNodeOperator nodeOperation = this.metaNodeOperatorProxy.getNodeOperation(nodeInformationClass.getName());
            return nodeOperation.get(guid);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private String getNodeName(GUIDDistributedScopeNode node){
        UOI type = node.getType();
        GenericDynamicFactory genericDynamicFactory = new GenericDynamicFactory();
        try {
            Object nodeInformation = genericDynamicFactory.loadInstance(type.getObjectName(), null, null);
            Class<?> nodeInformationClass = nodeInformation.getClass();
            MetaNodeOperator nodeOperation = this.metaNodeOperatorProxy.getNodeOperation(nodeInformationClass.getName());
            ServiceTreeNode nodeWideData = nodeOperation.get(node.getGuid());
            return nodeWideData.getName();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private void updatePath(GUID guid){
        GUIDDistributedScopeNode node = this.scopeTreeManipulator.selectNode(guid);
        String nodeName = getNodeName(node);
        String pathString="";
        pathString=pathString+nodeName;
        while (node.getParentGUID() != null){
            node=this.scopeTreeManipulator.selectNode(node.getParentGUID());
            nodeName = getNodeName(node);
            pathString=nodeName + "." + pathString;
        }
        this.scopeTreeManipulator.updatePath(guid,pathString);
    }

    public ServiceTreeNode parsePath(String path) {
        GenericDistributedScopeTree distributedScopeTree = new GenericDistributedScopeTree(this.scopeTreeManipulator,
                this.applicationNodeManipulator,
                this.serviceNodeManipulator,
                this.classifNodeManipulator,
                new MetaNodeOperatorProxy());
        // 先查看缓存表中是否存在路径信息
        GUID guid = this.scopeTreeManipulator.parsePath(path);
        if (guid != null) {
            return selectNode(guid);
        }

        // 如果不存在，则根据路径信息获取节点信息并且更新缓存表
        // 分割路径，并处理括号
        String[] parts = processPath(path).split("\\.");

        // 根据最后一个节点尝试查找 ServiceNode
        List<GenericServiceNode> genericServiceNodes = this.serviceNodeManipulator.fetchServiceNodeByName(parts[parts.length - 1]);
        for (GenericServiceNode genericServiceNode : genericServiceNodes) {
            String nodePath = distributedScopeTree.getPath(genericServiceNode.getGuid());
            if (nodePath.equals(path)) {
                return selectNode(genericServiceNode.getGuid());
            }
        }

        // 根据最后一个节点尝试查找 ApplicationNode
        List<GenericApplicationNode> genericApplicationNodes = this.applicationNodeManipulator.fetchApplicationNodeByName(parts[parts.length - 1]);
        for (GenericApplicationNode genericApplicationNode : genericApplicationNodes) {
            String nodePath = distributedScopeTree.getPath(genericApplicationNode.getGuid());
            if (nodePath.equals(path)) {
                return selectNode(genericApplicationNode.getGuid());
            }
        }

        // 根据最后一个节点尝试查找 ClassificationNode
        List<GenericClassificationNode> genericClassificationNodes = this.classifNodeManipulator.fetchClassifNodeByName(parts[parts.length - 1]);
        for (GenericClassificationNode genericClassificationNode : genericClassificationNodes) {
            String nodePath = distributedScopeTree.getPath(genericClassificationNode.getGuid());
            if (nodePath.equals(path)) {
                return selectNode(genericClassificationNode.getGuid());
            }
        }

        return null;
    }

    private String processPath(String path) {
        // 使用正则表达式移除所有的括号及其内容
        return path.replaceAll("\\(.*?\\)", "");
    }
}
