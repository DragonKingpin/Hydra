package com.pinecone.hydra.service.tree.operator;

import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.uoi.UOI;
import com.pinecone.hydra.service.tree.UOIUtils;
import com.pinecone.hydra.service.tree.nodes.ApplicationNode;
import com.pinecone.hydra.service.tree.nodes.ServiceNode;
import com.pinecone.hydra.service.tree.nodes.ServiceTreeNode;
import com.pinecone.hydra.service.tree.GenericNodeCommonData;
import com.pinecone.hydra.service.tree.meta.GenericServiceNodeMeta;
import com.pinecone.hydra.service.tree.nodes.GenericServiceNode;
import com.pinecone.hydra.service.tree.source.DefaultMetaNodeManipulators;
import com.pinecone.hydra.unit.udsn.source.ScopeTreeManipulator;
import com.pinecone.hydra.unit.udsn.GUIDDistributedScopeNode;
import com.pinecone.hydra.service.tree.source.CommonDataManipulator;
import com.pinecone.hydra.service.tree.source.ServiceMetaManipulator;
import com.pinecone.hydra.service.tree.source.ServiceNodeManipulator;
import com.pinecone.ulf.util.id.UUIDBuilder;
import com.pinecone.ulf.util.id.UidGenerator;

public class ServiceNodeOperator implements MetaNodeOperator {
    private ServiceNodeManipulator  serviceNodeManipulator;
    private ServiceMetaManipulator  serviceMetaManipulator;
    private CommonDataManipulator commonDataManipulator;
    private ScopeTreeManipulator scopeTreeManipulator;

    public ServiceNodeOperator( DefaultMetaNodeManipulators manipulators ) {
        this(
                manipulators.getServiceNodeManipulator(),
                manipulators.getServiceMetaManipulator(),
                manipulators.getCommonDataManipulator(),
                manipulators.getScopeTreeManipulator()
        );
    }

    public ServiceNodeOperator(
            ServiceNodeManipulator serviceNodeManipulator, ServiceMetaManipulator serviceMetaManipulator,
            CommonDataManipulator commonDataManipulator, ScopeTreeManipulator scopeTreeManipulator
    ){
        this.serviceNodeManipulator  = serviceNodeManipulator;
        this.serviceMetaManipulator  = serviceMetaManipulator;
        this.commonDataManipulator   = commonDataManipulator;
        this.scopeTreeManipulator    = scopeTreeManipulator;
    }


    @Override
    public GUID insert(ServiceTreeNode nodeWideData) {
        GenericServiceNode serviceNodeInformation=(GenericServiceNode) nodeWideData;

        //将信息写入数据库
        //将节点信息存入应用节点表
        UidGenerator uidGenerator= UUIDBuilder.getBuilder();
        GUID serviceNodeGUID = uidGenerator.getGUID72();
        serviceNodeInformation.setGuid(serviceNodeGUID);
        this.serviceNodeManipulator.insert(serviceNodeInformation);

        //将应用节点基础信息存入信息表
        GUID descriptionGUID = uidGenerator.getGUID72();
        GenericServiceNodeMeta serviceDescription = serviceNodeInformation.getServiceNodeMetadata();
        serviceDescription.setGuid(descriptionGUID);
        this.serviceMetaManipulator.insert(serviceDescription);

        //将应用元信息存入元信息表
        GUID metadataGUID = uidGenerator.getGUID72();
        GenericNodeCommonData metadata = serviceNodeInformation.getNodeCommonData();
        metadata.setGuid(metadataGUID);
        this.commonDataManipulator.insert(metadata);

        //将节点信息存入主表
        GUIDDistributedScopeNode node = new GUIDDistributedScopeNode();
        node.setBaseDataGUID(descriptionGUID);
        node.setGuid(serviceNodeGUID);
        node.setNodeMetadataGUID(metadataGUID);
        node.setType( UOIUtils.createLocalJavaClass( nodeWideData.getClass().getName() ) );
        this.scopeTreeManipulator.saveNode(node);
        return serviceNodeGUID;
    }

    @Override
    public void remove(GUID guid) {
        GUIDDistributedScopeNode node = this.scopeTreeManipulator.selectNode(guid);
        this.serviceNodeManipulator.remove(node.getGuid());
        this.serviceMetaManipulator.remove(node.getBaseDataGUID());
        this.commonDataManipulator.remove(node.getNodeMetadataGUID());
    }

    @Override
    public ServiceTreeNode get(GUID guid) {
        GUIDDistributedScopeNode node = this.scopeTreeManipulator.selectNode(guid);
        GenericServiceNode genericServiceNode = new GenericServiceNode();
        GenericServiceNodeMeta serviceMeta = this.serviceMetaManipulator.getServiceMeta(node.getBaseDataGUID());
        GenericNodeCommonData commonData = this.commonDataManipulator.getNodeMetadata(node.getNodeMetadataGUID());
        GUIDDistributedScopeNode guidDistributedScopeNode = this.scopeTreeManipulator.selectNode(guid);

        genericServiceNode.setServiceNodeMetadata(serviceMeta);
        genericServiceNode.setNodeCommonData(commonData);
        genericServiceNode.setDistributedTreeNode(guidDistributedScopeNode);
        genericServiceNode.setGuid(guid);
        genericServiceNode.setName(serviceMeta.getName());

        return genericServiceNode;
    }

    @Override
    public void update(ServiceTreeNode nodeWideData) {

    }

}
