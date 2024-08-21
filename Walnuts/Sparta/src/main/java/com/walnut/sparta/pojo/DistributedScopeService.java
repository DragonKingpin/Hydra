package com.walnut.sparta.pojo;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.GenericApplicationDescription;
import com.pinecone.hydra.service.GenericApplicationNode;
import com.pinecone.hydra.service.GenericClassificationNode;
import com.pinecone.hydra.service.GenericClassificationRules;
import com.pinecone.hydra.service.GenericNodeMetadata;
import com.pinecone.hydra.service.GenericServiceDescription;
import com.pinecone.hydra.service.GenericServiceNode;
import com.pinecone.hydra.service.NodeInformation;
import com.pinecone.hydra.service.ServiceTreeMapper;
import com.pinecone.hydra.unit.udsn.ApplicationDescriptionManipinate;
import com.pinecone.hydra.unit.udsn.ApplicationNodeManipinate;
import com.pinecone.hydra.unit.udsn.ClassifNodeManipinate;
import com.pinecone.hydra.unit.udsn.ClassifRulesManipinate;
import com.pinecone.hydra.unit.udsn.DistrubuteScopeTreeDataManipinate;
import com.pinecone.hydra.unit.udsn.GUIDDistributedScopeNode;
import com.pinecone.hydra.unit.udsn.NodeMetadataManipinate;
import com.pinecone.hydra.unit.udsn.ServiceDescriptionManipinate;
import com.pinecone.hydra.unit.udsn.ServiceNodeManipinate;
import com.pinecone.ulf.util.id.UUIDBuilder;
import com.pinecone.ulf.util.id.UidGenerator;

import java.util.List;

public class DistributedScopeService implements Pinenut {
    private ServiceTreeMapper serviceTreeMapper;
    private ApplicationDescriptionManipinate applicationDescriptionManipinate;
    private ApplicationNodeManipinate applicationNodeManipinate;
    private ServiceDescriptionManipinate serviceDescriptionManipinate;
    private ServiceNodeManipinate serviceNodeManipinate;
    private ClassifRulesManipinate classifRulesManipinate;
    private ClassifNodeManipinate classifNodeManipinate;
    private NodeMetadataManipinate nodeMetadataManipinate;

    public DistributedScopeService(ServiceTreeMapper serviceTreeMapper,ApplicationNodeManipinate applicationNodeManipinate,
                                   ApplicationDescriptionManipinate applicationDescriptionManipinate,ServiceDescriptionManipinate serviceDescriptionManipinate,
                                   ServiceNodeManipinate serviceNodeManipinate,ClassifNodeManipinate classifNodeManipinate,ClassifRulesManipinate classifRulesManipinate,
                                   NodeMetadataManipinate nodeMetadataManipinate){
        this.serviceTreeMapper=serviceTreeMapper;
        this.applicationDescriptionManipinate=applicationDescriptionManipinate;
        this.applicationNodeManipinate=applicationNodeManipinate;
        this.serviceDescriptionManipinate=serviceDescriptionManipinate;
        this.serviceNodeManipinate=serviceNodeManipinate;
        this.classifNodeManipinate=classifNodeManipinate;
        this.classifRulesManipinate=classifRulesManipinate;
        this.nodeMetadataManipinate=nodeMetadataManipinate;
    }

    private final static String ApplicationNode="applicationNode";

    private final static String ServiceNode="serviceNode";

    private final static String ClassifNode="classifNode";

    //保存节点
    public GUID saveApplicationNode(ApplicationNodeInformation applicationNodeInformation){
        //将信息写入数据库
        //将节点信息存入应用节点表
        UidGenerator uidGenerator= UUIDBuilder.getBuilder();
        GUID applicationNodeGUID = uidGenerator.getGUID72();
        GenericApplicationNode applicationNode = applicationNodeInformation.getApplicationNode();
        applicationNode.setUUID(applicationNodeGUID);
        this.applicationNodeManipinate.saveApplicationNode(applicationNode);
        //将应用节点基础信息存入信息表
        GUID descriptionGUID = uidGenerator.getGUID72();
        GenericApplicationDescription applicationDescription = applicationNodeInformation.getApplicationDescription();
        applicationDescription.setUUID(descriptionGUID);
        this.applicationDescriptionManipinate.saveApplicationDescription(applicationDescription);
        //将应用元信息存入元信息表
        GUID metadataGUID = uidGenerator.getGUID72();
        GenericNodeMetadata metadata = applicationNodeInformation.getMetadata();
        metadata.setUUID(metadataGUID);
        this.nodeMetadataManipinate.saveNodeMetadata(metadata);
        //将节点信息存入主表
        GUIDDistributedScopeNode node = new GUIDDistributedScopeNode();
        node.setBaseDataUUID(descriptionGUID);
        node.setUUID(applicationNodeGUID);
        node.setNodeMetadataUUID(metadataGUID);
        node.setType(ApplicationNode);
        this.serviceTreeMapper.saveNode(node);
        return applicationNodeGUID;
    }

