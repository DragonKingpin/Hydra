package com.pinecone.hydra.device.kom.operator;

import java.time.LocalDateTime;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.device.kom.DeviceInstrument;
import com.pinecone.hydra.device.kom.entity.ElementNode;
import com.pinecone.hydra.device.kom.source.DeviceMasterManipulator;
import com.pinecone.hydra.system.ko.UOIUtils;
import com.pinecone.hydra.unit.imperium.GUIDImperialTrieNode;
import com.pinecone.hydra.unit.imperium.ImperialTree;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;

public abstract class ArchElementOperator implements ElementOperator {
    protected DeviceInstrument              deviceInstrument;
    protected ImperialTree                  imperialTree;
    protected DeviceMasterManipulator       deviceMasterManipulator;
    protected ElementOperatorFactory        factory;

    public ArchElementOperator( ElementOperatorFactory factory ){
        this( factory.getTaskMasterManipulator(),factory.getServicesTree() );
        this.factory = factory;
    }

    public ArchElementOperator(DeviceMasterManipulator masterManipulator, DeviceInstrument deviceInstrument){
        this.imperialTree = deviceInstrument.getMasterTrieTree();
        this.deviceInstrument = deviceInstrument;
        this.deviceMasterManipulator = masterManipulator;
        //this.factory = new GenericServiceOperatorFactory(servicesTree,masterManipulator);
    }

    public ElementOperatorFactory getOperatorFactory() {
        return this.factory;
    }

    protected GUID affirmGuid( ElementNode elementNode ) {
        GUID guid = elementNode.getGuid();
        if( guid == null ) {
            GuidAllocator guidAllocator = this.deviceInstrument.getGuidAllocator();
            guid = guidAllocator.nextGUID();
            elementNode.setGuid( guid );
        }
        return guid;
    }

    protected GUIDImperialTrieNode newKernelNode( TreeNode treeNode, GUID guid ) {
        GUIDImperialTrieNode node = new GUIDImperialTrieNode();
        node.setGuid( guid );
        node.setBaseDataGUID( guid );
        node.setNodeMetadataGUID( guid );
        node.setType( UOIUtils.createLocalJavaClass( treeNode.getClass().getName() ) );
        return node;
    }

    protected void touchForUpdate( ElementNode elementNode ) {
        elementNode.setUpdateTime( LocalDateTime.now() );
    }

    protected void applyTreeNode( ElementNode elementNode, GUIDImperialTrieNode node ) {
        if( node == null ) {
            return;
        }
        elementNode.setDistributedTreeNode( node );
        elementNode.setGuid( node.getGuid() );
        elementNode.setMetaGuid( node.getNodeMetadataGUID() );
    }
}
