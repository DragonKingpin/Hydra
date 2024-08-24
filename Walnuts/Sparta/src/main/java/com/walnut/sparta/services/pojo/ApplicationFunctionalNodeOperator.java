package com.walnut.sparta.services.pojo;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.tree.FunctionalNodeMeta;
import com.pinecone.hydra.service.tree.MetaNodeOperator;
import com.pinecone.hydra.service.tree.GenericApplicationNodeMetadata;
import com.pinecone.hydra.service.tree.nodes.GenericApplicationNode;
import com.pinecone.hydra.service.tree.GenericNodeCommonData;
import com.pinecone.hydra.service.tree.ServiceTreeMapper;
import com.pinecone.hydra.service.tree.source.ApplicationDescriptionManipulator;
import com.pinecone.hydra.service.tree.source.ApplicationNodeManipulator;
import com.pinecone.hydra.unit.udsn.GUIDDistributedScopeNode;
import com.pinecone.hydra.service.tree.source.NodeMetadataManipulator;
import com.pinecone.ulf.util.id.UUIDBuilder;
import com.pinecone.ulf.util.id.UidGenerator;

public class ApplicationFunctionalNodeOperator implements MetaNodeOperator {
    private ApplicationNodeManipulator applicationNodeManipinate;
    private ApplicationDescriptionManipulator applicationDescriptionManipinate;
    private NodeMetadataManipulator nodeMetadataManipinate;
    private ServiceTreeMapper serviceTreeMapper;

    public ApplicationFunctionalNodeOperator(ApplicationNodeManipulator applicationNodeManipinate, ApplicationDescriptionManipulator applicationDescriptionManipinate,
                                             NodeMetadataManipulator nodeMetadataManipinate, ServiceTreeMapper serviceTreeMapper){
        this.applicationNodeManipinate=applicationNodeManipinate;
        this.applicationDescriptionManipinate=applicationDescriptionManipinate;
        this.nodeMetadataManipinate=nodeMetadataManipinate;
        this.serviceTreeMapper=serviceTreeMapper;
    }

    @Override
    public GUID insert(FunctionalNodeMeta functionalNodeMeta) {
        ApplicationFunctionalNodeMeta applicationNodeInformation = (ApplicationFunctionalNodeMeta) functionalNodeMeta;
        //将信息写入数据库
        //将节点信息存入应用节点表
        UidGenerator uidGenerator= UUIDBuilder.getBuilder();
        GUID applicationNodeGUID = uidGenerator.getGUID72();
        GenericApplicationNode applicationNode = applicationNodeInformation.getApplicationNode();
        applicationNode.setGuid(applicationNodeGUID);
        this.applicationNodeManipinate.insertApplicationNode(applicationNode);

        //将应用节点基础信息存入信息表
        GUID descriptionGUID = uidGenerator.getGUID72();
        GenericApplicationNodeMetadata applicationDescription = applicationNodeInformation.getApplicationDescription();
        applicationDescription.setGuid(descriptionGUID);
        this.applicationDescriptionManipinate.insertApplicationDescription(applicationDescription);

        //将应用元信息存入元信息表
        GUID metadataGUID = uidGenerator.getGUID72();
        GenericNodeCommonData metadata = applicationNodeInformation.getMetadata();
        metadata.setGuid(metadataGUID);
        this.nodeMetadataManipinate.insertNodeMetadata(metadata);

        //将节点信息存入主表
        GUIDDistributedScopeNode node = new GUIDDistributedScopeNode();
        node.setBaseDataGUID(descriptionGUID);
        node.setGuid(applicationNodeGUID);
        node.setNodeMetadataGUID(metadataGUID);
        node.setType("com.walnut.sparta.pojo.ApplicationFunctionalNodeInformation");
        this.serviceTreeMapper.saveNode(node);
        return applicationNodeGUID;
    }

    @Override
    public void remove(GUID guid) {
        GUIDDistributedScopeNode node = this.serviceTreeMapper.selectNode(guid);
        this.applicationDescriptionManipinate.deleteApplicationDescription(node.getBaseDataGUID());
        this.nodeMetadataManipinate.deleteNodeMetadata(node.getNodeMetadataGUID());
        this.applicationNodeManipinate.deleteApplicationNode(node.getGuid());
    }

    @Override
    public FunctionalNodeMeta get(GUID guid) {
        GUIDDistributedScopeNode node = this.serviceTreeMapper.selectNode(guid);
        ApplicationFunctionalNodeMeta applicationNodeInformation = new ApplicationFunctionalNodeMeta();
        GenericApplicationNodeMetadata applicationDescription = this.applicationDescriptionManipinate.getApplicationDescription(node.getBaseDataGUID());
        GenericApplicationNode applicationNode = this.applicationNodeManipinate.getApplicationNode(node.getGuid());
        GenericNodeCommonData nodeMetadata = this.nodeMetadataManipinate.getNodeMetadata(node.getNodeMetadataGUID());
        applicationNodeInformation.setApplicationDescription(applicationDescription);
        applicationNodeInformation.setMetadata(nodeMetadata);
        applicationNodeInformation.setApplicationNode(applicationNode);
        return applicationNodeInformation;
    }

    @Override
    public void update(FunctionalNodeMeta functionalNodeMeta) {

    }

}
