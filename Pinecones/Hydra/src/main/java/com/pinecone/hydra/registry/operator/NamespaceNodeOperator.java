package com.pinecone.hydra.registry.operator;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.uoi.UOI;
import com.pinecone.hydra.registry.DistributedRegistry;
import com.pinecone.hydra.registry.entity.GenericNamespaceNode;
import com.pinecone.hydra.registry.entity.GenericNamespaceNodeMeta;
import com.pinecone.hydra.registry.entity.GenericNodeAttribute;
import com.pinecone.hydra.registry.entity.NamespaceNode;
import com.pinecone.hydra.registry.entity.NamespaceNodeMeta;
import com.pinecone.hydra.registry.entity.NodeAttribute;
import com.pinecone.hydra.registry.entity.RegistryTreeNode;
import com.pinecone.hydra.unit.udtt.entity.TreeNode;
import com.pinecone.hydra.registry.source.RegistryMasterManipulator;
import com.pinecone.hydra.registry.source.RegistryNSNodeManipulator;
import com.pinecone.hydra.registry.source.RegistryNSNodeMetaManipulator;
import com.pinecone.hydra.registry.source.RegistryCommonDataManipulator;
import com.pinecone.hydra.service.tree.UOIUtils;
import com.pinecone.hydra.unit.udtt.DistributedTrieTree;
import com.pinecone.hydra.unit.udtt.DistributedTreeNode;
import com.pinecone.hydra.unit.udtt.GUIDDistributedTrieNode;
import com.pinecone.hydra.unit.udtt.GenericDistributedTrieTree;
import com.pinecone.hydra.unit.udtt.source.TreeMasterManipulator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NamespaceNodeOperator implements RegistryNodeOperator {
    private RegistryMasterManipulator     registryMasterManipulator;

    private RegistryNSNodeManipulator     namespaceNodeManipulator;

    private DistributedTrieTree           distributedTrieTree;

    private RegistryCommonDataManipulator registryCommonDataManipulator;

    private RegistryNSNodeMetaManipulator namespaceNodeMetaManipulator;

    private GenericConfigOperatorFactory  factory;

    protected DistributedRegistry         registry;

    public NamespaceNodeOperator ( GenericConfigOperatorFactory factory ) {
        this( factory.getMasterManipulator(),(DistributedRegistry) factory.getRegistry() );
        this.factory = factory;
    }

    public NamespaceNodeOperator( RegistryMasterManipulator masterManipulator , DistributedRegistry registry){
        this.registryMasterManipulator      = masterManipulator;
        this.namespaceNodeManipulator       = this.registryMasterManipulator.getNSNodeManipulator();
        this.registryCommonDataManipulator  = this.registryMasterManipulator.getRegistryCommonDataManipulator();
        this.namespaceNodeMetaManipulator   = this.registryMasterManipulator.getNSNodeMetaManipulator();
        this.distributedTrieTree            = new GenericDistributedTrieTree( (TreeMasterManipulator) masterManipulator.getSkeletonMasterManipulator() );
        this.registry                       = registry;
    }

    @Override
    public GUID insert( TreeNode treeNode ) {
        GenericNamespaceNode namespaceNode = (GenericNamespaceNode) treeNode;
        GUID guid72 = this.registry.getGuidAllocator().nextGUID72();

        namespaceNode.setGuid(guid72);
        namespaceNode.setCreateTime(LocalDateTime.now());
        namespaceNode.setUpdateTime(LocalDateTime.now());

        DistributedTreeNode distributeConfTreeNode = new GUIDDistributedTrieNode();
        distributeConfTreeNode.setGuid(guid72);
        distributeConfTreeNode.setType(UOIUtils.createLocalJavaClass(namespaceNode.getClass().getName()));

        NamespaceNodeMeta namespaceNodeMeta = namespaceNode.getNamespaceNodeMeta();
        GUID namespaceNodeMetaGuid = this.registry.getGuidAllocator().nextGUID72();
        if (namespaceNodeMeta != null){
            namespaceNodeMeta.setGuid(namespaceNodeMetaGuid);
            this.namespaceNodeMetaManipulator.insert( namespaceNodeMeta );
        }
        else {
            namespaceNodeMetaGuid = null;
        }


        NodeAttribute nodeAttribute = namespaceNode.getNodeAttribute();
        GUID nodeCommonDataGuid = this.registry.getGuidAllocator().nextGUID72();
        if (nodeAttribute != null){
            nodeAttribute.setGuid( nodeCommonDataGuid );
            this.registryCommonDataManipulator.insert(nodeAttribute);
        }
        else {
            nodeCommonDataGuid = null;
        }

        distributeConfTreeNode.setNodeMetadataGUID(namespaceNodeMetaGuid);
        distributeConfTreeNode.setBaseDataGUID(nodeCommonDataGuid);
        this.distributedTrieTree.insert( distributeConfTreeNode );
        this.namespaceNodeManipulator.insert( namespaceNode );
        return guid72;
    }

    @Override
    public void purge( GUID guid ) {
        //namespace节点需要递归删除其拥有节点若其引用节点，没有其他引用则进行清理
        List<GUIDDistributedTrieNode> childNodes = this.distributedTrieTree.getChildren(guid);
        GUIDDistributedTrieNode node = this.distributedTrieTree.getNode(guid);
        if ( !childNodes.isEmpty() ){
            List<GUID > subordinates = this.distributedTrieTree.getSubordinates(guid);
            if ( !subordinates.isEmpty() ){
                for ( GUID subordinateGuid : subordinates ){
                    this.purge( subordinateGuid );
                }
            }
            childNodes = this.distributedTrieTree.getChildren( guid );
            for( GUIDDistributedTrieNode childNode : childNodes ){
                List<GUID > parentNodes = this.distributedTrieTree.getParentGuids(childNode.getGuid());
                if ( parentNodes.size() > 1 ){
                    this.distributedTrieTree.removeInheritance(childNode.getGuid(),guid);
                }
                else {
                    this.purge( childNode.getGuid() );
                }
            }
        }

        if ( node.getType().getObjectName().equals(GenericNamespaceNode.class.getName()) ){
            this.removeNode(guid);
        }
        else {
            UOI uoi = node.getType();
            String metaType = this.factory.getMetaType( uoi.getObjectName() );
            if( metaType == null ) {
                TreeNode newInstance = (TreeNode)uoi.newInstance( new Class<? >[]{ DistributedRegistry.class }, this.registry );
                metaType = newInstance.getMetaType();
            }

            RegistryNodeOperator operator = this.factory.getOperator( metaType );
            operator.purge( guid );
        }
    }

    @Override
    public RegistryTreeNode get( GUID guid ) {
        return this.getNamespaceNodeWideData(guid);
    }

    @Override
    public RegistryTreeNode getSelf( GUID guid ) {
        return this.getNamespaceNodeWideData(guid);
    }

    @Override
    public void update( TreeNode treeNode ) {

    }

    @Override
    public void updateName( GUID guid, String name ) {
        this.namespaceNodeManipulator.updateName( guid, name );
    }

    private NamespaceNode getNamespaceNodeWideData( GUID guid ){
        NamespaceNode namespaceNode = this.namespaceNodeManipulator.getNamespaceMeta(guid);
        if ( namespaceNode instanceof GenericNamespaceNode ){
             ((GenericNamespaceNode) namespaceNode).apply(this.registry);
        }
        GUIDDistributedTrieNode node = this.distributedTrieTree.getNode(guid);
        GenericNodeAttribute nodeCommonData = (GenericNodeAttribute) this.registryCommonDataManipulator.getNodeCommonData(node.getBaseDataGUID());
        GenericNamespaceNodeMeta namespaceNodeMeta = (GenericNamespaceNodeMeta) this.namespaceNodeMetaManipulator.getNamespaceNodeMeta(node.getNodeMetadataGUID());
        List<GUIDDistributedTrieNode> childNode = this.distributedTrieTree.getChildren(guid);
        ArrayList<GUID> guids = new ArrayList<>();
        for ( GUIDDistributedTrieNode n : childNode ){
            guids.add( n.getGuid() );
        }
        namespaceNode.setContentGuids(guids);
        namespaceNode.setNodeAttribute(nodeCommonData);
        namespaceNode.setNamespaceNodeMeta(namespaceNodeMeta);
        return namespaceNode;
    }

    private void removeNode( GUID guid ){
        GUIDDistributedTrieNode node = this.distributedTrieTree.getNode(guid);
        this.distributedTrieTree.purge( guid );
        this.distributedTrieTree.removeCachePath(guid);
        this.namespaceNodeManipulator.remove(guid);
        this.namespaceNodeMetaManipulator.remove(node.getNodeMetadataGUID());
        this.registryCommonDataManipulator.remove(node.getBaseDataGUID());
    }

}
