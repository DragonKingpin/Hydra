package com.pinecone.hydra.deploy.kom.operator;

import java.util.List;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.framework.util.uoi.UOI;
import com.pinecone.hydra.deploy.kom.entity.GenericNamespace;
import com.pinecone.hydra.system.ko.UOIUtils;
import com.pinecone.hydra.deploy.kom.DeployInstrument;
import com.pinecone.hydra.deploy.kom.entity.GenericClusterElement;
import com.pinecone.hydra.deploy.kom.entity.Namespace;
import com.pinecone.hydra.deploy.kom.source.DeployMasterManipulator;
import com.pinecone.hydra.deploy.kom.source.DeployNamespaceManipulator;
import com.pinecone.hydra.unit.imperium.GUIDImperialTrieNode;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;

public class NamespaceOperator extends ArchElementOperator implements ElementOperator {
    protected DeployNamespaceManipulator namespaceManipulator;

    public NamespaceOperator( ElementOperatorFactory factory ) {
        this( factory.getTaskMasterManipulator(),factory.getServicesTree() );
        this.factory = factory;
    }

    public NamespaceOperator( DeployMasterManipulator masterManipulator, DeployInstrument deployInstrument ){
        super( masterManipulator, deployInstrument);
        this.namespaceManipulator = masterManipulator.getNamespaceManipulator();
    }

    @Override
    public GUID insert( TreeNode treeNode ) {
        GenericNamespace ns = ( GenericNamespace ) treeNode;

        //存节点基础信息
        GuidAllocator          guidAllocator = this.deployInstrument.getGuidAllocator();
        GUID              namespaceRulesGuid = ns.getGuid();

        GUID namespaceGuid = guidAllocator.nextGUID();
        ns.setGuid( namespaceGuid );
        this.namespaceManipulator.insert( ns );

        //存元信息
        GUID metadataGUID = guidAllocator.nextGUID();
        ns.setMetaGuid( metadataGUID );
        this.nodeMetaManipulator.insertNS( ns );


        GUIDImperialTrieNode node = new GUIDImperialTrieNode();
        node.setBaseDataGUID( namespaceRulesGuid );
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
                TreeNode newInstance = (TreeNode)uoi.newInstance( new Class<? >[]{ DeployInstrument.class }, this.deployInstrument);
                metaType = newInstance.getMetaType();
            }

            ElementOperator operator = this.getOperatorFactory().getOperator( metaType );
            operator.purge( guid );
        }
    }

    @Override
    public Namespace get( GUID guid ) {
        GUIDImperialTrieNode node = this.imperialTree.getNode( guid );
        GenericNamespace                      namespace = new GenericNamespace( this.deployInstrument);
        GUIDImperialTrieNode guidDistributedTrieNode = this.imperialTree.getNode( node.getGuid() );

        GUID metaGuid = guidDistributedTrieNode.getNodeMetadataGUID();
        namespace.setDistributedTreeNode( guidDistributedTrieNode );
        namespace.setName( this.namespaceManipulator.getNamespace( guid ).getName() );
        this.applyCommonMeta( namespace, this.nodeMetaManipulator.getNodeCommonMeta( metaGuid ) ); // GUID / MetaGUID difference.
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
        this.nodeMetaManipulator.update( ns );
    }

    @Override
    public void updateName( GUID guid, String name ) {

    }

    protected void removeNode( GUID guid ){
        GUIDImperialTrieNode node = this.imperialTree.getNode(guid);
        this.imperialTree.purge( guid );
        this.imperialTree.removeCachePath( guid );
        this.namespaceManipulator.remove( node.getGuid() );
        this.nodeMetaManipulator.remove( node.getAttributesGUID() );
    }
}
