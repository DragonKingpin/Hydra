package com.pinecone.hydra.deploy.kom.operator;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.deploy.kom.DeployInstrument;
import com.pinecone.hydra.deploy.kom.entity.GenericContainerElement;
import com.pinecone.hydra.deploy.kom.entity.ContainerElement;
import com.pinecone.hydra.deploy.kom.source.DeployMasterManipulator;
import com.pinecone.hydra.deploy.kom.source.ContainerElementManipulator;
import com.pinecone.hydra.system.ko.UOIUtils;
import com.pinecone.hydra.unit.imperium.GUIDImperialTrieNode;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;

public class ContainerElementOperator extends ArchElementOperator implements ElementOperator{

    protected ContainerElementManipulator containerElementManipulator;



    public ContainerElementOperator(ElementOperatorFactory factory ) {
        this( factory.getTaskMasterManipulator(),factory.getServicesTree() );
        this.factory = factory;
    }

    public ContainerElementOperator(DeployMasterManipulator masterManipulator, DeployInstrument deployInstrument){
        super( masterManipulator, deployInstrument);

        this.containerElementManipulator = masterManipulator.getContainerElementManipulator();
    }


    @Override
    public GUID insert(TreeNode treeNode ) {
        GenericContainerElement containerElement = ( GenericContainerElement ) treeNode;

        //将信息写入数据库
        //将节点信息存入应用节点表
        GuidAllocator guidAllocator = this.deployInstrument.getGuidAllocator();
        GUID taskNodeGUID = guidAllocator.nextGUID();
        containerElement.setGuid(taskNodeGUID);

        this.containerElementManipulator.insert( containerElement );
        //将应用元信息存入元信息表
        this.nodeMetaManipulator.insert( containerElement );


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
    public ContainerElement get( GUID guid ) {
        GUIDImperialTrieNode node = this.imperialTree.getNode( guid );
        ContainerElement ContainerElement   = this.containerElementManipulator.getContainerElement( guid, this.deployInstrument);
        //TODO
/*
        this.applyCommonMeta( ContainerElement, this.nodeMetaManipulator.getNodeCommonMeta( guid ) );
*/

        ContainerElement.setDistributedTreeNode(node);

        ContainerElement.setGuid( guid );

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
        this.containerElementManipulator.update( serviceElement );
        this.nodeMetaManipulator.update( serviceElement );
    }

    @Override
    public void updateName( GUID guid, String name ) {

    }

    private void removeNode( GUID guid ){
        GUIDImperialTrieNode node = this.imperialTree.getNode(guid);
        this.imperialTree.purge( guid );
        this.imperialTree.removeCachePath( guid );
        this.containerElementManipulator.remove( node.getGuid() );
        this.nodeMetaManipulator.remove( node.getNodeMetadataGUID() );
    }
}
