package com.pinecone.hydra.registry.operator;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.registry.entity.GenericNamespaceNode;
import com.pinecone.hydra.registry.entity.GenericNamespaceNodeMeta;
import com.pinecone.hydra.registry.entity.GenericNodeCommonData;
import com.pinecone.hydra.registry.entity.NamespaceNode;
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
import com.pinecone.hydra.unit.udtt.operator.TreeNodeOperator;
import com.pinecone.hydra.unit.udtt.source.TreeMasterManipulator;
import com.pinecone.ulf.util.id.UUIDBuilder;
import com.pinecone.ulf.util.id.UidGenerator;

import java.time.LocalDateTime;
import java.util.List;

public class NamespaceNodeOperator implements TreeNodeOperator {
    private RegistryMasterManipulator     registryMasterManipulator;

    private RegistryNSNodeManipulator     namespaceNodeManipulator;

    private DistributedTrieTree           distributedTrieTree;

    private RegistryCommonDataManipulator registryCommonDataManipulator;

    private RegistryNSNodeMetaManipulator namespaceNodeMetaManipulator;

    public NamespaceNodeOperator( RegistryMasterManipulator registryMasterManipulator, TreeMasterManipulator treeManipulatorSharer ) {
        this.registryMasterManipulator      = registryMasterManipulator;
        this.namespaceNodeManipulator       =   this.registryMasterManipulator.getNSNodeManipulator();
        this.distributedTrieTree            =   new GenericDistributedTrieTree(treeManipulatorSharer);
        this.registryCommonDataManipulator  =   this.registryMasterManipulator.getRegistryCommonDataManipulator();
        this.namespaceNodeMetaManipulator   =   this.registryMasterManipulator.getNSNodeMetaManipulator();
    }

    @Override
    public GUID insert( TreeNode treeNode ) {
        GenericNamespaceNode namespaceNode = (GenericNamespaceNode) treeNode;
        UidGenerator uidGenerator= UUIDBuilder.getBuilder();
        GUID guid72 = uidGenerator.getGUID72();

        namespaceNode.setGuid(guid72);
        namespaceNode.setCreateTime(LocalDateTime.now());
        namespaceNode.setUpdateTime(LocalDateTime.now());

        DistributedTreeNode distributeConfTreeNode = new GUIDDistributedTrieNode();
        distributeConfTreeNode.setGuid(guid72);
        distributeConfTreeNode.setType(UOIUtils.createLocalJavaClass(namespaceNode.getClass().getName()));

        GenericNamespaceNodeMeta namespaceNodeMeta = namespaceNode.getNamespaceNodeMeta();
        GUID namespaceNodeMetaGuid = uidGenerator.getGUID72();
        namespaceNodeMeta.setGuid(namespaceNodeMetaGuid);

        GenericNodeCommonData nodeCommonData = namespaceNode.getNodeCommonData();
        GUID nodeCommonDataGuid = uidGenerator.getGUID72();
        nodeCommonData.setGuid(nodeCommonDataGuid);

        distributeConfTreeNode.setNodeMetadataGUID(namespaceNodeMetaGuid);
        distributeConfTreeNode.setBaseDataGUID(nodeCommonDataGuid);

        this.namespaceNodeMetaManipulator.insert(namespaceNodeMeta);
        this.registryCommonDataManipulator.insert(nodeCommonData);
        this.distributedTrieTree.insert(distributeConfTreeNode);
        this.namespaceNodeManipulator.insert(namespaceNode);
        return guid72;
    }

    @Override
    public void remove(GUID guid) {
        //namespace节点需要递归删除其拥有节点若其引用节点没有其他引用则进行清理
        List<GUIDDistributedTrieNode> childNodes = this.distributedTrieTree.getChildNode(guid);
        if (childNodes.isEmpty()){
            this.removeNode(guid);
        }
        else {
            List<GUID> subordinates = this.distributedTrieTree.getSubordinates(guid);
            if (!subordinates.isEmpty()){
                for (GUID subordinateGuid : subordinates){
                    this.remove(subordinateGuid);
                }
            }
             childNodes = this.distributedTrieTree.getChildNode(guid);
            for(GUIDDistributedTrieNode childNode : childNodes){
                List<GUID> parentNodes = this.distributedTrieTree.getParentNodes(childNode.getGuid());
                if (parentNodes.size() > 1){
                    this.distributedTrieTree.removeInheritance(childNode.getGuid(),guid);
                }
                else {
                    this.remove(childNode.getGuid());
                }
            }
            this.removeNode(guid);
        }
    }

    @Override
    public TreeNode get( GUID guid ) {
        return this.getNamespaceNodeWideData(guid);
    }

    @Override
    public TreeNode getWithoutInheritance( GUID guid ) {
        return this.getNamespaceNodeWideData(guid);
    }

    private NamespaceNode getNamespaceNodeWideData( GUID guid ){
        NamespaceNode namespaceNode = this.namespaceNodeManipulator.getNamespaceMeta(guid);
        GUIDDistributedTrieNode node = this.distributedTrieTree.getNode(guid);
        GenericNodeCommonData nodeCommonData = (GenericNodeCommonData) this.registryCommonDataManipulator.getNodeCommonData(node.getBaseDataGUID());
        GenericNamespaceNodeMeta namespaceNodeMeta = (GenericNamespaceNodeMeta) this.namespaceNodeMetaManipulator.getNamespaceNodeMeta(node.getNodeMetadataGUID());
        namespaceNode.setNodeCommonData(nodeCommonData);
        namespaceNode.setNamespaceNodeMeta(namespaceNodeMeta);
        return namespaceNode;
    }

    private void removeNode( GUID guid ){
        GUIDDistributedTrieNode node = this.distributedTrieTree.getNode(guid);
        this.distributedTrieTree.remove(guid);
        this.distributedTrieTree.removePath(guid);
        this.namespaceNodeManipulator.remove(guid);
        this.namespaceNodeMetaManipulator.remove(node.getNodeMetadataGUID());
        this.registryCommonDataManipulator.remove(node.getBaseDataGUID());
    }
}
