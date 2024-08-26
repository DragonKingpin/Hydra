package com.pinecone.hydra.service.tree.operator;

import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.uoi.UOI;
import com.pinecone.hydra.service.tree.UOIUtils;
import com.pinecone.hydra.service.tree.nodes.ApplicationNode;
import com.pinecone.hydra.service.tree.nodes.ServiceTreeNode;
import com.pinecone.hydra.service.tree.meta.GenericApplicationNodeMeta;
import com.pinecone.hydra.service.tree.nodes.GenericApplicationNode;
import com.pinecone.hydra.service.tree.GenericNodeCommonData;
import com.pinecone.hydra.service.tree.source.DefaultMetaNodeManipulators;
import com.pinecone.hydra.unit.udsn.source.ScopeTreeManipulator;
import com.pinecone.hydra.service.tree.source.ApplicationMetaManipulator;
import com.pinecone.hydra.service.tree.source.ApplicationNodeManipulator;
import com.pinecone.hydra.unit.udsn.GUIDDistributedScopeNode;
import com.pinecone.hydra.service.tree.source.CommonDataManipulator;
import com.pinecone.ulf.util.id.UUIDBuilder;
import com.pinecone.ulf.util.id.UidGenerator;

public class ApplicationNodeOperator implements MetaNodeOperator {
    private ApplicationNodeManipulator        applicationNodeManipulator;
    private ApplicationMetaManipulator        applicationMetaManipulator;
    private CommonDataManipulator             commonDataManipulator;
    private ScopeTreeManipulator              scopeTreeManipulator;

    public ApplicationNodeOperator( DefaultMetaNodeManipulators manipulators ) {
        this(
                manipulators.getApplicationNodeManipulator(),
                manipulators.getApplicationMetaManipulator(),
                manipulators.getCommonDataManipulator(),
                manipulators.getServiceTreeMapper()
        );
    }

    public ApplicationNodeOperator(
            ApplicationNodeManipulator applicationNodeManipulator, ApplicationMetaManipulator applicationMetaManipulator,
            CommonDataManipulator commonDataManipulator, ScopeTreeManipulator scopeTreeManipulator
    ){
        this.applicationNodeManipulator = applicationNodeManipulator;
        this.applicationMetaManipulator = applicationMetaManipulator;
        this.commonDataManipulator      = commonDataManipulator;
        this.scopeTreeManipulator       = scopeTreeManipulator;
    }


    @Override
    public GUID insert( ServiceTreeNode nodeWideData ) {
        Debug.trace("保存节点"+nodeWideData);
        ApplicationNodeWideData applicationNodeInformation = (ApplicationNodeWideData) nodeWideData;
        //将信息写入数据库
        //将节点信息存入应用节点表
        UidGenerator uidGenerator= UUIDBuilder.getBuilder();
        GUID applicationNodeGUID = uidGenerator.getGUID72();
        GenericApplicationNode applicationNode = applicationNodeInformation.getApplicationNode();
        applicationNode.setGuid(applicationNodeGUID);
        this.applicationNodeManipulator.insert(applicationNode);

        //将应用节点基础信息存入信息表
        GUID descriptionGUID = uidGenerator.getGUID72();
        GenericApplicationNodeMeta applicationDescription = applicationNodeInformation.getApplicationDescription();
        applicationDescription.setGuid(descriptionGUID);
        this.applicationMetaManipulator.insert(applicationDescription);

        //将应用元信息存入元信息表
        GUID metadataGUID = uidGenerator.getGUID72();
        GenericNodeCommonData metadata = applicationNodeInformation.getMetadata();
        metadata.setGuid(metadataGUID);
        this.commonDataManipulator.insert(metadata);

        //将节点信息存入主表
        GUIDDistributedScopeNode node = new GUIDDistributedScopeNode();
        node.setBaseDataGUID(descriptionGUID);
        node.setGuid(applicationNodeGUID);
        node.setNodeMetadataGUID(metadataGUID);
        node.setType( UOIUtils.createLocalJavaClass( nodeWideData.getClass().getName() ) );
        this.scopeTreeManipulator.saveNode(node);
        return applicationNodeGUID;
    }

    @Override
    public void remove(GUID guid) {
        GUIDDistributedScopeNode node = this.scopeTreeManipulator.selectNode(guid);
        this.applicationMetaManipulator.remove(node.getBaseDataGUID());
        this.commonDataManipulator.remove(node.getNodeMetadataGUID());
        this.applicationNodeManipulator.remove(node.getGuid());
    }

    @Override
    public ServiceTreeNode get(GUID guid) {
        GUIDDistributedScopeNode node = this.scopeTreeManipulator.selectNode(guid);
        GenericApplicationNode genericApplicationNode = new GenericApplicationNode();

        GenericApplicationNodeMeta applicationDescription = this.applicationMetaManipulator.getApplicationMeta(node.getBaseDataGUID());
        GUIDDistributedScopeNode guidDistributedScopeNode = this.scopeTreeManipulator.selectNode(guid);
        GenericNodeCommonData nodeMetadata = this.commonDataManipulator.getNodeMetadata(node.getNodeMetadataGUID());

        genericApplicationNode.setApplicationNodeMeta(applicationDescription);
        genericApplicationNode.setNodeCommonData(nodeMetadata);
        genericApplicationNode.setDistributedTreeNode(guidDistributedScopeNode);

        genericApplicationNode.setName(applicationDescription.getName());
        genericApplicationNode.setGuid(genericApplicationNode.getGuid());
        return genericApplicationNode;
    }

    @Override
    public void update(ServiceTreeNode nodeWideData) {

    }

}
