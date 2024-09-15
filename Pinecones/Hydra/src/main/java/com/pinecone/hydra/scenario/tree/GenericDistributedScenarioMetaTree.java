package com.pinecone.hydra.scenario.tree;

import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.udsn.entity.TreeNode;
import com.pinecone.hydra.scenario.entity.GenericNamespaceNode;
import com.pinecone.hydra.scenario.entity.GenericNamespaceNodeMeta;
import com.pinecone.hydra.scenario.entity.GenericScenarioCommonData;
import com.pinecone.hydra.scenario.entity.NamespaceNode;
import com.pinecone.hydra.scenario.entity.NamespaceNodeMeta;
import com.pinecone.hydra.scenario.entity.ScenarioCommonData;
import com.pinecone.hydra.scenario.source.ScenarioMetaManipulatorSharer;
import com.pinecone.hydra.scenario.source.NamespaceNodeManipulator;
import com.pinecone.hydra.scenario.source.NamespaceNodeMetaManipulator;
import com.pinecone.hydra.scenario.source.ScenarioCommonDataManipulator;
import com.pinecone.hydra.service.tree.UOIUtils;
import com.pinecone.hydra.unit.udsn.DistributedScopeTree;
import com.pinecone.hydra.unit.udsn.DistributedTreeNode;
import com.pinecone.hydra.unit.udsn.GUIDDistributedScopeNode;
import com.pinecone.hydra.unit.udsn.GenericDistributedScopeTree;
import com.pinecone.hydra.unit.udsn.source.TreeManipulatorSharer;
import com.pinecone.ulf.util.id.UUIDBuilder;
import com.pinecone.ulf.util.id.UidGenerator;

import java.time.LocalDateTime;
import java.util.List;

public class GenericDistributedScenarioMetaTree implements DistributedScenarioMetaTree{
    private DistributedScopeTree            distributedScenarioTree;
    private ScenarioMetaManipulatorSharer scenarioMetaManipulatorSharer;

    private NamespaceNodeMetaManipulator    namespaceNodeMetaManipulator;
    private NamespaceNodeManipulator        namespaceNodeManipulator;
    private ScenarioCommonDataManipulator   scenarioCommonDataManipulator;

    public GenericDistributedScenarioMetaTree(ScenarioMetaManipulatorSharer scenarioMetaManipulatorSharer, TreeManipulatorSharer treeManipulatorSharer){
        this.scenarioMetaManipulatorSharer = scenarioMetaManipulatorSharer;
        this.distributedScenarioTree        =   new GenericDistributedScopeTree(treeManipulatorSharer);
        this.namespaceNodeManipulator       =   this.scenarioMetaManipulatorSharer.getNamespaceNodeManipulator();
        this.namespaceNodeMetaManipulator   =   this.scenarioMetaManipulatorSharer.getNamespaceNodeMetaManipulator();
        this.scenarioCommonDataManipulator  =   this.scenarioMetaManipulatorSharer.getScenarioCommonDataManipulator();
    }

    @Override
    public String getPath(GUID guid) {
        String path = this.distributedScenarioTree.getPath(guid);
        if (path!=null) return path;
        DistributedTreeNode node = this.distributedScenarioTree.getNode(guid);
        Debug.trace(node.toString());
            String assemblePath = this.getNodeName(node);
            while (!node.getParentGUIDs().isEmpty()){
                List<GUID> parentGuids = node.getParentGUIDs();
                node = this.distributedScenarioTree.getNode(parentGuids.get(0));
                String nodeName = this.getNodeName(node);
                assemblePath = nodeName + "." + assemblePath;
            }
            this.distributedScenarioTree.insertPath(guid,assemblePath);
            return assemblePath;
    }

