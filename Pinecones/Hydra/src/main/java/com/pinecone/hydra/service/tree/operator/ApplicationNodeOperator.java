package com.pinecone.hydra.service.tree.operator;

import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.tree.UOIUtils;
import com.pinecone.hydra.service.tree.nodes.ServiceTreeNode;
import com.pinecone.hydra.service.tree.meta.GenericApplicationNodeMeta;
import com.pinecone.hydra.service.tree.nodes.GenericApplicationNode;
import com.pinecone.hydra.service.tree.GenericNodeCommonData;
import com.pinecone.hydra.service.tree.source.ServiceMasterManipulator;
import com.pinecone.hydra.unit.udtt.source.TireOwnerManipulator;
import com.pinecone.hydra.unit.udtt.source.TrieTreeManipulator;
import com.pinecone.hydra.service.tree.source.ApplicationMetaManipulator;
import com.pinecone.hydra.service.tree.source.ApplicationNodeManipulator;
import com.pinecone.hydra.unit.udtt.GUIDDistributedTrieNode;
import com.pinecone.hydra.service.tree.source.CommonDataManipulator;
import com.pinecone.ulf.util.id.GuidAllocator;
import com.pinecone.ulf.util.id.GUIDs;

public class ApplicationNodeOperator implements MetaNodeOperator {
    protected ApplicationNodeManipulator        applicationNodeManipulator;
    protected ApplicationMetaManipulator        applicationMetaManipulator;
    protected CommonDataManipulator             commonDataManipulator;
    protected TrieTreeManipulator               trieTreeManipulator;
    protected TireOwnerManipulator              tireOwnerManipulator;

    public ApplicationNodeOperator( ServiceMasterManipulator manipulators ) {
        this(
                manipulators.getApplicationNodeManipulator(),
                manipulators.getApplicationMetaManipulator(),
                manipulators.getCommonDataManipulator(),
                manipulators.getTrieTreeManipulator(),
                manipulators.getTireOwnerManipulator()
        );
    }

    public ApplicationNodeOperator(
            ApplicationNodeManipulator applicationNodeManipulator, ApplicationMetaManipulator applicationMetaManipulator,
            CommonDataManipulator commonDataManipulator, TrieTreeManipulator trieTreeManipulator, TireOwnerManipulator ownerManipulator
    ){
        this.applicationNodeManipulator = applicationNodeManipulator;
        this.applicationMetaManipulator = applicationMetaManipulator;
        this.commonDataManipulator      = commonDataManipulator;
        this.trieTreeManipulator        = trieTreeManipulator;
        this.tireOwnerManipulator       = ownerManipulator;
    }


    @Override
    public GUID insert( ServiceTreeNode nodeWideData ) {
        Debug.trace("保存节点"+nodeWideData);
        GenericApplicationNode applicationNodeInformation = (GenericApplicationNode) nodeWideData;
        //将信息写入数据库
        //将节点信息存入应用节点表
        GuidAllocator guidAllocator = GUIDs.newGuidAllocator();
        GUID applicationNodeGUID = guidAllocator.nextGUID72();
        applicationNodeInformation.setGuid(applicationNodeGUID);
        this.applicationNodeManipulator.insert(applicationNodeInformation);

        //将应用节点基础信息存入信息表
        GUID descriptionGUID = guidAllocator.nextGUID72();
        GenericApplicationNodeMeta applicationDescription = applicationNodeInformation.getApplicationNodeMeta();
        applicationDescription.setGuid(descriptionGUID);
        this.applicationMetaManipulator.insert(applicationDescription);

        //将应用元信息存入元信息表
        GUID metadataGUID = guidAllocator.nextGUID72();
        GenericNodeCommonData metadata = applicationNodeInformation.getNodeCommonData();
        metadata.setGuid(metadataGUID);
        this.commonDataManipulator.insert(metadata);

        //将节点信息存入主表
        GUIDDistributedTrieNode node = new GUIDDistributedTrieNode();
        node.setBaseDataGUID(descriptionGUID);
        node.setGuid(applicationNodeGUID);
        node.setNodeMetadataGUID(metadataGUID);
        node.setType( UOIUtils.createLocalJavaClass( nodeWideData.getClass().getName() ) );
        this.trieTreeManipulator.insert( this.tireOwnerManipulator, node );
        return applicationNodeGUID;
    }

    @Override
    public void remove(GUID guid) {
        GUIDDistributedTrieNode node = this.trieTreeManipulator.getNode(guid);
        this.applicationMetaManipulator.remove(node.getBaseDataGUID());
        this.commonDataManipulator.remove(node.getNodeMetadataGUID());
        this.applicationNodeManipulator.remove(node.getGuid());
    }

    @Override
    public ServiceTreeNode get(GUID guid) {
        GUIDDistributedTrieNode node = this.trieTreeManipulator.getNode(guid);
        GenericApplicationNode genericApplicationNode = new GenericApplicationNode();

        GenericApplicationNodeMeta applicationDescription = this.applicationMetaManipulator.getApplicationMeta(node.getBaseDataGUID());
        GUIDDistributedTrieNode guidDistributedTrieNode = this.trieTreeManipulator.getNode(guid);
        GenericNodeCommonData nodeMetadata = this.commonDataManipulator.getNodeMetadata(node.getNodeMetadataGUID());

        genericApplicationNode.setApplicationNodeMeta(applicationDescription);
        genericApplicationNode.setNodeCommonData(nodeMetadata);
        genericApplicationNode.setDistributedTreeNode(guidDistributedTrieNode);

        genericApplicationNode.setName(applicationDescription.getName());
        genericApplicationNode.setGuid(genericApplicationNode.getGuid());
        return genericApplicationNode;
    }

    @Override
    public void update(ServiceTreeNode nodeWideData) {

    }

}
