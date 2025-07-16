package com.pinecone.hydra.storage.file;

import com.pinecone.framework.system.Nullable;
import com.pinecone.framework.system.executum.Processum;
import com.pinecone.framework.util.StringUtils;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.framework.util.uoi.UOI;
import com.pinecone.hydra.storage.StorageConstants;
import com.pinecone.hydra.storage.file.cache.DefaultCacheConstants;
import com.pinecone.hydra.storage.file.external.ExternalFileSystemInstrument;
import com.pinecone.hydra.storage.file.external.KenExternalFileSystemInstrument;
import com.pinecone.hydra.storage.file.entity.Cluster;
import com.pinecone.hydra.storage.file.entity.ClusterPage;
import com.pinecone.hydra.storage.file.entity.ClusterPage64;
import com.pinecone.hydra.storage.file.entity.FSNodeAllotment;
import com.pinecone.hydra.storage.file.entity.GenericFSNodeAllotment;
import com.pinecone.hydra.storage.file.entity.FileNode;
import com.pinecone.hydra.storage.file.entity.FileTreeNode;
import com.pinecone.hydra.storage.file.entity.Folder;
import com.pinecone.hydra.storage.file.entity.GenericFileNode;
import com.pinecone.hydra.storage.file.entity.GenericFolder;
import com.pinecone.hydra.storage.file.entity.LocalCluster;
import com.pinecone.hydra.storage.file.entity.RemoteCluster;
import com.pinecone.hydra.storage.file.operator.FileSystemOperator;
import com.pinecone.hydra.storage.file.operator.FileSystemOperatorFactory;
import com.pinecone.hydra.storage.file.operator.GenericFileSystemOperatorFactory;
import com.pinecone.hydra.storage.file.source.FileSystemAttributeManipulator;
import com.pinecone.hydra.storage.file.source.FileManipulator;
import com.pinecone.hydra.storage.file.source.FileMasterManipulator;
import com.pinecone.hydra.storage.file.source.FileMetaManipulator;
import com.pinecone.hydra.storage.file.source.FolderManipulator;
import com.pinecone.hydra.storage.file.source.FolderMetaManipulator;
import com.pinecone.hydra.storage.file.source.FolderVolumeMappingManipulator;
import com.pinecone.hydra.storage.file.source.LocalClusterManipulator;
import com.pinecone.hydra.storage.file.source.RemoteClusterManipulator;
import com.pinecone.hydra.storage.file.source.SymbolicManipulator;
import com.pinecone.hydra.storage.file.source.SymbolicMetaManipulator;
import com.pinecone.hydra.storage.file.entity.ElementNode;
import com.pinecone.hydra.storage.file.transmit.exporter.FileExportEntity;
import com.pinecone.hydra.storage.file.transmit.exporter.TitanFileExportEntity64;
import com.pinecone.hydra.storage.file.transmit.receiver.FileReceiveEntity;
import com.pinecone.hydra.storage.file.transmit.receiver.TitanFileReceiveEntity64;
import com.pinecone.hydra.storage.io.TitanFileChannelChanface;
import com.pinecone.hydra.storage.io.TitanOutputStreamChanface;
import com.pinecone.hydra.storage.volume.VolumeManager;
import com.pinecone.hydra.system.identifier.KOPathResolver;
import com.pinecone.hydra.system.ko.dao.GUIDNameManipulator;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.system.ko.driver.KOIMasterManipulator;
import com.pinecone.hydra.system.ko.kom.ArchReparseKOMTree;
import com.pinecone.hydra.system.ko.kom.GenericReparseKOMTreeAddition;
import com.pinecone.hydra.system.ko.kom.StandardPathSelector;
import com.pinecone.hydra.unit.imperium.ImperialTreeNode;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;
import com.pinecone.hydra.unit.imperium.operator.TreeNodeOperator;
import com.pinecone.slime.map.indexable.IndexableMapQuerier;
import com.pinecone.ulf.util.guid.GUIDs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.TreeMap;

