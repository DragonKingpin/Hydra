package com.pinecone.hydra.service.kom.operator;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.system.ko.UOIUtils;
import com.pinecone.hydra.service.kom.nodes.ServiceTreeNode;
import com.pinecone.hydra.service.kom.GenericNodeCommonData;
import com.pinecone.hydra.service.kom.meta.GenericServiceNodeMeta;
import com.pinecone.hydra.service.kom.nodes.GenericServiceNode;
import com.pinecone.hydra.service.kom.source.ServiceMasterManipulator;
import com.pinecone.hydra.unit.udtt.source.TireOwnerManipulator;
import com.pinecone.hydra.unit.udtt.source.TrieTreeManipulator;
import com.pinecone.hydra.unit.udtt.GUIDDistributedTrieNode;
import com.pinecone.hydra.service.kom.source.CommonDataManipulator;
import com.pinecone.hydra.service.kom.source.ServiceMetaManipulator;
import com.pinecone.hydra.service.kom.source.ServiceNodeManipulator;
import com.pinecone.ulf.util.id.GuidAllocator;
import com.pinecone.ulf.util.id.GUIDs;

public class ServiceNodeOperator implements MetaNodeOperator {
    protected ServiceNodeManipulator  serviceNodeManipulator;
    protected ServiceMetaManipulator  serviceMetaManipulator;
    protected CommonDataManipulator   commonDataManipulator;
    protected TrieTreeManipulator     trieTreeManipulator;
    protected TireOwnerManipulator    tireOwnerManipulator;

    public ServiceNodeOperator( ServiceMasterManipulator manipulators ) {
        this(
                manipulators.getServiceNodeManipulator(),
                manipulators.getServiceMetaManipulator(),
                manipulators.getCommonDataManipulator(),
                manipulators.getTrieTreeManipulator(),
                manipulators.getTireOwnerManipulator()
        );
    }

    public ServiceNodeOperator(
            ServiceNodeManipulator serviceNodeManipulator, ServiceMetaManipulator serviceMetaManipulator,
            CommonDataManipulator commonDataManipulator, TrieTreeManipulator trieTreeManipulator,
            TireOwnerManipulator ownerManipulator
    ){
        this.serviceNodeManipulator  = serviceNodeManipulator;
        this.serviceMetaManipulator  = serviceMetaManipulator;
        this.commonDataManipulator   = commonDataManipulator;
        this.trieTreeManipulator     = trieTreeManipulator;
        this.tireOwnerManipulator    = ownerManipulator;
    }


    @Override
    public GUID insert(ServiceTreeNode nodeWideData) {
        GenericServiceNode serviceNodeInformation=(GenericServiceNode) nodeWideData;

        //将信息写入数据库
        //将节点信息存入应用节点表
        GuidAllocator guidAllocator = GUIDs.newGuidAllocator();
        GUID serviceNodeGUID = guidAllocator.nextGUID72();
        serviceNodeInformation.setGuid(serviceNodeGUID);
        this.serviceNodeManipulator.insert(serviceNodeInformation);

        //将应用节点基础信息存入信息表
        GUID descriptionGUID = guidAllocator.nextGUID72();
        GenericServiceNodeMeta serviceDescription = serviceNodeInformation.getServiceNodeMetadata();
        serviceDescription.setGuid(descriptionGUID);
        this.serviceMetaManipulator.insert(serviceDescription);

        //将应用元信息存入元信息表
        GUID metadataGUID = guidAllocator.nextGUID72();
        GenericNodeCommonData metadata = serviceNodeInformation.getAttributes();
        metadata.setGuid(metadataGUID);
        this.commonDataManipulator.insert(metadata);

        //将节点信息存入主表
        GUIDDistributedTrieNode node = new GUIDDistributedTrieNode();
        node.setBaseDataGUID(descriptionGUID);
        node.setGuid(serviceNodeGUID);
        node.setNodeMetadataGUID(metadataGUID);
        node.setType( UOIUtils.createLocalJavaClass( nodeWideData.getClass().getName() ) );
        this.trieTreeManipulator.insert( this.tireOwnerManipulator, node);
        return serviceNodeGUID;
    }

    @Override
    public void remove( GUID guid ) {
        GUIDDistributedTrieNode node = this.trieTreeManipulator.getNode(guid);
        this.serviceNodeManipulator.remove(node.getGuid());
        this.serviceMetaManipulator.remove(node.getAttributesGUID());
        this.commonDataManipulator.remove(node.getNodeMetadataGUID());
    }

    @Override
    public ServiceTreeNode get(GUID guid) {
        GUIDDistributedTrieNode node = this.trieTreeManipulator.getNode(guid);
        GenericServiceNode genericServiceNode = new GenericServiceNode();
        GenericServiceNodeMeta serviceMeta = this.serviceMetaManipulator.getServiceMeta(node.getAttributesGUID());
        GenericNodeCommonData commonData = this.commonDataManipulator.getNodeMetadata(node.getNodeMetadataGUID());
        GUIDDistributedTrieNode guidDistributedTrieNode = this.trieTreeManipulator.getNode(guid);

        genericServiceNode.setServiceNodeMetadata(serviceMeta);
        genericServiceNode.setNodeCommonData(commonData);
        genericServiceNode.setDistributedTreeNode(guidDistributedTrieNode);
        genericServiceNode.setGuid(guid);
        genericServiceNode.setName(serviceMeta.getName());

        return genericServiceNode;
    }

    @Override
    public void update(ServiceTreeNode nodeWideData) {

    }

}
