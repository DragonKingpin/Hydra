package com.pinecone.hydra.registry;

import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.uoi.UOI;
import com.pinecone.hydra.registry.entity.ConfigNode;
import com.pinecone.hydra.registry.entity.ArchConfigNode;
import com.pinecone.hydra.registry.entity.GenericProperty;
import com.pinecone.hydra.registry.entity.GenericPropertiesNode;
import com.pinecone.hydra.registry.entity.GenericTextConfigNode;
import com.pinecone.hydra.registry.entity.GenericTextValue;
import com.pinecone.hydra.registry.entity.Property;
import com.pinecone.hydra.registry.entity.RegistryTreeNode;
import com.pinecone.hydra.registry.entity.TextValue;
import com.pinecone.hydra.registry.operator.RegistryNodeOperator;
import com.pinecone.hydra.service.tree.UOIUtils;
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
import java.util.ArrayList;
import java.util.List;

public class GenericDistributeRegistry implements DistributedRegistry {
    protected Hydrarum                        hydrarum;

    protected DistributedTrieTree             distributedConfTree;
    protected RegistryMasterManipulator       registryMasterManipulator;
    protected RegistryPropertiesManipulator   registryPropertiesManipulator;
    protected RegistryTextValueManipulator    registryTextValueManipulator;
    protected RegistryNodeManipulator         nodeManipulator;
    protected RegistryNSNodeManipulator       namespaceNodeManipulator;
    protected ConfigOperatorFactory           configOperatorFactory;


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
        this.configOperatorFactory         =  new GenericConfigOperatorFactory( this, this.registryMasterManipulator );
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
    public GUID put( TreeNode treeNode ) {
        Debug.trace(treeNode.getMetaType());
        TreeNodeOperator operator = this.configOperatorFactory.getOperator(treeNode.getMetaType());
        return operator.insert(treeNode);
    }

    protected RegistryNodeOperator getOperatorByGuid( GUID guid ) {
        DistributedTreeNode node = this.distributedConfTree.getNode(guid);
        TreeNode newInstance = (TreeNode)node.getType().newInstance( new Class<? >[]{ DistributedRegistry.class }, this );
        //Debug.trace( newInstance, newInstance.getMetaType() );
        return this.configOperatorFactory.getOperator( newInstance.getMetaType() );
    }

    @Override
    public RegistryTreeNode get( GUID guid ) {
        return this.getOperatorByGuid( guid ).get( guid );
    }

    @Override
    public RegistryTreeNode getThis( GUID guid ) {
        return this.getOperatorByGuid( guid ).getWithoutInheritance( guid );
    }