    public GUID saveServiceNode(ServiceNodeInformation serviceNodeInformation){
        //将信息写入数据库
        //将节点信息存入应用节点表
        UidGenerator uidGenerator= UUIDBuilder.getBuilder();
        GUID serviceNodeGUID = uidGenerator.getGUID72();
        GenericServiceNode serviceNode = serviceNodeInformation.getServiceNode();
        serviceNode.setUUID(serviceNodeGUID);
        this.serviceNodeManipinate.saveServiceNode(serviceNode);
        //将应用节点基础信息存入信息表
        GUID descriptionGUID = uidGenerator.getGUID72();
        GenericServiceDescription serviceDescription = serviceNodeInformation.getServiceDescription();
        serviceDescription.setUUID(descriptionGUID);
        this.serviceDescriptionManipinate.saveServiceDescription(serviceDescription);
        //将应用元信息存入元信息表
        GUID metadataGUID = uidGenerator.getGUID72();
        GenericNodeMetadata metadata = serviceNodeInformation.getNodeMetadata();
        metadata.setUUID(metadataGUID);
        this.nodeMetadataManipinate.saveNodeMetadata(metadata);
        //将节点信息存入主表
        GUIDDistributedScopeNode node = new GUIDDistributedScopeNode();
        node.setBaseDataUUID(descriptionGUID);
        node.setUUID(serviceNodeGUID);
        node.setNodeMetadataUUID(metadataGUID);
        node.setType(ServiceNode);
        this.serviceTreeMapper.saveNode(node);
        return serviceNodeGUID;
    }

    public GUID saveClassifNode(ClassifNodeInformation classifNodeInformation){
        //将应用节点基础信息存入信息表
        UidGenerator uidGenerator= UUIDBuilder.getBuilder();
        GUID descriptionGUID = uidGenerator.getGUID72();
        GenericClassificationRules classificationRules = classifNodeInformation.getClassificationRules();
        classificationRules.setUUID(descriptionGUID);
        this.classifRulesManipinate.saveClassifRules(classificationRules);
        //将信息写入数据库
        //将节点信息存入应用节点表
        GUID classifNodeGUID = uidGenerator.getGUID72();
        GenericClassificationNode classificationNode = classifNodeInformation.getClassificationNode();
        classificationNode.setUUID(classifNodeGUID);
        classificationNode.setRulesUUID(descriptionGUID);
        this.classifNodeManipinate.saveClassifNode(classificationNode);
        //将应用元信息存入元信息表
        GUID metadataGUID = uidGenerator.getGUID72();
        GenericNodeMetadata metadata = classifNodeInformation.getNodeMetadata();
        metadata.setUUID(metadataGUID);
        this.nodeMetadataManipinate.saveNodeMetadata(metadata);
        //将节点信息存入主表
        GUIDDistributedScopeNode node = new GUIDDistributedScopeNode();
        node.setBaseDataUUID(descriptionGUID);
        node.setUUID(classifNodeGUID);
        node.setNodeMetadataUUID(metadataGUID);
        node.setType(ClassifNode);
        this.serviceTreeMapper.saveNode(node);
        return classifNodeGUID;
    }
    //todo 大抽象，实现后可以让用户自己定义节点类型并且保存节点
    public GUID saveNode(){
        return null;
    }

    //删除节点
    public void deleteNode(GUID UUID){
        //获取节点信息
        GUIDDistributedScopeNode node = this.serviceTreeMapper.selectNode(UUID);
        //根据类型删除节点
        //todo 改为适配器模式
        String type = node.getType();
//        if (type.equals(ApplicationNode)){
//            this.distrubuteScopeTreeDataManipinate.deleteApplicationDescription(node.getBaseDataUUID());
//            this.distrubuteScopeTreeDataManipinate.deleteNodeMetadata(node.getNodeMetadataUUID());
//            this.distrubuteScopeTreeDataManipinate.deleteApplicationNode(node.getUUID());
//            this.serviceTreeMapper.deleteNode(node.getUUID());
//        }else if (type.equals(ServiceNode)){
//            this.distrubuteScopeTreeDataManipinate.deleteServiceNode(node.getUUID());
//            this.distrubuteScopeTreeDataManipinate.deleteServiceDescription(node.getBaseDataUUID());
//            this.serviceTreeMapper.deleteNode(node.getUUID());
//            this.distrubuteScopeTreeDataManipinate.deleteNodeMetadata(node.getNodeMetadataUUID());
//        }else if(type.equals(ClassifNode)){
//            this.distrubuteScopeTreeDataManipinate.deleteClassifNode(node.getUUID());
//            this.distrubuteScopeTreeDataManipinate.deleteClassifRules(node.getUUID());
//            this.serviceTreeMapper.deleteNode(node.getUUID());
//            this.distrubuteScopeTreeDataManipinate.deleteNodeMetadata(node.getNodeMetadataUUID());
//        }
        //更新路径逻辑
        List<GUIDDistributedScopeNode> childNodes = this.serviceTreeMapper.selectChildNode(node.getUUID());
        for (GUIDDistributedScopeNode childNode:childNodes){
            childNode.setParentUUID(node.getParentUUID());
            this.serviceTreeMapper.updateNode(childNode);
            updatePath(childNode.getUUID());
        }

    }

