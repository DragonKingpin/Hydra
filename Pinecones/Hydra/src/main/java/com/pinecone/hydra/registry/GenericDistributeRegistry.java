package com.pinecone.hydra.registry;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.lang.DynamicFactory;
import com.pinecone.framework.util.lang.GenericDynamicFactory;
import com.pinecone.framework.util.uoi.UOI;
import com.pinecone.hydra.registry.entity.ConfigNode;
import com.pinecone.hydra.registry.entity.GenericNamespaceNode;
import com.pinecone.hydra.registry.entity.GenericProperties;
import com.pinecone.hydra.registry.entity.GenericTextConfigNode;
import com.pinecone.hydra.registry.entity.GenericTextValue;
import com.pinecone.hydra.registry.entity.NamespaceNode;
import com.pinecone.hydra.registry.entity.Properties;
import com.pinecone.hydra.registry.entity.Property;
import com.pinecone.hydra.registry.entity.RegistryTreeNode;
import com.pinecone.hydra.registry.entity.TextConfigNode;
import com.pinecone.hydra.registry.entity.TextValue;
import com.pinecone.hydra.registry.operator.RegistryNodeOperator;
import com.pinecone.hydra.system.Hydrarum;
import com.pinecone.hydra.system.ko.dao.GUIDNameManipulator;
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
import com.pinecone.ulf.util.id.GUIDs;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GenericDistributeRegistry implements DistributedRegistry {
    protected Hydrarum                        hydrarum;
    protected RegistryConfig                  registryConfig;
    protected DynamicFactory                  dynamicFactory;

    protected DistributedTrieTree             distributedConfTree;
    protected RegistryMasterManipulator       registryMasterManipulator;
    protected RegistryPropertiesManipulator   registryPropertiesManipulator;
    protected RegistryTextValueManipulator    registryTextValueManipulator;
    protected RegistryNodeManipulator         nodeManipulator;
    protected RegistryNSNodeManipulator       namespaceNodeManipulator;
    protected ConfigOperatorFactory           configOperatorFactory;


    public GenericDistributeRegistry( Hydrarum hydrarum, KOIMasterManipulator masterManipulator ){
        this.hydrarum                    =  hydrarum;
        this.registryConfig              =  Registry.KernelRegistryConfig;
        this.registryMasterManipulator   =  (RegistryMasterManipulator) masterManipulator;
        this.dynamicFactory              =  new GenericDynamicFactory( hydrarum.getTaskManager().getClassLoader() );

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
    public RegistryConfig getRegistryConfig() {
        return this.registryConfig;
    }


    protected String getNS( GUID guid, String szSeparator ) {
        String path = this.distributedConfTree.getPath(guid);
        if ( path != null ) {
            return path;
        }

        DistributedTreeNode node = this.distributedConfTree.getNode(guid);
        GUID owner = this.distributedConfTree.getOwner(guid);
        if ( owner == null ){
            String assemblePath = this.getNodeName(node);
            while ( !node.getParentGUIDs().isEmpty() && this.allNonNull( node.getParentGUIDs() ) ){
                List<GUID> parentGuids = node.getParentGUIDs();
                for( int i = 0; i < parentGuids.size(); ++i ){
                    if ( parentGuids.get(i) != null ){
                        node = this.distributedConfTree.getNode(parentGuids.get(i));
                        break;
                    }
                }
                String nodeName = this.getNodeName(node);
                assemblePath = nodeName + szSeparator + assemblePath;
            }
            this.distributedConfTree.insertPath(guid,assemblePath);
            return assemblePath;
        }
        else{
            String assemblePath = this.getNodeName( node );
            while ( !node.getParentGUIDs().isEmpty() && this.allNonNull( node.getParentGUIDs() ) ){
                node = this.distributedConfTree.getNode( owner );
                String nodeName = this.getNodeName( node );
                assemblePath = nodeName + szSeparator + assemblePath;
                owner = this.distributedConfTree.getOwner( node.getGuid() );
            }
            this.distributedConfTree.insertPath(guid,assemblePath);
            return assemblePath;
        }
    }

    @Override
    public String getPath( GUID guid ) {
        return this.getNS( guid, this.registryConfig.getPathNameSeparator() );
    }

    @Override
    public String getFullName( GUID guid ) {
        return this.getNS( guid, this.registryConfig.getFullNameSeparator() );
    }

    @Override
    public GUID put( TreeNode treeNode ) {
        TreeNodeOperator operator = this.configOperatorFactory.getOperator(treeNode.getMetaType());
        return operator.insert(treeNode);
    }

    protected RegistryNodeOperator getOperatorByGuid( GUID guid ) {
        DistributedTreeNode node = this.distributedConfTree.getNode( guid );
        TreeNode newInstance = (TreeNode)node.getType().newInstance( new Class<? >[]{ DistributedRegistry.class }, this );
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
        //GUID guid = this.distributedConfTree.queryGUIDByPath( path );
        GUID guid = this.queryGUIDByPath( path );
        if( guid != null ) {
            return this.get( guid );
        }
        return null;
    }

    @Override
    public Properties getProperties( GUID guid ) {
        return this.get( guid ).evinceProperties();
    }

    @Override
    public Properties getProperties( String path ) {
        return this.getProperties( this.queryGUIDByPath( path ) );
    }

    @Override
    public NamespaceNode getNamespaceNode( GUID guid ) {
        return this.get( guid ).evinceNamespaceNode();
    }

    @Override
    public NamespaceNode getNamespaceNode( String path ){
        return this.getNamespaceNode( this.queryGUIDByPath( path ) );
    }

    @Override
    public List<Property > fetchProperties( GUID guid ) {
        ArrayList<Property > properties = new ArrayList<>();
        List<Property > genericProperties = this.registryPropertiesManipulator.getProperties(guid);
        for ( Property p : genericProperties ){
            properties.add( (Property) p );
        }
        return properties;
    }

    @Override
    public List<Property > fetchProperties( String path ) {
        return this.fetchProperties( this.queryGUIDByPath( path ) );
    }

    @Override
    public TextValue getTextValue( GUID guid ) {
        return this.registryTextValueManipulator.getTextValue( guid );
    }

    @Override
    public TextValue getTextValue( String path ) {
        return this.getTextValue( this.queryGUIDByPath( path ) );
    }



    protected GUID searchGUID( String path, GUIDNameManipulator[] manipulators, String[] parts )  {
        for( GUIDNameManipulator manipulator : manipulators ) {
            List<GUID > nodeByNames = manipulator.getGuidsByName( parts[parts.length - 1] );
            for ( GUID nodeGuid : nodeByNames ){
                String nodePath = this.getPath( nodeGuid );
                if ( nodePath.equals( path ) ){
                    return nodeGuid;
                }
            }
        }

        return null;
    }



    /** Final Solution 20240929: 无法获取类型 */
    public GUID queryGUIDByNS( String path, String szBadSep, String szTargetSep ) {
        if( szTargetSep != null ) {
            path = path.replace( szBadSep, szTargetSep );
        }

        GUID guid = this.distributedConfTree.queryGUIDByPath( path );
        if ( guid != null ){
            return guid;
        }

        String[] parts = this.processPath( path ).split( this.registryConfig.getPathNameSepRegex() );

        return this.searchGUID(
                path, new GUIDNameManipulator[] { this.nodeManipulator, this.namespaceNodeManipulator }, parts
        );
    }

    @Override
    public GUID queryGUIDByPath( String path ) {
        return this.queryGUIDByNS( path, null, null );
    }

    @Override
    public GUID queryGUIDByFN( String fullName ) {
        return this.queryGUIDByNS(
                fullName, this.registryConfig.getFullNameSeparator(), this.registryConfig.getPathNameSeparator()
        );
    }

    @Override
    public void putProperty( Property property, GUID configNodeGuid ) {
        property.setGuid( configNodeGuid );
        property.setCreateTime( LocalDateTime.now() );
        property.setUpdateTime( LocalDateTime.now() );
        this.registryPropertiesManipulator.insert( property );
    }

    @Override
    public void remove( GUID guid ){
        GUIDDistributedTrieNode node = this.distributedConfTree.getNode(guid);
        TreeNode newInstance = (TreeNode)node.getType().newInstance();
        TreeNodeOperator operator = this.configOperatorFactory.getOperator(newInstance.getMetaType());
        operator.purge(guid);
    }


    @Override
    public void remove( String path ) {
        GUID guid = this.queryGUIDByPath( path );
        if( guid != null ) {
            this.remove( guid );
        }
    }

    @Override
    public void updateProperty( Property property, GUID configNodeGuid ) {
        property.setGuid( configNodeGuid );
        property.setUpdateTime( LocalDateTime.now() );
        this.registryPropertiesManipulator.update( property );
    }

    @Override
    public void updateTextValue( TextValue textValue, GUID configNodeGuid ) {
        textValue.setGuid( configNodeGuid );
        textValue.setUpdateTime( LocalDateTime.now() );
        this.registryTextValueManipulator.update( textValue );
    }

    @Override
    public void updateTextValue( GUID guid, String text, String type ) {
        TextValue textValue = GenericTextValue.newUpdateTextValue( guid, text, type );
        this.registryTextValueManipulator.update( textValue );
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
    public void setAffinity( GUID sourceGuid, GUID targetGuid ) {
        this.distributedConfTree.setOwner(sourceGuid,targetGuid);
    }

    @Override
    public void setInheritance(GUID childGuid, GUID parentGuid) {
        this.nodeManipulator.setParentGuid(childGuid,parentGuid);
    }

    @Override
    public List<TreeNode > getChildren( GUID guid ) {
        List<GUIDDistributedTrieNode> childNodes = this.distributedConfTree.getChildren(guid);
        ArrayList<TreeNode> configNodes = new ArrayList<>();
        for(GUIDDistributedTrieNode node : childNodes){
            TreeNode treeNode =  this.get(node.getGuid());
            configNodes.add(treeNode);
        }
        return configNodes;
    }

    @Override
    public List<TreeNode > selectByName( String name ) {
        List<GUID> nodes = this.namespaceNodeManipulator.getGuidsByName(name);
        ArrayList<TreeNode> configNodes = new ArrayList<>();
        for( GUID guid : nodes ){
            TreeNode treeNode =  this.get(guid);
            configNodes.add(treeNode);
        }
        return configNodes;
    }

    @Override
    public void moveTo( String sourcePath, String destinationPath ) {
        GUID sourceGuid      = this.queryGUIDByPath( sourcePath );
        GUID destinationGuid = this.queryGUIDByPath( destinationPath );
        if( sourceGuid != null ) {
            this.distributedConfTree.moveTo( sourceGuid, destinationGuid );
        }
    }

    @Override
    public List<RegistryTreeNode> listRoot() {
        List<GUID> guids = this.distributedConfTree.listRoot();
        ArrayList<RegistryTreeNode> registryTreeNodes = new ArrayList<>();
        for(GUID guid : guids){
            RegistryTreeNode treeNode = this.get(guid);
            registryTreeNodes.add(treeNode);
        }
        return registryTreeNodes;
    }

    @Override
    public void rename( GUID guid, String name ) {
        GUIDDistributedTrieNode node = this.distributedConfTree.getNode(guid);
        TreeNode newInstance = (TreeNode)node.getType().newInstance();
        TreeNodeOperator operator = this.configOperatorFactory.getOperator( newInstance.getMetaType() );
        operator.updateName(guid, name);
    }

    @Override
    public List<TreeNode > getAllTreeNode() {
        List<GUID> nameSpaceNodes = this.namespaceNodeManipulator.getAll();
        List<GUID> confNodes      = this.nodeManipulator.getALL();
        ArrayList<TreeNode> treeNodes = new ArrayList<>();
        for (GUID guid : nameSpaceNodes){
            TreeNode treeNode = this.get(guid);
            treeNodes.add(treeNode);
        }
        for ( GUID guid : confNodes ){
            TreeNode treeNode = this.get(guid);
            treeNodes.add(treeNode);
        }
        return treeNodes;
    }

    @Override
    public void insertRegistryTreeNode( GUID parentGuid, GUID childGuid ) {
        this.distributedConfTree.insertOwnedNode( childGuid, parentGuid );
    }


    // TODO, Unchecked type affirmed.
    protected RegistryTreeNode affirmTreeNodeByPath( String path, Class<? > cnSup, Class<? > nsSup ) {
        String[] parts = this.processPath( path ).split( this.registryConfig.getPathNameSepRegex() );
        String currentPath = "";
        GUID parentGuid = GUIDs.Dummy72();

        RegistryTreeNode node = this.getNodeByPath( path );
        if( node != null ) {
            return node;
        }

        RegistryTreeNode ret = null;
        for( int i = 0; i < parts.length; ++i ){
            currentPath = currentPath + ( i > 0 ? this.registryConfig.getPathNameSeparator() : "" ) + parts[ i ];
            node = this.getNodeByPath( currentPath );
            if ( node == null){
                if ( i == parts.length - 1 && cnSup != null ){
                    ConfigNode configNode = (ConfigNode) this.dynamicFactory.optNewInstance( cnSup, new Object[]{ this } );
                    configNode.setName( parts[i] );
                    GUID guid = this.put( configNode );
                    this.insertRegistryTreeNode( parentGuid, guid );
                    return configNode;
                }
                else {
                    NamespaceNode namespaceNode = (NamespaceNode) this.dynamicFactory.optNewInstance( nsSup, new Object[]{ this } );
                    namespaceNode.setName(parts[i]);
                    GUID guid = this.put( namespaceNode );
                    if ( i != 0 ){
                        this.insertRegistryTreeNode( parentGuid, guid );
                        parentGuid = guid;
                    }
                    else {
                        parentGuid = guid;
                    }

                    ret = namespaceNode;
                }
            }
            else {
                parentGuid = node.getGuid();
            }
        }

        return ret;
    }



    @Override
    public NamespaceNode    affirmNamespace      ( String path ) {
        return (NamespaceNode) this.affirmTreeNodeByPath( path, null, GenericNamespaceNode.class );
    }

    @Override
    public Properties       affirmProperties     ( String path ) {
        return (Properties) this.affirmTreeNodeByPath( path, GenericProperties.class, GenericNamespaceNode.class );
    }

    @Override
    public TextConfigNode   affirmTextConfig     ( String path ) {
        return (TextConfigNode) this.affirmTreeNodeByPath( path, GenericTextConfigNode.class, GenericNamespaceNode.class );
    }



    @Override
    public Properties putProperties( String path, Map<String, Object > properties ) {
        Properties pro = this.affirmProperties( path );
        pro.puts( properties );
        return pro;
    }

    @Override
    public TextConfigNode putTextValue( String path, String type, String value ) {
        TextConfigNode pro = this.affirmTextConfig( path );
        pro.put( new GenericTextValue( pro.getGuid(), value, type ) );
        return pro;
    }

    @Override
    public void putTextValue( GUID guid, String text, String format ){
        GenericTextValue genericTextValue = new GenericTextValue( guid, text, format );
        this.registryTextValueManipulator.insert( genericTextValue );
    }


    @Override
    public ConfigNode getConfigNode( GUID guid ) {
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
        TreeNodeOperator operator = this.configOperatorFactory.getOperator(newInstance.getMetaType());
        TreeNode treeNode = operator.get(node.getGuid());
        return treeNode.getName();
    }

    protected String processPath( String path ) {
        return path;
    }


    @Override
    public Object querySelector( String szSelector ) {
        return null;
    }

    private boolean allNonNull( List<?> list ) {
        return list.stream().noneMatch( Objects::isNull );
    }
}
