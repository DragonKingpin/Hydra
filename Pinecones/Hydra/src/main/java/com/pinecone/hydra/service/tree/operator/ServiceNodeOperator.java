package com.pinecone.hydra.service.tree.operator;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.uoi.UOI;
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
        ServiceNodeWideData serviceNodeInformation=(ServiceNodeWideData) nodeWideData;

        //将信息写入数据库
        //将节点信息存入应用节点表
        UidGenerator uidGenerator= UUIDBuilder.getBuilder();
        GUID serviceNodeGUID = uidGenerator.getGUID72();
        GenericServiceNode serviceNode = serviceNodeInformation.getServiceNode();
        serviceNode.setGuid(serviceNodeGUID);
        this.serviceNodeManipulator.insert(serviceNode);

        //将应用节点基础信息存入信息表
        GUID descriptionGUID = uidGenerator.getGUID72();
        GenericServiceNodeMeta serviceDescription = serviceNodeInformation.getServiceDescription();
        serviceDescription.setGuid(descriptionGUID);
        this.serviceMetaManipulator.insert(serviceDescription);

        //将应用元信息存入元信息表
        GUID metadataGUID = uidGenerator.getGUID72();
        GenericNodeCommonData metadata = serviceNodeInformation.getNodeMetadata();
        metadata.setGuid(metadataGUID);
        this.commonDataManipulator.insert(metadata);

        //将节点信息存入主表
        GUIDDistributedScopeNode node = new GUIDDistributedScopeNode();
        node.setBaseDataGUID(descriptionGUID);
        node.setGuid(serviceNodeGUID);
        node.setNodeMetadataGUID(metadataGUID);
        node.setType( UOI.create( "java-class:///com.walnut.sparta.pojo.ServiceFunctionalNodeInformation" ) );
        this.scopeTreeManipulator.saveNode(node);
        return serviceNodeGUID;
    }

    @Override
    public void remove(GUID guid) {
        GUIDDistributedScopeNode node = this.scopeTreeManipulator.selectNode(guid);
        this.serviceNodeManipulator.delete(node.getGuid());
        this.serviceMetaManipulator.delete(node.getBaseDataGUID());
        this.commonDataManipulator.delete(node.getNodeMetadataGUID());
    }

    @Override
    public ServiceTreeNode get(GUID guid) {
        GUIDDistributedScopeNode node = this.scopeTreeManipulator.selectNode(guid);
        ServiceNodeWideData serviceNodeInformation = new ServiceNodeWideData();
        serviceNodeInformation.setServiceNode(this.serviceNodeManipulator.getServiceNode(node.getGuid()));
        serviceNodeInformation.setNodeMetadata(this.commonDataManipulator.getNodeMetadata(node.getNodeMetadataGUID()));
        serviceNodeInformation.setServiceDescription(this.serviceMetaManipulator.getServiceMeta(node.getBaseDataGUID()));
        return serviceNodeInformation;
    }

    @Override
    public void update(ServiceTreeNode nodeWideData) {

    }

}
