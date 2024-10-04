package com.pinecone.hydra.registry;

import com.pinecone.framework.system.Nullable;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.lang.DynamicFactory;
import com.pinecone.framework.util.lang.GenericDynamicFactory;
import com.pinecone.framework.util.name.path.PathResolver;
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
import com.pinecone.hydra.system.identifier.KOPathResolver;
import com.pinecone.hydra.system.ko.dao.GUIDNameManipulator;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.system.ko.driver.KOIMasterManipulator;
import com.pinecone.hydra.system.ko.driver.KOISkeletonMasterManipulator;
import com.pinecone.hydra.unit.udtt.entity.EntityNode;
import com.pinecone.hydra.unit.udtt.entity.ReparseLinkNode;
import com.pinecone.hydra.unit.udtt.entity.TreeNode;
import com.pinecone.hydra.registry.operator.GenericConfigOperatorFactory;
import com.pinecone.hydra.registry.operator.ConfigOperatorFactory;
import com.pinecone.hydra.unit.udtt.operator.TreeNodeOperator;
import com.pinecone.hydra.registry.source.RegistryMasterManipulator;
import com.pinecone.hydra.registry.source.RegistryConfigNodeManipulator;
import com.pinecone.hydra.registry.source.RegistryNSNodeManipulator;
import com.pinecone.hydra.registry.source.RegistryPropertiesManipulator;
import com.pinecone.hydra.registry.source.RegistryTextValueManipulator;
import com.pinecone.hydra.unit.udtt.DistributedTrieTree;
import com.pinecone.hydra.unit.udtt.DistributedTreeNode;
import com.pinecone.hydra.unit.udtt.GUIDDistributedTrieNode;
import com.pinecone.hydra.unit.udtt.GenericDistributedTrieTree;
import com.pinecone.hydra.unit.udtt.source.TreeMasterManipulator;
import com.pinecone.ulf.util.id.GUIDs;
import com.pinecone.ulf.util.id.GuidAllocator;