/**
 *  Pinecone Ursus For Java UniformObjectFileSystem
 *  Author: Ken, Harold.E (Dragon King)
 *  Copyright © 2008 - 2028 Bean Nuts Foundation All rights reserved.
 *  *****************************************************************************************
 *  Uniform Object File System (Ken`s OFS / KOFS)
 *  Uniform Distribute Object Storage File System
 *  Supported TB-PB-ZB Level Big Data Storage
 *
 *  *****************************************************************************************
 */
public class UniformObjectFileSystem extends ArchReparseKOMTree implements KOMFileSystem {
    protected FSNodeAllotment                         fsNodeAllotment;

    protected FileSystemAttributeManipulator          fileSystemAttributeManipulator;
    protected FileManipulator                         fileManipulator;
    protected FileMasterManipulator                   fileMasterManipulator;
    protected FileMetaManipulator                     fileMetaManipulator;
    protected FolderManipulator                       folderManipulator;
    protected FolderMetaManipulator                   folderMetaManipulator;
    protected LocalClusterManipulator                 localClusterManipulator;
    protected RemoteClusterManipulator                remoteClusterManipulator;
    protected SymbolicManipulator                     symbolicManipulator;
    protected SymbolicMetaManipulator                 symbolicMetaManipulator;
    protected FolderVolumeMappingManipulator          folderVolumeMappingManipulator;

    protected IndexableMapQuerier<String, String >    globalPathGuidCacheQuerier;

    protected ExternalFileSystemInstrument directFileSystemAccessor;


    public UniformObjectFileSystem(
            Processum superiorProcess, KOIMasterManipulator masterManipulator, KOMFileSystem parent,
            String name, IndexableMapQuerier<String, String > globalPathGuidCacheQuerier,
            FileSystemConfig fileSystemConfig, @Nullable GuidAllocator guidAllocator
    ){
        // Phase [1] Construct system.
        super( superiorProcess, masterManipulator, fileSystemConfig, parent, name, guidAllocator );

        // Phase [2] Construct fundamentals.
        this.fileMasterManipulator         = (FileMasterManipulator) masterManipulator;
        this.pathResolver                  =  new KOPathResolver( this.kernelObjectConfig );

        // Phase [3] Construct manipulators.
        this.operatorFactory                 =  new GenericFileSystemOperatorFactory( this, (FileMasterManipulator) masterManipulator );
        this.fileSystemAttributeManipulator  =  this.fileMasterManipulator.getAttributeManipulator();
        this.fileManipulator                 =  this.fileMasterManipulator.getFileManipulator();
        this.fileMetaManipulator             =  this.fileMasterManipulator.getFileMetaManipulator();
        this.folderManipulator               =  this.fileMasterManipulator.getFolderManipulator();
        this.folderMetaManipulator           =  this.fileMasterManipulator.getFolderMetaManipulator();
        this.localClusterManipulator         =  this.fileMasterManipulator.getLocalClusterManipulator();
        this.remoteClusterManipulator        =  this.fileMasterManipulator.getRemoteClusterManipulator();
        this.symbolicManipulator             =  this.fileMasterManipulator.getSymbolicManipulator();
        this.symbolicMetaManipulator         =  this.fileMasterManipulator.getSymbolicMetaManipulator();
        this.folderVolumeMappingManipulator  =  this.fileMasterManipulator.getFolderVolumeRelationManipulator();

        // Phase [4] Construct selectors.
        this.pathSelector                    =  new StandardPathSelector(
                this.pathResolver, this.imperialTree, this.folderManipulator, new GUIDNameManipulator[] { this.fileManipulator }
        );
        // Warning: ReparseKOMTreeAddition must be constructed only after `pathSelector` has been constructed.
        this.mReparseKOM                     =  new GenericReparseKOMTreeAddition( this );

        // Phase [5] Construct misc.
//        this.propertyTypeConverter         =  new DefaultPropertyConverter();
//        this.textValueTypeConverter        =  new DefaultTextValueConverter();
        this.fsNodeAllotment                 =  new GenericFSNodeAllotment(this.fileMasterManipulator,this);
        this.globalPathGuidCacheQuerier      =  globalPathGuidCacheQuerier;

        this.directFileSystemAccessor = new KenExternalFileSystemInstrument(this);
    }

//    public GenericKOMFileSystem( Hydrogen hydrogen ) {
//        this.hydrogen = hydrogen;
//    }