    //查找节点信息
    public NodeInformation selectNode(GUID UUID){
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
        //先搜索出节点信息再根据节点类型进行完善
        // todo 继承机制还没有实现,改成适配器模式
        GUIDDistributedScopeNode node = this.serviceTreeMapper.selectNode(UUID);
        if (node.getType().equals(ApplicationNode)){
            ApplicationNodeInformation applicationNodeInformation = new ApplicationNodeInformation();
            GenericApplicationDescription applicationDescription = this.applicationDescriptionManipinate.selectApplicationDescription(node.getBaseDataUUID());
            GenericApplicationNode applicationNode = this.applicationNodeManipinate.selectApplicationNode(node.getUUID());
            GenericNodeMetadata nodeMetadata = this.nodeMetadataManipinate.selectNodeMetadata(node.getNodeMetadataUUID());
            applicationNodeInformation.setApplicationDescription(applicationDescription);
            applicationNodeInformation.setMetadata(nodeMetadata);
            applicationNodeInformation.setApplicationNode(applicationNode);
            return applicationNodeInformation;
        }else if(node.getType().equals(ServiceNode)){
            ServiceNodeInformation serviceNodeInformation = new ServiceNodeInformation();
            serviceNodeInformation.setServiceNode(this.serviceNodeManipinate.selectServiceNode(node.getUUID()));
            serviceNodeInformation.setNodeMetadata(this.nodeMetadataManipinate.selectNodeMetadata(node.getNodeMetadataUUID()));
            serviceNodeInformation.setServiceDescription(this.serviceDescriptionManipinate.selectServiceDescription(node.getBaseDataUUID()));
            return serviceNodeInformation;
        } else if (node.getType().equals(ClassifNode)) {
            ClassifNodeInformation classifNodeInformation = new ClassifNodeInformation();
            classifNodeInformation.setClassificationNode(this.classifNodeManipinate.selectClassifNode(node.getUUID()));
            classifNodeInformation.setNodeMetadata(this.nodeMetadataManipinate.selectNodeMetadata(node.getNodeMetadataUUID()));
            classifNodeInformation.setClassificationRules(this.classifRulesManipinate.selectClassifRules(node.getBaseDataUUID()));
            return classifNodeInformation;
        }
        return null;
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
    public NodeInformation parsePath(String path){
        DistributedScopeTree distributedScopeTree = new DistributedScopeTree(this.serviceTreeMapper,this.applicationNodeManipinate,this.serviceNodeManipinate,this.classifNodeManipinate);
        //先查看缓存表中是否存在路径信息
        GUID guid = this.serviceTreeMapper.parsePath(path);
        if (guid!=null){
            return selectNode(guid);
        }
        //如果不存在则根据路径信息获取节点信息并且更新缓存表
        String[] parts = path.split("\\.");
        List<GenericServiceNode> genericServiceNodes = this.serviceNodeManipinate.selectServiceNodeByName(parts[parts.length - 1]);
        for (GenericServiceNode genericServiceNode:genericServiceNodes){
            String nodePath = distributedScopeTree.getPath(genericServiceNode.getUUID());
            if (nodePath.equals(path)){
                return selectNode(genericServiceNode.getUUID());
            }
        }
        List<GenericApplicationNode> genericApplicationNodes = this.applicationNodeManipinate.selectApplicationNodeByName(parts[parts.length - 1]);
        for (GenericApplicationNode genericApplicationNode:genericApplicationNodes){
            String nodePath = distributedScopeTree.getPath(genericApplicationNode.getUUID());
            if (nodePath.equals(path)){
                return selectNode(genericApplicationNode.getUUID());
            }
        }
        List<GenericClassificationNode> genericClassificationNodes = this.classifNodeManipinate.selectClassifNodeByName(parts[parts.length - 1]);
        for(GenericClassificationNode genericClassificationNode:genericClassificationNodes){
            String nodePath = distributedScopeTree.getPath(genericClassificationNode.getUUID());
            if (nodePath.equals(path)){
                return selectNode(genericClassificationNode.getUUID());
            }
        }
        return null;
    }

}
