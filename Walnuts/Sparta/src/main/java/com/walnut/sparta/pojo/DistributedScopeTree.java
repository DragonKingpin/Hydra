package com.walnut.sparta.pojo;


import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.GenericApplicationDescription;
import com.pinecone.hydra.service.GenericApplicationNode;
import com.pinecone.hydra.service.GenericClassificationNode;
import com.pinecone.hydra.service.GenericClassificationRules;
import com.pinecone.hydra.service.NodeInformation;
import com.pinecone.hydra.service.ServiceTreeMapper;
import com.pinecone.hydra.unit.udsn.DistrubuteScopeTreeDataManipinate;
import com.pinecone.hydra.unit.udsn.GUIDDistributedScopeNode;
import com.pinecone.hydra.service.GenericNodeMetadata;
import com.pinecone.hydra.service.GenericServiceDescription;
import com.pinecone.hydra.service.GenericServiceNode;
import com.pinecone.ulf.util.id.UUIDBuilder;
import com.pinecone.ulf.util.id.UidGenerator;

import java.util.List;

/**
 * 提供服务树的相应方法
 */public class DistributedScopeTree implements Pinenut {
     private ServiceTreeMapper serviceTreeMapper;

     private DistrubuteScopeTreeDataManipinate distrubuteScopeTreeDataManipinate;

     public DistributedScopeTree(ServiceTreeMapper serviceTreeMapper,DistrubuteScopeTreeDataManipinate distrubuteScopeTreeDataManipinate){
         this.serviceTreeMapper=serviceTreeMapper;
         this.distrubuteScopeTreeDataManipinate=distrubuteScopeTreeDataManipinate;
     }

    private final static String ApplicationNode="applicationNode";

    private final static String ServiceNode="serviceNode";

    private final static String ClassifNode="classifNode";

    //保存节点
    public void saveApplicationNode(ApplicationNodeInformation applicationNodeInformation){
        //将信息写入数据库
        //将节点信息存入应用节点表
        UidGenerator uidGenerator= UUIDBuilder.getBuilder();
        GUID applicationNodeGUID = uidGenerator.getGUID72();
        GenericApplicationNode applicationNode = applicationNodeInformation.getApplicationNode();
        applicationNode.setUUID(applicationNodeGUID);
        this.distrubuteScopeTreeDataManipinate.saveApplicationNode(applicationNode);
        //将应用节点基础信息存入信息表
        GUID descriptionGUID = uidGenerator.getGUID72();
        GenericApplicationDescription applicationDescription = applicationNodeInformation.getApplicationDescription();
        applicationDescription.setUUID(descriptionGUID);
        this.distrubuteScopeTreeDataManipinate.saveApplicationDescription(applicationDescription);
        //将应用元信息存入元信息表
        GUID metadataGUID = uidGenerator.getGUID72();
        GenericNodeMetadata metadata = applicationNodeInformation.getMetadata();
        metadata.setUUID(metadataGUID);
        this.distrubuteScopeTreeDataManipinate.saveNodeMetadata(metadata);
        //将节点信息存入主表
        GUIDDistributedScopeNode node = new GUIDDistributedScopeNode();
        node.setBaseDataUUID(descriptionGUID);
        node.setUUID(applicationNodeGUID);
        node.setNodeMetadataUUID(metadataGUID);
        node.setType(ApplicationNode);
        this.serviceTreeMapper.saveNode(node);
    }

    public void saveServiceNode(ServiceNodeInformation serviceNodeInformation){
        //将信息写入数据库
        //将节点信息存入应用节点表
        UidGenerator uidGenerator= UUIDBuilder.getBuilder();
        GUID serviceNodeGUID = uidGenerator.getGUID72();
        GenericServiceNode serviceNode = serviceNodeInformation.getServiceNode();
        serviceNode.setUUID(serviceNodeGUID);
        this.distrubuteScopeTreeDataManipinate.saveServiceNode(serviceNode);
        //将应用节点基础信息存入信息表
        GUID descriptionGUID = uidGenerator.getGUID72();
        GenericServiceDescription serviceDescription = serviceNodeInformation.getServiceDescription();
        serviceDescription.setUUID(descriptionGUID);
        this.distrubuteScopeTreeDataManipinate.saveServiceDescription(serviceDescription);
        //将应用元信息存入元信息表
        GUID metadataGUID = uidGenerator.getGUID72();
        GenericNodeMetadata metadata = serviceNodeInformation.getNodeMetadata();
        metadata.setUUID(metadataGUID);
        this.distrubuteScopeTreeDataManipinate.saveNodeMetadata(metadata);
        //将节点信息存入主表
        GUIDDistributedScopeNode node = new GUIDDistributedScopeNode();
        node.setBaseDataUUID(descriptionGUID);
        node.setUUID(serviceNodeGUID);
        node.setNodeMetadataUUID(metadataGUID);
        node.setType(ServiceNode);
        this.serviceTreeMapper.saveNode(node);
    }

    public void saveClassifNode(ClassifNodeInformation classifNodeInformation){
        //将应用节点基础信息存入信息表
        UidGenerator uidGenerator= UUIDBuilder.getBuilder();
        GUID descriptionGUID = uidGenerator.getGUID72();
        GenericClassificationRules classificationRules = classifNodeInformation.getClassificationRules();
        classificationRules.setUUID(descriptionGUID);
        this.distrubuteScopeTreeDataManipinate.saveClassifRules(classificationRules);
        //将信息写入数据库
        //将节点信息存入应用节点表
        GUID classifNodeGUID = uidGenerator.getGUID72();
        GenericClassificationNode classificationNode = classifNodeInformation.getClassificationNode();
        classificationNode.setUUID(classifNodeGUID);
        classificationNode.setRulesUUID(descriptionGUID);
        this.distrubuteScopeTreeDataManipinate.saveClassifNode(classificationNode);
        //将应用元信息存入元信息表
        GUID metadataGUID = uidGenerator.getGUID72();
        GenericNodeMetadata metadata = classifNodeInformation.getNodeMetadata();
        metadata.setUUID(metadataGUID);
        this.distrubuteScopeTreeDataManipinate.saveNodeMetadata(metadata);
        //将节点信息存入主表
        GUIDDistributedScopeNode node = new GUIDDistributedScopeNode();
        node.setBaseDataUUID(descriptionGUID);
        node.setUUID(classifNodeGUID);
        node.setNodeMetadataUUID(metadataGUID);
        node.setType(ClassifNode);
        this.serviceTreeMapper.saveNode(node);
    }

    //删除节点
    public void deleteNode(GUID UUID){
        //获取节点信息
        GUIDDistributedScopeNode node = this.serviceTreeMapper.selectNode(UUID);
        //根据类型删除节点
        String type = node.getType();
        if (type.equals(ApplicationNode)){
            this.distrubuteScopeTreeDataManipinate.deleteApplicationDescription(node.getBaseDataUUID());
            this.distrubuteScopeTreeDataManipinate.deleteNodeMetadata(node.getNodeMetadataUUID());
            this.distrubuteScopeTreeDataManipinate.deleteApplicationNode(node.getUUID());
            this.serviceTreeMapper.deleteNode(node.getUUID());
        }else if (type.equals(ServiceNode)){
            this.distrubuteScopeTreeDataManipinate.deleteServiceNode(node.getUUID());
            this.distrubuteScopeTreeDataManipinate.deleteServiceDescription(node.getBaseDataUUID());
            this.serviceTreeMapper.deleteNode(node.getUUID());
            this.distrubuteScopeTreeDataManipinate.deleteNodeMetadata(node.getNodeMetadataUUID());
        }else if(type.equals(ClassifNode)){
            this.distrubuteScopeTreeDataManipinate.deleteClassifNode(node.getUUID());
            this.distrubuteScopeTreeDataManipinate.deleteClassifRules(node.getUUID());
            this.serviceTreeMapper.deleteNode(node.getUUID());
            this.distrubuteScopeTreeDataManipinate.deleteNodeMetadata(node.getNodeMetadataUUID());
        }
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
            String nodeName = getNodeName(node);
            String pathString="";
            pathString=pathString+nodeName;
            while (node.getParentUUID() != null){
                node=this.serviceTreeMapper.selectNode(node.getParentUUID());
                 nodeName = getNodeName(node);
                pathString=nodeName + "." + pathString;
            }
            this.serviceTreeMapper.savePath(pathString,UUID);
        }
        //先搜索出节点信息再根据节点类型进行完善
        // todo 继承机制还没有实现
        GUIDDistributedScopeNode node = this.serviceTreeMapper.selectNode(UUID);
        if (node.getType().equals(ApplicationNode)){
            ApplicationNodeInformation applicationNodeInformation = new ApplicationNodeInformation();
            GenericApplicationDescription applicationDescription = this.distrubuteScopeTreeDataManipinate.selectApplicationDescription(node.getBaseDataUUID());
            GenericApplicationNode applicationNode = this.distrubuteScopeTreeDataManipinate.selectApplicationNode(node.getUUID());
            GenericNodeMetadata nodeMetadata = this.distrubuteScopeTreeDataManipinate.selectNodeMetadata(node.getNodeMetadataUUID());
            applicationNodeInformation.setNode(node);
            applicationNodeInformation.setApplicationDescription(applicationDescription);
            applicationNodeInformation.setMetadata(nodeMetadata);
            applicationNodeInformation.setApplicationNode(applicationNode);
            return applicationNodeInformation;
        }else if(node.getType().equals(ServiceNode)){
            ServiceNodeInformation serviceNodeInformation = new ServiceNodeInformation();
            serviceNodeInformation.setNode(node);
            serviceNodeInformation.setServiceNode(this.distrubuteScopeTreeDataManipinate.selectServiceNode(node.getUUID()));
            serviceNodeInformation.setNodeMetadata(this.distrubuteScopeTreeDataManipinate.selectNodeMetadata(node.getNodeMetadataUUID()));
            serviceNodeInformation.setServiceDescription(this.distrubuteScopeTreeDataManipinate.selectServiceDescription(node.getBaseDataUUID()));
            return serviceNodeInformation;
        } else if (node.getType().equals(ClassifNode)) {
            ClassifNodeInformation classifNodeInformation = new ClassifNodeInformation();
            classifNodeInformation.setNode(node);
            classifNodeInformation.setClassificationNode(this.distrubuteScopeTreeDataManipinate.selectClassifNode(node.getUUID()));
            classifNodeInformation.setNodeMetadata(this.distrubuteScopeTreeDataManipinate.selectNodeMetadata(node.getNodeMetadataUUID()));
            classifNodeInformation.setClassificationRules(this.distrubuteScopeTreeDataManipinate.selectClassifRules(node.getBaseDataUUID()));
            return classifNodeInformation;
        }
        return null;
    }

    //打印路径信息
    public String getPath(GUID UUID){
        String path = this.serviceTreeMapper.selectPath(UUID);
        System.out.println("查找到路径："+path);
        //若不存在path信息则更新缓存表
        if (path==null){
            GUIDDistributedScopeNode node = this.serviceTreeMapper.selectNode(UUID);
            System.out.println("查询到节点:"+node);
            String nodeName = getNodeName(node);
            String pathString="";
            pathString=pathString+nodeName;
            while (node.getParentUUID() != null){
                node=this.serviceTreeMapper.selectNode(node.getParentUUID());
                System.out.println("查询到节点:"+node);
                nodeName = getNodeName(node);
                pathString=nodeName + "." + pathString;
            }
            this.serviceTreeMapper.savePath(pathString,UUID);
            return pathString;
        }
        return path;

    }

    private String getNodeName(GUIDDistributedScopeNode node){
        if (node.getType().equals(ApplicationNode)){
          return this.distrubuteScopeTreeDataManipinate.selectApplicationNode(node.getUUID()).getName();
        }else if(node.getType().equals(ServiceNode)){

            return this.distrubuteScopeTreeDataManipinate.selectServiceNode(node.getUUID()).getName();
        } else if (node.getType().equals(ClassifNode)) {

            return this.distrubuteScopeTreeDataManipinate.selectClassifNode(node.getUUID()).getName();
        }
        return null;
    }

    private void updatePath(GUID UUID){
        GUIDDistributedScopeNode node = this.serviceTreeMapper.selectNode(UUID);
        String nodeName = getNodeName(node);
        String pathString="";
        pathString=pathString+nodeName;
        while (node.getParentUUID() != null){
            node=this.serviceTreeMapper.selectNode(node.getParentUUID());
            nodeName = getNodeName(node);
            pathString=nodeName + "." + pathString;
        }
        this.serviceTreeMapper.updatePath(UUID,pathString);
    }
    public NodeInformation parsePath(String path){
        //先查看缓存表中是否存在路径信息
        GUID guid = this.serviceTreeMapper.parsePath(path);
        if (guid!=null){
            return selectNode(guid);
        }
        //如果不存在则根据路径信息获取节点信息并且更新缓存表
        String[] parts = path.split("\\.");
        List<GenericServiceNode> genericServiceNodes = this.distrubuteScopeTreeDataManipinate.selectServiceNodeByName(parts[parts.length - 1]);
        for (GenericServiceNode genericServiceNode:genericServiceNodes){
            String nodePath = getPath(genericServiceNode.getUUID());
            if (nodePath.equals(path)){
                return selectNode(genericServiceNode.getUUID());
            }
        }
        List<GenericApplicationNode> genericApplicationNodes = this.distrubuteScopeTreeDataManipinate.selectApplicationNodeByName(parts[parts.length - 1]);
        for (GenericApplicationNode genericApplicationNode:genericApplicationNodes){
            String nodePath = getPath(genericApplicationNode.getUUID());
            if (nodePath.equals(path)){
                return selectNode(genericApplicationNode.getUUID());
            }
        }
        List<GenericClassificationNode> genericClassificationNodes = this.distrubuteScopeTreeDataManipinate.selectClassifNodeByName(parts[parts.length - 1]);
        for(GenericClassificationNode genericClassificationNode:genericClassificationNodes){
            String nodePath = getPath(genericClassificationNode.getUUID());
            if (nodePath.equals(path)){
                return selectNode(genericClassificationNode.getUUID());
            }
        }
        return null;
    }

}
