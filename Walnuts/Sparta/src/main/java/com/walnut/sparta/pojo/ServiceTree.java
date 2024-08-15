package com.walnut.sparta.pojo;


import com.walnut.sparta.ServiceTree.DataSourceFactory;
import com.walnut.sparta.ServiceTree.Interface.ServiceTreeDao;
import com.pinecone.hydra.service.GenericApplicationDescription;
import com.pinecone.hydra.service.GenericApplicationNode;
import com.pinecone.hydra.service.GenericClassificationNode;
import com.pinecone.hydra.service.GenericClassificationRules;
import com.pinecone.hydra.unit.udsn.UUIDDistributedScopeNode;
import com.pinecone.hydra.service.GenericNodeMetadata;
import com.pinecone.hydra.service.GenericServiceDescription;
import com.pinecone.hydra.service.GenericServiceNode;
import com.walnut.sparta.utils.UUIDUtil;

import org.springframework.stereotype.Component;

import java.util.List;
import javax.annotation.Resource;

/**
 * 提供服务树的相应方法
 */
@Component
public class ServiceTree {
    @Resource
    private ServiceTreeDao systemMapper;

    @Resource
    private DataSourceFactory dataSourceFactory;

    //保存节点
    public void saveApplicationNode(ApplicationNodeInformation applicationNodeInformation){
        //将信息写入数据库
        //将节点信息存入应用节点表
        String nodeUUID = UUIDUtil.createUUID();
        GenericApplicationNode applicationNode = applicationNodeInformation.getApplicationNode();
        applicationNode.setUUID(nodeUUID);
        systemMapper.saveApplicationNode(applicationNode);
        //将应用节点基础信息存入信息表
        String descriptionUUID = UUIDUtil.createUUID();
        GenericApplicationDescription applicationDescription = applicationNodeInformation.getApplicationDescription();
        applicationDescription.setUUID(descriptionUUID);
        systemMapper.saveApplicationDescription(applicationDescription);
        //将应用元信息存入元信息表
        String metadataUUID = UUIDUtil.createUUID();
        GenericNodeMetadata metadata = applicationNodeInformation.getMetadata();
        metadata.setUUID(metadataUUID);
        systemMapper.saveNodeMetadata(metadata);
        //将节点信息存入主表
        UUIDDistributedScopeNode node = new UUIDDistributedScopeNode();
        node.setBaseDataUUID(descriptionUUID);
        node.setUUID(nodeUUID);
        node.setNodeMetadataUUID(metadataUUID);
        node.setType("applicationNode");
        systemMapper.saveNode(node);
    }

    public void saveServiceNode(ServiceNodeInformation serviceNodeInformation){
        //将信息写入数据库
        //将节点信息存入应用节点表
        String nodeUUID = UUIDUtil.createUUID();
        GenericServiceNode serviceNode = serviceNodeInformation.getServiceNode();
        serviceNode.setUUID(nodeUUID);
        systemMapper.saveServiceNode(serviceNode);
        //将应用节点基础信息存入信息表
        String descriptionUUID = UUIDUtil.createUUID();
        GenericServiceDescription serviceDescription = serviceNodeInformation.getServiceDescription();
        serviceDescription.setUUID(descriptionUUID);
        systemMapper.saveServiceDescription(serviceDescription);
        //将应用元信息存入元信息表
        String metadataUUID = UUIDUtil.createUUID();
        GenericNodeMetadata metadata = serviceNodeInformation.getNodeMetadata();
        metadata.setUUID(metadataUUID);
        systemMapper.saveNodeMetadata(metadata);
        //将节点信息存入主表
        UUIDDistributedScopeNode node = new UUIDDistributedScopeNode();
        node.setBaseDataUUID(descriptionUUID);
        node.setUUID(nodeUUID);
        node.setNodeMetadataUUID(metadataUUID);
        node.setType("serviceNode");
        systemMapper.saveNode(node);
    }

