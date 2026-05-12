package com.pinecone.hydra.deploy.kom.operator;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.deploy.kom.DeployInstrument;
import com.pinecone.hydra.deploy.kom.entity.GenericQuickElement;
import com.pinecone.hydra.deploy.kom.entity.QuickElement;
import com.pinecone.hydra.deploy.kom.source.DeployMasterManipulator;
import com.pinecone.hydra.deploy.kom.source.QuickElementManipulator;
import com.pinecone.hydra.unit.imperium.GUIDImperialTrieNode;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;

public class QuickElementOperator extends ArchElementOperator implements ElementOperator{
    protected QuickElementManipulator quickElementManipulator;



    public QuickElementOperator( ElementOperatorFactory factory ) {
        this( factory.getTaskMasterManipulator(),factory.getServicesTree() );
        this.factory = factory;
    }

    public QuickElementOperator( DeployMasterManipulator masterManipulator, DeployInstrument deployInstrument ){
        super( masterManipulator, deployInstrument);

        this.quickElementManipulator = masterManipulator.getQuickElementManipulator();
    }


    @Override
    public GUID insert(TreeNode treeNode ) {
        GenericQuickElement quickElement = ( GenericQuickElement ) treeNode;

        //将信息写入数据库
        //将节点信息存入应用节点表
        GUID taskNodeGUID = this.affirmGuid( quickElement );
        quickElement.setGuid(taskNodeGUID);

        this.quickElementManipulator.insert( quickElement );

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
    public QuickElement get(GUID guid ) {
        GUIDImperialTrieNode node = this.imperialTree.getNode( guid );
        QuickElement quickElement   = this.quickElementManipulator.getQuickElement( guid, this.deployInstrument);
        if( quickElement == null ) {
            return null;
        }
        this.applyTreeNode( quickElement, node );

        return quickElement;
    }

    @Override
    public QuickElement get(GUID guid, int depth ) {
        return this.get( guid );
    }

    @Override
    public QuickElement getAsRootDepth(GUID guid ) {
        return this.get( guid );
    }

    @Override
    public void update( TreeNode nodeWideData ) {
        QuickElement quickElement = (QuickElement) nodeWideData;
        this.touchForUpdate( quickElement );
        this.quickElementManipulator.update( quickElement );
        this.imperialTree.removeCachePath( quickElement.getGuid() );
    }

    @Override
    public void updateName( GUID guid, String name ) {

    }

    private void removeNode( GUID guid ){
        GUIDImperialTrieNode node = this.imperialTree.getNode(guid);
        this.imperialTree.purge( guid );
        this.imperialTree.removeCachePath( guid );
        this.quickElementManipulator.remove( node.getGuid() );
    }
}
