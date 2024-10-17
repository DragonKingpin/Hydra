package com.pinecone.hydra.service.kom.operator;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.uoi.UOI;
import com.pinecone.hydra.service.kom.ServicesTree;
import com.pinecone.hydra.system.ko.UOIUtils;
import com.pinecone.hydra.service.kom.nodes.ServiceTreeNode;
import com.pinecone.hydra.service.kom.GenericNodeCommonData;
import com.pinecone.hydra.service.kom.meta.GenericServiceNodeMeta;
import com.pinecone.hydra.service.kom.nodes.GenericServiceNode;
import com.pinecone.hydra.service.kom.source.ServiceMasterManipulator;
import com.pinecone.hydra.unit.udtt.entity.TreeNode;
import com.pinecone.hydra.unit.udtt.source.TireOwnerManipulator;
import com.pinecone.hydra.unit.udtt.source.TrieTreeManipulator;
import com.pinecone.hydra.unit.udtt.GUIDDistributedTrieNode;
import com.pinecone.hydra.service.kom.source.ServiceAttributeManipulator;
import com.pinecone.hydra.service.kom.source.ServiceMetaManipulator;
import com.pinecone.hydra.service.kom.source.ServiceNodeManipulator;
import com.pinecone.ulf.util.id.GuidAllocator;
import com.pinecone.ulf.util.id.GUIDs;

import java.util.List;

public class ServiceNodeOperator extends ArchServiceOperator implements ServiceOperator {
    protected ServiceNodeManipulator  serviceNodeManipulator;
    protected ServiceMetaManipulator  serviceMetaManipulator;

    public ServiceNodeOperator( ServiceOperatorFactory factory ) {
        this( factory.getServiceMasterManipulator(),factory.getServicesTree() );
        this.factory = factory;
    }

    public ServiceNodeOperator(ServiceMasterManipulator masterManipulator, ServicesTree servicesTree){
        super( masterManipulator,servicesTree );
       this.serviceNodeManipulator = masterManipulator.getServiceNodeManipulator();
       this.serviceMetaManipulator = masterManipulator.getServiceMetaManipulator();

    }


    @Override
    public GUID insert(TreeNode nodeWideData) {
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
        if ( serviceDescription != null ){
            serviceDescription.setGuid(descriptionGUID);
            this.serviceMetaManipulator.insert(serviceDescription);
        }
        else {
            descriptionGUID = null;
        }


        //将应用元信息存入元信息表
        GUID metadataGUID = guidAllocator.nextGUID72();
        GenericNodeCommonData metadata = serviceNodeInformation.getAttributes();
        if ( metadata != null ){
            metadata.setGuid(metadataGUID);
            this.attributeManipulator.insert(metadata);
        }
        else {
            metadataGUID = null;
        }


        //将节点信息存入主表
        GUIDDistributedTrieNode node = new GUIDDistributedTrieNode();
        node.setBaseDataGUID(descriptionGUID);
        node.setGuid(serviceNodeGUID);
        node.setNodeMetadataGUID(metadataGUID);
        node.setType( UOIUtils.createLocalJavaClass( nodeWideData.getClass().getName() ) );
        this.distributedTrieTree.insert( node);
        return serviceNodeGUID;
    }

    @Override
    public void purge(GUID guid ) {
        this.removeNode( guid );
    }

    @Override
    public ServiceTreeNode get(GUID guid) {
        GUIDDistributedTrieNode node = this.distributedTrieTree.getNode(guid);
        GenericServiceNode genericServiceNode = new GenericServiceNode();
        GenericServiceNodeMeta serviceMeta = this.serviceMetaManipulator.getServiceMeta(node.getAttributesGUID());
        GenericNodeCommonData commonData = this.attributeManipulator.getNodeMetadata(node.getNodeMetadataGUID());
        GenericServiceNode serviceNode = this.serviceNodeManipulator.getServiceNode(guid);
        genericServiceNode.setServiceNodeMetadata(serviceMeta);
        genericServiceNode.setNodeCommonData(commonData);
        genericServiceNode.setDistributedTreeNode(node);
        genericServiceNode.setGuid(guid);
        genericServiceNode.setName(serviceNode.getName());

        return genericServiceNode;
    }

    @Override
    public TreeNode get(GUID guid, int depth) {
        return null;
    }

    @Override
    public TreeNode getSelf(GUID guid) {
        return this.get( guid );
    }

    @Override
    public void update(TreeNode nodeWideData) {

    }

    @Override
    public void updateName(GUID guid, String name) {

    }

    private void removeNode( GUID guid ){
        GUIDDistributedTrieNode node = this.distributedTrieTree.getNode(guid);
        this.distributedTrieTree.purge( guid );
        this.distributedTrieTree.removeCachePath(guid);
        this.serviceNodeManipulator.remove(node.getGuid());
        this.serviceMetaManipulator.remove(node.getAttributesGUID());
        this.attributeManipulator.remove(node.getNodeMetadataGUID());
    }
}
