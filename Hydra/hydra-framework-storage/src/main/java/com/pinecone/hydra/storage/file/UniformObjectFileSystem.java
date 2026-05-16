package com.pinecone.hydra.storage.file;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.pinecone.framework.system.Nullable;
import com.pinecone.framework.system.executum.Processum;
import com.pinecone.framework.util.StringUtils;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.framework.util.uoi.UOI;

import com.pinecone.hydra.storage.bucket.*;
import com.pinecone.ulf.util.guid.GUIDs;

import com.pinecone.hydra.storage.StorageConstants;
import com.pinecone.hydra.storage.file.cache.DefaultCacheConstants;
import com.pinecone.hydra.storage.file.entity.ElementNode;
import com.pinecone.hydra.storage.file.external.ExternalFileSystemInstrument;
import com.pinecone.hydra.storage.file.external.TitanExternalFileSystemInstrument;
import com.pinecone.hydra.storage.file.entity.FSNodeAllotment;
import com.pinecone.hydra.storage.file.entity.FileNode;
import com.pinecone.hydra.storage.file.entity.FileTreeNode;
import com.pinecone.hydra.storage.file.entity.Folder;
import com.pinecone.hydra.storage.file.entity.GenericFSNodeAllotment;
import com.pinecone.hydra.storage.file.entity.GenericFileNode;
import com.pinecone.hydra.storage.file.entity.GenericFolder;
import com.pinecone.hydra.storage.file.fat.FatChunkInstrument;
import com.pinecone.hydra.storage.file.fat.TitanFatChunkInstrument;
import com.pinecone.hydra.storage.file.fat.entity.FileChunk;
import com.pinecone.hydra.storage.file.fat.entity.FileChunkLocation;
import com.pinecone.hydra.storage.file.fat.io.FatChunkStore;
import com.pinecone.hydra.storage.file.fat.io.FatFileStore;
import com.pinecone.hydra.storage.file.fat.io.LinearVolumeSpaceAllocator;
import com.pinecone.hydra.storage.file.fat.io.TitanFatChunkStore;
import com.pinecone.hydra.storage.file.fat.io.TitanFatFileStore;
import com.pinecone.hydra.storage.file.fat.io.VolumeSpaceAllocator;
import com.pinecone.hydra.storage.file.fat.service.ChunkSlice;
import com.pinecone.hydra.storage.file.journal.JournalInstrument;
import com.pinecone.hydra.storage.file.journal.JournalType;
import com.pinecone.hydra.storage.file.journal.TitanJournalInstrument;
import com.pinecone.hydra.storage.file.journal.TitanJournalRecoveryInstrument;
import com.pinecone.hydra.storage.file.operator.FileSystemOperator;
import com.pinecone.hydra.storage.file.operator.FileSystemOperatorFactory;
import com.pinecone.hydra.storage.file.operator.GenericFileSystemOperatorFactory;
import com.pinecone.hydra.storage.file.reparse.TitanUofsSymbolicPathResolver;
import com.pinecone.hydra.storage.file.reparse.UofsSymbolicPathResolver;
import com.pinecone.hydra.storage.file.reparse.UofsSymbolicResolveConfig;
import com.pinecone.hydra.storage.file.reparse.UofsSymbolicResolveResult;
import com.pinecone.hydra.storage.file.source.FileManipulator;
import com.pinecone.hydra.storage.file.source.FileMasterManipulator;
import com.pinecone.hydra.storage.file.source.FolderManipulator;
import com.pinecone.hydra.storage.file.source.FolderVolumeMappingManipulator;
import com.pinecone.hydra.storage.file.source.SymbolicManipulator;
import com.pinecone.hydra.storage.file.transmit.address.UofsAddress;
import com.pinecone.hydra.storage.file.transmit.address.UofsAddressResolver;
import com.pinecone.hydra.storage.file.transmit.address.UofsResolveContext;
import com.pinecone.hydra.storage.file.transmit.channel.TitanFileChannel;
import com.pinecone.hydra.storage.file.transmit.channel.UFileChannel;
import com.pinecone.hydra.storage.file.transmit.channel.UFileOpenOption;
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
import com.pinecone.hydra.unit.imperium.source.TreeMasterManipulator;
import com.pinecone.hydra.unit.imperium.source.TrieTreeManipulator;

