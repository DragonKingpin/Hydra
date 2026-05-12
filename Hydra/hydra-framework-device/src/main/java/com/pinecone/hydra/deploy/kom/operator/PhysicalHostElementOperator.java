package com.pinecone.hydra.deploy.kom.operator;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.deploy.kom.DeployInstrument;
import com.pinecone.hydra.deploy.kom.entity.GenericPhysicalHostElement;
import com.pinecone.hydra.deploy.kom.entity.PhysicalHostElement;
import com.pinecone.hydra.deploy.kom.source.DeployMasterManipulator;
import com.pinecone.hydra.deploy.kom.source.PhysicalHostManipulator;
import com.pinecone.hydra.unit.imperium.GUIDImperialTrieNode;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;

public class PhysicalHostElementOperator extends ArchElementOperator implements ElementOperator{
    protected PhysicalHostManipulator physicalHostManipulator;



    public PhysicalHostElementOperator( ElementOperatorFactory factory ) {
        this( factory.getTaskMasterManipulator(),factory.getServicesTree() );
        this.factory = factory;
    }

    public PhysicalHostElementOperator( DeployMasterManipulator masterManipulator, DeployInstrument deployInstrument ){
        super( masterManipulator, deployInstrument);

        this.physicalHostManipulator = masterManipulator.getPhysicalHostManipulator();
    }


    @Override
    public GUID insert(TreeNode treeNode ) {
        GenericPhysicalHostElement physicalHostElement = ( GenericPhysicalHostElement ) treeNode;

        //将信息写入数据库
        //将节点信息存入应用节点表
        GUID taskNodeGUID = this.affirmGuid( physicalHostElement );
        physicalHostElement.setGuid(taskNodeGUID);

        this.physicalHostManipulator.insert( physicalHostElement );

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
    public PhysicalHostElement get(GUID guid ) {
        GUIDImperialTrieNode node = this.imperialTree.getNode( guid );
        PhysicalHostElement physicalHostElement   = this.physicalHostManipulator.getPhysicalHostElement( guid, this.deployInstrument);
        if( physicalHostElement == null ) {
            return null;
        }
        this.applyTreeNode( physicalHostElement, node );

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
        this.touchForUpdate( serviceElement );
        this.physicalHostManipulator.update( serviceElement );
        this.imperialTree.removeCachePath( serviceElement.getGuid() );
    }

    @Override
    public void updateName( GUID guid, String name ) {

    }

    private void removeNode( GUID guid ){
        GUIDImperialTrieNode node = this.imperialTree.getNode(guid);
        this.imperialTree.purge( guid );
        this.imperialTree.removeCachePath( guid );
        this.physicalHostManipulator.remove( node.getGuid() );
    }
    
}
