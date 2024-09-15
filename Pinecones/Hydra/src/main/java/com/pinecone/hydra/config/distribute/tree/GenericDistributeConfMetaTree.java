package com.pinecone.hydra.config.distribute.tree;

import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.uoi.UOI;
import com.pinecone.hydra.config.distribute.entity.GenericTextValue;
import com.pinecone.hydra.config.distribute.entity.Properties;
import com.pinecone.hydra.unit.udsn.entity.TreeNode;
import com.pinecone.hydra.config.distribute.operator.GenericConfOperatorFactory;
import com.pinecone.hydra.config.distribute.operator.ConfOperatorFactory;
import com.pinecone.hydra.unit.udsn.operator.TreeNodeOperator;
import com.pinecone.hydra.config.distribute.source.ConfigMasterManipulator;
import com.pinecone.hydra.config.distribute.source.ConfigNodeManipulator;
import com.pinecone.hydra.config.distribute.source.ConfigNSNodeManipulator;
import com.pinecone.hydra.config.distribute.source.PropertiesManipulator;
import com.pinecone.hydra.config.distribute.source.TextValueManipulator;
import com.pinecone.hydra.unit.udsn.DistributedScopeTree;
import com.pinecone.hydra.unit.udsn.DistributedTreeNode;
import com.pinecone.hydra.unit.udsn.GUIDDistributedScopeNode;
import com.pinecone.hydra.unit.udsn.GenericDistributedScopeTree;
import com.pinecone.hydra.unit.udsn.source.TreeMasterManipulator;

import java.time.LocalDateTime;
import java.util.List;

public class GenericDistributeConfMetaTree implements DistributedConfMetaTree{
    //继承DistributedConfTree提供树结构信息
    private DistributedScopeTree            distributedConfTree;
    private ConfigMasterManipulator         configMasterManipulator;
    private PropertiesManipulator           propertiesManipulator;
    private TextValueManipulator            textValueManipulator;
    private ConfigNodeManipulator           confNodeManipulator;
    private ConfigNSNodeManipulator         namespaceNodeManipulator;
    private ConfOperatorFactory             confOperatorFactory;

    public GenericDistributeConfMetaTree( ConfigMasterManipulator configMasterManipulator, TreeMasterManipulator treeManipulatorSharer ){
        this.configMasterManipulator     =    configMasterManipulator;
        this.distributedConfTree         =    new GenericDistributedScopeTree(treeManipulatorSharer);
        this.propertiesManipulator       =    this.configMasterManipulator.getPropertiesManipulator();
        this.textValueManipulator        =    this.configMasterManipulator.getTextValueManipulator();
        this.confNodeManipulator         =    this.configMasterManipulator.getConfigurationManipulator();
        this.namespaceNodeManipulator    =    this.configMasterManipulator.getNamespaceManipulator();
        this.confOperatorFactory         =    new GenericConfOperatorFactory(this.configMasterManipulator,treeManipulatorSharer);
    }


    @Override
    public String getPath(GUID guid) {
        String path = this.distributedConfTree.getPath(guid);
        if (path!=null) return path;
        DistributedTreeNode node = this.distributedConfTree.getNode(guid);
        Debug.trace(node.toString());
        GUID owner = this.distributedConfTree.getOwner(guid);
        if (owner == null){
            String assemblePath = this.getNodeName(node);
            while (!node.getParentGUIDs().isEmpty()){
                List<GUID> parentGuids = node.getParentGUIDs();
                node = this.distributedConfTree.getNode(parentGuids.get(0));
                String nodeName = this.getNodeName(node);
                assemblePath = nodeName + "." + assemblePath;
            }
            this.distributedConfTree.insertPath(guid,assemblePath);
            return assemblePath;
        }
        else{
            String assemblePath = this.getNodeName(node);
            while (!node.getParentGUIDs().isEmpty()){
                node = this.distributedConfTree.getNode(owner);
                String nodeName = this.getNodeName(node);
                assemblePath = nodeName + "." + assemblePath;
            }
            this.distributedConfTree.insertPath(guid,assemblePath);
            return assemblePath;
        }
    }