    public void saveClassifNode(ClassifNodeInformation classifNodeInformation){
        //将应用节点基础信息存入信息表
        String descriptionUUID = UUIDUtil.createUUID();
        GenericClassificationRules classificationRules = classifNodeInformation.getClassificationRules();
        classificationRules.setUUID(descriptionUUID);
        systemMapper.saveClassifRules(classificationRules);
        //将信息写入数据库
        //将节点信息存入应用节点表
        String nodeUUID = UUIDUtil.createUUID();
        GenericClassificationNode classificationNode = classifNodeInformation.getClassificationNode();
        classificationNode.setUUID(nodeUUID);
        classificationNode.setRulesUUID(descriptionUUID);
        systemMapper.saveClassifNode(classificationNode);
        //将应用元信息存入元信息表
        String metadataUUID = UUIDUtil.createUUID();
        GenericNodeMetadata metadata = classifNodeInformation.getNodeMetadata();
        metadata.setUUID(metadataUUID);
        systemMapper.saveNodeMetadata(metadata);
        //将节点信息存入主表
        UUIDDistributedScopeNode node = new UUIDDistributedScopeNode();
        node.setBaseDataUUID(descriptionUUID);
        node.setUUID(nodeUUID);
        node.setNodeMetadataUUID(metadataUUID);
        node.setType("classifNode");
        systemMapper.saveNode(node);
    }

    //删除节点
    public void deleteNode(String UUID){
        //获取节点信息
        UUIDDistributedScopeNode node = systemMapper.selectNode(UUID);
        //根据类型删除节点
        String type = node.getType();
        if (type.equals("applicationNode")){
            systemMapper.deleteApplicationDescription(node.getBaseDataUUID());
            systemMapper.deleteNodeMetadata(node.getNodeMetadataUUID());
            systemMapper.deleteApplicationNode(node.getUUID());
            systemMapper.deleteNode(node.getUUID());
        }else if (type.equals("serviceNode")){
            systemMapper.deleteServiceNode(node.getUUID());
            systemMapper.deleteServiceDescription(node.getBaseDataUUID());
            systemMapper.deleteNode(node.getUUID());
            systemMapper.deleteNodeMetadata(node.getNodeMetadataUUID());
        }else if(type.equals("classifNode")){
            systemMapper.deleteClassifNode(node.getUUID());
            systemMapper.deleteClassifRules(node.getUUID());
            systemMapper.deleteNode(node.getUUID());
            systemMapper.deleteNodeMetadata(node.getNodeMetadataUUID());
        }
        //更新路径逻辑
        List<UUIDDistributedScopeNode> childNodes = systemMapper.selectChildNode(node.getUUID());
        for (UUIDDistributedScopeNode childNode:childNodes){
            childNode.setParentUUID(node.getParentUUID());
            systemMapper.updateNode(childNode);
            updatePath(childNode.getUUID());
        }

    }

