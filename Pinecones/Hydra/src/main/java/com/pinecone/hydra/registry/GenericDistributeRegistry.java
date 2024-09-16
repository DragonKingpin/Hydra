package com.pinecone.hydra.registry;

import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.uoi.UOI;
import com.pinecone.hydra.registry.entity.GenericTextValue;
import com.pinecone.hydra.registry.entity.Properties;
import com.pinecone.hydra.system.Hydrarum;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.system.ko.driver.KOIMasterManipulator;
import com.pinecone.hydra.system.ko.driver.KOISkeletonMasterManipulator;
import com.pinecone.hydra.unit.udtt.entity.TreeNode;
import com.pinecone.hydra.registry.operator.GenericConfigOperatorFactory;
import com.pinecone.hydra.registry.operator.ConfigOperatorFactory;
import com.pinecone.hydra.unit.udtt.operator.TreeNodeOperator;
import com.pinecone.hydra.registry.source.RegistryMasterManipulator;
import com.pinecone.hydra.registry.source.RegistryNodeManipulator;
import com.pinecone.hydra.registry.source.RegistryNSNodeManipulator;
import com.pinecone.hydra.registry.source.RegistryPropertiesManipulator;
import com.pinecone.hydra.registry.source.RegistryTextValueManipulator;
import com.pinecone.hydra.unit.udtt.DistributedTrieTree;
import com.pinecone.hydra.unit.udtt.DistributedTreeNode;
import com.pinecone.hydra.unit.udtt.GUIDDistributedTrieNode;
import com.pinecone.hydra.unit.udtt.GenericDistributedTrieTree;
import com.pinecone.hydra.unit.udtt.source.TreeMasterManipulator;

import java.time.LocalDateTime;
import java.util.List;

public class GenericDistributeRegistry implements DistributedRegistry {
    protected Hydrarum                      hydrarum;

    private DistributedTrieTree             distributedConfTree;
    private RegistryMasterManipulator       registryMasterManipulator;
    private RegistryPropertiesManipulator   registryPropertiesManipulator;
    private RegistryTextValueManipulator    registryTextValueManipulator;
    private RegistryNodeManipulator         nodeManipulator;
    private RegistryNSNodeManipulator       namespaceNodeManipulator;
    private ConfigOperatorFactory           configOperatorFactory;

    public GenericDistributeRegistry( Hydrarum hydrarum, KOIMasterManipulator masterManipulator ){
        this.hydrarum                    =  hydrarum;
        this.registryMasterManipulator =  (RegistryMasterManipulator) masterManipulator;

        KOISkeletonMasterManipulator skeletonMasterManipulator = this.registryMasterManipulator.getSkeletonMasterManipulator();
        TreeMasterManipulator        treeMasterManipulator     = (TreeMasterManipulator) skeletonMasterManipulator;

        this.distributedConfTree           =  new GenericDistributedTrieTree( treeMasterManipulator );
        this.registryPropertiesManipulator =  this.registryMasterManipulator.getRegistryPropertiesManipulator();
        this.registryTextValueManipulator  =  this.registryMasterManipulator.getRegistryTextValueManipulator();
        this.nodeManipulator               =  this.registryMasterManipulator.getRegistryNodeManipulator();
        this.namespaceNodeManipulator      =  this.registryMasterManipulator.getNSNodeManipulator();
        this.configOperatorFactory =  new GenericConfigOperatorFactory( this.registryMasterManipulator, treeMasterManipulator );
    }

    public GenericDistributeRegistry( Hydrarum hydrarum ) {
        this.hydrarum = hydrarum;
    }

    public GenericDistributeRegistry( KOIMappingDriver driver ) {
        this(
                driver.getSystem(),
                driver.getMasterManipulator()
        );
    }


    @Override
    public String getPath( GUID guid ) {
        String path = this.distributedConfTree.getPath(guid);
        if ( path != null ) {
            return path;
        }

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
    public GUID insert( TreeNode treeNode ) {
        Debug.trace(treeNode.getMetaType());
        TreeNodeOperator operator = this.configOperatorFactory.getOperator(treeNode.getMetaType());
        return operator.insert(treeNode);
    }

    @Override
    public TreeNode get( GUID guid ) {
        DistributedTreeNode node = this.distributedConfTree.getNode(guid);
        TreeNode newInstance = (TreeNode)node.getType().newInstance();
        TreeNodeOperator operator = this.configOperatorFactory.getOperator( newInstance.getMetaType() );
        return operator.get( guid );
    }

    @Override
    public TreeNode getThis( GUID guid ) {
        DistributedTreeNode node = this.distributedConfTree.getNode(guid);
        TreeNode newInstance = (TreeNode)node.getType().newInstance();
        TreeNodeOperator operator = this.configOperatorFactory.getOperator(newInstance.getMetaType());
        return operator.getWithoutInheritance(guid);
    }

    @Override
    public TreeNode getNodeByPath( String path ){
        GUID guid = this.distributedConfTree.queryGUIDByPath( path );
        if ( guid != null ){
            return this.get( guid );
        }
        else {
            String[] parts = this.processPath( path ).split("\\.");
            //匹配configNode
            List<GUID > nodeByNames = this.nodeManipulator.getNodeByName(parts[parts.length - 1]);
            for ( GUID nodeGuid : nodeByNames ){
                String nodePath = this.getPath( nodeGuid );
                if ( nodePath.equals( path ) ){
                    return this.get( nodeGuid );
                }
            }
            //匹配namespaceNode
            nodeByNames = this.namespaceNodeManipulator.getNodeByName(parts[parts.length - 1]);
            for ( GUID nodeGuid : nodeByNames ){
                String nodePath = this.getPath(nodeGuid);
                if ( nodePath.equals( path ) ){
                    return this.get( nodeGuid );
                }
            }
        }
        return null;
    }

    @Override
    public void insertProperties( Properties properties, GUID configNodeGuid ) {
        properties.setGuid(configNodeGuid);
        this.registryPropertiesManipulator.insert(properties);
    }

    @Override
    public void remove( GUID guid ){
        GUIDDistributedTrieNode node = this.distributedConfTree.getNode(guid);
        TreeNode newInstance = (TreeNode)node.getType().newInstance();
        TreeNodeOperator operator = this.configOperatorFactory.getOperator(newInstance.getMetaType());
        operator.remove(guid);
    }

    @Override
    public void insertTextValue( GUID guid, String text, String type ){
        GenericTextValue genericTextValue = new GenericTextValue();
        genericTextValue.setCreateTime(LocalDateTime.now());
        genericTextValue.setUpdateTime(LocalDateTime.now());
        genericTextValue.setValue(text);
        genericTextValue.setType(type);
        genericTextValue.setGuid(guid);
        this.registryTextValueManipulator.insert(genericTextValue);
    }

    private String getNodeName( DistributedTreeNode node ){
        UOI type = node.getType();
        TreeNode newInstance = (TreeNode)type.newInstance();
        Debug.trace(newInstance);
        TreeNodeOperator operator = this.configOperatorFactory.getOperator(newInstance.getMetaType());
        TreeNode treeNode = operator.get(node.getGuid());
        return treeNode.getName();
    }

    private String processPath(String path) {
        return path.replaceAll("\\(.*?\\)", "");
    }
}
