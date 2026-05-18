package com.pinecone.hydra.device.kom.operator;

import java.util.List;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.uoi.UOI;
import com.pinecone.hydra.device.kom.entity.GenericNamespace;
import com.pinecone.hydra.device.kom.DeviceInstrument;
import com.pinecone.hydra.device.kom.entity.GenericClusterElement;
import com.pinecone.hydra.device.kom.entity.Namespace;
import com.pinecone.hydra.device.kom.source.DeviceMasterManipulator;
import com.pinecone.hydra.device.kom.source.DeviceNamespaceManipulator;
import com.pinecone.hydra.unit.imperium.GUIDImperialTrieNode;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;

public class NamespaceOperator extends ArchElementOperator implements ElementOperator {
    protected DeviceNamespaceManipulator namespaceManipulator;

    public NamespaceOperator( ElementOperatorFactory factory ) {
        this( factory.getTaskMasterManipulator(),factory.getServicesTree() );
        this.factory = factory;
    }

    public NamespaceOperator( DeviceMasterManipulator masterManipulator, DeviceInstrument deviceInstrument ){
        super( masterManipulator, deviceInstrument);
        this.namespaceManipulator = masterManipulator.getNamespaceManipulator();
    }

    @Override
    public GUID insert( TreeNode treeNode ) {
        GenericNamespace ns = ( GenericNamespace ) treeNode;

        GUID namespaceGuid = this.affirmGuid( ns );
        ns.setGuid( namespaceGuid );
        this.namespaceManipulator.insert( ns );

        GUIDImperialTrieNode node = this.newKernelNode( treeNode, namespaceGuid );
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
                    this.purge( subordinateGuid );
                }
            }
            childNodes = this.imperialTree.getChildren( guid );
            for( GUIDImperialTrieNode childNode : childNodes ){
                List<GUID > parentNodes = this.imperialTree.fetchParentGuids(childNode.getGuid());
                if ( parentNodes.size() > 1 ){
                    this.imperialTree.removeInheritance(childNode.getGuid(),guid);
                }
                else {
                    this.purge( childNode.getGuid() );
                }
            }
        }

        if ( node.getType().getObjectName().equals(GenericNamespace.class.getName()) ||  node.getType().getObjectName().equals(GenericClusterElement.class.getName())){
            this.removeNode(guid);
        }
        else {
            UOI uoi = node.getType();
            String metaType = this.getOperatorFactory().getMetaType( uoi.getObjectName() );
            if( metaType == null ) {
                TreeNode newInstance = (TreeNode)uoi.newInstance( new Class<? >[]{ DeviceInstrument.class }, this.deviceInstrument);
                metaType = newInstance.getMetaType();
            }

            ElementOperator operator = this.getOperatorFactory().getOperator( metaType );
            operator.purge( guid );
        }
    }

    @Override
    public Namespace get( GUID guid ) {
        GUIDImperialTrieNode node = this.imperialTree.getNode( guid );
        Namespace namespace = this.namespaceManipulator.getNamespace( guid );
        if( namespace == null ) {
            return null;
        }
        ( (GenericNamespace) namespace ).apply( this.deviceInstrument );
        this.applyTreeNode( namespace, node );

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
        this.touchForUpdate( ns );
        this.namespaceManipulator.update( ns );
        this.imperialTree.removeCachePath( ns.getGuid() );
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