    @Override
    public GUID insert(TreeNode treeNode) {
        Debug.trace(treeNode.getMetaType());
        TreeNodeOperator operator = this.confOperatorFactory.getOperator(treeNode.getMetaType());
        return operator.insert(treeNode);
    }

    @Override
    public TreeNode get(GUID guid) {
        DistributedTreeNode node = this.distributedConfTree.getNode(guid);
        TreeNode newInstance = (TreeNode)node.getType().newInstance();
        TreeNodeOperator operator = this.confOperatorFactory.getOperator(newInstance.getMetaType());
        return operator.get(guid);
    }

    @Override
    public void insertProperties(Properties properties, GUID confNodeGuid) {
        properties.setGuid(confNodeGuid);
        this.propertiesManipulator.insert(properties);
    }

    @Override
    public TreeNode parsePath(String path){
        GUID guid = this.distributedConfTree.parsePath(path);
        if (guid!=null){
            DistributedTreeNode node = this.distributedConfTree.getNode(guid);
            TreeNode newInstance = (TreeNode)node.getType().newInstance();
            TreeNodeOperator operator = this.confOperatorFactory.getOperator(newInstance.getMetaType());
            return operator.get(guid);
        }
        else {
            String[] parts = this.processPath(path).split("\\.");
            //匹配confNode
            List<GUID> nodeByNames = this.confNodeManipulator.getNodeByName(parts[parts.length - 1]);
            for (GUID nodeGuid : nodeByNames){
                String nodePath = this.getPath(nodeGuid);
                if (nodePath.equals(path)){
                    DistributedTreeNode node = this.distributedConfTree.getNode(nodeGuid);
                    TreeNode newInstance = (TreeNode)node.getType().newInstance();
                    TreeNodeOperator operator = this.confOperatorFactory.getOperator(newInstance.getMetaType());
                    return operator.get(nodeGuid);
                }
            }
            //匹配namespaceNode
            nodeByNames = this.namespaceNodeManipulator.getNodeByName(parts[parts.length - 1]);
            for (GUID nodeGuid : nodeByNames){
                String nodePath = this.getPath(nodeGuid);
                if (nodePath.equals(path)){
                    DistributedTreeNode node = this.distributedConfTree.getNode(nodeGuid);
                    TreeNode newInstance = (TreeNode)node.getType().newInstance();
                    TreeNodeOperator operator = this.confOperatorFactory.getOperator(newInstance.getMetaType());
                    return operator.get(nodeGuid);
                }
            }
        }
        return null;
    }

    @Override
    public void remove(GUID guid){
        GUIDDistributedScopeNode node = this.distributedConfTree.getNode(guid);
        TreeNode newInstance = (TreeNode)node.getType().newInstance();
        TreeNodeOperator operator = this.confOperatorFactory.getOperator(newInstance.getMetaType());
        operator.remove(guid);
    }

    @Override
    public void  insertTextValue(GUID guid,String text,String type){
        GenericTextValue genericTextValue = new GenericTextValue();
        genericTextValue.setCreateTime(LocalDateTime.now());
        genericTextValue.setUpdateTime(LocalDateTime.now());
        genericTextValue.setValue(text);
        genericTextValue.setType(type);
        genericTextValue.setGuid(guid);
        this.textValueManipulator.insert(genericTextValue);
    }

    @Override
    public TreeNode getWithoutInheritance(GUID guid) {
        DistributedTreeNode node = this.distributedConfTree.getNode(guid);
        TreeNode newInstance = (TreeNode)node.getType().newInstance();
        TreeNodeOperator operator = this.confOperatorFactory.getOperator(newInstance.getMetaType());
        return operator.getWithoutInheritance(guid);
    }

    private String getNodeName(DistributedTreeNode node){
        UOI type = node.getType();
        TreeNode newInstance = (TreeNode)type.newInstance();
        Debug.trace(newInstance);
        TreeNodeOperator operator = this.confOperatorFactory.getOperator(newInstance.getMetaType());
        TreeNode treeNode = operator.get(node.getGuid());
        return treeNode.getName();
    }

    private String processPath(String path) {
        // 使用正则表达式移除所有的括号及其内容
        return path.replaceAll("\\(.*?\\)", "");
    }
}
