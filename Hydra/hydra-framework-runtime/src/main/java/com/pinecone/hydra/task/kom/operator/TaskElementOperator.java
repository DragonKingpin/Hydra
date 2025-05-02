package com.pinecone.hydra.task.kom.operator;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.task.kom.TaskInstrument;
import com.pinecone.hydra.task.kom.entity.GenericTaskElement;
import com.pinecone.hydra.task.kom.entity.TaskElement;
import com.pinecone.hydra.task.kom.source.TaskMasterManipulator;
import com.pinecone.hydra.task.kom.source.TaskNodeManipulator;
import com.pinecone.hydra.system.ko.UOIUtils;
import com.pinecone.hydra.unit.imperium.GUIDImperialTrieNode;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;

public class TaskElementOperator extends ArchElementOperator implements ElementOperator {
    protected TaskNodeManipulator taskNodeManipulator;

    public TaskElementOperator( ElementOperatorFactory factory ) {
        this( factory.getTaskMasterManipulator(),factory.getServicesTree() );
        this.factory = factory;
    }

    public TaskElementOperator( TaskMasterManipulator masterManipulator, TaskInstrument taskInstrument ){
        super( masterManipulator, taskInstrument);
        this.taskNodeManipulator = masterManipulator.getTaskNodeManipulator();
    }


    @Override
    public GUID insert( TreeNode treeNode ) {
        GenericTaskElement taskElement = (GenericTaskElement) treeNode;

        //将信息写入数据库
        //将节点信息存入应用节点表
        GuidAllocator guidAllocator = this.taskInstrument.getGuidAllocator();
        GUID taskNodeGUID = guidAllocator.nextGUID();
        taskElement.setGuid(taskNodeGUID);
        this.taskNodeManipulator.insert( taskElement );


        //将应用元信息存入元信息表
       this.nodeMetaManipulator.insert( taskElement );


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
    public TaskElement get( GUID guid ) {
        GUIDImperialTrieNode node = this.imperialTree.getNode( guid );
        TaskElement taskElement   = this.taskNodeManipulator.getTaskNode( guid, this.taskInstrument );

        this.applyCommonMeta( taskElement, this.nodeMetaManipulator.getNodeCommonMeta( guid ) );

        taskElement.setDistributedTreeNode(node);
        taskElement.setGuid( guid );

        return taskElement;
    }

    @Override
    public TaskElement get( GUID guid, int depth ) {
        return this.get( guid );
    }

    @Override
    public TaskElement getSelf( GUID guid ) {
        return this.get( guid );
    }

    @Override
    public void update( TreeNode nodeWideData ) {
        TaskElement serviceElement = (TaskElement) nodeWideData;
        this.taskNodeManipulator.update( serviceElement );
        this.nodeMetaManipulator.update( serviceElement );
    }

    @Override
    public void updateName( GUID guid, String name ) {

    }

    private void removeNode( GUID guid ){
        GUIDImperialTrieNode node = this.imperialTree.getNode(guid);
        this.imperialTree.purge( guid );
        this.imperialTree.removeCachePath( guid );
        this.taskNodeManipulator.remove( node.getGuid() );
        this.nodeMetaManipulator.remove( node.getNodeMetadataGUID() );
    }
}
