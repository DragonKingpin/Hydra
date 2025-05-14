package com.pinecone.hydra.deploy.kom.operator;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.deploy.kom.DeployInstrument;
import com.pinecone.hydra.deploy.kom.entity.GenericPhysicalHostElement;
import com.pinecone.hydra.deploy.kom.entity.PhysicalHostElement;
import com.pinecone.hydra.deploy.kom.source.DeployMasterManipulator;
import com.pinecone.hydra.deploy.kom.source.PhysicalHostManipulator;
import com.pinecone.hydra.system.ko.UOIUtils;
import com.pinecone.hydra.unit.imperium.GUIDImperialTrieNode;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;

public class PhysicalHostElementOperator extends ArchElementOperator implements ElementOperator{
    protected PhysicalHostManipulator physicalHostManipulator;



    public PhysicalHostElementOperator(ElementOperatorFactory factory ) {
        this( factory.getTaskMasterManipulator(),factory.getServicesTree() );
        this.factory = factory;
    }

    public PhysicalHostElementOperator(DeployMasterManipulator masterManipulator, DeployInstrument deployInstrument){
        super( masterManipulator, deployInstrument);

        this.physicalHostManipulator = masterManipulator.getPhysicalHostManipulator();
    }


    @Override
    public GUID insert(TreeNode treeNode ) {
        GenericPhysicalHostElement physicalHostElement = ( GenericPhysicalHostElement ) treeNode;

        //将信息写入数据库
        //将节点信息存入应用节点表
        GuidAllocator guidAllocator = this.deployInstrument.getGuidAllocator();
        GUID taskNodeGUID = guidAllocator.nextGUID();
        physicalHostElement.setGuid(taskNodeGUID);

        this.physicalHostManipulator.insert( physicalHostElement );
        //将应用元信息存入元信息表
        this.nodeMetaManipulator.insert( physicalHostElement );


        //将节点信息存入主表
        GUIDImperialTrieNode node = new GUIDImperialTrieNode();
        node.setNodeMetadataGUID( taskNodeGUID ); // Since 20250419, the meta has been merged into the `node`.
        node.setGuid( taskNodeGUID );
        node.setType( UOIUtils.createLocalJavaClass( treeNode.getClass().getName() ) );
        this.imperialTree.insert( node );
        return taskNodeGUID;
    }

    @Override
    public void purge( GUID guid ) {
        this.removeNode( guid );
    }

    @Override
    public PhysicalHostElement get(GUID guid ) {
        GUIDImperialTrieNode node = this.imperialTree.getNode( guid );
        PhysicalHostElement physicalHostElement   = this.physicalHostManipulator.getDeployNode( guid, this.deployInstrument);
        //TODO
/*
        this.applyCommonMeta( physicalHostElement, this.nodeMetaManipulator.getNodeCommonMeta( guid ) );
*/

        physicalHostElement.setDistributedTreeNode(node);

        physicalHostElement.setGuid( guid );

        return physicalHostElement;
    }

    @Override
    public PhysicalHostElement get(GUID guid, int depth ) {
        return this.get( guid );
    }

    @Override
    public PhysicalHostElement getAsRootDepth(GUID guid ) {
        return this.get( guid );
    }

    @Override
    public void update( TreeNode nodeWideData ) {
        PhysicalHostElement serviceElement = (PhysicalHostElement) nodeWideData;
        this.physicalHostManipulator.update( serviceElement );
        this.nodeMetaManipulator.update( serviceElement );
    }

    @Override
    public void updateName( GUID guid, String name ) {

    }

    private void removeNode( GUID guid ){
        GUIDImperialTrieNode node = this.imperialTree.getNode(guid);
        this.imperialTree.purge( guid );
        this.imperialTree.removeCachePath( guid );
        this.physicalHostManipulator.remove( node.getGuid() );
        this.nodeMetaManipulator.remove( node.getNodeMetadataGUID() );
    }
    
}
