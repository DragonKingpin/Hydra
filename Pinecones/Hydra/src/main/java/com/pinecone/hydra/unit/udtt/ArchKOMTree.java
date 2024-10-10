package com.pinecone.hydra.unit.udtt;

import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.lang.DynamicFactory;
import com.pinecone.framework.util.lang.GenericDynamicFactory;
import com.pinecone.framework.util.name.path.PathResolver;
import com.pinecone.framework.util.uoi.UOI;
import com.pinecone.hydra.file.KOMFileSystem;
import com.pinecone.hydra.registry.KOMRegistry;
import com.pinecone.hydra.registry.ReparseLinkSelector;
import com.pinecone.hydra.registry.StandardPathSelector;
import com.pinecone.hydra.registry.entity.ElementNode;
import com.pinecone.hydra.system.Hydrarum;
import com.pinecone.hydra.system.identifier.KOPathResolver;
import com.pinecone.hydra.system.ko.KOMInstrument;
import com.pinecone.hydra.system.ko.KernelObjectConfig;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.system.ko.driver.KOIMasterManipulator;
import com.pinecone.hydra.system.ko.driver.KOISkeletonMasterManipulator;
import com.pinecone.hydra.system.ko.kom.PathSelector;
import com.pinecone.hydra.system.ko.kom.ReparsePointSelector;
import com.pinecone.hydra.unit.udtt.entity.EntityNode;
import com.pinecone.hydra.unit.udtt.entity.ReparseLinkNode;
import com.pinecone.hydra.unit.udtt.entity.TreeNode;
import com.pinecone.hydra.unit.udtt.operator.OperatorFactory;
import com.pinecone.hydra.unit.udtt.operator.TreeNodeOperator;
import com.pinecone.hydra.unit.udtt.source.TreeMasterManipulator;
import com.pinecone.ulf.util.id.GuidAllocator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ArchKOMTree implements KOMTree, KOMInstrument {
    protected Hydrarum              hydrarum;
    protected GuidAllocator         guidAllocator;
    protected KOMInstrument         komInstrument;
    protected OperatorFactory       operatorFactory;

    protected DistributedTrieTree   distributedTrieTree;
    protected KernelObjectConfig    kernelObjectConfig;
    protected PathResolver          pathResolver;
    protected PathSelector          pathSelector;
    protected ReparsePointSelector  reparsePointSelector;
    protected DynamicFactory        dynamicFactory;
    public ArchKOMTree(KOIMasterManipulator masterManipulator , OperatorFactory operatorFactory, KernelObjectConfig kernelObjectConfig, PathSelector pathSelector, Hydrarum hydrarum){
        this.hydrarum                      =  hydrarum;
        this.komInstrument                 =  komInstrument;
        this.operatorFactory               =  operatorFactory;
        this.kernelObjectConfig            =  kernelObjectConfig;
        this.pathResolver                  =  new KOPathResolver( this.kernelObjectConfig );
        this.dynamicFactory                =  new GenericDynamicFactory( hydrarum.getTaskManager().getClassLoader() );

        KOISkeletonMasterManipulator skeletonMasterManipulator = masterManipulator.getSkeletonMasterManipulator();
        TreeMasterManipulator treeMasterManipulator     = (TreeMasterManipulator) skeletonMasterManipulator;
        this.distributedTrieTree           =  new GenericDistributedTrieTree( treeMasterManipulator );
        this.pathSelector                  =  pathSelector;
        this.reparsePointSelector          =  new ReparseLinkSelector( (StandardPathSelector) this.pathSelector );
    }
    @Override
    public GUID put( TreeNode treeNode ) {
        TreeNodeOperator operator = this.operatorFactory.getOperator(treeNode.getMetaType());
        return operator.insert( treeNode );
    }

    @Override
    public TreeNode get(GUID guid, int depth) {
        return this.getOperatorByGuid( guid ).get( guid, depth );
    }

    @Override
    public TreeNode getSelf(GUID guid) {
        return this.getOperatorByGuid( guid ).getSelf( guid );
    }

    protected String getNS(GUID guid, String szSeparator ) {
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
    public String getPath(GUID guid) {
        return this.getNS( guid, this.kernelObjectConfig.getPathNameSeparator() );
    }

    @Override
    public String getFullName(GUID guid) {
        return this.getNS( guid, this.kernelObjectConfig.getFullNameSeparator() );
    }

    protected TreeNodeOperator getOperatorByGuid(GUID guid ) {
        DistributedTreeNode node = this.distributedTrieTree.getNode( guid );
        TreeNode newInstance = (TreeNode)node.getType().newInstance( new Class<? >[]{ komInstrument.getClass() }, this );
        return this.operatorFactory.getOperator( newInstance.getMetaType() );
    }
    @Override
    public TreeNode get( GUID guid ) {
        return this.getOperatorByGuid( guid ).get( guid );
    }
    @Override
    public GUID queryGUIDByNS( String path, String szBadSep, String szTargetSep ) {
        if( szTargetSep != null ) {
            path = path.replace( szBadSep, szTargetSep );
        }

        String[] parts = this.pathResolver.segmentPathParts( path );
        List<String > resolvedParts = this.pathResolver.resolvePath( parts );
        path = this.pathResolver.assemblePath( resolvedParts );

        GUID guid = this.distributedTrieTree.queryGUIDByPath( path );
        if ( guid != null ){
            return guid;
        }


        guid = this.pathSelector.searchGUID( resolvedParts );
        if( guid != null ){
            this.distributedTrieTree.insertCachePath( guid, path );
        }
        return guid;
    }

    @Override
    public GUID queryGUIDByPath(String path) {
        return this.queryGUIDByNS( path, null, null );
    }
    public ReparseLinkNode queryReparseLinkByNS( String path, String szBadSep, String szTargetSep ) {
        if( szTargetSep != null ) {
            path = path.replace( szBadSep, szTargetSep );
        }

        String[] parts = this.pathResolver.segmentPathParts( path );
        return this.reparsePointSelector.searchLinkNode( parts );
    }
    @Override
    public ElementNode queryElement(String path ){
        //GUID guid = this.distributedConfTree.queryGUIDByPath( path );
        GUID guid = this.queryGUIDByPath( path );
        if( guid != null ) {
            return (ElementNode) this.get( guid );
        }
        return null;
    }
    @Override
    public ReparseLinkNode queryReparseLink(String path) {
        return this.queryReparseLinkByNS( path, null, null );
    }

    @Override
    public void remove(GUID guid) {
        GUIDDistributedTrieNode node = this.distributedTrieTree.getNode( guid );
        TreeNode newInstance = (TreeNode)node.getType().newInstance();
        TreeNodeOperator operator = this.operatorFactory.getOperator( newInstance.getMetaType() );
        operator.purge( guid );
    }
    public Object queryEntityHandleByNS( String path, String szBadSep, String szTargetSep ) {
        if( szTargetSep != null ) {
            path = path.replace( szBadSep, szTargetSep );
        }

        String[] parts = this.pathResolver.segmentPathParts( path );
        return this.reparsePointSelector.search( parts );
    }
    public Object queryEntityHandle( String path ) {
        return this.queryEntityHandleByNS( path, null, null );
    }

    @Override
    public void affirmOwnedNode(GUID parentGuid, GUID childGuid) {
        this.distributedTrieTree.affirmOwnedNode( childGuid, parentGuid );
    }

    @Override
    public void newHardLink( GUID sourceGuid, GUID targetGuid ) {
        this.distributedTrieTree.newHardLink( sourceGuid, targetGuid );
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
    public void remove(String path) {
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
    public void removeReparseLink(GUID guid) {
        this.distributedTrieTree.removeReparseLink( guid );
    }

    @Override
    public void newLinkTag(String originalPath, String dirPath, String tagName) {
        GUID originalGuid           = this.queryGUIDByPath( originalPath );
        GUID dirGuid                = this.queryGUIDByPath( dirPath );

        if( this.distributedTrieTree.getOriginalGuid( tagName, dirGuid ) == null ) {
            this.distributedTrieTree.newLinkTag( originalGuid, dirGuid, tagName, this );
        }
    }

    @Override
    public List<TreeNode > getChildren( GUID guid ) {
        List<GUIDDistributedTrieNode > childNodes = this.distributedTrieTree.getChildren( guid );
        ArrayList<TreeNode > nodes = new ArrayList<>();
        for( GUIDDistributedTrieNode node : childNodes ){
            TreeNode treeNode =  this.get(node.getGuid());
            nodes.add( treeNode );
        }
        return nodes;
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

    @Override
    public List<TreeNode> listRoot() {
        List<GUID> guids = this.distributedTrieTree.listRoot();
        ArrayList<TreeNode> treeNodes = new ArrayList<>();
        for( GUID guid : guids ){
            TreeNode treeNode = this.get(guid);
            treeNodes.add(treeNode);
        }
        return treeNodes;
    }

    @Override
    public void rename( GUID guid, String name ) {
        GUIDDistributedTrieNode node = this.distributedTrieTree.getNode(guid);
        TreeNode newInstance = (TreeNode)node.getType().newInstance();
        TreeNodeOperator operator = this.operatorFactory.getOperator( newInstance.getMetaType() );
        operator.updateName( guid, name );

        this.distributedTrieTree.removeCachePath( guid );
    }

    @Override
    public EntityNode queryNode(String path ) {
        return this.queryNodeByNS( path, null, null );
    }



    @Override
    public GUID queryGUIDByFN(String fullName) {
        return this.queryGUIDByNS(
                fullName, this.kernelObjectConfig.getFullNameSeparator(), this.kernelObjectConfig.getPathNameSeparator()
        );
    }

    private String getNodeName( DistributedTreeNode node ){
        UOI type = node.getType();
        TreeNode newInstance = (TreeNode)type.newInstance();
        TreeNodeOperator operator = this.operatorFactory.getOperator(newInstance.getMetaType());
        TreeNode treeNode = operator.get(node.getGuid());
        return treeNode.getName();
    }


    private boolean allNonNull( List<?> list ) {
        return list.stream().noneMatch( Objects::isNull );
    }

    @Override
    public GuidAllocator getGuidAllocator() {
        return null;
    }

    @Override
    public DistributedTrieTree getMasterTrieTree() {
        return null;
    }
}
