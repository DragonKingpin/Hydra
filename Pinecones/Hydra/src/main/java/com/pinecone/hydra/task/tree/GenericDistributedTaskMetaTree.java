package com.pinecone.hydra.task.tree;

import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.udsn.entity.TreeNode;
import com.pinecone.hydra.service.tree.UOIUtils;
import com.pinecone.hydra.task.entity.GenericTaskCommonData;
import com.pinecone.hydra.task.entity.GenericTaskNode;
import com.pinecone.hydra.task.entity.GenericTaskNodeMeta;
import com.pinecone.hydra.task.entity.TaskNode;
import com.pinecone.hydra.task.source.TaskCommonDataManipulator;
import com.pinecone.hydra.task.source.TaskManipulatorSharer;
import com.pinecone.hydra.task.source.TaskNodeManipulator;
import com.pinecone.hydra.task.source.TaskNodeMetaManipulator;
import com.pinecone.hydra.unit.udsn.DistributedScopeTree;
import com.pinecone.hydra.unit.udsn.DistributedTreeNode;
import com.pinecone.hydra.unit.udsn.GUIDDistributedScopeNode;
import com.pinecone.hydra.unit.udsn.GenericDistributedScopeTree;
import com.pinecone.hydra.unit.udsn.source.TreeMasterManipulator;
import com.pinecone.ulf.util.id.UUIDBuilder;
import com.pinecone.ulf.util.id.UidGenerator;

import java.time.LocalDateTime;
import java.util.List;

public class GenericDistributedTaskMetaTree implements DistributedTaskMetaTree{
    private DistributedScopeTree            distributedScopeTree;

    private TaskNodeManipulator             taskNodeManipulator;

    private TaskNodeMetaManipulator         taskNodeMetaManipulator;

    private TaskCommonDataManipulator       taskCommonDataManipulator;
    public GenericDistributedTaskMetaTree(TaskManipulatorSharer taskManipulatorSharer, TreeMasterManipulator treeManipulatorSharer){
        this.taskCommonDataManipulator = taskManipulatorSharer.getTaskCommonDataManipulator();
        this.taskNodeMetaManipulator   = taskManipulatorSharer.getTaskNodeMetaManipulator();
        this.taskNodeManipulator       = taskManipulatorSharer.getTaskNodeManipulator();
        this.distributedScopeTree      = new GenericDistributedScopeTree(treeManipulatorSharer);
    }

    @Override
    public String getPath(GUID guid) {
        String path = this.distributedScopeTree.getPath(guid);
        if (path!=null) return path;
        DistributedTreeNode node = this.distributedScopeTree.getNode(guid);
        Debug.trace(node.toString());
        String assemblePath = this.getNodeName(node);
        while (!node.getParentGUIDs().isEmpty()){
            List<GUID> parentGuids = node.getParentGUIDs();
            node = this.distributedScopeTree.getNode(parentGuids.get(0));
            String nodeName = this.getNodeName(node);
            assemblePath = nodeName + "." + assemblePath;
        }
        this.distributedScopeTree.insertPath(guid,assemblePath);
        return assemblePath;
    }

    @Override
    public GUID insert(TreeNode treeNode) {
        GenericTaskNode taskNode = (GenericTaskNode) treeNode;
        UidGenerator uidGenerator= UUIDBuilder.getBuilder();

        GenericTaskNodeMeta genericTaskNodeMeta = taskNode.getGenericTaskNodeMeta();
        GUID TaskNodeMetaGuid = uidGenerator.getGUID72();
        genericTaskNodeMeta.setGuid(TaskNodeMetaGuid);

        GenericTaskCommonData genericTaskCommonData = taskNode.getGenericTaskCommonData();
        GUID TaskCommonDataGuid = uidGenerator.getGUID72();
        genericTaskCommonData.setGuid(TaskCommonDataGuid);
        genericTaskCommonData.setCreateTime(LocalDateTime.now());
        genericTaskCommonData.setUpdateTime(LocalDateTime.now());

        GUIDDistributedScopeNode guidDistributedScopeNode = new GUIDDistributedScopeNode();
        GUID nodeGuid = uidGenerator.getGUID72();
        guidDistributedScopeNode.setGuid(nodeGuid);
        guidDistributedScopeNode.setNodeMetadataGUID(TaskNodeMetaGuid);
        guidDistributedScopeNode.setBaseDataGUID(TaskCommonDataGuid);
        guidDistributedScopeNode.setType(UOIUtils.createLocalJavaClass(taskNode.getClass().getName()));
        taskNode.setGuid(nodeGuid);

        this.distributedScopeTree.insert(guidDistributedScopeNode);
        this.taskNodeManipulator.insert(taskNode);
        this.taskNodeMetaManipulator.insert(genericTaskNodeMeta);
        this.taskCommonDataManipulator.insert(genericTaskCommonData);
        return nodeGuid;
    }

    @Override
    public TreeNode get(GUID guid) {
        GUIDDistributedScopeNode node = this.distributedScopeTree.getNode(guid);
        TaskNode taskNode = this.taskNodeManipulator.getTaskNode(guid);
        GenericTaskNodeMeta taskNodeMeta = (GenericTaskNodeMeta) this.taskNodeMetaManipulator.getTaskNodeMeta(node.getNodeMetadataGUID());
        GenericTaskCommonData taskCommonData = (GenericTaskCommonData) this.taskCommonDataManipulator.getTaskCommonData(node.getBaseDataGUID());
        taskNode.setGenericTaskNodeMeta(taskNodeMeta);
        taskNode.setGenericTaskCommonData(taskCommonData);
        return taskNode;
    }

    @Override
    public TreeNode parsePath(String path) {
        GUID guid = this.distributedScopeTree.parsePath(path);
        if (guid != null){
            return this.get(guid);
        }
        else{
            String[] parts = this.processPath(path).split("\\.");
            List<GUID> nodeByName = this.taskNodeManipulator.getNodeByName(parts[parts.length - 1]);
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
        List<GUIDDistributedScopeNode> childNodes = this.distributedScopeTree.getChildNode(guid);
        if (childNodes == null || childNodes.isEmpty()){
            this.removeNode(guid);
        }
        else {
            for(GUIDDistributedScopeNode childNode : childNodes){
                List<GUID> parentNodes = this.distributedScopeTree.getParentNodes(childNode.getGuid());
                if (parentNodes.size() > 1){
                    this.distributedScopeTree.removeInheritance(childNode.getGuid(),guid);
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
        return this.taskNodeManipulator.getTaskNode(node.getGuid()).getName();
    }

    private String processPath(String path) {
        // 使用正则表达式移除所有的括号及其内容
        return path.replaceAll("\\(.*?\\)", "");
    }

    private void removeNode(GUID guid){
        GUIDDistributedScopeNode node = this.distributedScopeTree.getNode(guid);
        this.distributedScopeTree.remove(guid);
        this.distributedScopeTree.removePath(guid);
        this.taskNodeManipulator.remove(guid);
        this.taskNodeMetaManipulator.remove(node.getNodeMetadataGUID());
        this.taskCommonDataManipulator.remove(node.getBaseDataGUID());
    }
}
