package com.pinecone.hydra.deploy.kom.operator;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.deploy.kom.DeployInstrument;
import com.pinecone.hydra.deploy.kom.entity.GenericQuickElement;
import com.pinecone.hydra.deploy.kom.entity.QuickElement;
import com.pinecone.hydra.deploy.kom.source.DeployMasterManipulator;
import com.pinecone.hydra.deploy.kom.source.QuickElementManipulator;
import com.pinecone.hydra.system.ko.UOIUtils;
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
        GuidAllocator guidAllocator = this.deployInstrument.getGuidAllocator();
        GUID taskNodeGUID = guidAllocator.nextGUID();
        quickElement.setGuid(taskNodeGUID);

        this.quickElementManipulator.insert( quickElement );
        //将应用元信息存入元信息表
        this.nodeMetaManipulator.insert( quickElement );


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
    public QuickElement get(GUID guid ) {
        GUIDImperialTrieNode node = this.imperialTree.getNode( guid );
        QuickElement quickElement   = this.quickElementManipulator.getQuickElement( guid, this.deployInstrument);
    //TODO
/*
        this.applyCommonMeta( quickElement, this.nodeMetaManipulator.getNodeCommonMeta( guid ) );
*/

        quickElement.setDistributedTreeNode(node);

        quickElement.setGuid( guid );

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
        this.quickElementManipulator.update( quickElement );
        this.nodeMetaManipulator.update( quickElement );
    }

    @Override
    public void updateName( GUID guid, String name ) {

    }

    private void removeNode( GUID guid ){
        GUIDImperialTrieNode node = this.imperialTree.getNode(guid);
        this.imperialTree.purge( guid );
        this.imperialTree.removeCachePath( guid );
        this.quickElementManipulator.remove( node.getGuid() );
        this.nodeMetaManipulator.remove( node.getNodeMetadataGUID() );
    }
}
