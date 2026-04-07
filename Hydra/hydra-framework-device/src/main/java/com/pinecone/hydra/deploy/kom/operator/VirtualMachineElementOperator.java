package com.pinecone.hydra.deploy.kom.operator;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.deploy.kom.entity.GenericVirtualMachineElement;
import com.pinecone.hydra.deploy.kom.DeployInstrument;
import com.pinecone.hydra.deploy.kom.entity.VirtualMachineElement;

import com.pinecone.hydra.deploy.kom.source.DeployMasterManipulator;
import com.pinecone.hydra.deploy.kom.source.VirtualMachineManipulator;
import com.pinecone.hydra.system.ko.UOIUtils;
import com.pinecone.hydra.unit.imperium.GUIDImperialTrieNode;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;

public class VirtualMachineElementOperator extends ArchElementOperator implements ElementOperator{

    protected VirtualMachineManipulator virtualMachineManipulator;



    public VirtualMachineElementOperator( ElementOperatorFactory factory ) {
        this( factory.getTaskMasterManipulator(),factory.getServicesTree() );
        this.factory = factory;
    }

    public VirtualMachineElementOperator( DeployMasterManipulator masterManipulator, DeployInstrument deployInstrument ){
        super( masterManipulator, deployInstrument);

        this.virtualMachineManipulator = masterManipulator.getVirtualMachineManipulator();
    }


    @Override
    public GUID insert( TreeNode treeNode ) {
        GenericVirtualMachineElement virtualMachineElement = ( GenericVirtualMachineElement ) treeNode;

        //将信息写入数据库
        //将节点信息存入应用节点表
        GuidAllocator guidAllocator = this.deployInstrument.getGuidAllocator();
        GUID taskNodeGUID = guidAllocator.nextGUID();
        virtualMachineElement.setGuid(taskNodeGUID);

        this.virtualMachineManipulator.insert( virtualMachineElement );
        //将应用元信息存入元信息表
        this.nodeMetaManipulator.insert( virtualMachineElement );


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
    public VirtualMachineElement get(GUID guid ) {
        GUIDImperialTrieNode node = this.imperialTree.getNode( guid );
        VirtualMachineElement virtualMachineElement   = this.virtualMachineManipulator.getDeployNode( guid, this.deployInstrument);
        //TODO
/*
        this.applyCommonMeta( virtualMachineElement, this.nodeMetaManipulator.getNodeCommonMeta( guid ) );
*/

        virtualMachineElement.setDistributedTreeNode(node);

        virtualMachineElement.setGuid( guid );

        return virtualMachineElement;
    }

    @Override
    public VirtualMachineElement get(GUID guid, int depth ) {
        return this.get( guid );
    }

    @Override
    public VirtualMachineElement getAsRootDepth(GUID guid ) {
        return this.get( guid );
    }

    @Override
    public void update( TreeNode nodeWideData ) {
        VirtualMachineElement serviceElement = (VirtualMachineElement) nodeWideData;
        this.virtualMachineManipulator.update( serviceElement );
        this.nodeMetaManipulator.update( serviceElement );
    }

    @Override
    public void updateName( GUID guid, String name ) {

    }

    private void removeNode( GUID guid ){
        GUIDImperialTrieNode node = this.imperialTree.getNode(guid);
        this.imperialTree.purge( guid );
        this.imperialTree.removeCachePath( guid );
        this.virtualMachineManipulator.remove( node.getGuid() );
        this.nodeMetaManipulator.remove( node.getNodeMetadataGUID() );
    }
}
