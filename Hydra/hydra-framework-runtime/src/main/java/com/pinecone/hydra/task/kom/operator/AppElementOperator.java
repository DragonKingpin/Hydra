package com.pinecone.hydra.task.kom.operator;

import java.util.List;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.task.kom.TaskInstrument;
import com.pinecone.hydra.task.kom.entity.GenericAppElement;
import com.pinecone.hydra.task.kom.entity.AppElement;
import com.pinecone.hydra.task.kom.source.AppNodeManipulator;
import com.pinecone.hydra.task.kom.source.TaskMasterManipulator;
import com.pinecone.hydra.system.ko.UOIUtils;
import com.pinecone.hydra.unit.imperium.GUIDImperialTrieNode;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;

public class AppElementOperator extends ArchElementOperator implements ElementOperator {
    protected AppNodeManipulator appNodeManipulator;

    public AppElementOperator(ElementOperatorFactory factory ) {
        this( factory.getTaskMasterManipulator(),factory.taskInstrument() );
        this.factory = factory;
    }

    public AppElementOperator(TaskMasterManipulator masterManipulator, TaskInstrument taskInstrument){
        super( masterManipulator, taskInstrument);
        this.appNodeManipulator = masterManipulator.getAppNodeManipulator();
    }


    @Override
    public GUID insert( TreeNode treeNode ) {
        GenericAppElement appElement = (GenericAppElement) treeNode;

        GuidAllocator guidAllocator = this.taskInstrument.getGuidAllocator();
        GUID appNodeGUID = guidAllocator.nextGUID();
        appElement.setGuid( appNodeGUID );
        this.appNodeManipulator.insert( appElement );


        //将节点信息存入主表
        GUIDImperialTrieNode node = new GUIDImperialTrieNode();
        node.setNodeMetadataGUID(appNodeGUID);
        node.setGuid(appNodeGUID);
        node.setType( UOIUtils.createLocalJavaClass( treeNode.getClass().getName() ) );
        this.imperialTree.insert( node );
        return appNodeGUID;
    }


    @Override
    public void purge( GUID guid ) {
        //namespace节点需要递归删除其拥有节点若其引用节点，没有其他引用则进行清理
        List<GUIDImperialTrieNode> childNodes = this.imperialTree.getChildren(guid);
        GUIDImperialTrieNode node = this.imperialTree.getNode(guid);
        if ( !childNodes.isEmpty() ){
            List<GUID > subordinates = this.imperialTree.getSubordinates(guid);
            if ( !subordinates.isEmpty() ){
                for ( GUID subordinateGuid : subordinates ){
                    this.purgeByNodeType( subordinateGuid );
                }
            }
            childNodes = this.imperialTree.getChildren( guid );
            for( GUIDImperialTrieNode childNode : childNodes ){
                List<GUID > parentNodes = this.imperialTree.fetchParentGuids(childNode.getGuid());
                if ( parentNodes.size() > 1 ){
                    this.imperialTree.removeInheritance(childNode.getGuid(),guid);
                }
                else {
                    this.purgeByNodeType( childNode.getGuid() );
                }
            }
        }

        if ( this.isFolderElement( node ) && this.isAssignedToThisOperator( node ) ){
            this.removeNode(guid);
        }
        else {
            ElementOperator operator = this.resolveOperator( node );
            operator.purge( guid );
        }
    }

    @Override
    public AppElement get(GUID guid ) {
        AppElement appElement;
        appElement = this.appNodeManipulator.getAppElement( guid, this.taskInstrument );

        appElement.setGuid(appElement.getGuid());
        return appElement;
    }

    @Override
    public AppElement get(GUID guid, int depth ) {
        return this.get( guid );
    }

    @Override
    public AppElement getAsRootDepth(GUID guid ) {
        return this.get( guid );
    }

    @Override
    public void update( TreeNode treeNode ) {
        GenericAppElement applicationElement = (GenericAppElement) treeNode;
        this.appNodeManipulator.update( applicationElement );
    }

    @Override
    public void updateName( GUID guid, String name ) {

    }

    protected void removeNode( GUID guid ){
        GUIDImperialTrieNode node = this.imperialTree.getNode( guid );
        this.imperialTree.purge( guid );
        this.imperialTree.removeCachePath(guid);
        this.appNodeManipulator.remove( node.getGuid( ));
    }
}
