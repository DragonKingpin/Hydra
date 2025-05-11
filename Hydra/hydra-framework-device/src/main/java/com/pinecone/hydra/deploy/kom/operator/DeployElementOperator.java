package com.pinecone.hydra.deploy.kom.operator;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.deploy.kom.DeployInstrument;
import com.pinecone.hydra.deploy.kom.entity.DeployElement;
import com.pinecone.hydra.deploy.kom.entity.GenericDeployElement;
import com.pinecone.hydra.system.ko.UOIUtils;
import com.pinecone.hydra.deploy.kom.source.DeployMasterManipulator;
import com.pinecone.hydra.deploy.kom.source.DeployNodeManipulator;
import com.pinecone.hydra.unit.imperium.GUIDImperialTrieNode;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;

public class DeployElementOperator extends ArchElementOperator implements ElementOperator {
    protected DeployNodeManipulator deployNodeManipulator;

    public DeployElementOperator(ElementOperatorFactory factory ) {
        this( factory.getTaskMasterManipulator(),factory.getServicesTree() );
        this.factory = factory;
    }

    public DeployElementOperator(DeployMasterManipulator masterManipulator, DeployInstrument deployInstrument){
        super( masterManipulator, deployInstrument);

        this.deployNodeManipulator = masterManipulator.getDeployNodeManipulator();
    }


    @Override
    public GUID insert( TreeNode treeNode ) {
        GenericDeployElement deployElement = ( GenericDeployElement ) treeNode;

        //将信息写入数据库
        //将节点信息存入应用节点表
        GuidAllocator guidAllocator = this.deployInstrument.getGuidAllocator();
        GUID taskNodeGUID = guidAllocator.nextGUID();
        deployElement.setGuid(taskNodeGUID);
        this.deployNodeManipulator.insert( deployElement );


        //将应用元信息存入元信息表
       this.nodeMetaManipulator.insert( deployElement );


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
    public DeployElement get(GUID guid ) {
        GUIDImperialTrieNode node = this.imperialTree.getNode( guid );
        DeployElement deployElement   = this.deployNodeManipulator.getDeployNode( guid, this.deployInstrument);

        this.applyCommonMeta( deployElement, this.nodeMetaManipulator.getNodeCommonMeta( guid ) );

        deployElement.setDistributedTreeNode(node);

        deployElement.setGuid( guid );

        return deployElement;
    }

    @Override
    public DeployElement get(GUID guid, int depth ) {
        return this.get( guid );
    }

    @Override
    public DeployElement getAsRootDepth(GUID guid ) {
        return this.get( guid );
    }

    @Override
    public void update( TreeNode nodeWideData ) {
        DeployElement serviceElement = (DeployElement) nodeWideData;
        this.deployNodeManipulator.update( serviceElement );
        this.nodeMetaManipulator.update( serviceElement );
    }

    @Override
    public void updateName( GUID guid, String name ) {

    }

    private void removeNode( GUID guid ){
        GUIDImperialTrieNode node = this.imperialTree.getNode(guid);
        this.imperialTree.purge( guid );
        this.imperialTree.removeCachePath( guid );
        this.deployNodeManipulator.remove( node.getGuid() );
        this.nodeMetaManipulator.remove( node.getNodeMetadataGUID() );
    }
}