import java.io.StringReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GenericDistributeRegistry implements DistributedRegistry {
    protected Hydrarum                        hydrarum;
    protected GuidAllocator                   guidAllocator;
    protected RegistryConfig                  registryConfig;
    protected DynamicFactory                  dynamicFactory;
    protected PathResolver                    pathResolver;
    protected PathSelector                    pathSelector;
    protected ReparsePointSelector            reparsePointSelector;

    protected DistributedTrieTree             distributedTrieTree;
    protected RegistryMasterManipulator       registryMasterManipulator;
    protected RegistryPropertiesManipulator   registryPropertiesManipulator;
    protected RegistryTextValueManipulator    registryTextValueManipulator;
    protected RegistryConfigNodeManipulator   configNodeManipulator;
    protected RegistryNSNodeManipulator       namespaceNodeManipulator;
    protected ConfigOperatorFactory           configOperatorFactory;


    public GenericDistributeRegistry( Hydrarum hydrarum, KOIMasterManipulator masterManipulator ){
        this.hydrarum                      =  hydrarum;
        this.registryConfig                =  Registry.KernelRegistryConfig;
        this.registryMasterManipulator     =  (RegistryMasterManipulator) masterManipulator;
        this.dynamicFactory                =  new GenericDynamicFactory( hydrarum.getTaskManager().getClassLoader() );
        this.pathResolver                  =  new KOPathResolver( this.registryConfig );

        KOISkeletonMasterManipulator skeletonMasterManipulator = this.registryMasterManipulator.getSkeletonMasterManipulator();
        TreeMasterManipulator        treeMasterManipulator     = (TreeMasterManipulator) skeletonMasterManipulator;

        this.distributedTrieTree           =  new GenericDistributedTrieTree( treeMasterManipulator );
        this.registryPropertiesManipulator =  this.registryMasterManipulator.getRegistryPropertiesManipulator();
        this.registryTextValueManipulator  =  this.registryMasterManipulator.getRegistryTextValueManipulator();
        this.configNodeManipulator         =  this.registryMasterManipulator.getRegistryConfigNodeManipulator();
        this.namespaceNodeManipulator      =  this.registryMasterManipulator.getNSNodeManipulator();
        this.configOperatorFactory         =  new GenericConfigOperatorFactory( this, this.registryMasterManipulator );

        this.guidAllocator                 =  GUIDs.newGuidAllocator();
        this.pathSelector                  =  new StandardPathSelector(
                this.pathResolver, this.distributedTrieTree, this.namespaceNodeManipulator, new GUIDNameManipulator[] { this.configNodeManipulator }
        );
        this.reparsePointSelector          =  new ReparseLinkSelector( (StandardPathSelector) this.pathSelector );
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
    public GuidAllocator getGuidAllocator() {
        return this.guidAllocator;
    }

    @Override
    public DistributedTrieTree getMasterTrieTree() {
        return this.distributedTrieTree;
    }

    @Override
    public RegistryConfig getRegistryConfig() {
        return this.registryConfig;
    }


    protected String getNS( GUID guid, String szSeparator ) {
        String path = this.distributedTrieTree.getCachePath(guid);
        if ( path != null ) {
            return path;
        }

        DistributedTreeNode node = this.distributedTrieTree.getNode(guid);
        GUID owner = this.distributedTrieTree.getOwner(guid);
        if ( owner == null ){
            String assemblePath = this.getNodeName(node);
            while ( !node.getParentGUIDs().isEmpty() && this.allNonNull( node.getParentGUIDs() ) ){
                List<GUID> parentGuids = node.getParentGUIDs();
                for( int i = 0; i < parentGuids.size(); ++i ){
                    if ( parentGuids.get(i) != null ){
                        node = this.distributedTrieTree.getNode( parentGuids.get(i) );
                        break;
                    }
                }
                String nodeName = this.getNodeName(node);
                assemblePath = nodeName + szSeparator + assemblePath;
            }
            this.distributedTrieTree.insertCachePath( guid, assemblePath );
            return assemblePath;
        }
        else{
            String assemblePath = this.getNodeName( node );
            while ( !node.getParentGUIDs().isEmpty() && this.allNonNull( node.getParentGUIDs() ) ){
                node = this.distributedTrieTree.getNode( owner );
                String nodeName = this.getNodeName( node );
                assemblePath = nodeName + szSeparator + assemblePath;
                owner = this.distributedTrieTree.getOwner( node.getGuid() );
            }
            this.distributedTrieTree.insertCachePath( guid, assemblePath );
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
        DistributedTreeNode node = this.distributedTrieTree.getNode( guid );
        TreeNode newInstance = (TreeNode)node.getType().newInstance( new Class<? >[]{ DistributedRegistry.class }, this );
        return this.configOperatorFactory.getOperator( newInstance.getMetaType() );
    }

    @Override
    public RegistryTreeNode get( GUID guid ) {
        return this.getOperatorByGuid( guid ).get( guid );
    }

    @Override
    public RegistryTreeNode get( GUID guid, int depth ) {
        return this.getOperatorByGuid( guid ).get( guid, depth );
    }

    @Override
    public RegistryTreeNode getSelf( GUID guid ) {
        return this.getOperatorByGuid( guid ).getSelf( guid );
    }

    @Override
    public RegistryTreeNode queryTreeNode( String path ){
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



    /** Final Solution 20240929: 无法获取类型 */
    public GUID queryGUIDByNS( String path, String szBadSep, String szTargetSep ) {
        if( szTargetSep != null ) {
            path = path.replace( szBadSep, szTargetSep );
        }

        GUID guid = this.distributedTrieTree.queryGUIDByPath( path );
        if ( guid != null ){
            return guid;
        }

        String[] lpResolvedPath = new String[ 1 ];
        String[] parts = this.pathResolver.segmentPathParts( path );
        guid = this.pathSelector.searchGUID( parts, lpResolvedPath );
        if( guid != null ){
            this.distributedTrieTree.insertCachePath( guid, lpResolvedPath[ 0 ] );
        }
        return guid;
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
    public void newLinkTag( String originalPath, String dirPath, String tagName ) {
        GUID originalGuid           = this.queryGUIDByPath( originalPath );
        GUID dirGuid                = this.queryGUIDByPath( dirPath );

        if( this.distributedTrieTree.getOriginalGuid( tagName, dirGuid ) == null ) {
            this.distributedTrieTree.newLinkTag( originalGuid, dirGuid, tagName, this );
        }
    }



    @Override
    public void remove( GUID guid ){
        GUIDDistributedTrieNode node = this.distributedTrieTree.getNode( guid );
        TreeNode newInstance = (TreeNode)node.getType().newInstance();
        TreeNodeOperator operator = this.configOperatorFactory.getOperator( newInstance.getMetaType() );
        operator.purge( guid );
    }

    @Override
    public void removeReparseLink( GUID guid ) {
        this.distributedTrieTree.removeReparseLink( guid );
    }

    @Override
    public void remove( String path ) {
        Object handle = this.queryEntityHandle( path );
        if( handle instanceof GUID ) {
            this.remove( (GUID) handle );
        }
        else if( handle instanceof ReparseLinkNode ) {
            ReparseLinkNode linkNode = (ReparseLinkNode) handle;
            this.removeReparseLink( linkNode.getTagGuid() );
        }
    }

    @Override
    public void updateProperty( @Nullable GUID configNodeGuid, Property property ) {
        if( configNodeGuid != null ) {
            property.setGuid( configNodeGuid );
        }
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
    public void affirmOwnedNode( GUID parentGuid, GUID childGuid ) {
        this.distributedTrieTree.affirmOwnedNode( childGuid, parentGuid );
    }

    @Override
    public void newHardLink( GUID sourceGuid, GUID targetGuid ) {
        this.distributedTrieTree.newHardLink( sourceGuid, targetGuid );
    }

    @Override
    public void setDataAffinityGuid( GUID childGuid, GUID parentGuid ) {
        this.configNodeManipulator.setDataAffinityGuid( childGuid, parentGuid );
    }

    @Override
    public void newLinkTag( GUID originalGuid, GUID dirGuid, String tagName ) {
        this.distributedTrieTree.newLinkTag( originalGuid, dirGuid, tagName, this );
    }

    @Override
    public void updateLinkTag( GUID tagGuid, String tagName ) {
        this.distributedTrieTree.updateLinkTagName( tagGuid, tagName );
    }

    @Override
    public List<TreeNode > getChildren( GUID guid ) {
        List<GUIDDistributedTrieNode> childNodes = this.distributedTrieTree.getChildren(guid);
        ArrayList<TreeNode> configNodes = new ArrayList<>();
        for(GUIDDistributedTrieNode node : childNodes){
            TreeNode treeNode =  this.get(node.getGuid());
            configNodes.add(treeNode);
        }
        return configNodes;
    }


    public ReparseLinkNode queryReparseLinkByNS( String path, String szBadSep, String szTargetSep ) {
        if( szTargetSep != null ) {
            path = path.replace( szBadSep, szTargetSep );
        }

        String[] parts = this.pathResolver.segmentPathParts( path );
        return this.reparsePointSelector.searchLinkNode( parts );
    }

    /** ReparseLinkNode or GUID **/
    public Object queryEntityHandleByNS( String path, String szBadSep, String szTargetSep ) {
        if( szTargetSep != null ) {
            path = path.replace( szBadSep, szTargetSep );
        }

        String[] parts = this.pathResolver.segmentPathParts( path );
        return this.reparsePointSelector.search( parts );
    }

    public EntityNode queryNodeByNS( String path, String szBadSep, String szTargetSep ) {
        Object ret = this.queryEntityHandleByNS( path, szBadSep, szTargetSep );
        if( ret instanceof EntityNode ) {
            return (EntityNode) ret;
        }
        else if( ret instanceof GUID ) {
            return this.get( (GUID) ret );
        }

        return null;
    }

    public Object queryEntityHandle( String path ) {
        return this.queryEntityHandleByNS( path, null, null );
    }

    @Override
    public EntityNode queryNode( String path ) {
        return this.queryNodeByNS( path, null, null );
    }

    @Override
    public ReparseLinkNode queryReparseLink( String path ) {
        return this.queryReparseLinkByNS( path, null, null );
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
    public void moveTo( String sourcePath, String destinationPath ) throws IllegalArgumentException {
        GUID sourceGuid      = this.queryGUIDByPath( sourcePath );
        if( sourceGuid == null ) {
            throw new IllegalArgumentException( "Undefined source '" + sourcePath + "'" );
        }

        GUID destinationGuid = this.queryGUIDByPath( destinationPath );
        if( !this.namespaceNodeManipulator.isNamespaceNode( destinationGuid ) ){
            throw new IllegalArgumentException( "Illegal destination '" + destinationPath + "', should be namespace." );
        }

        if( destinationGuid == null ) {
            throw new IllegalArgumentException( "Undefined destination '" + destinationPath + "'" );
        }

        if( sourceGuid == destinationGuid ) {
            throw new IllegalArgumentException( "Cyclic path detected '" + sourcePath + "'" );
        }
        this.distributedTrieTree.moveTo( sourceGuid, destinationGuid );
        this.distributedTrieTree.removeCachePath( sourceGuid );
    }

    @Override
    public void move( String sourcePath, String destinationPath ) {
        GUID sourceGuid      = this.queryGUIDByPath( sourcePath );
        if( sourceGuid == null ) {
            throw new IllegalArgumentException( "Undefined source '" + sourcePath + "'" );
        }

        List<String > sourParts = this.pathResolver.resolvePathParts( sourcePath );
        List<String > destParts = this.pathResolver.resolvePathParts( destinationPath );

        String szLastDestTarget = destParts.get( destParts.size() - 1 );

        // Case1: Move "game/terraria/npc" => "game/minecraft/npc", which has the same dest name.
        if( sourParts.get( sourParts.size() - 1 ).equals( szLastDestTarget ) || szLastDestTarget.equals( "." ) ) {
            destParts.remove( destParts.size() - 1 );
            String szParentPath = this.pathResolver.assemblePath( destParts );
            destParts.add( szLastDestTarget );

            // Move to, which has the same name or explicit current dir `.`.
            this.moveTo( sourcePath, szParentPath );
        }
        // Case2: Move "game/terraria/npc" => "game/minecraft/character", move all children therein.
        //    game/terraria/npc/f1 => game/minecraft/character/f1
        //    game/terraria/npc/f2 => game/minecraft/character/f2
        //    etc.
        else {
            // Case2-1: Is config or other none namespace node.
            //          Move "game/terraria/file" => "game/minecraft/dir".
            if( !this.namespaceNodeManipulator.isNamespaceNode( sourceGuid ) ) {
                NamespaceNode target = this.affirmNamespace( destinationPath );
                this.distributedTrieTree.moveTo( sourceGuid, target.getGuid() );
            }
            else {
                List<TreeNode > children = this.getChildren( sourceGuid );
                if( !children.isEmpty() ) {
                    NamespaceNode target = this.affirmNamespace( destinationPath );
                    for( TreeNode node : children ) {
                        this.distributedTrieTree.moveTo( node.getGuid(), target.getGuid() );
                    }
                }
            }

            this.distributedTrieTree.removeTreeNodeOnly( sourceGuid );
            this.distributedTrieTree.removeCachePath( sourceGuid );
        }
    }

    @Override
    public List<RegistryTreeNode> listRoot() {
        List<GUID> guids = this.distributedTrieTree.listRoot();
        ArrayList<RegistryTreeNode> registryTreeNodes = new ArrayList<>();
        for(GUID guid : guids){
            RegistryTreeNode treeNode = this.get(guid);
            registryTreeNodes.add(treeNode);
        }
        return registryTreeNodes;
    }

    @Override
    public void rename( GUID guid, String name ) {
        GUIDDistributedTrieNode node = this.distributedTrieTree.getNode(guid);
        TreeNode newInstance = (TreeNode)node.getType().newInstance();
        TreeNodeOperator operator = this.configOperatorFactory.getOperator( newInstance.getMetaType() );
        operator.updateName( guid, name );

        this.distributedTrieTree.removeCachePath( guid );
    }

    @Override
    public List<TreeNode > getAllTreeNode() {
        List<GUID> nameSpaceNodes = this.namespaceNodeManipulator.dumpGuid();
        List<GUID> confNodes      = this.configNodeManipulator.dumpGuid();
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



    // TODO, Unchecked type affirmed.
    protected RegistryTreeNode affirmTreeNodeByPath( String path, Class<? > cnSup, Class<? > nsSup ) {
        String[] parts = this.pathResolver.segmentPathParts( path );
        String currentPath = "";
        GUID parentGuid = GUIDs.Dummy72();

        RegistryTreeNode node = this.queryTreeNode( path );
        if( node != null ) {
            return node;
        }

        RegistryTreeNode ret = null;
        for( int i = 0; i < parts.length; ++i ){
            currentPath = currentPath + ( i > 0 ? this.registryConfig.getPathNameSeparator() : "" ) + parts[ i ];
            node = this.queryTreeNode( currentPath );
            if ( node == null){
                if ( i == parts.length - 1 && cnSup != null ){
                    ConfigNode configNode = (ConfigNode) this.dynamicFactory.optNewInstance( cnSup, new Object[]{ this } );
                    configNode.setName( parts[i] );
                    GUID guid = this.put( configNode );
                    this.affirmOwnedNode( parentGuid, guid );
                    return configNode;
                }
                else {
                    NamespaceNode namespaceNode = (NamespaceNode) this.dynamicFactory.optNewInstance( nsSup, new Object[]{ this } );
                    namespaceNode.setName(parts[i]);
                    GUID guid = this.put( namespaceNode );
                    if ( i != 0 ){
                        this.affirmOwnedNode( parentGuid, guid );
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
    public void copyPropertiesTo( GUID sourceGuid, GUID destinationGuid ) {
        this.registryPropertiesManipulator.copyPropertiesTo( sourceGuid, destinationGuid );
    }

    @Override
    public void copyTextValueTo( GUID sourceGuid, GUID destinationGuid ) {
        this.registryTextValueManipulator.copyTextValueTo( sourceGuid, destinationGuid );
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


    @Override
    public Object querySelector( String szSelector ) {
        RegistryJPathSelector selector = new RegistryJPathSelector(
                new StringReader( szSelector ), this.pathResolver, this, this.namespaceNodeManipulator, new GUIDNameManipulator[] { this.configNodeManipulator }
        );
        return selector.eval() ;
    }

    private boolean allNonNull( List<?> list ) {
        return list.stream().noneMatch( Objects::isNull );
    }
}