import com.pinecone.slime.map.indexable.IndexableMapQuerier;

/**
 *  Pinecone Ursus For Java UniformObjectFileSystem
 *  Author: Harald.E (Dragon King), Ken
 *  Copyright © 2008 - 2028 Bean Nuts Foundation All rights reserved.
 *  *****************************************************************************************
 *  Uniform Object File System (Titan UOFS)
 *  Uniform Distribute Object Storage File System
 *  Supported TB-PB-ZB Level Big Data Storage
 *  *****************************************************************************************
 */
public class UniformObjectFileSystem extends ArchReparseKOMTree implements KOMFileSystem {
    protected FileMasterManipulator                   fileMasterManipulator;
    protected FileManipulator                         fileManipulator;
    protected FolderManipulator                       folderManipulator;
    protected SymbolicManipulator                     symbolicManipulator;
    protected FolderVolumeMappingManipulator          folderVolumeMappingManipulator;

    protected FSNodeAllotment                         fsNodeAllotment;
    protected FatChunkInstrument                      fatChunkInstrument;
    protected JournalInstrument                       journalInstrument;
    protected BucketInstrument                        bucketInstrument;
    protected BucketResolver                          bucketResolver;

    protected UofsSymbolicPathResolver                symbolicPathResolver;
    protected ExternalFileSystemInstrument            directFileSystemAccessor;
    protected IndexableMapQuerier<String, String >    globalPathGuidCacheQuerier;


    public UniformObjectFileSystem(
            Processum superiorProcess, KOIMasterManipulator masterManipulator, KOMFileSystem parent,
            String name, IndexableMapQuerier<String, String > globalPathGuidCacheQuerier,
            FileSystemConfig fileSystemConfig, @Nullable GuidAllocator guidAllocator
    ) {
        super( superiorProcess, masterManipulator, fileSystemConfig, parent, name, guidAllocator );

        this.initCore( masterManipulator );
        this.initInstrumentEtManipulators();
        this.initFatChunkInstrument();
        this.initJournalInstrument();
        this.initSelectors();
        this.initMisc( globalPathGuidCacheQuerier );
    }

    public UniformObjectFileSystem( Processum superiorProcess, KOIMasterManipulator masterManipulator, KOMFileSystem parent, String name, FileSystemConfig fileSystemConfig ) {
        this( superiorProcess, masterManipulator, parent, name, null, fileSystemConfig, null );
    }

    public UniformObjectFileSystem( Processum superiorProcess, KOIMasterManipulator masterManipulator, FileSystemConfig fileSystemConfig ) {
        this( superiorProcess, masterManipulator, null, KOMFileSystem.class.getSimpleName(), fileSystemConfig );
    }

