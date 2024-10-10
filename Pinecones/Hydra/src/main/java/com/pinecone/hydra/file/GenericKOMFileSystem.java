package com.pinecone.hydra.file;

import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.lang.DynamicFactory;
import com.pinecone.framework.util.lang.GenericDynamicFactory;
import com.pinecone.framework.util.name.path.PathResolver;
import com.pinecone.framework.util.uoi.UOI;
import com.pinecone.hydra.file.creator.FileSystemCreator;
import com.pinecone.hydra.file.creator.GenericFileSystemCreator;
import com.pinecone.hydra.file.entity.FileNode;
import com.pinecone.hydra.file.entity.FileTreeNode;
import com.pinecone.hydra.file.entity.Folder;
import com.pinecone.hydra.file.entity.Frame;
import com.pinecone.hydra.file.entity.GenericFileNode;
import com.pinecone.hydra.file.entity.GenericFolder;
import com.pinecone.hydra.file.entity.LocalFrame;
import com.pinecone.hydra.file.entity.RemoteFrame;
import com.pinecone.hydra.file.entity.Symbolic;
import com.pinecone.hydra.file.operator.FileSystemOperator;
import com.pinecone.hydra.file.operator.FileSystemOperatorFactory;
import com.pinecone.hydra.file.operator.GenericFileSystemOperatorFactory;
import com.pinecone.hydra.file.source.FileSystemAttributeManipulator;
import com.pinecone.hydra.file.source.FileManipulator;
import com.pinecone.hydra.file.source.FileMasterManipulator;
import com.pinecone.hydra.file.source.FileMetaManipulator;
import com.pinecone.hydra.file.source.FolderManipulator;
import com.pinecone.hydra.file.source.FolderMetaManipulator;
import com.pinecone.hydra.file.source.LocalFrameManipulator;
import com.pinecone.hydra.file.source.RemoteFrameManipulator;
import com.pinecone.hydra.file.source.SymbolicManipulator;
import com.pinecone.hydra.file.source.SymbolicMetaManipulator;
import com.pinecone.hydra.registry.ReparseLinkSelector;
import com.pinecone.hydra.registry.StandardPathSelector;
import com.pinecone.hydra.file.entity.ElementNode;
import com.pinecone.hydra.registry.entity.ConfigNode;
import com.pinecone.hydra.registry.entity.Namespace;
import com.pinecone.hydra.registry.entity.RegistryTreeNode;
import com.pinecone.hydra.system.Hydrarum;
import com.pinecone.hydra.system.identifier.KOPathResolver;
import com.pinecone.hydra.system.ko.dao.GUIDNameManipulator;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.system.ko.driver.KOIMasterManipulator;
import com.pinecone.hydra.system.ko.driver.KOISkeletonMasterManipulator;
import com.pinecone.hydra.system.ko.kom.PathSelector;
import com.pinecone.hydra.system.ko.kom.ReparsePointSelector;
import com.pinecone.hydra.unit.udtt.ArchKOMTree;
import com.pinecone.hydra.unit.udtt.DistributedTreeNode;
import com.pinecone.hydra.unit.udtt.DistributedTrieTree;
import com.pinecone.hydra.unit.udtt.GUIDDistributedTrieNode;
import com.pinecone.hydra.unit.udtt.GenericDistributedTrieTree;
import com.pinecone.hydra.unit.udtt.KOMTree;
import com.pinecone.hydra.unit.udtt.entity.EntityNode;
import com.pinecone.hydra.unit.udtt.entity.ReparseLinkNode;
import com.pinecone.hydra.unit.udtt.entity.TreeNode;
import com.pinecone.hydra.unit.udtt.operator.TreeNodeOperator;
import com.pinecone.hydra.unit.udtt.source.TreeMasterManipulator;
import com.pinecone.ulf.util.id.GUIDs;
import com.pinecone.ulf.util.id.GuidAllocator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class GenericKOMFileSystem implements KOMFileSystem {
    //显式继承ArchKOMTree
    protected KOMTree                   komTree;
    protected Hydrarum                  hydrarum;
    protected DistributedTrieTree       distributedTrieTree;
    protected FileSystemCreator         fileSystemCreator;

    protected FileSystemConfig          fileSystemConfig;
    protected DynamicFactory            dynamicFactory;
    protected PathResolver              pathResolver;
    protected PathSelector              pathSelector;
    protected ReparsePointSelector      reparsePointSelector;
    protected FileSystemAttributeManipulator fileSystemAttributeManipulator;
    protected FileManipulator           fileManipulator;
    protected FileMasterManipulator     fileMasterManipulator;
    protected FileMetaManipulator       fileMetaManipulator;
    protected FolderManipulator         folderManipulator;
    protected FolderMetaManipulator     folderMetaManipulator;
    protected LocalFrameManipulator     localFrameManipulator;
    protected RemoteFrameManipulator    remoteFrameManipulator;
    protected SymbolicManipulator       symbolicManipulator;
    protected SymbolicMetaManipulator   symbolicMetaManipulator;
    protected FileSystemOperatorFactory fileSystemOperatorFactory;
    protected GuidAllocator                   guidAllocator;

    public GenericKOMFileSystem(Hydrarum hydrarum, KOIMasterManipulator masterManipulator){
        this.hydrarum                   =  hydrarum;
        this.fileMasterManipulator      =  (FileMasterManipulator) masterManipulator;
        this.dynamicFactory                =  new GenericDynamicFactory( hydrarum.getTaskManager().getClassLoader() );
        this.fileSystemConfig           =   KOMFileSystem.KernelFileSystemConfig;
        this.pathResolver               =   new KOPathResolver( this.fileSystemConfig );


        KOISkeletonMasterManipulator skeletonMasterManipulator = this.fileMasterManipulator.getSkeletonMasterManipulator();
        TreeMasterManipulator treeMasterManipulator     = (TreeMasterManipulator) skeletonMasterManipulator;

        this.distributedTrieTree           =  new GenericDistributedTrieTree( treeMasterManipulator );
        this.fileSystemAttributeManipulator =  fileMasterManipulator.getAttributeManipulator();
        this.fileManipulator               =  fileMasterManipulator.getFileManipulator();
        this.fileMetaManipulator           =  fileMasterManipulator.getFileMetaManipulator();
        this.folderManipulator             =  fileMasterManipulator.getFolderManipulator();
        this.folderMetaManipulator         =  fileMasterManipulator.getFolderMetaManipulator();
        this.localFrameManipulator         =  fileMasterManipulator.getLocalFrameManipulator();
        this.remoteFrameManipulator        =  fileMasterManipulator.getRemoteFrameManipulator();
        this.symbolicManipulator           =  fileMasterManipulator.getSymbolicManipulator();
        this.symbolicMetaManipulator       =  fileMasterManipulator.getSymbolicMetaManipulator();
        this.fileSystemOperatorFactory     =  new GenericFileSystemOperatorFactory(this,this.fileMasterManipulator);
        this.guidAllocator                 = GUIDs.newGuidAllocator();
        this.guidAllocator                 =  GUIDs.newGuidAllocator();
        this.pathSelector                  =  new StandardPathSelector(
                this.pathResolver, this.distributedTrieTree, this.folderManipulator, new GUIDNameManipulator[] { this.fileManipulator }
        );
        this.reparsePointSelector          =  new ReparseLinkSelector( (StandardPathSelector) this.pathSelector );
        this.fileSystemCreator             =  new GenericFileSystemCreator(this.fileMasterManipulator,this);
        this.komTree                       =  new ArchKOMTree(this.fileMasterManipulator,this.fileSystemOperatorFactory,this.fileSystemConfig,this.pathSelector,this.hydrarum);
    }

    public GenericKOMFileSystem( Hydrarum hydrarum ) {
        this.hydrarum = hydrarum;
    }

    public GenericKOMFileSystem( KOIMappingDriver driver ) {
        this(
                driver.getSystem(),
                driver.getMasterManipulator()
        );
    }


    @Override
    public String getPath(GUID guid) {
        return this.komTree.getPath(guid);
    }

    @Override
    public String getFullName(GUID guid) {
        return this.komTree.getFullName(guid);
    }

    @Override
    public GUID put(TreeNode treeNode) {
        //return this.komTree.put( treeNode );
        FileSystemOperator operator = this.fileSystemOperatorFactory.getOperator(treeNode.getMetaType());
        return operator.insert(treeNode);
    }
    protected FileSystemOperator getOperatorByGuid(GUID guid ) {
        DistributedTreeNode node = this.distributedTrieTree.getNode( guid );
        TreeNode newInstance = (TreeNode)node.getType().newInstance( new Class<? >[]{ KOMFileSystem.class }, this );
        return this.fileSystemOperatorFactory.getOperator( newInstance.getMetaType() );
    }
    @Override
    public FileTreeNode get(GUID guid) {
        //return (FileTreeNode) this.komTree.get(guid);
        return this.getOperatorByGuid(guid).get(guid);
    }

    @Override
    public FileTreeNode get(GUID guid, int depth) {
//        return (FileTreeNode) this.komTree.get( guid, depth );
        return this.getOperatorByGuid( guid ).get( guid, depth );
    }

    @Override
    public FileTreeNode getSelf(GUID guid) {
       //return (FileTreeNode) this.komTree.getSelf( guid );
        return this.getOperatorByGuid( guid ).getSelf( guid );
    }

    @Override
    public FileNode getFileNode(GUID guid) {
        return ( FileNode ) this.get( guid );
    }

    @Override
    public Folder getFolder(GUID guid) {
        return ( Folder ) this.get( guid );
    }
    /** Final Solution 20240929: 无法获取类型 */
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
        //return this.komTree.queryGUIDByNS(path,szBadSep,szTargetSep);
    }

    @Override
    public GUID queryGUIDByPath(String path) {
        return this.queryGUIDByNS( path, null, null );
    }

    @Override
    public GUID queryGUIDByFN(String fullName) {
        return this.queryGUIDByNS(
                fullName, this.fileSystemConfig.getFullNameSeparator(), this.fileSystemConfig.getPathNameSeparator()
        );
    }

    @Override
    public void remove(GUID guid) {
        GUIDDistributedTrieNode node = this.distributedTrieTree.getNode( guid );
        TreeNode newInstance = (TreeNode)node.getType().newInstance();
        TreeNodeOperator operator = this.fileSystemOperatorFactory.getOperator( newInstance.getMetaType() );
        operator.purge( guid );
        //this.komTree.remove( guid );
    }

    @Override
    public void removeReparseLink(GUID guid) {
        this.distributedTrieTree.removeReparseLink( guid );
    }

    @Override
    public void removeFileNode(GUID guid) {

    }

    @Override
    public void removeFolder(GUID guid) {

    }

    @Override
    public List<TreeNode> getChildren(GUID guid) {
        List<GUIDDistributedTrieNode > childNodes = this.distributedTrieTree.getChildren( guid );
        ArrayList<TreeNode > configNodes = new ArrayList<>();
        for( GUIDDistributedTrieNode node : childNodes ){
            TreeNode treeNode =  this.get(node.getGuid());
            configNodes.add( treeNode );
        }
        return configNodes;
        //return this.komTree.getChildren( guid );
    }

    @Override
    public void rename(GUID guid, String name) {
        GUIDDistributedTrieNode node = this.distributedTrieTree.getNode(guid);
        TreeNode newInstance = (TreeNode)node.getType().newInstance();
        TreeNodeOperator operator = this.fileSystemOperatorFactory.getOperator( newInstance.getMetaType() );
        operator.updateName( guid, name );

        this.distributedTrieTree.removeCachePath( guid );
        //this.komTree.rename(guid,name);
    }

    @Override
    public List<TreeNode> getAllTreeNode() {
        List<GUID> nameSpaceNodes = this.fileManipulator.dumpGuid();
        List<GUID> confNodes      = this.folderManipulator.dumpGuid();
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
    public void affirmOwnedNode(GUID parentGuid, GUID childGuid) {
        this.distributedTrieTree.affirmOwnedNode(childGuid,parentGuid);
    }

    protected FileTreeNode affirmTreeNodeByPath( String path, Class<? > cnSup, Class<? > nsSup ) {
        String[] parts = this.pathResolver.segmentPathParts( path );
        String currentPath = "";
        GUID parentGuid = GUIDs.Dummy72();

        FileTreeNode node = this.queryElement( path );
        if( node != null ) {
            return node;
        }

        FileTreeNode ret = null;
        for( int i = 0; i < parts.length; ++i ){
            currentPath = currentPath + ( i > 0 ? this.fileSystemConfig.getPathNameSeparator() : "" ) + parts[ i ];
            node = this.queryElement( currentPath );
            if ( node == null){
                if ( i == parts.length - 1 && cnSup != null ){
                    FileNode fileNode = (FileNode) this.dynamicFactory.optNewInstance( cnSup, new Object[]{ this } );
                    fileNode.setName( parts[i] );
                    GUID guid = this.put( fileNode );
                    this.affirmOwnedNode( parentGuid, guid );
                    return fileNode;
                }
                else {
                    Folder folder = (Folder) this.dynamicFactory.optNewInstance( nsSup, new Object[]{ this } );
                    folder.setName(parts[i]);
                    GUID guid = this.put(folder);
                    if ( i != 0 ){
                        this.affirmOwnedNode( parentGuid, guid );
                        parentGuid = guid;
                    }
                    else {
                        parentGuid = guid;
                    }

                    ret = folder;
                }
            }
            else {
                parentGuid = node.getGuid();
            }
        }

        return ret;
    }

    @Override
    public FileNode affirmFileNode(String path) {
        return ( FileNode ) this.affirmTreeNodeByPath(path,GenericFileNode.class, GenericFolder.class);
    }

    @Override
    public Folder affirmFolder(String path) {
        return ( Folder ) this.affirmTreeNodeByPath(path, null,GenericFolder.class);
    }
    @Override
    public void newHardLink(GUID sourceGuid, GUID targetGuid) {
        this.komTree.newHardLink( sourceGuid, targetGuid );
    }

    @Override
    public void setDataAffinityGuid(GUID childGuid, GUID affinityParentGuid) {

    }

    @Override
    public void newLinkTag(GUID originalGuid, GUID dirGuid, String tagName) {
        this.komTree.newLinkTag( originalGuid, dirGuid, tagName );
    }

    @Override
    public void newLinkTag(String originalPath, String dirPath, String tagName) {
        this.komTree.newLinkTag( originalPath,dirPath,tagName );
    }

    @Override
    public void updateLinkTag(GUID tagGuid, String tagName) {
        this.komTree.updateLinkTag( tagGuid,tagName );
    }



    @Override
    public ElementNode queryElement(String path) {
        GUID guid = this.queryGUIDByPath( path );
        if( guid != null ) {
            return (ElementNode) this.get( guid );
        }
        return null;
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
        //this.komTree.remove( path );
    }



    @Override
    public EntityNode queryNode(String path) {
        return this.komTree.queryNode(path);
    }

    @Override
    public ReparseLinkNode queryReparseLink(String path) {
        return this.komTree.queryReparseLink( path );
    }

    @Override
    public List<TreeNode> selectByName(String name) {
        return null;
    }

    @Override
    public void moveTo(String sourcePath, String destinationPath) {
        GUID[] pair = this.assertCopyMove( sourcePath, destinationPath );
        GUID sourceGuid      = pair[ 0 ];
        GUID destinationGuid = pair[ 1 ];

        this.distributedTrieTree.moveTo( sourceGuid, destinationGuid );
        this.distributedTrieTree.removeCachePath( sourceGuid );
    }

    @Override
    public void move(String sourcePath, String destinationPath) {
        GUID sourceGuid         = this.assertPath( sourcePath, "source" );

        List<String > sourParts = this.pathResolver.resolvePathParts( sourcePath );
        List<String > destParts = this.pathResolver.resolvePathParts( destinationPath );

        String szLastDestTarget = destParts.get( destParts.size() - 1 );
        sourcePath      = sourcePath.trim();
        destinationPath = destinationPath.trim();

        //   Case1: Move "game/terraria/npc"   => "game/minecraft/npc", which has the same dest name.
        // Case1-1: Move "game/terraria/npc/"  => "game/minecraft/npc/"
        // Case1-2: Move "game/terraria/npc/." => "game/minecraft/npc/."
        if(
                sourParts.get( sourParts.size() - 1 ).equals( szLastDestTarget ) || szLastDestTarget.equals( "." ) ||
                        ( sourcePath.endsWith( this.fileSystemConfig.getPathNameSeparator() ) && destinationPath.endsWith( this.fileSystemConfig.getPathNameSeparator() ) )
        ) {
            destParts.remove( destParts.size() - 1 );
            String szParentPath = this.pathResolver.assemblePath( destParts );
            destParts.add( szLastDestTarget );

            // Move to, which has the same name or explicit current dir `.`.
            this.moveTo( sourcePath, szParentPath );
        }
        // Case 2: "game/terraria/npc" => "game/minecraft/character/" || "game/minecraft/character/."
        //    game/terraria/npc => game/minecraft/character/npc
        else if ( !sourcePath.endsWith( this.fileSystemConfig.getPathNameSeparator() ) && (
                destinationPath.endsWith( this.fileSystemConfig.getPathNameSeparator() ) || destinationPath.endsWith( "." ) )
        ) {
            Folder target = this.affirmFolder( destinationPath );
            this.distributedTrieTree.moveTo( sourceGuid, target.getGuid() );
        }
        // Case3: Move "game/terraria/npc" => "game/minecraft/character", move all children therein.
        //    game/terraria/npc/f1 => game/minecraft/character/f1
        //    game/terraria/npc/f2 => game/minecraft/character/f2
        //    etc.
        else {
            //  Case3-1: Is config or other none namespace node.
            //           Move "game/terraria/file" => "game/minecraft/dir".
            //  Case3-2: "game/terraria/npc/" => "game/minecraft/character"
            // Eq.Case2: Move "game/terraria/npc" => "game/minecraft/character",
            if( !this.folderManipulator.isFolder( sourceGuid ) ) {
                Folder target = this.affirmFolder( destinationPath );
                this.distributedTrieTree.moveTo( sourceGuid, target.getGuid() );
            }
            else {
                List<TreeNode > children = this.getChildren( sourceGuid );
                if( !children.isEmpty() ) {
                    Folder target = this.affirmFolder( destinationPath );
                    for( TreeNode node : children ) {
                        this.distributedTrieTree.moveTo( node.getGuid(), target.getGuid() );
                    }
                }
            }

            this.distributedTrieTree.removeTreeNodeOnly( sourceGuid );
        }

        this.distributedTrieTree.removeCachePath( sourceGuid );
    }

    @Override
    public void copyTo(String sourcePath, String destinationPath) {
    }

    @Override
    public void copy(String sourcePath, String destinationPath) {

    }

    @Override
    public List<FileTreeNode> listRoot() {
        ArrayList<FileTreeNode> fileTreeNodes = new ArrayList<>();
        for ( TreeNode treeNode : this.komTree.listRoot() ){
            fileTreeNodes.add( (FileTreeNode) treeNode );
        }
        return fileTreeNodes;
    }


    @Override
    public Object querySelectorJ(String szSelector) {
        return null;
    }


    @Override
    public TreeMap<Long, Frame> getFrameByFileGuid(GUID guid) {
        TreeMap< Long, Frame > frameMap = new TreeMap<>();
        List<LocalFrame> localFrames = this.localFrameManipulator.getLocalFrameByFileGuid(guid);
        for( Frame frame : localFrames ){
            frameMap.put( frame.getSegId(),frame );
        }
        List<RemoteFrame> remoteFrames = this.remoteFrameManipulator.getRemoteFrameByFileGuid(guid);
        for ( Frame frame : remoteFrames ){
            frameMap.put( frame.getSegId(),frame );
        }

        return frameMap;
    }

    @Override
    public FileSystemCreator getFileSystemCreator() {
        return this.fileSystemCreator;
    }

    @Override
    public Object querySelector(String szSelector) {
        return null;
    }

    @Override
    public List querySelectorAll(String szSelector) {
        return null;
    }

    @Override
    public void copyFileNodeTo(GUID sourceGuid, GUID destinationGuid) {

    }

    @Override
    public void copyFolderTo(GUID sourceGuid, GUID destinationGuid) {

    }

    @Override
    public GuidAllocator getGuidAllocator() {
        return this.guidAllocator;
    }

    @Override
    public DistributedTrieTree getMasterTrieTree() {
        return this.distributedTrieTree;
    }

    private String getNodeName( DistributedTreeNode node ){
        UOI type = node.getType();
        TreeNode newInstance = (TreeNode)type.newInstance();
        TreeNodeOperator operator = this.fileSystemOperatorFactory.getOperator(newInstance.getMetaType());
        TreeNode treeNode = operator.get(node.getGuid());
        return treeNode.getName();
    }

    private boolean allNonNull( List<?> list ) {
        return list.stream().noneMatch( Objects::isNull );
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

    protected GUID[] assertCopyMove ( String sourcePath, String destinationPath ) throws IllegalArgumentException {
        GUID sourceGuid      = this.queryGUIDByPath( sourcePath );
        if( sourceGuid == null ) {
            throw new IllegalArgumentException( "Undefined source '" + sourcePath + "'" );
        }

        GUID destinationGuid = this.queryGUIDByPath( destinationPath );
        if( !this.folderManipulator.isFolder( destinationGuid ) ){
            throw new IllegalArgumentException( "Illegal destination '" + destinationPath + "', should be namespace." );
        }

        if( destinationGuid == null ) {
            throw new IllegalArgumentException( "Undefined destination '" + destinationPath + "'" );
        }

        if( sourceGuid == destinationGuid ) {
            throw new IllegalArgumentException( "Cyclic path detected '" + sourcePath + "'" );
        }

        return new GUID[] { sourceGuid, destinationGuid };
    }

}
