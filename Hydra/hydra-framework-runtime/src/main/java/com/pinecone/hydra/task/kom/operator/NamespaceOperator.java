package com.pinecone.hydra.task.kom.operator;

import java.util.List;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.task.kom.TaskInstrument;
import com.pinecone.hydra.task.kom.entity.GenericNamespace;
import com.pinecone.hydra.task.kom.entity.Namespace;
import com.pinecone.hydra.task.kom.source.TaskMasterManipulator;
import com.pinecone.hydra.task.kom.source.TaskNamespaceManipulator;
import com.pinecone.hydra.system.ko.UOIUtils;
import com.pinecone.hydra.unit.imperium.GUIDImperialTrieNode;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;

public class NamespaceOperator extends ArchElementOperator implements ElementOperator {
    protected TaskNamespaceManipulator namespaceManipulator;

    public NamespaceOperator( ElementOperatorFactory factory ) {
        this( factory.getTaskMasterManipulator(),factory.taskInstrument() );
        this.factory = factory;
    }

    public NamespaceOperator( TaskMasterManipulator masterManipulator, TaskInstrument taskInstrument ) {
        super( masterManipulator, taskInstrument);
        this.namespaceManipulator = masterManipulator.getNamespaceManipulator();
    }

    @Override
    public GUID insert( TreeNode treeNode ) {
        GenericNamespace ns = ( GenericNamespace ) treeNode;

        //存节点基础信息
        GuidAllocator          guidAllocator = this.taskInstrument.getGuidAllocator();

        GUID namespaceGuid = guidAllocator.nextGUID();
        ns.setGuid( namespaceGuid );
        this.namespaceManipulator.insert( ns );

        //存元信息
        GUID metadataGUID = guidAllocator.nextGUID();
        ns.setMetaGuid( metadataGUID );
        //this.nodeMetaManipulator.insertNS( ns );


        GUIDImperialTrieNode node = new GUIDImperialTrieNode();
        node.setGuid( namespaceGuid );
        node.setNodeMetadataGUID( metadataGUID );
        node.setType( UOIUtils.createLocalJavaClass( treeNode.getClass().getName() ) );
        this.imperialTree.insert( node );
        return namespaceGuid;
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
    public Namespace get( GUID guid ) {
        GUIDImperialTrieNode node = this.imperialTree.getNode( guid );
        GenericNamespace                      namespace = new GenericNamespace( this.taskInstrument);
        GUIDImperialTrieNode guidDistributedTrieNode = this.imperialTree.getNode( node.getGuid() );

        GUID metaGuid = guidDistributedTrieNode.getNodeMetadataGUID();
        namespace.setDistributedTreeNode( guidDistributedTrieNode );
        namespace.setName( this.namespaceManipulator.getNamespace( guid ).getName() );
        namespace.setGuid( guid );
        namespace.setMetaGuid( metaGuid );

        return namespace;
    }

    @Override
    public Namespace get( GUID guid, int depth ) {
        return this.get( guid );
    }

    @Override
    public Namespace getAsRootDepth( GUID guid ) {
        return this.get( guid );
    }

    @Override
    public void update( TreeNode nodeWideData ) {
        GenericNamespace ns = ( GenericNamespace ) nodeWideData;
        this.namespaceManipulator.update( ns );
    }

    @Override
    public void updateName( GUID guid, String name ) {

    }

    protected void removeNode( GUID guid ){
        GUIDImperialTrieNode node = this.imperialTree.getNode(guid);
        this.imperialTree.purge( guid );
        this.imperialTree.removeCachePath( guid );
        this.namespaceManipulator.remove( node.getGuid() );
    }
}
