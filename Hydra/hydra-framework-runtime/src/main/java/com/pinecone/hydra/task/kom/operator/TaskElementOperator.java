package com.pinecone.hydra.task.kom.operator;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.task.kom.TasksInstrument;
import com.pinecone.hydra.task.kom.entity.GenericTaskElement;
import com.pinecone.hydra.task.kom.entity.TaskElement;
import com.pinecone.hydra.task.kom.source.TaskMasterManipulator;
import com.pinecone.hydra.task.kom.source.TaskMetaManipulator;

import com.pinecone.hydra.system.ko.UOIUtils;
import com.pinecone.hydra.task.kom.source.TaskNodeManipulator;
import com.pinecone.hydra.unit.imperium.GUIDImperialTrieNode;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;

public class TaskElementOperator extends ArchElementOperator implements ElementOperator{
    protected TaskNodeManipulator taskNodeManipulator;
    protected TaskMetaManipulator taskMetaManipulator;

    public TaskElementOperator( ElementOperatorFactory factory ) {
        this( factory.getTaskMasterManipulator(),factory.getTasksTree() );
        this.factory = factory;
    }

    public TaskElementOperator(TaskMasterManipulator masterManipulator, TasksInstrument servicesInstrument ){
        super( masterManipulator, servicesInstrument);
        this.taskNodeManipulator = masterManipulator.getTaskNodeManipulator();
        this.taskMetaManipulator = masterManipulator.getTaskMetaManipulator();

    }


    @Override
    public GUID insert(TreeNode treeNode ) {
        TaskElement taskElement = (TaskElement) treeNode;
/*        TaskNode taskNode = (TaskNode) treeNode;*/

        //将信息写入数据库
        //将节点信息存入应用节点表
        GuidAllocator guidAllocator = this.tasksInstrument.getGuidAllocator();
        GUID taskNodeGUID = guidAllocator.nextGUID();
/*        taskNode.setGuid(taskNodeGUID);*/
        taskElement.setGuid(taskNodeGUID);
        /*this.taskNodeManipulator.insert(taskElement);*/
        this.taskNodeManipulator.insert( taskElement );

        //将应用节点基础信息存入信息表
     /*   GUID metaGUID = guidAllocator.nextGUID();
        if ( taskElement.getMetaGuid() == null ){
            taskElement.setMetaGuid( metaGUID );
        }*/
       /* this.taskMetaManipulator.insert( taskElement );*/

        //将应用元信息存入元信息表
      /*  this.commonDataManipulator.insert( taskElement );*/

        //将节点信息存入主表
        GUIDImperialTrieNode node = new GUIDImperialTrieNode();
        /*node.setNodeMetadataGUID( metaGUID );*/
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
    public TaskElement get(GUID guid ) {
        GUIDImperialTrieNode node = this.imperialTree.getNode(guid);
        TaskElement taskElement = new GenericTaskElement();
        if( node.getNodeMetadataGUID() != null ){
            taskElement = this.taskMetaManipulator.getTaskMeta( node.getNodeMetadataGUID() );
        }

        this.applyCommonMeta( taskElement, this.commonDataManipulator.getNodeCommonData( guid ) );

        taskElement.setDistributedTreeNode(node);
        taskElement.setGuid( guid );
        taskElement.setName( this.taskNodeManipulator.getTaskNode(guid).getName() );

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
        GenericTaskElement serviceElement = (GenericTaskElement) nodeWideData;
        this.taskNodeManipulator.update( serviceElement );
        this.taskMetaManipulator.update( serviceElement );
        this.commonDataManipulator.update( serviceElement );
    }

    @Override
    public void updateName(GUID guid, String name) {

    }

    private void removeNode( GUID guid ){
        GUIDImperialTrieNode node = this.imperialTree.getNode(guid);
        this.imperialTree.purge( guid );
        this.imperialTree.removeCachePath( guid );
        this.taskNodeManipulator.remove( node.getGuid() );
        this.taskMetaManipulator.remove( node.getAttributesGUID() );
        this.commonDataManipulator.remove( node.getNodeMetadataGUID() );
    }
}
