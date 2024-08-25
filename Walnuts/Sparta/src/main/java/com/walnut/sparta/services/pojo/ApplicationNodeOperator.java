package com.walnut.sparta.services.pojo;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.uoi.UOI;
import com.pinecone.hydra.service.tree.NodeWideData;
import com.pinecone.hydra.service.tree.MetaNodeOperator;
import com.pinecone.hydra.service.tree.GenericApplicationNodeMeta;
import com.pinecone.hydra.service.tree.nodes.GenericApplicationNode;
import com.pinecone.hydra.service.tree.GenericNodeCommonData;
import com.pinecone.hydra.service.tree.ServiceTreeMapper;
import com.pinecone.hydra.service.tree.source.ApplicationMetaManipulator;
import com.pinecone.hydra.service.tree.source.ApplicationNodeManipulator;
import com.pinecone.hydra.unit.udsn.GUIDDistributedScopeNode;
import com.pinecone.hydra.service.tree.source.NodeMetadataManipulator;
import com.pinecone.ulf.util.id.UUIDBuilder;
import com.pinecone.ulf.util.id.UidGenerator;

public class ApplicationNodeOperator implements MetaNodeOperator {
    private ApplicationNodeManipulator        applicationNodeManipulator;
    private ApplicationMetaManipulator applicationMetaManipulator;
    private NodeMetadataManipulator           nodeMetadataManipulator;
    private ServiceTreeMapper                 serviceTreeMapper;

    public ApplicationNodeOperator(
            ApplicationNodeManipulator applicationNodeManipulator, ApplicationMetaManipulator applicationMetaManipulator,
            NodeMetadataManipulator nodeMetadataManipulator, ServiceTreeMapper serviceTreeMapper
    ){
        this.applicationNodeManipulator = applicationNodeManipulator;
        this.applicationMetaManipulator = applicationMetaManipulator;
        this.nodeMetadataManipulator = nodeMetadataManipulator;
        this.serviceTreeMapper = serviceTreeMapper;
    }

    @Override
    public GUID insert(NodeWideData nodeWideData) {
        ApplicationNodeWideData applicationNodeInformation = (ApplicationNodeWideData) nodeWideData;
        //将信息写入数据库
        //将节点信息存入应用节点表
        UidGenerator uidGenerator= UUIDBuilder.getBuilder();
        GUID applicationNodeGUID = uidGenerator.getGUID72();
        GenericApplicationNode applicationNode = applicationNodeInformation.getApplicationNode();
        applicationNode.setGuid(applicationNodeGUID);
        this.applicationNodeManipulator.insertApplicationNode(applicationNode);

        //将应用节点基础信息存入信息表
        GUID descriptionGUID = uidGenerator.getGUID72();
        GenericApplicationNodeMeta applicationDescription = applicationNodeInformation.getApplicationDescription();
        applicationDescription.setGuid(descriptionGUID);
        this.applicationMetaManipulator.insertApplicationDescription(applicationDescription);

        //将应用元信息存入元信息表
        GUID metadataGUID = uidGenerator.getGUID72();
        GenericNodeCommonData metadata = applicationNodeInformation.getMetadata();
        metadata.setGuid(metadataGUID);
        this.nodeMetadataManipulator.insertNodeMetadata(metadata);

        //将节点信息存入主表
        GUIDDistributedScopeNode node = new GUIDDistributedScopeNode();
        node.setBaseDataGUID(descriptionGUID);
        node.setGuid(applicationNodeGUID);
        node.setNodeMetadataGUID(metadataGUID);
        node.setType( UOI.create( "com.walnut.sparta.pojo.ApplicationFunctionalNodeInformation" ) );
        this.serviceTreeMapper.saveNode(node);
        return applicationNodeGUID;
    }

    @Override
    public void remove(GUID guid) {
        GUIDDistributedScopeNode node = this.serviceTreeMapper.selectNode(guid);
        this.applicationMetaManipulator.delete(node.getBaseDataGUID());
        this.nodeMetadataManipulator.deleteNodeMetadata(node.getNodeMetadataGUID());
        this.applicationNodeManipulator.deleteApplicationNode(node.getGuid());
    }

    @Override
    public NodeWideData get(GUID guid) {
        GUIDDistributedScopeNode node = this.serviceTreeMapper.selectNode(guid);
        ApplicationNodeWideData applicationNodeInformation = new ApplicationNodeWideData();
        GenericApplicationNodeMeta applicationDescription = this.applicationMetaManipulator.getApplicationDescription(node.getBaseDataGUID());
        GenericApplicationNode applicationNode = this.applicationNodeManipulator.getApplicationNode(node.getGuid());
        GenericNodeCommonData nodeMetadata = this.nodeMetadataManipulator.getNodeMetadata(node.getNodeMetadataGUID());
        applicationNodeInformation.setApplicationDescription(applicationDescription);
        applicationNodeInformation.setMetadata(nodeMetadata);
        applicationNodeInformation.setApplicationNode(applicationNode);
        return applicationNodeInformation;
    }

    @Override
    public void update(NodeWideData nodeWideData) {

    }

}