    public UniformObjectFileSystem( Processum superiorProcess, KOIMasterManipulator masterManipulator, IndexableMapQuerier<String, String > globalPathGuidCacheQuerier, FileSystemConfig fileSystemConfig ) {
        this( superiorProcess, masterManipulator, null, KOMFileSystem.class.getSimpleName(), globalPathGuidCacheQuerier, fileSystemConfig, null );
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

    public UniformObjectFileSystem( KOIMappingDriver driver, FileSystemConfig fileSystemConfig ) {
        this(
                driver.getSuperiorProcess(),
                driver.getMasterManipulator(),
                fileSystemConfig
        );
    }

    public UniformObjectFileSystem( KOIMappingDriver driver, IndexableMapQuerier<String, String > globalPathGuidCacheQuerier, FileSystemConfig fileSystemConfig ) {
        this(
                driver.getSuperiorProcess(),
                driver.getMasterManipulator(),
                globalPathGuidCacheQuerier,
                fileSystemConfig
        );
    }

    private void initCore( KOIMasterManipulator masterManipulator ) {
        this.fileMasterManipulator = (FileMasterManipulator) masterManipulator;
        this.pathResolver          = new KOPathResolver( this.kernelObjectConfig );
        this.operatorFactory       = new GenericFileSystemOperatorFactory( this, this.fileMasterManipulator );
    }

    private void initInstrumentEtManipulators() {
        this.fileManipulator                = this.fileMasterManipulator.getFileManipulator();
        this.folderManipulator              = this.fileMasterManipulator.getFolderManipulator();
        this.symbolicManipulator            = this.fileMasterManipulator.getSymbolicManipulator();
        this.folderVolumeMappingManipulator = this.fileMasterManipulator.getFolderVolumeRelationManipulator();

        this.bucketInstrument               = new TitanBucketInstrument( this.fileMasterManipulator.getBucketManipulator() );
        this.bucketResolver                 = new BucketResolver( this.bucketInstrument );
    }

    private void initFatChunkInstrument() {
        this.fatChunkInstrument = new TitanFatChunkInstrument(
                this.fileMasterManipulator.getFileChunkManipulator(),
                this.fileMasterManipulator.getFileChunkLocationManipulator(),
                this.getGuidAllocator()
        );
    }

    private void initJournalInstrument() {
        if ( !this.getConfig().isJournalEnabled() ) {
            this.journalInstrument = null;
            return;
        }
        this.journalInstrument = new TitanJournalInstrument(
                this.fileMasterManipulator.getJournalManipulator(),
                this.fileMasterManipulator.getJournalItemManipulator(),
                this.getGuidAllocator()
        );
        if ( this.getConfig().isJournalAutoRecoveryEnabled() ) {
            new TitanJournalRecoveryInstrument(
                    this.journalInstrument,
                    this.fileMasterManipulator.getJournalManipulator(),
                    this.fileMasterManipulator.getJournalItemManipulator(),
                    this.fatChunkInstrument,
                    null,
                    this.fileManipulator
            ).recover();
        }
    }

    private void initSelectors() {
        this.pathSelector = new StandardPathSelector(
                this.pathResolver, this.imperialTree, this.folderManipulator, new GUIDNameManipulator[] {
                        this.fileManipulator,
                        this.symbolicManipulator
                }
        );
        this.symbolicPathResolver = new TitanUofsSymbolicPathResolver(
                this.pathResolver,
                this.imperialTree,
                this.symbolicManipulator,
                this.getConfig().getPathNameSeparator(),
                new UofsSymbolicResolveConfig( this.getConfig().getSymbolicReparseMaxDepth() )
        );
        // ReparseKOM must be constructed after pathSelector.
        this.mReparseKOM = new GenericReparseKOMTreeAddition( this );
    }

    private void initMisc( IndexableMapQuerier<String, String > globalPathGuidCacheQuerier ) {
        this.fsNodeAllotment            = new GenericFSNodeAllotment( this.fileMasterManipulator, this );
        this.globalPathGuidCacheQuerier = globalPathGuidCacheQuerier;
        this.directFileSystemAccessor   = new TitanExternalFileSystemInstrument( this );
    }

    protected void apply( IndexableMapQuerier<String, String > globalPathGuidCacheQuerier ) {
        this.globalPathGuidCacheQuerier = globalPathGuidCacheQuerier;
    }


    @Override
    public BucketInstrument bucketInstrument() {
        return this.bucketInstrument;
    }

    @Override
    public FileTreeNode get( GUID guid, int depth ) {
        return (FileTreeNode) super.get( guid, depth );
    }

    @Override
    public FileMasterManipulator getFileMasterManipulator() {
        return this.fileMasterManipulator;
    }

    @Override
    public FatChunkInstrument getFatChunkInstrument() {
        return this.fatChunkInstrument;
    }

    @Override
    public FileTreeNode get( GUID guid ) {
        return (FileTreeNode) super.get( guid );
    }

    @Override
    public void update( FileTreeNode node ) {
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
    public FileNode getFileNode( GUID guid ) {
        return ( FileNode ) this.get( guid );
    }

    @Override
    public Folder getFolder( GUID guid ) {
        return ( Folder ) this.get( guid );
    }

    @Override
    public void remove( String path ) {
        String key = DefaultCacheConstants.FilePathCacheNS + path;
        this.globalPathGuidCacheQuerier.erase(key);
        super.remove(path);
    }

    @Override
    public void remove( GUID guid ){
        super.remove( guid );
        this.deleteFileChunks( guid );
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
    public FileNode affirmFileNode( String path ) {
        FileNode fileNode = (FileNode) this.affirmTreeNodeByPath(path, GenericFileNode.class, GenericFolder.class);
        return fileNode;
    }

    @Override
    public Folder affirmFolder( String path ) {
        Folder folder = (Folder) this.affirmTreeNodeByPath(path, null, GenericFolder.class);
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
        GUID guid =  this.queryDirectGUIDByPath(path); // Into OLTP-RDB
        if ( guid == null ) {
            UofsSymbolicResolveResult result = this.symbolicPathResolver.resolve(path, this::queryDirectGUIDByPath);
            guid = result.getResolvedGuid();
        }
        if ( this.globalPathGuidCacheQuerier != null ) {
            String key = DefaultCacheConstants.FilePathCacheNS + path;
            if ( guid != null ) {
                this.globalPathGuidCacheQuerier.insert( key, guid.toString(), config.getPathQueryExpiryTimeHotMil() );
            }
        }
        return guid;
    }

    protected GUID queryDirectGUIDByPath( String path ) {
        return super.queryGUIDByPath(path);
    }

    @Override
    public ElementNode queryElement( String path ) {
        GUID guid = this.queryGUIDByPath( path );
        if( guid != null ) {
            return (ElementNode) this.get( guid );
        }
        return this.directFileSystemAccessor.queryElement(path);
    }

    @Override
    public List<TreeNode> selectByName( String name ) {
        return null;
    }

    @Override
    public void moveTo( String sourcePath, String destinationPath ) {
        GUID[] pair = this.assertCopyMove( sourcePath, destinationPath );
        GUID sourceGuid      = pair[ 0 ];
        GUID destinationGuid = pair[ 1 ];

        this.imperialTree.moveTo( sourceGuid, destinationGuid );
        this.imperialTree.removeCachePath( sourceGuid );
    }

    @Override
    public void move( String sourcePath, String destinationPath ) {
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
    public void copy( String sourcePath, String destinationPath, VolumeManager volumeManager ) throws  IOException {
        ElementNode sourceNode = this.queryElement( sourcePath );
        if ( !( sourceNode instanceof FileTreeNode ) ) {
            throw new IllegalArgumentException( "Undefined UOFS copy source: " + sourcePath );
        }
        ElementNode destinationNode = this.queryElement( destinationPath );
        if ( !( destinationNode instanceof Folder ) ) {
            throw new IllegalArgumentException( "UOFS copy destination should be a folder: " + destinationPath );
        }
        this.copyNodeToFolder( (FileTreeNode) sourceNode, this.normalizeUofsFolderPath( destinationPath ), volumeManager );
    }

    @Override
    public void directCopy( String sourcePath, String destinationPath ) throws IOException {
        this.directFileSystemAccessor.copy( sourcePath,destinationPath );
    }

    protected void copyNodeToFolder(
            FileTreeNode sourceNode,
            String destinationFolderPath,
            VolumeManager volumeManager
    ) throws IOException {
        String destinationPath = this.joinUofsPath( destinationFolderPath, sourceNode.getName() );
        if( sourceNode instanceof Folder ){
            this.affirmFolder( destinationPath );
            List<TreeNode> children = this.getChildren( sourceNode.getGuid() );
            for( TreeNode child : children ){
                FileTreeNode childFileTreeNode = this.get( child.getGuid() );
                this.copyNodeToFolder( childFileTreeNode, destinationPath, volumeManager );
            }
            return;
        }

        this.copyFileByChannel( (FileNode) sourceNode, destinationPath, volumeManager );
    }

    protected void copyFileByChannel(
            FileNode sourceNode,
            String destinationPath,
            VolumeManager volumeManager
    ) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate( 1024 * 1024 );
        try ( UFileChannel sourceChannel = this.open( sourceNode, UFileOpenOption.READ, volumeManager );
              UFileChannel destinationChannel = this.open( destinationPath, UFileOpenOption.CREATE_OVERWRITE, volumeManager ) ) {
            while ( true ) {
                buffer.clear();
                int read = sourceChannel.read( buffer );
                if ( read < 0 ) {
                    break;
                }
                if ( read == 0 ) {
                    break;
                }
                buffer.flip();
                while ( buffer.hasRemaining() ) {
                    destinationChannel.write( buffer );
                }
            }
        }
    }

    protected String normalizeUofsFolderPath( String path ) {
        if ( path == null || path.isEmpty() ) {
            return "";
        }
        while ( path.endsWith( StorageConstants.PathSeparator ) ) {
            path = path.substring( 0, path.length() - StorageConstants.PathSeparator.length() );
        }
        return path;
    }

    protected String joinUofsPath( String folderPath, String name ) {
        if ( folderPath == null || folderPath.isEmpty() ) {
            return name;
        }
        return this.normalizeUofsFolderPath( folderPath ) + StorageConstants.PathSeparator + name;
    }

    @Override
    public Object querySelectorJ( String szSelector ) {
        return null;
    }

    @Override
    public List<FileChunk> getChunksByFileGuid( GUID fileGuid ) {
        return this.fatChunkInstrument.fetchChunks( fileGuid );
    }

    @Override
    public List<FileChunkLocation> getChunkLocations( GUID chunkGuid ) {
        return this.fatChunkInstrument.fetchLocations( chunkGuid );
    }

    @Override
    public List<ChunkSlice> fetchChunkSlices( GUID fileGuid, long offset, long length ) {
        return this.fatChunkInstrument.fetchSlices( fileGuid, offset, length );
    }

    @Override
    public long countFileChunks( GUID fileGuid ) {
        return this.fatChunkInstrument.fetchChunks( fileGuid ).size();
    }

    @Override
    public void deleteFileChunks( GUID fileGuid ) {
        this.fatChunkInstrument.deleteFileChunks( fileGuid );
    }


    @Override
    public FSNodeAllotment getFSNodeAllotment() {
        return this.fsNodeAllotment;
    }

    @Override
    public Object querySelector( String szSelector ) {
        return null;
    }

    @Override
    public List querySelectorAll( String szSelector ) {
        return null;
    }

    private String getNodeName( ImperialTreeNode node ){
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
    public UFileChannel open( String path, UFileOpenOption option, VolumeManager volumeManager ) throws IOException {
        UofsResolveContext resolveContext = new UofsResolveContext();
        resolveContext.setPathNameSeparator( this.getConfig().getPathNameSeparator() );
        UofsAddress address = new UofsAddressResolver().resolve( path, resolveContext );
        String key = address.getKey();
        if ( key == null || key.isEmpty() ) {
            throw new IllegalArgumentException( "UOFS file path is empty: " + path );
        }
        Bucket bucket = this.resolveBucket( address );
        String treePath = this.toBucketTreePath( address );

        FileNode fileNode;
        if ( option == UFileOpenOption.READ || option == UFileOpenOption.APPEND ) {
            ElementNode elementNode = this.queryElement( treePath );
            if ( !( elementNode instanceof FileNode ) ) {
                throw new IllegalArgumentException( "UOFS file not found: " + path );
            }
            fileNode = (FileNode) elementNode;
        }
        else if ( option == UFileOpenOption.CREATE ) {
            if ( this.queryElement( treePath ) != null ) {
                throw new IllegalArgumentException( "UOFS file already exists: " + path );
            }
            fileNode = this.affirmFileNode( treePath );
        }
        else if ( option == UFileOpenOption.CREATE_OVERWRITE ) {
            fileNode = this.affirmFileNode( treePath );
        }
        else {
            throw new IllegalArgumentException( "Unsupported UOFS open option: " + option );
        }
        this.markBucketGuid( treePath, bucket.getGuid() );
        return this.open( fileNode, option, volumeManager, bucket.getGuid(), bucket.getVolumeGuid() );
    }

    @Override
    public UFileChannel open( FileNode fileNode, UFileOpenOption option, VolumeManager volumeManager ) throws IOException {
        return this.open(
                fileNode,
                option,
                volumeManager,
                fileNode.getBucketGuid(),
                GUIDs.GUID128( this.getConfig().getDefaultVolumeGuid() )
        );
    }

    protected UFileChannel open(
            FileNode fileNode,
            UFileOpenOption option,
            VolumeManager volumeManager,
            GUID bucketGuid,
            GUID writeVolumeGuid
    ) throws IOException {
        FatChunkStore chunkStore = new TitanFatChunkStore( volumeManager );
        VolumeSpaceAllocator allocator = new LinearVolumeSpaceAllocator(
                this.fileMasterManipulator.getFileChunkLocationManipulator()
        );
        FatFileStore fileStore = new TitanFatFileStore(
                this,
                this.fatChunkInstrument,
                chunkStore,
                allocator,
                volumeManager,
                bucketGuid,
                writeVolumeGuid,
                this.journalInstrument,
                option == UFileOpenOption.CREATE ? JournalType.CREATE : JournalType.OVERWRITE
        );
        return new TitanFileChannel( this, fileNode, option, fileStore );
    }

    protected Bucket resolveBucket( UofsAddress address ) {
        if ( this.bucketResolver == null ) {
            throw new IllegalStateException( "UOFS bucket resolver is not initialized" );
        }
        return this.bucketResolver.resolve( address.getUserIdentifier(), address.getBucketName() );
    }

    protected String toBucketTreePath( UofsAddress address ) {
        return address.getUserIdentifier()
                + "@"
                + address.getBucketName()
                + this.getConfig().getPathNameSeparator()
                + address.getKey();
    }

    protected void markBucketGuid( String treePath, GUID bucketGuid ) {
        BucketNodeManipulator manipulator = this.getBucketNodeManipulator();
        if ( manipulator == null || bucketGuid == null ) {
            return;
        }
        String[] parts = this.pathResolver.segmentPathParts( treePath );
        String currentPath = "";
        for ( int i = 0; i < parts.length; ++i ) {
            currentPath = currentPath + ( i > 0 ? this.getConfig().getPathNameSeparator() : "" ) + parts[i];
            ElementNode node = this.queryElement( currentPath );
            if ( node != null ) {
                manipulator.updateBucketGuid( node.getGuid(), bucketGuid );
                node.setBucketGuid( bucketGuid );
            }
        }
    }

    protected BucketNodeManipulator getBucketNodeManipulator() {
        if ( !( this.fileMasterManipulator.getSkeletonMasterManipulator() instanceof TreeMasterManipulator ) ) {
            return null;
        }
        TrieTreeManipulator treeManipulator = ( (TreeMasterManipulator) this.fileMasterManipulator.getSkeletonMasterManipulator() ).getTrieTreeManipulator();
        if ( treeManipulator instanceof BucketNodeManipulator ) {
            return (BucketNodeManipulator) treeManipulator;
        }
        return null;
    }

    @Override
    public void setFolderVolumeMapping( GUID folderGuid, GUID volumeGuid ) {
        throw new UnsupportedOperationException( "Folder volume mapping has been replaced by UOFS bucket volume binding" );
    }

    @Override
    public GUID getMappingVolume( GUID folderGuid ) {
        throw new UnsupportedOperationException( "Folder volume mapping has been replaced by UOFS bucket volume binding" );
    }

    @Override
    public GUID getMappingVolume( String path ) {
        throw new UnsupportedOperationException( "Folder volume mapping has been replaced by UOFS bucket volume binding" );
    }

    @Override
    public void renameFile( String filePath, String newFileName ) {
        ElementNode elementNode = this.queryElement(filePath);
        elementNode.setName( newFileName );

        FileSystemOperator operator = (FileSystemOperator)this.operatorFactory.getOperator(elementNode.getMetaType());
        operator.rename( elementNode.getGuid(), newFileName );
    }

}