    @Override
    public GUID insert(TreeNode treeNode) {
        GenericNamespaceNode namespaceNode = (GenericNamespaceNode) treeNode;
        UidGenerator uidGenerator= UUIDBuilder.getBuilder();

        NamespaceNodeMeta namespaceNodeMeta = namespaceNode.getNamespaceNodeMeta();
        GUID namespaceNodeMetaGuid = uidGenerator.getGUID72();
        namespaceNodeMeta.setGuid(namespaceNodeMetaGuid);

        ScenarioCommonData scenarioCommonData = namespaceNode.getScenarioCommonData();
        GUID scenarioCommonDataGuid = uidGenerator.getGUID72();
        scenarioCommonData.setGuid(scenarioCommonDataGuid);
        scenarioCommonData.setCreateTime(LocalDateTime.now());
        scenarioCommonData.setUpdateTime(LocalDateTime.now());

        GUIDDistributedScopeNode guidDistributedScopeNode = new GUIDDistributedScopeNode();
        GUID nodeGuid = uidGenerator.getGUID72();
        namespaceNode.setGuid(nodeGuid);
        guidDistributedScopeNode.setGuid(nodeGuid);
        guidDistributedScopeNode.setNodeMetadataGUID(namespaceNodeMetaGuid);
        guidDistributedScopeNode.setBaseDataGUID(scenarioCommonDataGuid);
        guidDistributedScopeNode.setType(UOIUtils.createLocalJavaClass(namespaceNode.getClass().getName()));

        this.distributedScenarioTree.insert(guidDistributedScopeNode);
        this.namespaceNodeMetaManipulator.insert(namespaceNodeMeta);
        this.scenarioCommonDataManipulator.insert(scenarioCommonData);
        this.namespaceNodeManipulator.insert(namespaceNode);
        return null;
    }

    @Override
    public TreeNode get(GUID guid) {
        GUIDDistributedScopeNode node = this.distributedScenarioTree.getNode(guid);
        NamespaceNode namespaceNode = this.namespaceNodeManipulator.getNamespaceNode(guid);
        GenericScenarioCommonData scenarioCommonData = (GenericScenarioCommonData) this.scenarioCommonDataManipulator.getScenarioCommonData(node.getBaseDataGUID());
        GenericNamespaceNodeMeta namespaceNodeMeta = (GenericNamespaceNodeMeta) this.namespaceNodeMetaManipulator.getNamespaceNodeMeta(node.getNodeMetadataGUID());
        namespaceNode.setNamespaceNodeMeta(namespaceNodeMeta);
        namespaceNode.setScenarioCommonData(scenarioCommonData);
        return namespaceNode;
    }

    @Override
    public TreeNode parsePath(String path) {
        GUID guid = this.distributedScenarioTree.parsePath(path);
        if (guid != null){
            return this.get(guid);
        }
        else{
            String[] parts = this.processPath(path).split("\\.");
            List<GUID> nodeByName = this.namespaceNodeManipulator.getNodeByName(parts[parts.length - 1]);
            for(GUID nodeGuid :nodeByName){
                if (this.getPath(nodeGuid).equals(path)){
                    return this.get(nodeGuid);
                }
            }
        }
        return null;
    }

    @Override
    public void remove(GUID guid) {
        List<GUIDDistributedScopeNode> childNodes = this.distributedScenarioTree.getChildNode(guid);
        if (childNodes == null || childNodes.isEmpty()){
            this.removeNode(guid);
        }
        else {
            for(GUIDDistributedScopeNode childNode : childNodes){
                List<GUID> parentNodes = this.distributedScenarioTree.getParentNodes(childNode.getGuid());
                if (parentNodes.size() > 1){
                    this.distributedScenarioTree.removeInheritance(childNode.getGuid(),guid);
                }else {
                    this.remove(childNode.getGuid());
                }
            }
            this.removeNode(guid);
        }
    }

    @Override
    public TreeNode getWithoutInheritance(GUID guid) {
        return null;
    }

    private String getNodeName(DistributedTreeNode node){
        return this.namespaceNodeManipulator.getNamespaceNode(node.getGuid()).getName();
    }

    private String processPath(String path) {
        // 使用正则表达式移除所有的括号及其内容
        return path.replaceAll("\\(.*?\\)", "");
    }

    private void removeNode(GUID guid){
        GUIDDistributedScopeNode node = this.distributedScenarioTree.getNode(guid);
        this.distributedScenarioTree.remove(guid);
        this.namespaceNodeManipulator.remove(guid);
        this.namespaceNodeMetaManipulator.remove(node.getNodeMetadataGUID());
        this.scenarioCommonDataManipulator.remove(node.getBaseDataGUID());
        this.distributedScenarioTree.removePath(guid);
    }

}