    public UniformObjectFileSystem( Processum superiorProcess, KOIMasterManipulator masterManipulator, KOMFileSystem parent, String name,FileSystemConfig fileSystemConfig ) {
        this( superiorProcess, masterManipulator, parent, name, null,fileSystemConfig, null );
    }

    public UniformObjectFileSystem( Processum superiorProcess, KOIMasterManipulator masterManipulator, FileSystemConfig fileSystemConfig ){
        this( superiorProcess, masterManipulator, null, KOMFileSystem.class.getSimpleName(),fileSystemConfig );
    }

    public UniformObjectFileSystem( Processum superiorProcess, KOIMasterManipulator masterManipulator, IndexableMapQuerier<String, String > globalPathGuidCacheQuerier, FileSystemConfig fileSystemConfig  ){
        this( superiorProcess, masterManipulator, null, KOMFileSystem.class.getSimpleName(), globalPathGuidCacheQuerier,fileSystemConfig, null );
    }

    public UniformObjectFileSystem( KOIMappingDriver driver, KOMFileSystem parent, String name, FileSystemConfig fileSystemConfig ) {
        this(
                driver.getSuperiorProcess(),
                driver.getMasterManipulator(),
                parent,
                name,
                fileSystemConfig
        );
    }

    public UniformObjectFileSystem( KOIMappingDriver driver,FileSystemConfig fileSystemConfig ) {
        this(
                driver.getSuperiorProcess(),
                driver.getMasterManipulator(),
                fileSystemConfig
        );
    }

    public UniformObjectFileSystem( KOIMappingDriver driver, IndexableMapQuerier<String, String > globalPathGuidCacheQuerier, FileSystemConfig fileSystemConfig  ) {
        this(
                driver.getSuperiorProcess(),
                driver.getMasterManipulator(),
                globalPathGuidCacheQuerier,
                fileSystemConfig
        );
    }



    protected void apply( IndexableMapQuerier<String, String > globalPathGuidCacheQuerier ) {
        this.globalPathGuidCacheQuerier = globalPathGuidCacheQuerier;
    }


    @Override
    public FileTreeNode get(GUID guid, int depth ) {
        return (FileTreeNode) super.get( guid, depth );
    }

    @Override
    public FileMasterManipulator getFileMasterManipulator() {
        return this.fileMasterManipulator;
    }

    @Override
    public FileTreeNode get( GUID guid ) {
        return (FileTreeNode) super.get( guid );
    }

    @Override
    public void update(FileTreeNode node) {
        TreeNodeOperator operator = this.operatorFactory.getOperator(node.getMetaType());
        operator.update( node );
    }

