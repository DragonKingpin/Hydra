package com.walnut.sparta.services.pojo;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.uoi.UOI;
import com.pinecone.hydra.service.tree.FunctionalNodeMeta;
import com.pinecone.hydra.service.tree.MetaNodeOperator;
import com.pinecone.hydra.service.tree.GenericNodeCommonData;
import com.pinecone.hydra.service.tree.GenericServiceNodeMeta;
import com.pinecone.hydra.service.tree.nodes.GenericServiceNode;
import com.pinecone.hydra.service.tree.ServiceTreeMapper;
import com.pinecone.hydra.unit.udsn.GUIDDistributedScopeNode;
import com.pinecone.hydra.service.tree.source.NodeMetadataManipulator;
import com.pinecone.hydra.service.tree.source.ServiceDescriptionManipulator;
import com.pinecone.hydra.service.tree.source.ServiceNodeManipulator;
import com.pinecone.ulf.util.id.UUIDBuilder;
import com.pinecone.ulf.util.id.UidGenerator;

public class ServiceNodeOperator implements MetaNodeOperator {
    private ServiceNodeManipulator serviceNodeManipulator;
    private ServiceDescriptionManipulator serviceDescriptionManipulator;
    private NodeMetadataManipulator nodeMetadataManipulator;
    private ServiceTreeMapper serviceTreeMapper;

    public ServiceNodeOperator(ServiceNodeManipulator serviceNodeManipulator, ServiceDescriptionManipulator serviceDescriptionManipulator,
                               NodeMetadataManipulator nodeMetadataManipulator, ServiceTreeMapper serviceTreeMapper){
        this.serviceNodeManipulator = serviceNodeManipulator;
        this.serviceDescriptionManipulator=serviceDescriptionManipulator;
        this.nodeMetadataManipulator=nodeMetadataManipulator;
        this.serviceTreeMapper=serviceTreeMapper;
    }
    @Override
    public GUID insert(FunctionalNodeMeta functionalNodeMeta) {
        ServiceNodeMeta serviceNodeInformation=(ServiceNodeMeta) functionalNodeMeta;
        //将信息写入数据库
        //将节点信息存入应用节点表
        UidGenerator uidGenerator= UUIDBuilder.getBuilder();
        GUID serviceNodeGUID = uidGenerator.getGUID72();
        GenericServiceNode serviceNode = serviceNodeInformation.getServiceNode();
        serviceNode.setGuid(serviceNodeGUID);
        this.serviceNodeManipulator.saveServiceNode(serviceNode);
        //将应用节点基础信息存入信息表
        GUID descriptionGUID = uidGenerator.getGUID72();
        GenericServiceNodeMeta serviceDescription = serviceNodeInformation.getServiceDescription();
        serviceDescription.setGuid(descriptionGUID);
        this.serviceDescriptionManipulator.insertServiceDescription(serviceDescription);
        //将应用元信息存入元信息表
        GUID metadataGUID = uidGenerator.getGUID72();
        GenericNodeCommonData metadata = serviceNodeInformation.getNodeMetadata();
        metadata.setGuid(metadataGUID);
        this.nodeMetadataManipulator.insertNodeMetadata(metadata);
        //将节点信息存入主表
        GUIDDistributedScopeNode node = new GUIDDistributedScopeNode();
        node.setBaseDataGUID(descriptionGUID);
        node.setGuid(serviceNodeGUID);
        node.setNodeMetadataGUID(metadataGUID);
        node.setType( UOI.create( "java-class:///com.walnut.sparta.pojo.ServiceFunctionalNodeInformation" ) );
        this.serviceTreeMapper.saveNode(node);
        return serviceNodeGUID;
    }

    @Override
    public void remove(GUID guid) {
        GUIDDistributedScopeNode node = this.serviceTreeMapper.selectNode(guid);
        this.serviceNodeManipulator.deleteServiceNode(node.getGuid());
        this.serviceDescriptionManipulator.deleteServiceDescription(node.getBaseDataGUID());
        this.nodeMetadataManipulator.deleteNodeMetadata(node.getNodeMetadataGUID());
    }

    @Override
    public FunctionalNodeMeta get(GUID guid) {
        GUIDDistributedScopeNode node = this.serviceTreeMapper.selectNode(guid);
        ServiceNodeMeta serviceNodeInformation = new ServiceNodeMeta();
        serviceNodeInformation.setServiceNode(this.serviceNodeManipulator.selectServiceNode(node.getGuid()));
        serviceNodeInformation.setNodeMetadata(this.nodeMetadataManipulator.getNodeMetadata(node.getNodeMetadataGUID()));
        serviceNodeInformation.setServiceDescription(this.serviceDescriptionManipulator.getServiceDescription(node.getBaseDataGUID()));
        return serviceNodeInformation;
    }

    @Override
    public void update(FunctionalNodeMeta functionalNodeMeta) {

    }

}
