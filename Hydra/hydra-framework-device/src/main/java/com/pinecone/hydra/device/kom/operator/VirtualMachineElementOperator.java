package com.pinecone.hydra.device.kom.operator;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.device.kom.entity.GenericVirtualMachineElement;
import com.pinecone.hydra.device.kom.DeviceInstrument;
import com.pinecone.hydra.device.kom.entity.VirtualMachineElement;

import com.pinecone.hydra.device.kom.source.DeviceMasterManipulator;
import com.pinecone.hydra.device.kom.source.VirtualMachineManipulator;
import com.pinecone.hydra.unit.imperium.GUIDImperialTrieNode;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;

public class VirtualMachineElementOperator extends ArchElementOperator implements ElementOperator{

    protected VirtualMachineManipulator virtualMachineManipulator;



    public VirtualMachineElementOperator( ElementOperatorFactory factory ) {
        this( factory.getTaskMasterManipulator(),factory.getServicesTree() );
        this.factory = factory;
    }

    public VirtualMachineElementOperator( DeviceMasterManipulator masterManipulator, DeviceInstrument deviceInstrument ){
        super( masterManipulator, deviceInstrument);

        this.virtualMachineManipulator = masterManipulator.getVirtualMachineManipulator();
    }


    @Override
    public GUID insert( TreeNode treeNode ) {
        GenericVirtualMachineElement virtualMachineElement = ( GenericVirtualMachineElement ) treeNode;

        //将信息写入数据库
        //将节点信息存入应用节点表
        GUID taskNodeGUID = this.affirmGuid( virtualMachineElement );
        virtualMachineElement.setGuid(taskNodeGUID);

        this.virtualMachineManipulator.insert( virtualMachineElement );

        //将节点信息存入主表
        GUIDImperialTrieNode node = this.newKernelNode( treeNode, taskNodeGUID );
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
        VirtualMachineElement virtualMachineElement   = this.virtualMachineManipulator.getDeviceNode( guid, this.deviceInstrument);
        if( virtualMachineElement == null ) {
            return null;
        }
        this.applyTreeNode( virtualMachineElement, node );

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
        this.touchForUpdate( serviceElement );
        this.virtualMachineManipulator.update( serviceElement );
        this.imperialTree.removeCachePath( serviceElement.getGuid() );
    }

    @Override
    public void updateName( GUID guid, String name ) {

    }

    private void removeNode( GUID guid ){
        GUIDImperialTrieNode node = this.imperialTree.getNode(guid);
        this.imperialTree.purge( guid );
        this.imperialTree.removeCachePath( guid );
        this.virtualMachineManipulator.remove( node.getGuid() );
    }
}