    @Override
    public RegistryTreeNode getNodeByPath( String path ){
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
    public void putProperties( Property property, GUID configNodeGuid ) {
        //todo 更改节点类型
        this.distributedConfTree.updateType(UOIUtils.createLocalJavaClass(GenericPropertiesNode.class.getName()),configNodeGuid);
        property.setGuid(configNodeGuid);
        property.setCreateTime(LocalDateTime.now());
        property.setUpdateTime(LocalDateTime.now());
        this.registryPropertiesManipulator.insert(property);
    }

    @Override
    public void remove( GUID guid ){
        GUIDDistributedTrieNode node = this.distributedConfTree.getNode(guid);
        TreeNode newInstance = (TreeNode)node.getType().newInstance();
        TreeNodeOperator operator = this.configOperatorFactory.getOperator(newInstance.getMetaType());
        operator.remove(guid);
    }

    @Override
    public void updateProperty( Property property, GUID configNodeGuid ) {
        property.setGuid(configNodeGuid);
        property.setUpdateTime(LocalDateTime.now());
        this.registryPropertiesManipulator.update(property);
    }

    @Override
    public void updateTextValue( GUID guid, String text, String type ) {
        GenericTextValue genericTextValue = new GenericTextValue();
        genericTextValue.setUpdateTime(LocalDateTime.now());
        genericTextValue.setValue(text);
        genericTextValue.setType(type);
        this.registryTextValueManipulator.update(genericTextValue);
    }

    @Override
    public List<Property> getProperties( GUID guid ) {
        ArrayList<Property > properties = new ArrayList<>();
        List<GenericProperty > genericProperties = this.registryPropertiesManipulator.getProperties(guid);
        for ( GenericProperty p : genericProperties ){
            properties.add((Property) p);
        }
        return properties;
    }

    @Override
    public TextValue getTextValue( GUID guid ) {
        return this.registryTextValueManipulator.getTextValue(guid);
    }

    @Override
    public void removeProperty( GUID guid, String key ) {
        this.registryPropertiesManipulator.remove(guid,key);
    }

    @Override
    public void removeTextValue( GUID guid ) {
        this.registryTextValueManipulator.remove(guid);
    }

    @Override
    public List<TreeNode > getChildConf( GUID guid ) {
        List<GUIDDistributedTrieNode> childNodes = this.distributedConfTree.getChildNode(guid);
        ArrayList<TreeNode> configNodes = new ArrayList<>();
        for(GUIDDistributedTrieNode node : childNodes){
            TreeNode treeNode =  this.get(node.getGuid());
            configNodes.add(treeNode);
        }
        return configNodes;
    }

    @Override
    public List<TreeNode > selectByName( String name ) {
        List<GUID> nodes = this.namespaceNodeManipulator.getNodeByName(name);
        ArrayList<TreeNode> configNodes = new ArrayList<>();
        for(GUID guid : nodes){
            TreeNode treeNode =  this.get(guid);
            configNodes.add(treeNode);
        }
        return configNodes;
    }

    @Override
    public void rename( String name, GUID guid ) {
        GUIDDistributedTrieNode node = this.distributedConfTree.getNode(guid);
        TreeNode newInstance = (TreeNode)node.getType().newInstance();
        TreeNodeOperator operator = this.configOperatorFactory.getOperator( newInstance.getMetaType() );
//        ArchConfigNode configNode = new ArchConfigNode( this );
//        configNode.setGuid( guid );
//        configNode.setName( name );
//        operator.update( configNode );
    }

    @Override
    public List<TreeNode > getAllTreeNode() {
        List<GUID> nameSpaceNodes = this.namespaceNodeManipulator.getAll();
        List<GUID> confNodes = this.nodeManipulator.getALL();
        ArrayList<TreeNode> treeNodes = new ArrayList<>();
        for (GUID guid : nameSpaceNodes){
            TreeNode treeNode = this.get(guid);
            treeNodes.add(treeNode);
        }
        for (GUID guid : confNodes){
            TreeNode treeNode = this.get(guid);
            treeNodes.add(treeNode);
        }
        return treeNodes;
    }

    @Override
    public void insertRegistryTreeNode( GUID parentGuid, GUID childGuid ) {
        this.distributedConfTree.insertNodeToParent(childGuid,parentGuid);
    }

    @Override
    public void putTextValue( GUID guid, String text, String type ){
        //todo 更改节点类型
        this.distributedConfTree.updateType(UOIUtils.createLocalJavaClass(GenericTextConfigNode.class.getName()),guid);
        GenericTextValue genericTextValue = new GenericTextValue();
        genericTextValue.setCreateTime(LocalDateTime.now());
        genericTextValue.setUpdateTime(LocalDateTime.now());
        genericTextValue.setValue(text);
        genericTextValue.setType(type);
        genericTextValue.setGuid(guid);
        this.registryTextValueManipulator.insert(genericTextValue);
    }


    @Override
    public ConfigNode getConfigNodeByGuid( GUID guid ) {
        RegistryTreeNode p = this.get( guid );
        ConfigNode cn = p.evinceConfigNode() ;
        if( cn != null ) {
            return cn;
        }
        return null;
    }

    private String getNodeName( DistributedTreeNode node ){
        UOI type = node.getType();
        TreeNode newInstance = (TreeNode)type.newInstance();
        Debug.trace(newInstance);
        TreeNodeOperator operator = this.configOperatorFactory.getOperator(newInstance.getMetaType());
        TreeNode treeNode = operator.get(node.getGuid());
        return treeNode.getName();
    }

    private String processPath( String path ) {
        return path.replaceAll("\\(.*?\\)", "");
    }
}
