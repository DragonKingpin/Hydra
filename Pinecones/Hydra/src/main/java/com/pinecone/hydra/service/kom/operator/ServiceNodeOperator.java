package com.pinecone.hydra.service.kom.operator;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.kom.ServiceFamilyNode;
import com.pinecone.hydra.service.kom.ServicesInstrument;
import com.pinecone.hydra.service.kom.entity.GenericServiceElement;
import com.pinecone.hydra.service.kom.entity.ServiceElement;
import com.pinecone.hydra.service.kom.entity.ServiceTreeNode;
import com.pinecone.hydra.service.kom.source.ServiceMasterManipulator;
import com.pinecone.hydra.service.kom.source.ServiceMetaManipulator;
import com.pinecone.hydra.service.kom.source.ServiceNodeManipulator;
import com.pinecone.hydra.system.ko.UOIUtils;
import com.pinecone.hydra.unit.udtt.GUIDDistributedTrieNode;
import com.pinecone.hydra.unit.udtt.entity.TreeNode;
import com.pinecone.ulf.util.id.GUIDs;
import com.pinecone.ulf.util.id.GuidAllocator;

public class ServiceNodeOperator extends ArchServiceOperator implements ServiceOperator {
    protected ServiceNodeManipulator  serviceNodeManipulator;
    protected ServiceMetaManipulator  serviceMetaManipulator;

    public ServiceNodeOperator( ServiceOperatorFactory factory ) {
        this( factory.getServiceMasterManipulator(),factory.getServicesTree() );
        this.factory = factory;
    }

    public ServiceNodeOperator(ServiceMasterManipulator masterManipulator, ServicesInstrument servicesInstrument){
        super( masterManipulator, servicesInstrument);
       this.serviceNodeManipulator = masterManipulator.getServiceNodeManipulator();
       this.serviceMetaManipulator = masterManipulator.getServiceMetaManipulator();

    }


    @Override
    public GUID insert(TreeNode nodeWideData) {
        GenericServiceElement serviceElement=(GenericServiceElement) nodeWideData;

        //将信息写入数据库
        //将节点信息存入应用节点表
        GuidAllocator guidAllocator = GUIDs.newGuidAllocator();
        GUID serviceNodeGUID = guidAllocator.nextGUID72();
        serviceElement.setGuid(serviceNodeGUID);
        this.serviceNodeManipulator.insert(serviceElement);

        //将应用节点基础信息存入信息表
        GUID descriptionGUID = guidAllocator.nextGUID72();
        if ( serviceElement.getMetaGuid() == null ){
            serviceElement.setMetaGuid( descriptionGUID);
        }
        this.serviceMetaManipulator.insert( serviceElement );


        //将应用元信息存入元信息表
       this.commonDataManipulator.insert( serviceElement );


        //将节点信息存入主表
        GUIDDistributedTrieNode node = new GUIDDistributedTrieNode();
        node.setNodeMetadataGUID(descriptionGUID);
        node.setGuid(serviceNodeGUID);
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
        ServiceElement serviceElement = new GenericServiceElement();
        if( node.getNodeMetadataGUID() != null ){
            serviceElement = this.serviceMetaManipulator.getServiceMeta( node.getNodeMetadataGUID() );
        }

        this.applyServiceNode( serviceElement, this.commonDataManipulator.getNodeCommonData( guid ) );

        serviceElement.setDistributedTreeNode(node);
        serviceElement.setGuid(guid);
        serviceElement.setName(this.serviceNodeManipulator.getServiceNode(guid).getName());

        return serviceElement;
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
        this.commonDataManipulator.remove(node.getNodeMetadataGUID());
    }

    private void applyServiceNode( ServiceElement serviceElement, ServiceFamilyNode serviceFamilyNode ){
        serviceElement.setGuid( serviceFamilyNode.getGuid() );
        serviceElement.setScenario( serviceFamilyNode.getScenario() );
        serviceElement.setPrimaryImplLang( serviceFamilyNode.getPrimaryImplLang() );
        serviceElement.setExtraInformation( serviceFamilyNode.getExtraInformation() );
        serviceElement.setLevel( serviceElement.getLevel() );
        serviceElement.setDescription( serviceElement.getDescription() );
    }
}
