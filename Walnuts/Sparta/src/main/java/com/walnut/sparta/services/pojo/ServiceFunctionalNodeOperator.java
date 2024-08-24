package com.walnut.sparta.services.pojo;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.tree.FunctionalNodeMeta;
import com.pinecone.hydra.service.tree.MetaNodeOperator;
import com.pinecone.hydra.service.tree.GenericNodeCommonData;
import com.pinecone.hydra.service.tree.GenericServiceNodeMetadata;
import com.pinecone.hydra.service.tree.nodes.GenericServiceNode;
import com.pinecone.hydra.service.tree.ServiceTreeMapper;
import com.pinecone.hydra.unit.udsn.GUIDDistributedScopeNode;
import com.pinecone.hydra.service.tree.source.NodeMetadataManipulator;
import com.pinecone.hydra.service.tree.source.ServiceDescriptionManipulator;
import com.pinecone.hydra.service.tree.source.ServiceNodeManipulator;
import com.pinecone.ulf.util.id.UUIDBuilder;
import com.pinecone.ulf.util.id.UidGenerator;

public class ServiceFunctionalNodeOperator implements MetaNodeOperator {
    private ServiceNodeManipulator serviceNodeManipulator;
    private ServiceDescriptionManipulator serviceDescriptionManipinate;
    private NodeMetadataManipulator nodeMetadataManipinate;
    private ServiceTreeMapper serviceTreeMapper;
    public ServiceFunctionalNodeOperator(ServiceNodeManipulator serviceNodeManipulator, ServiceDescriptionManipulator serviceDescriptionManipinate,
                                         NodeMetadataManipulator nodeMetadataManipinate, ServiceTreeMapper serviceTreeMapper){
        this.serviceNodeManipulator = serviceNodeManipulator;
        this.serviceDescriptionManipinate=serviceDescriptionManipinate;
        this.nodeMetadataManipinate=nodeMetadataManipinate;
        this.serviceTreeMapper=serviceTreeMapper;
    }
    @Override
    public GUID insert(FunctionalNodeMeta functionalNodeMeta) {
        ServiceFunctionalNodeMeta serviceNodeInformation=(ServiceFunctionalNodeMeta) functionalNodeMeta;
        //将信息写入数据库
        //将节点信息存入应用节点表
        UidGenerator uidGenerator= UUIDBuilder.getBuilder();
        GUID serviceNodeGUID = uidGenerator.getGUID72();
        GenericServiceNode serviceNode = serviceNodeInformation.getServiceNode();
        serviceNode.setGuid(serviceNodeGUID);
        this.serviceNodeManipulator.saveServiceNode(serviceNode);
        //将应用节点基础信息存入信息表
        GUID descriptionGUID = uidGenerator.getGUID72();
        GenericServiceNodeMetadata serviceDescription = serviceNodeInformation.getServiceDescription();
        serviceDescription.setGuid(descriptionGUID);
        this.serviceDescriptionManipinate.insertServiceDescription(serviceDescription);
        //将应用元信息存入元信息表
        GUID metadataGUID = uidGenerator.getGUID72();
        GenericNodeCommonData metadata = serviceNodeInformation.getNodeMetadata();
        metadata.setGuid(metadataGUID);
        this.nodeMetadataManipinate.insertNodeMetadata(metadata);
        //将节点信息存入主表
        GUIDDistributedScopeNode node = new GUIDDistributedScopeNode();
        node.setBaseDataGUID(descriptionGUID);
        node.setGuid(serviceNodeGUID);
        node.setNodeMetadataGUID(metadataGUID);
        node.setType("com.walnut.sparta.pojo.ServiceFunctionalNodeInformation");
        this.serviceTreeMapper.saveNode(node);
        return serviceNodeGUID;
    }

    @Override
    public void remove(GUID guid) {
        GUIDDistributedScopeNode node = this.serviceTreeMapper.selectNode(guid);
        this.serviceNodeManipulator.deleteServiceNode(node.getGuid());
        this.serviceDescriptionManipinate.deleteServiceDescription(node.getBaseDataGUID());
        this.nodeMetadataManipinate.deleteNodeMetadata(node.getNodeMetadataGUID());
    }

    @Override
    public FunctionalNodeMeta get(GUID guid) {
        GUIDDistributedScopeNode node = this.serviceTreeMapper.selectNode(guid);
        ServiceFunctionalNodeMeta serviceNodeInformation = new ServiceFunctionalNodeMeta();
        serviceNodeInformation.setServiceNode(this.serviceNodeManipulator.selectServiceNode(node.getGuid()));
        serviceNodeInformation.setNodeMetadata(this.nodeMetadataManipinate.getNodeMetadata(node.getNodeMetadataGUID()));
        serviceNodeInformation.setServiceDescription(this.serviceDescriptionManipinate.getServiceDescription(node.getBaseDataGUID()));
        return serviceNodeInformation;
    }

    @Override
    public void update(FunctionalNodeMeta functionalNodeMeta) {

    }

}