    //查找节点信息
    public Object selectNode(String UUID){
        //先查看缓存表中是否存在路径信息，不存在则补齐
        String path = systemMapper.selectPath(UUID);
        if (path==null){
            UUIDDistributedScopeNode node = systemMapper.selectNode(UUID);
            String nodeName = getNodeName(node);
            String pathString="";
            pathString=pathString+nodeName;
            while (node.getParentUUID() != null){
                node=systemMapper.selectNode(node.getParentUUID());
                 nodeName = getNodeName(node);
                pathString=nodeName + "." + pathString;
            }
            systemMapper.savePath(pathString,UUID);
        }
        //先搜索出节点信息再根据节点类型进行完善
        // todo 继承机制还没有实现
        UUIDDistributedScopeNode node = systemMapper.selectNode(UUID);
        if (node.getType().equals("applicationNode")){
            ApplicationNodeInformation applicationNodeInformation = new ApplicationNodeInformation();
            GenericApplicationDescription applicationDescription = systemMapper.selectApplicationDescription(node.getBaseDataUUID());
            GenericApplicationNode applicationNode = systemMapper.selectApplicationNode(node.getUUID());
            GenericNodeMetadata nodeMetadata = systemMapper.selectNodeMetadata(node.getNodeMetadataUUID());
            applicationNodeInformation.setNode(node);
            applicationNodeInformation.setApplicationDescription(applicationDescription);
            applicationNodeInformation.setMetadata(nodeMetadata);
            applicationNodeInformation.setApplicationNode(applicationNode);
            return applicationNodeInformation;
        }else if(node.getType().equals("serviceNode")){
            ServiceNodeInformation serviceNodeInformation = new ServiceNodeInformation();
            serviceNodeInformation.setNode(node);
            serviceNodeInformation.setServiceNode(systemMapper.selectServiceNode(node.getUUID()));
            serviceNodeInformation.setNodeMetadata(systemMapper.selectNodeMetadata(node.getNodeMetadataUUID()));
            serviceNodeInformation.setServiceDescription(systemMapper.selectServiceDescription(node.getBaseDataUUID()));
            return serviceNodeInformation;
        } else if (node.getType().equals("classifNode")) {
            ClassifNodeInformation classifNodeInformation = new ClassifNodeInformation();
            classifNodeInformation.setNode(node);
            classifNodeInformation.setClassificationNode(systemMapper.selectClassifNode(node.getUUID()));
            classifNodeInformation.setNodeMetadata(systemMapper.selectNodeMetadata(node.getNodeMetadataUUID()));
            classifNodeInformation.setClassificationRules(systemMapper.selectClassifRules(node.getBaseDataUUID()));
            return classifNodeInformation;
        }
        return null;
    }

    //打印路径信息
    public String getPath(String UUID){
        ServiceTreeDao dataAccess = dataSourceFactory.createDataAccess();
//        String path = systemMapper.selectPath(UUID);
//        //若不存在path信息则更新缓存表
//        if (path==null){
//            Node node = systemMapper.selectNode(UUID);
//            String nodeName = getNodeName(node);
//            String pathString="";
//            pathString=pathString+nodeName;
//            while (node.getParentUUID() != null){
//                node=systemMapper.selectNode(node.getParentUUID());
//                nodeName = getNodeName(node);
//                pathString=nodeName + "." + pathString;
//            }
//            systemMapper.savePath(pathString,UUID);
//            return pathString;
//        }
//        return path;
        //测试通过数据工厂注入
        String path = dataAccess.selectPath(UUID);
        //若不存在path信息则更新缓存表
        if (path==null){
            UUIDDistributedScopeNode node = dataAccess.selectNode(UUID);
            String nodeName = getNodeName(node);
            String pathString="";
            pathString=pathString+nodeName;
            while (node.getParentUUID() != null){
                node=dataAccess.selectNode(node.getParentUUID());
                nodeName = getNodeName(node);
                pathString=nodeName + "." + pathString;
            }
            dataAccess.savePath(pathString,UUID);
            return pathString;
        }
        return path;
    }

    private String getNodeName(UUIDDistributedScopeNode node){
        if (node.getType().equals("applicationNode")){
          return systemMapper.selectApplicationNode(node.getUUID()).getName();
        }else if(node.getType().equals("serviceNode")){

            return systemMapper.selectServiceNode(node.getUUID()).getName();
        } else if (node.getType().equals("classifNode")) {

            return systemMapper.selectClassifNode(node.getUUID()).getName();
        }
        return null;
    }

    private void updatePath(String UUID){
        UUIDDistributedScopeNode node = systemMapper.selectNode(UUID);
        String nodeName = getNodeName(node);
        String pathString="";
        pathString=pathString+nodeName;
        while (node.getParentUUID() != null){
            node=systemMapper.selectNode(node.getParentUUID());
            nodeName = getNodeName(node);
            pathString=nodeName + "." + pathString;
        }
        systemMapper.updatePath(UUID,pathString);
    }

}
