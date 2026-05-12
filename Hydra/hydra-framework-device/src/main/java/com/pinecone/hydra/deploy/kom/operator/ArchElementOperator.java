package com.pinecone.hydra.deploy.kom.operator;

import java.time.LocalDateTime;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.deploy.kom.DeployInstrument;
import com.pinecone.hydra.deploy.kom.entity.ElementNode;
import com.pinecone.hydra.deploy.kom.source.DeployMasterManipulator;
import com.pinecone.hydra.system.ko.UOIUtils;
import com.pinecone.hydra.unit.imperium.GUIDImperialTrieNode;
import com.pinecone.hydra.unit.imperium.ImperialTree;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;

public abstract class ArchElementOperator implements ElementOperator {
    protected DeployInstrument              deployInstrument;
    protected ImperialTree                  imperialTree;
    protected DeployMasterManipulator       deployMasterManipulator;
    protected ElementOperatorFactory        factory;

    public ArchElementOperator( ElementOperatorFactory factory ){
        this( factory.getTaskMasterManipulator(),factory.getServicesTree() );
        this.factory = factory;
    }

    public ArchElementOperator(DeployMasterManipulator masterManipulator, DeployInstrument deployInstrument){
        this.imperialTree = deployInstrument.getMasterTrieTree();
        this.deployInstrument = deployInstrument;
        this.deployMasterManipulator = masterManipulator;
        //this.factory = new GenericServiceOperatorFactory(servicesTree,masterManipulator);
    }

    public ElementOperatorFactory getOperatorFactory() {
        return this.factory;
    }

    protected GUID affirmGuid( ElementNode elementNode ) {
        GUID guid = elementNode.getGuid();
        if( guid == null ) {
            GuidAllocator guidAllocator = this.deployInstrument.getGuidAllocator();
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
