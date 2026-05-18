package com.pinecone.hydra.device.kom.operator;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.device.kom.DeviceInstrument;
import com.pinecone.hydra.device.kom.entity.GenericContainerElement;
import com.pinecone.hydra.device.kom.entity.ContainerElement;
import com.pinecone.hydra.device.kom.source.DeviceMasterManipulator;
import com.pinecone.hydra.device.kom.source.ContainerElementManipulator;
import com.pinecone.hydra.unit.imperium.GUIDImperialTrieNode;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;

public class ContainerElementOperator extends ArchElementOperator implements ElementOperator{

    protected ContainerElementManipulator containerElementManipulator;



    public ContainerElementOperator(ElementOperatorFactory factory ) {
        this( factory.getTaskMasterManipulator(),factory.getServicesTree() );
        this.factory = factory;
    }

    public ContainerElementOperator(DeviceMasterManipulator masterManipulator, DeviceInstrument deviceInstrument){
        super( masterManipulator, deviceInstrument);

        this.containerElementManipulator = masterManipulator.getContainerElementManipulator();
    }


    @Override
    public GUID insert(TreeNode treeNode ) {
        GenericContainerElement containerElement = ( GenericContainerElement ) treeNode;

        //将信息写入数据库
        //将节点信息存入应用节点表
        GUID taskNodeGUID = this.affirmGuid( containerElement );
        containerElement.setGuid(taskNodeGUID);

        this.containerElementManipulator.insert( containerElement );

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
    public ContainerElement get( GUID guid ) {
        GUIDImperialTrieNode node = this.imperialTree.getNode( guid );
        ContainerElement ContainerElement   = this.containerElementManipulator.getContainerElement( guid, this.deviceInstrument);
        if( ContainerElement == null ) {
            return null;
        }
        this.applyTreeNode( ContainerElement, node );

        return ContainerElement;
    }

    @Override
    public ContainerElement get(GUID guid, int depth ) {
        return this.get( guid );
    }

    @Override
    public ContainerElement getAsRootDepth(GUID guid ) {
        return this.get( guid );
    }

    @Override
    public void update( TreeNode nodeWideData ) {
        ContainerElement serviceElement = (ContainerElement) nodeWideData;
        this.touchForUpdate( serviceElement );
        this.containerElementManipulator.update( serviceElement );
        this.imperialTree.removeCachePath( serviceElement.getGuid() );
    }

    @Override
    public void updateName( GUID guid, String name ) {

    }

    private void removeNode( GUID guid ){
        GUIDImperialTrieNode node = this.imperialTree.getNode(guid);
        this.imperialTree.purge( guid );
        this.imperialTree.removeCachePath( guid );
        this.containerElementManipulator.remove( node.getGuid() );
    }
}
