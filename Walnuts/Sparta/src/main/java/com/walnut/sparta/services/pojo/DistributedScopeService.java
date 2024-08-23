package com.walnut.sparta.services.pojo;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.lang.GenericDynamicFactory;
import com.pinecone.hydra.service.tree.nodes.GenericApplicationNode;
import com.pinecone.hydra.service.tree.nodes.GenericClassificationNode;
import com.pinecone.hydra.service.tree.nodes.GenericServiceNode;
import com.pinecone.hydra.service.tree.FunctionalNodeInformation;
import com.pinecone.hydra.service.tree.FunctionalNodeOperation;
import com.pinecone.hydra.service.tree.ServiceTreeMapper;
import com.pinecone.hydra.service.tree.source.ApplicationNodeManipulator;
import com.pinecone.hydra.service.tree.source.ClassifNodeManipulator;
import com.pinecone.hydra.unit.udsn.GUIDDistributedScopeNode;
import com.pinecone.hydra.service.tree.source.ServiceNodeManipinate;
import com.pinecone.hydra.service.tree.FunctionalNodeFactory;
import com.pinecone.hydra.unit.udsn.GenericDistributedScopeTree;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class DistributedScopeService implements Pinenut {
    private ServiceTreeMapper serviceTreeMapper;
    private ApplicationNodeManipulator applicationNodeManipinate;
    private ServiceNodeManipinate serviceNodeManipinate;
    private ClassifNodeManipulator classifNodeManipinate;
    private FunctionalNodeOperation classifFunctionalNodeOperation;
    private FunctionalNodeOperation applicationFunctionalNodeOperation;
    private FunctionalNodeOperation serviceFunctionalNodeOperation;
    private FunctionalNodeFactory nodeAdapter;

    public DistributedScopeService(FunctionalNodeOperation classifFunctionalNodeOperation, FunctionalNodeOperation applicationFunctionalNodeOperation,
                                   FunctionalNodeOperation serviceFunctionalNodeOperation, ServiceTreeMapper serviceTreeMapper,
                                   ApplicationNodeManipulator applicationNodeManipinate, ServiceNodeManipinate serviceNodeManipinate, ClassifNodeManipulator classifNodeManipinate){
        this.classifFunctionalNodeOperation = classifFunctionalNodeOperation;
        this.applicationFunctionalNodeOperation = applicationFunctionalNodeOperation;
        this.serviceFunctionalNodeOperation = serviceFunctionalNodeOperation;
        this.serviceTreeMapper=serviceTreeMapper;
        this.applicationNodeManipinate=applicationNodeManipinate;
        this.serviceNodeManipinate=serviceNodeManipinate;
        this.classifNodeManipinate=classifNodeManipinate;
        nodeAdapter=new FunctionalNodeFactory();
        nodeAdapter.registration(ApplicationFunctionalNodeInformation.class, applicationFunctionalNodeOperation);
        nodeAdapter.registration(ClassifFunctionalNodeInformation.class, classifFunctionalNodeOperation);
        nodeAdapter.registration(ServiceFunctionalNodeInformation.class, serviceFunctionalNodeOperation);
    }

    private final static String ApplicationNode="applicationNode";

    private final static String ServiceNode="serviceNode";

    private final static String ClassifNode="classifNode";

    //保存节点
    public GUID saveApplicationNode(ApplicationFunctionalNodeInformation applicationNodeInformation){
        GenericDynamicFactory genericDynamicFactory = new GenericDynamicFactory();
        try {
            Object nodeInformation = genericDynamicFactory.loadInstance("com.walnut.sparta.pojo.ApplicationFunctionalNodeInformation", null, null);
            Class<?> nodeInformationClass = nodeInformation.getClass();
            FunctionalNodeOperation nodeOperation = nodeAdapter.getNodeOperation(nodeInformationClass);
            return nodeOperation.addOperation(applicationNodeInformation);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public GUID saveServiceNode(ServiceFunctionalNodeInformation serviceNodeInformation){
        GenericDynamicFactory genericDynamicFactory = new GenericDynamicFactory();
        try {
            Object nodeInformation = genericDynamicFactory.loadInstance("com.walnut.sparta.pojo.ServiceFunctionalNodeInformation", null, null);
            Class<?> nodeInformationClass = nodeInformation.getClass();
            FunctionalNodeOperation nodeOperation = nodeAdapter.getNodeOperation(nodeInformationClass);
            return nodeOperation.addOperation(serviceNodeInformation);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public GUID saveClassifNode(ClassifFunctionalNodeInformation classifNodeInformation){
        GenericDynamicFactory genericDynamicFactory = new GenericDynamicFactory();
        try {
            Object nodeInformation = genericDynamicFactory.loadInstance("com.walnut.sparta.pojo.ClassifFunctionalNodeInformation", null, null);
            Class<?> nodeInformationClass = nodeInformation.getClass();
            FunctionalNodeOperation nodeOperation = nodeAdapter.getNodeOperation(nodeInformationClass);
            return nodeOperation.addOperation(classifNodeInformation);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
    //todo 大抽象，实现后可以让用户自己定义节点类型并且保存节点
    public GUID saveNode(){
        return null;
    }

    //删除节点
    public void deleteNode(GUID UUID){
        GUIDDistributedScopeNode node = this.serviceTreeMapper.selectNode(UUID);
        String type = node.getType();
        GenericDynamicFactory genericDynamicFactory = new GenericDynamicFactory();
        try {
            Object nodeInformation = genericDynamicFactory.loadInstance(type, null, null);
            Class<?> nodeInformationClass = nodeInformation.getClass();
            FunctionalNodeOperation nodeOperation = nodeAdapter.getNodeOperation(nodeInformationClass);
            nodeOperation.deleteOperation(UUID);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    //查找节点信息
    public FunctionalNodeInformation selectNode(GUID UUID){
        //先查看缓存表中是否存在路径信息，不存在则补齐
        String path = this.serviceTreeMapper.selectPath(UUID);
        if (path==null){
            GUIDDistributedScopeNode node = this.serviceTreeMapper.selectNode(UUID);
            //如果是分类节点还要查询分类节点的分类表
            System.out.println("查询到节点:"+node);
            String nodeName = getNodeName(node);
            String pathString="";
            if(node.getType().equals(ClassifNode)){
                String classifNodeClassif = serviceTreeMapper.getClassifNodeClassif(node.getUUID());
                if (classifNodeClassif!=null){
                    pathString=pathString+"("+classifNodeClassif+")"+nodeName;
                }else {
                    pathString=pathString+"("+"默认分类"+")"+nodeName;
                }
            }else {
                pathString=pathString+nodeName;
            }
            while (node.getParentUUID() != null){
                node=this.serviceTreeMapper.selectNode(node.getParentUUID());
                System.out.println("查询到节点:"+node);
                nodeName = getNodeName(node);
                if(node.getType().equals(ClassifNode)){
                    String classifNodeClassif = serviceTreeMapper.getClassifNodeClassif(node.getUUID());
                    if (classifNodeClassif!=null){
                        pathString="("+classifNodeClassif+")"+nodeName+"."+pathString;
                    }else {
                        pathString="("+"默认分类"+")"+nodeName+"."+pathString;
                    }
                }else {
                    pathString=nodeName + "." + pathString;
                }
            }
            this.serviceTreeMapper.savePath(pathString,UUID);
        }
        GUIDDistributedScopeNode node = this.serviceTreeMapper.selectNode(UUID);
        String type = node.getType();
        GenericDynamicFactory genericDynamicFactory = new GenericDynamicFactory();
        try {
            Object nodeInformation = genericDynamicFactory.loadInstance(type, null, null);
            Class<?> nodeInformationClass = nodeInformation.getClass();
            FunctionalNodeOperation nodeOperation = nodeAdapter.getNodeOperation(nodeInformationClass);
            return nodeOperation.SelectOperation(UUID);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
    private String getNodeName(GUIDDistributedScopeNode node){
        if (node.getType().equals(ApplicationNode)){
            return this.applicationNodeManipinate.selectApplicationNode(node.getUUID()).getName();
        }
        else if(node.getType().equals(ServiceNode)){
            return this.serviceNodeManipinate.selectServiceNode(node.getUUID()).getName();
        }
        else if (node.getType().equals(ClassifNode)) {
            return this.classifNodeManipinate.selectClassifNode(node.getUUID()).getName();
        }
        return null;
    }
    private void updatePath(GUID UUID){
        GUIDDistributedScopeNode node = this.serviceTreeMapper.selectNode(UUID);
        //如果是分类节点还要查询分类节点的分类表
        System.out.println("查询到节点:"+node);
        String nodeName = getNodeName(node);
        String pathString="";
        if(node.getType().equals(ClassifNode)){
            String classifNodeClassif = serviceTreeMapper.getClassifNodeClassif(node.getUUID());
            if (classifNodeClassif==null){
                pathString=pathString+"("+classifNodeClassif+")"+nodeName;
            }else {
                pathString=pathString+"("+"默认分类"+")"+nodeName;
            }
        }else {
            pathString=pathString+nodeName;
        }
        while (node.getParentUUID() != null){
            node=this.serviceTreeMapper.selectNode(node.getParentUUID());
            System.out.println("查询到节点:"+node);
            nodeName = getNodeName(node);
            if(node.getType().equals(ClassifNode)){
                String classifNodeClassif = serviceTreeMapper.getClassifNodeClassif(node.getUUID());
                if (classifNodeClassif==null){
                    pathString="("+classifNodeClassif+")"+nodeName+"."+pathString;
                }else {
                    pathString="("+"默认分类"+")"+nodeName+"."+pathString;
                }
            }else {
                pathString=nodeName + "." + pathString;
            }
        }
        this.serviceTreeMapper.updatePath(UUID,pathString);
    }
    public FunctionalNodeInformation parsePath(String path) {
        GenericDistributedScopeTree distributedScopeTree = new GenericDistributedScopeTree(this.serviceTreeMapper,
                this.applicationNodeManipinate,
                this.serviceNodeManipinate,
                this.classifNodeManipinate);
        // 先查看缓存表中是否存在路径信息
        GUID guid = this.serviceTreeMapper.parsePath(path);
        if (guid != null) {
            return selectNode(guid);
        }

        // 如果不存在，则根据路径信息获取节点信息并且更新缓存表
        // 分割路径，并处理括号
        String[] parts = processPath(path).split("\\.");

        // 根据最后一个节点尝试查找 ServiceNode
        List<GenericServiceNode> genericServiceNodes = this.serviceNodeManipinate.selectServiceNodeByName(parts[parts.length - 1]);
        for (GenericServiceNode genericServiceNode : genericServiceNodes) {
            String nodePath = distributedScopeTree.getPath(genericServiceNode.getUUID());
            if (nodePath.equals(path)) {
                return selectNode(genericServiceNode.getUUID());
            }
        }

        // 根据最后一个节点尝试查找 ApplicationNode
        List<GenericApplicationNode> genericApplicationNodes = this.applicationNodeManipinate.selectApplicationNodeByName(parts[parts.length - 1]);
        for (GenericApplicationNode genericApplicationNode : genericApplicationNodes) {
            String nodePath = distributedScopeTree.getPath(genericApplicationNode.getUUID());
            if (nodePath.equals(path)) {
                return selectNode(genericApplicationNode.getUUID());
            }
        }

        // 根据最后一个节点尝试查找 ClassificationNode
        List<GenericClassificationNode> genericClassificationNodes = this.classifNodeManipinate.selectClassifNodeByName(parts[parts.length - 1]);
        for (GenericClassificationNode genericClassificationNode : genericClassificationNodes) {
            String nodePath = distributedScopeTree.getPath(genericClassificationNode.getUUID());
            if (nodePath.equals(path)) {
                return selectNode(genericClassificationNode.getUUID());
            }
        }

        return null;
    }
    private String processPath(String path) {
        // 使用正则表达式移除所有的括号及其内容
        return path.replaceAll("\\(.*?\\)", "");
    }
}
