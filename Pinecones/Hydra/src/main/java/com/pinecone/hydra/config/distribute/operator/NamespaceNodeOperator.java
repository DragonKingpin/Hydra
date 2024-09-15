package com.pinecone.hydra.config.distribute.operator;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.config.distribute.entity.GenericNamespaceNode;
import com.pinecone.hydra.config.distribute.entity.GenericNamespaceNodeMeta;
import com.pinecone.hydra.config.distribute.entity.GenericNodeCommonData;
import com.pinecone.hydra.config.distribute.entity.NamespaceNode;
import com.pinecone.hydra.unit.udsn.entity.TreeNode;
import com.pinecone.hydra.config.distribute.source.ConfManipulatorSharer;
import com.pinecone.hydra.config.distribute.source.NamespaceNodeManipulator;
import com.pinecone.hydra.config.distribute.source.NamespaceNodeMetaManipulator;
import com.pinecone.hydra.config.distribute.source.NodeCommonDataManipulator;
import com.pinecone.hydra.service.tree.UOIUtils;
import com.pinecone.hydra.unit.udsn.DistributedScopeTree;
import com.pinecone.hydra.unit.udsn.DistributedTreeNode;
import com.pinecone.hydra.unit.udsn.GUIDDistributedScopeNode;
import com.pinecone.hydra.unit.udsn.GenericDistributedScopeTree;
import com.pinecone.hydra.unit.udsn.operator.TreeNodeOperator;
import com.pinecone.hydra.unit.udsn.source.TreeManipulatorSharer;
import com.pinecone.ulf.util.id.UUIDBuilder;
import com.pinecone.ulf.util.id.UidGenerator;

import java.time.LocalDateTime;
import java.util.List;

public class NamespaceNodeOperator implements TreeNodeOperator {
    private ConfManipulatorSharer            confManipulatorSharer;

    private NamespaceNodeManipulator        namespaceNodeManipulator;

    private DistributedScopeTree            distributedConfTree;

    private NodeCommonDataManipulator       nodeCommonDataManipulator;

    private NamespaceNodeMetaManipulator    namespaceNodeMetaManipulator;
    public NamespaceNodeOperator(ConfManipulatorSharer confManipulatorSharer , TreeManipulatorSharer treeManipulatorSharer){
        this.confManipulatorSharer          =   confManipulatorSharer;
        this.namespaceNodeManipulator       =   this.confManipulatorSharer.getNamespaceManipulator();
        this.distributedConfTree            =   new GenericDistributedScopeTree(treeManipulatorSharer);
        this.nodeCommonDataManipulator      =   this.confManipulatorSharer.getNodeCommonDataManipulator();
        this.namespaceNodeMetaManipulator   =   this.confManipulatorSharer.getNamespaceNodeMetaManipulator();
    }
    @Override
    public GUID insert(TreeNode treeNode) {
        GenericNamespaceNode namespaceNode = (GenericNamespaceNode) treeNode;
        UidGenerator uidGenerator= UUIDBuilder.getBuilder();
        GUID guid72 = uidGenerator.getGUID72();

        namespaceNode.setGuid(guid72);
        namespaceNode.setCreateTime(LocalDateTime.now());
        namespaceNode.setUpdateTime(LocalDateTime.now());

        DistributedTreeNode distributeConfTreeNode = new GUIDDistributedScopeNode();
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
        this.nodeCommonDataManipulator.insert(nodeCommonData);
        this.distributedConfTree.insert(distributeConfTreeNode);
        this.namespaceNodeManipulator.insert(namespaceNode);
        return guid72;
    }

    @Override
    public void remove(GUID guid) {
        //namespace节点需要递归删除其拥有节点若其引用节点没有其他引用则进行清理
        List<GUIDDistributedScopeNode> childNodes = this.distributedConfTree.getChildNode(guid);
        if (childNodes.isEmpty()){
            this.removeNode(guid);
        }
        else {
            List<GUID> subordinates = this.distributedConfTree.getSubordinates(guid);
            if (!subordinates.isEmpty()){
                for (GUID subordinateGuid : subordinates){
                    this.remove(subordinateGuid);
                }
            }
             childNodes = this.distributedConfTree.getChildNode(guid);
            for(GUIDDistributedScopeNode childNode : childNodes){
                List<GUID> parentNodes = this.distributedConfTree.getParentNodes(childNode.getGuid());
                if (parentNodes.size() > 1){
                    this.distributedConfTree.removeInheritance(childNode.getGuid(),guid);
                }
                else {
                    this.remove(childNode.getGuid());
                }
            }
            this.removeNode(guid);
        }
    }

    @Override
    public TreeNode get(GUID guid) {
        return this.getNamespaceNodeWideData(guid);
    }

    @Override
    public TreeNode getWithoutInheritance(GUID guid) {
        return this.getNamespaceNodeWideData(guid);
    }

    private NamespaceNode getNamespaceNodeWideData(GUID guid){
        NamespaceNode namespaceNode = this.namespaceNodeManipulator.getNamespaceMeta(guid);
        GUIDDistributedScopeNode node = this.distributedConfTree.getNode(guid);
        GenericNodeCommonData nodeCommonData = (GenericNodeCommonData) this.nodeCommonDataManipulator.getNodeCommonData(node.getBaseDataGUID());
        GenericNamespaceNodeMeta namespaceNodeMeta = (GenericNamespaceNodeMeta) this.namespaceNodeMetaManipulator.getNamespaceNodeMeta(node.getNodeMetadataGUID());
        namespaceNode.setNodeCommonData(nodeCommonData);
        namespaceNode.setNamespaceNodeMeta(namespaceNodeMeta);
        return namespaceNode;
    }

    private void removeNode(GUID guid){
        GUIDDistributedScopeNode node = this.distributedConfTree.getNode(guid);
        this.distributedConfTree.remove(guid);
        this.distributedConfTree.removePath(guid);
        this.namespaceNodeManipulator.remove(guid);
        this.namespaceNodeMetaManipulator.remove(node.getNodeMetadataGUID());
        this.nodeCommonDataManipulator.remove(node.getBaseDataGUID());
    }
}
