package com.walnut.sparta.pojo;

import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.FunctionalNodeInformation;
import com.pinecone.hydra.service.FunctionalNodeOperation;
import com.pinecone.hydra.service.GenericApplicationDescription;
import com.pinecone.hydra.service.GenericApplicationNode;
import com.pinecone.hydra.service.GenericNodeMetadata;
import com.pinecone.hydra.service.ServiceTreeMapper;
import com.pinecone.hydra.unit.udsn.ApplicationDescriptionManipinate;
import com.pinecone.hydra.unit.udsn.ApplicationNodeManipinate;
import com.pinecone.hydra.unit.udsn.GUIDDistributedScopeNode;
import com.pinecone.hydra.unit.udsn.NodeMetadataManipinate;
import com.pinecone.ulf.util.id.UUIDBuilder;
import com.pinecone.ulf.util.id.UidGenerator;

import java.util.List;

public class ApplicationFunctionalNodeOperation implements FunctionalNodeOperation {
    private ApplicationNodeManipinate applicationNodeManipinate;
    private ApplicationDescriptionManipinate applicationDescriptionManipinate;
    private NodeMetadataManipinate nodeMetadataManipinate;
    private ServiceTreeMapper serviceTreeMapper;
    public ApplicationFunctionalNodeOperation(ApplicationNodeManipinate applicationNodeManipinate, ApplicationDescriptionManipinate applicationDescriptionManipinate,
                                              NodeMetadataManipinate nodeMetadataManipinate, ServiceTreeMapper serviceTreeMapper){
        this.applicationNodeManipinate=applicationNodeManipinate;
        this.applicationDescriptionManipinate=applicationDescriptionManipinate;
        this.nodeMetadataManipinate=nodeMetadataManipinate;
        this.serviceTreeMapper=serviceTreeMapper;
    }
    @Override
    public GUID addOperation(FunctionalNodeInformation functionalNodeInformation) {
        ApplicationFunctionalNodeInformation applicationNodeInformation = (ApplicationFunctionalNodeInformation) functionalNodeInformation;
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
        node.setType("com.walnut.sparta.pojo.ApplicationFunctionalNodeInformation");
        this.serviceTreeMapper.saveNode(node);
        return applicationNodeGUID;
    }

    @Override
    public void deleteOperation(GUID guid) {
        GUIDDistributedScopeNode node = this.serviceTreeMapper.selectNode(guid);
        this.applicationDescriptionManipinate.deleteApplicationDescription(node.getBaseDataUUID());
        this.nodeMetadataManipinate.deleteNodeMetadata(node.getNodeMetadataUUID());
        this.applicationNodeManipinate.deleteApplicationNode(node.getUUID());
    }

    @Override
    public FunctionalNodeInformation SelectOperation(GUID guid) {
        GUIDDistributedScopeNode node = this.serviceTreeMapper.selectNode(guid);
        ApplicationFunctionalNodeInformation applicationNodeInformation = new ApplicationFunctionalNodeInformation();
        GenericApplicationDescription applicationDescription = this.applicationDescriptionManipinate.selectApplicationDescription(node.getBaseDataUUID());
        GenericApplicationNode applicationNode = this.applicationNodeManipinate.selectApplicationNode(node.getUUID());
        GenericNodeMetadata nodeMetadata = this.nodeMetadataManipinate.selectNodeMetadata(node.getNodeMetadataUUID());
        applicationNodeInformation.setApplicationDescription(applicationDescription);
        applicationNodeInformation.setMetadata(nodeMetadata);
        applicationNodeInformation.setApplicationNode(applicationNode);
        return applicationNodeInformation;
    }

    @Override
    public void UpdateOperation(FunctionalNodeInformation functionalNodeInformation) {

    }

}