    @Override
    public FileTreeNode getAsRootDepth( GUID guid ) {
        return (FileTreeNode) super.getAsRootDepth( guid );
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public List<FileTreeNode > fetchRoot() {
        return (List) super.fetchRoot();
    }



    @Override
    public FileSystemConfig getConfig() {
        return (FileSystemConfig) super.getConfig();
    }

    public FileSystemOperatorFactory getOperatorFactory() {
        return (FileSystemOperatorFactory) this.operatorFactory;
    }

    @Override
    public FileNode getFileNode(GUID guid) {
        return ( FileNode ) this.get( guid );
    }

    @Override
    public Folder getFolder(GUID guid) {
        return ( Folder ) this.get( guid );
    }

    @Override
    public void remove(String path) {
        String key = DefaultCacheConstants.FilePathCacheNS + path;
        this.globalPathGuidCacheQuerier.erase(key);
        super.remove(path);
    }

    @Override
    public void remove( GUID guid ){
        super.remove( guid );
        this.remoteClusterManipulator.remove( guid );
        this.localClusterManipulator.remove( guid );
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

    protected FileTreeNode affirmTreeNodeByPath( String path, Class<? > cnSup, Class<? > nsSup ) {
        String[] parts = this.pathResolver.segmentPathParts( path );
        String currentPath = "";
        GUID parentGuid = GUIDs.Dummy128();

        FileTreeNode node = this.queryElement( path );
        if( node != null ) {
            return node;
        }

        FileTreeNode ret = null;
        for( int i = 0; i < parts.length; ++i ){
            currentPath = currentPath + ( i > 0 ? this.getConfig().getPathNameSeparator() : "" ) + parts[ i ];
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
        FileNode fileNode = (FileNode) this.affirmTreeNodeByPath(path, GenericFileNode.class, GenericFolder.class);
        this.initVolume( path );
        return fileNode;
    }

    @Override
    public Folder affirmFolder(String path) {
        Folder folder = (Folder) this.affirmTreeNodeByPath(path, null, GenericFolder.class);
        this.initVolume( path );
        return folder;
    }

    @Override
    public void setDataAffinityGuid( GUID childGuid, GUID affinityParentGuid ) {

    }


    @Override
    public GUID queryGUIDByPath( String path ) {
        FileSystemConfig config = this.getConfig();
        if ( this.globalPathGuidCacheQuerier != null ) {
            String key = DefaultCacheConstants.FilePathCacheNS + path;
            String szGUID = this.globalPathGuidCacheQuerier.get( key );
            if ( StringUtils.isNoneEmpty( szGUID ) ) {
                return GUIDs.GUID128( szGUID );
            }
        }
        GUID guid =  super.queryGUIDByPath( path ); // Into OLTP-RDB
        if ( this.globalPathGuidCacheQuerier != null ) {
            String key = DefaultCacheConstants.FilePathCacheNS + path;
            this.globalPathGuidCacheQuerier.insert( key, guid.toString(), config.getPathQueryExpiryTimeHotMil() );
        }
        return guid;
    }

    @Override
    public ElementNode queryElement(String path) {
        GUID guid = this.queryGUIDByPath( path );
        if( guid != null ) {
            return (ElementNode) this.get( guid );
        }
        return this.directFileSystemAccessor.queryElement(path);
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

        this.imperialTree.moveTo( sourceGuid, destinationGuid );
        this.imperialTree.removeCachePath( sourceGuid );
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
                        ( sourcePath.endsWith( this.getConfig().getPathNameSeparator() ) && destinationPath.endsWith( this.getConfig().getPathNameSeparator() ) )
        ) {
            destParts.remove( destParts.size() - 1 );
            String szParentPath = this.pathResolver.assemblePath( destParts );
            destParts.add( szLastDestTarget );

            // Move to, which has the same name or explicit current dir `.`.
            this.moveTo( sourcePath, szParentPath );
        }
        // Case 2: "game/terraria/npc" => "game/minecraft/character/" || "game/minecraft/character/."
        //    game/terraria/npc => game/minecraft/character/npc
        else if ( !sourcePath.endsWith( this.getConfig().getPathNameSeparator() ) && (
                destinationPath.endsWith( this.getConfig().getPathNameSeparator() ) || destinationPath.endsWith( "." ) )
        ) {
            Folder target = this.affirmFolder( destinationPath );
            this.imperialTree.moveTo( sourceGuid, target.getGuid() );
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
                this.imperialTree.moveTo( sourceGuid, target.getGuid() );
            }
            else {
                List<TreeNode > children = this.getChildren( sourceGuid );
                if( !children.isEmpty() ) {
                    Folder target = this.affirmFolder( destinationPath );
                    for( TreeNode node : children ) {
                        this.imperialTree.moveTo( node.getGuid(), target.getGuid() );
                    }
                }
            }

            this.imperialTree.removeTreeNodeOnly( sourceGuid );
        }

        this.imperialTree.removeCachePath( sourceGuid );
    }

    @Override
    public void copy(String sourcePath, String destinationPath, VolumeManager volumeManager) throws  IOException {
        ElementNode elementNode = this.queryElement(destinationPath);
        this.copy(sourcePath,elementNode,volumeManager);
    }

    @Override
    public void directCopy(String sourcePath, String destinationPath) throws IOException {
        this.directFileSystemAccessor.copy( sourcePath,destinationPath );
    }

    private void copy(String sourcePath, FileTreeNode fileTreeNode, VolumeManager volumeManager ) throws IOException {
        if( fileTreeNode instanceof Folder ){
            List<TreeNode> children = this.getChildren(fileTreeNode.getGuid());
            for(TreeNode child : children){
                FileTreeNode childFileTreeNode = this.get(child.getGuid());
                this.copy(sourcePath + StorageConstants.PathSeparator + fileTreeNode.getName(), childFileTreeNode,volumeManager);
            }
        }
        else {
            String name = fileTreeNode.getName();
            String[] split = name.split(StorageConstants.period);
//            File tempFile = File.createTempFile(split[0], StorageConstants.PathSeparator + split[1]);
            File tempFile = new File(this.getConfig().getDefaultTempFilePath()+name);
            if(!tempFile.createNewFile()){
                throw new IOException( "Creating file compromised, what :" + tempFile.toPath() );
            }
            FileOutputStream fileOutputStream = new FileOutputStream(tempFile);
            TitanOutputStreamChanface outputStreamChanface = new TitanOutputStreamChanface(fileOutputStream);
            TitanFileExportEntity64 exportEntity64 = new TitanFileExportEntity64(this, volumeManager,
                    (FileNode) fileTreeNode, outputStreamChanface);
            exportEntity64.export();

            FileNode fileNode = this.fsNodeAllotment.newFileNode();
            FileChannel channel = FileChannel.open(tempFile.toPath(), StandardOpenOption.READ);
            TitanFileChannelChanface titanFileChannelKChannel = new TitanFileChannelChanface( channel );
            fileNode.setDefinitionSize( tempFile.length() );
            fileNode.setName( tempFile.getName() );
            String destDirPath = sourcePath + StorageConstants.PathSeparator + name;
            TitanFileReceiveEntity64 receiveEntity64 = new TitanFileReceiveEntity64(this, destDirPath,
                    fileNode, titanFileChannelKChannel, volumeManager);
            this.receive( receiveEntity64 );

            tempFile.delete();
            fileOutputStream.close();
            channel.close();
        }
    }

    @Override
    public Object querySelectorJ( String szSelector ) {
        return null;
    }


    @Override
    public TreeMap<Long, Cluster> getClustersByFileGuid( GUID guid ) {
        TreeMap< Long, Cluster> frameMap = new TreeMap<>();

        List<RemoteCluster> remoteClusters = this.remoteClusterManipulator.fetchRemoteClusterByFileGuid( guid );
        for( RemoteCluster remoteCluster : remoteClusters ){
            if( remoteCluster.getDeviceGuid().equals( this.getConfig().getLocalHostGuid() )){
                LocalCluster localCluster = this.localClusterManipulator.getLocalClusterByGuid( remoteCluster.getSegGuid() );
                frameMap.put( localCluster.getSegId(), localCluster );
            }
            else {
                //todo 远程获取逻辑
            }
        }

        return frameMap;
    }


    @Override
    public List<RemoteCluster > fetchClustersPageByFileGuid( GUID fileGuid, long offset, int pageSize ) {
        return this.remoteClusterManipulator.fetchRemoteClusterByFileGuid( fileGuid, offset, pageSize );
    }

    @Override
    public ClusterPage fetchClustersByFileGuid( GUID fileGuid, int pageSize ) {
        return new ClusterPage64(this, this.remoteClusterManipulator, this.localClusterManipulator, fileGuid, pageSize );
    }

    @Override
    public ClusterPage fetchClustersByFileGuid( GUID fileGuid ) {
        return new ClusterPage64( this,this.remoteClusterManipulator, this.localClusterManipulator, fileGuid );
    }


    @Override
    public FSNodeAllotment getFSNodeAllotment() {
        return this.fsNodeAllotment;
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
    public Cluster getLastCluster(GUID guid) {
        RemoteCluster remoteCluster = this.remoteClusterManipulator.getLastCluster(guid);
        if ( remoteCluster.getDeviceGuid().equals( this.getConfig().getLocalHostGuid() )){
            return this.localClusterManipulator.getLocalClusterByGuid(remoteCluster.getSegGuid());
        }
        else {
            //todo 远端获取方法
        }
        return null;
    }

    private String getNodeName(ImperialTreeNode node ){
        UOI type = node.getType();
        TreeNode newInstance = (TreeNode)type.newInstance();
        TreeNodeOperator operator = this.getOperatorFactory().getOperator(newInstance.getMetaType());
        TreeNode treeNode = operator.get(node.getGuid());
        return treeNode.getName();
    }

    private boolean allNonNull( List<?> list ) {
        return list.stream().noneMatch( Objects::isNull );
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

    @Override
    public void receive( FileReceiveEntity entity) throws IOException {
        entity.receive();
    }

    @Override
    public void receive( FileReceiveEntity entity, Number offset, Number endSize) throws IOException {
        entity.receive(offset, endSize );
    }

    @Override
    public void randomReceive(FileReceiveEntity entity, Number offset, Number endSize) throws  IOException {
        entity.randomReceive( offset,endSize );
    }

    @Override
    public void export( FileExportEntity entity ) throws  IOException {
        entity.export();
    }

    @Override
    public void export( FileExportEntity entity, Number offset, Number endSize ) {

    }

    @Override
    public void setFolderVolumeMapping(GUID folderGuid, GUID volumeGuid) {
        this.folderVolumeMappingManipulator.insert( folderGuid, volumeGuid );
    }

    @Override
    public GUID getMappingVolume(GUID folderGuid) {
        return this.folderVolumeMappingManipulator.getVolumeGuid( folderGuid );
    }

    @Override
    public GUID getMappingVolume(String path) {
        String[] parts = this.pathResolver.segmentPathParts( path );
        GUID currentVolumeGuid = null;
        String currentPath = "";
        for( int i = 0; i < parts.length - 1; i++ ){
            currentPath = currentPath + ( i > 0 ? this.getConfig().getPathNameSeparator() : "" ) + parts[ i ];
            ElementNode elementNode = this.queryElement(currentPath);
            Folder folder = this.getFolder(elementNode.getGuid());
            GUID relationVolume = folder.getRelationVolume();
            if ( relationVolume != null ){
                currentVolumeGuid = relationVolume;
            }
        }
        return currentVolumeGuid;
    }

    @Override
    public Cluster getClusterByFileWithId(GUID fileGuid, long segId) {
        return this.localClusterManipulator.getClusterByFileWithId( fileGuid,segId );
    }

    @Override
    public void updateCluster(FileNode fileNode, long segId) {

    }

    @Override
    public void deleteCluster(FileNode fileNode, long segId) {
        this.remoteClusterManipulator.removeClusterByFileWithId( fileNode.getGuid(), segId );
        this.localClusterManipulator.removeClusterByFileWithId( fileNode.getGuid(), segId );
    }

    @Override
    public long countFileCluster(GUID fileGuid) {
        return this.remoteClusterManipulator.countFileClusters( fileGuid );
    }

    @Override
    public void renameFile(String filePath, String newFileName) {
        ElementNode elementNode = this.queryElement(filePath);
        elementNode.setName( newFileName );

        FileSystemOperator operator = (FileSystemOperator)this.operatorFactory.getOperator(elementNode.getMetaType());
        operator.rename( elementNode.getGuid(), newFileName );
    }

    private void initVolume(String path ){
        String[] parts = this.pathResolver.segmentPathParts( path );
        Folder root = this.getFolder(this.queryGUIDByPath(parts[0]));
        if( root.getRelationVolume() == null ){
            root.applyVolume( GUIDs.GUID128( this.getConfig().getDefaultVolumeGuid() ) );
        }
    }
}
