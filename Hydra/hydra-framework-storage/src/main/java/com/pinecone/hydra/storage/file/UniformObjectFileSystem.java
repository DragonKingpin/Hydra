package com.pinecone.hydra.storage.file;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import com.pinecone.framework.system.Nullable;
import com.pinecone.framework.system.executum.Processum;
import com.pinecone.framework.util.StringUtils;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.framework.util.uoi.UOI;

import com.pinecone.hydra.storage.bucket.*;
import com.pinecone.hydra.storage.bucket.purge.BucketPurgeProgressListener;
import com.pinecone.hydra.storage.bucket.purge.BucketPurgeReport;
import com.pinecone.hydra.storage.bucket.purge.GenericBucketPurgeExecutor;
import com.pinecone.ulf.util.guid.GUIDs;

import com.pinecone.hydra.storage.StorageConstants;
import com.pinecone.hydra.storage.file.cache.DefaultCacheConstants;
import com.pinecone.hydra.storage.file.delete.UofsDeleteExecutor;
import com.pinecone.hydra.storage.file.delete.UofsDeletePolicy;
import com.pinecone.hydra.storage.file.delete.UofsFileDataDeleter;
import com.pinecone.hydra.storage.file.delete.UofsMetadataDeleteOperator;
import com.pinecone.hydra.storage.file.entity.ElementNode;
import com.pinecone.hydra.storage.file.entity.ExternalSymbolic;
import com.pinecone.hydra.storage.file.external.ExternalFileSystemInstrument;
import com.pinecone.hydra.storage.file.external.ExternalFile;
import com.pinecone.hydra.storage.file.external.ExternalFolder;
import com.pinecone.hydra.storage.file.external.NativeExternalFileChannel;
import com.pinecone.hydra.storage.file.external.TitanExternalFileSystemInstrument;
import com.pinecone.hydra.storage.file.entity.FSNodeAllotment;
import com.pinecone.hydra.storage.file.entity.FileNode;
import com.pinecone.hydra.storage.file.entity.FileTreeNode;
import com.pinecone.hydra.storage.file.entity.Folder;
import com.pinecone.hydra.unit.imperium.entity.HardlinkEntry;
import com.pinecone.hydra.storage.file.entity.GenericFSNodeAllotment;
import com.pinecone.hydra.storage.file.entity.GenericFileNode;
import com.pinecone.hydra.storage.file.entity.GenericFolder;
import com.pinecone.hydra.storage.file.entity.Symbolic;
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
import com.pinecone.hydra.storage.file.query.FileChildPage;
import com.pinecone.hydra.storage.file.query.FileChildQuery;
import com.pinecone.hydra.storage.file.query.FileChildType;
import com.pinecone.hydra.storage.file.reparse.TitanUofsSymbolicPathResolver;
import com.pinecone.hydra.storage.file.reparse.UofsSymbolicPathResolver;
import com.pinecone.hydra.storage.file.reparse.UofsSymbolicResolveConfig;
import com.pinecone.hydra.storage.file.reparse.UofsSymbolicResolveResult;
import com.pinecone.hydra.storage.file.source.FileChildManipulator;
import com.pinecone.hydra.storage.file.source.FileManipulator;
import com.pinecone.hydra.storage.file.source.FileMasterManipulator;
import com.pinecone.hydra.storage.file.source.FolderManipulator;
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
    protected FileChildManipulator                    fileChildManipulator;

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
        this.fileChildManipulator           = this.fileMasterManipulator.getFileChildManipulator();
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
    public FileChildPage fetchRoot( FileChildQuery query ) {
        FileChildQuery normalized = this.normalizeChildQuery( query );
        List<GUID> guids = this.fileChildManipulator.fetchRootChildren( normalized );
        List<FileTreeNode> nodes = this.hydrateFileTreeNodes( guids );
        long total = this.fileChildManipulator.countRoot( normalized );
        return FileChildPage.of( nodes, total, normalized.getOffset(), normalized.getLimit() );
    }

    @Override
    public long countRoot( FileChildQuery query ) {
        return this.fileChildManipulator.countRoot( this.normalizeChildQuery( query ) );
    }

    @Override
    public FileChildPage fetchChildren( GUID parentGuid, FileChildQuery query ) {
        FileChildQuery normalized = this.normalizeChildQuery( query );
        normalized.setParentGuid( parentGuid );
        List<GUID> guids = this.fileChildManipulator.fetchChildren( normalized );
        List<FileTreeNode> nodes = this.hydrateFileTreeNodes( guids );
        long total = this.fileChildManipulator.countChildren( normalized );
        return FileChildPage.of( nodes, total, normalized.getOffset(), normalized.getLimit() );
    }

    @Override
    public FileChildPage fetchChildren( String path, FileChildQuery query ) {
        ElementNode parent = this.queryElement( path );
        if ( parent == null ) {
            return FileChildPage.of( List.of(), 0L, this.normalizeChildQuery( query ).getOffset(), this.normalizeChildQuery( query ).getLimit() );
        }
        if ( parent instanceof ExternalSymbolic ) {
            ElementNode externalElement = this.directFileSystemAccessor.queryElement( path );
            if ( externalElement != null ) {
                parent = externalElement;
            }
        }
        Symbolic symbolic = parent.evinceSymbolic();
        if ( symbolic != null ) {
            return this.fetchChildren( symbolic.getReparsedPoint(), query );
        }
        if ( parent instanceof ExternalFolder ) {
            return this.fetchExternalChildren( (ExternalFolder) parent, query );
        }
        if ( parent instanceof Folder ) {
            return this.fetchChildren( parent.getGuid(), query );
        }
        throw new IllegalArgumentException( "UOFS path is not a folder: " + path );
    }

    @Override
    public List<HardlinkEntry> listHardlinks( String keyword, int offset, int limit ) {
        return this.imperialTree.listHardlinks( keyword, offset, limit );
    }

    @Override
    public long countHardlinks( String keyword ) {
        return this.imperialTree.countHardlinks( keyword );
    }

    protected FileChildQuery normalizeChildQuery( FileChildQuery query ) {
        return query == null ? new FileChildQuery() : query.normalized();
    }

    protected List<FileTreeNode> hydrateFileTreeNodes( List<GUID> guids ) {
        List<FileTreeNode> nodes = new ArrayList<>();
        if ( guids == null ) {
            return nodes;
        }
        for ( GUID guid : guids ) {
            FileTreeNode node = this.get( guid );
            if ( node != null ) {
                nodes.add( node );
            }
        }
        return nodes;
    }

    protected FileChildPage fetchExternalChildren( ExternalFolder folder, FileChildQuery query ) {
        FileChildQuery normalized = this.normalizeChildQuery( query );
        List<FileTreeNode> source = folder.listItem();
        List<FileTreeNode> filtered = new ArrayList<>();
        for ( FileTreeNode node : source ) {
            if ( this.matchExternalChildQuery( node, normalized ) ) {
                filtered.add( node );
            }
        }
        filtered.sort( this.externalChildComparator( normalized ) );

        int offset = Math.min( normalized.getOffset(), filtered.size() );
        int limit = normalized.getLimit();
        int end = Math.min( filtered.size(), offset + limit );
        return FileChildPage.of( filtered.subList( offset, end ), filtered.size(), offset, limit );
    }

    protected boolean matchExternalChildQuery( FileTreeNode node, FileChildQuery query ) {
        if ( !query.getHasTypeFilter() ) {
            return true;
        }
        if ( node instanceof ExternalFolder ) {
            return query.getIncludeFolder();
        }
        if ( node instanceof ExternalFile ) {
            return query.getIncludeFile();
        }
        if ( node instanceof ElementNode ) {
            ElementNode elementNode = (ElementNode) node;
            if ( elementNode.evinceFolder() != null ) {
                return query.getIncludeFolder();
            }
            if ( elementNode.evinceFileNode() != null ) {
                return query.getIncludeFile();
            }
            if ( elementNode.evinceSymbolic() != null ) {
                return query.getIncludeReparse();
            }
        }
        return false;
    }

    protected Comparator<FileTreeNode> externalChildComparator( FileChildQuery query ) {
        Comparator<FileTreeNode> comparator;
        if ( query.getOrderByCreateTime() ) {
            comparator = Comparator.comparing( this::externalChildCreateTime, Comparator.nullsLast( Comparator.naturalOrder() ) );
        }
        else if ( query.getOrderByUpdateTime() ) {
            comparator = Comparator.comparing( this::externalChildUpdateTime, Comparator.nullsLast( Comparator.naturalOrder() ) );
        }
        else {
            comparator = Comparator.comparing( FileTreeNode::getName, Comparator.nullsLast( String::compareToIgnoreCase ) );
        }
        return query.getAscending() ? comparator : comparator.reversed();
    }

    protected java.time.LocalDateTime externalChildCreateTime( FileTreeNode node ) {
        return node instanceof ElementNode ? ( (ElementNode) node ).getCreateTime() : null;
    }

    protected java.time.LocalDateTime externalChildUpdateTime( FileTreeNode node ) {
        return node instanceof ElementNode ? ( (ElementNode) node ).getUpdateTime() : null;
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
        this.remove( path, null );
    }

    @Override
    public void remove( String path, VolumeManager volumeManager ) {
        this.newDeleteExecutor( volumeManager ).remove( path );
    }

    @Override
    public void remove( GUID guid ){
        this.remove( guid, null );
    }

    @Override
    public void remove( GUID guid, VolumeManager volumeManager ){
        this.newDeleteExecutor( volumeManager ).remove( guid );
    }

    @Override
    public BucketPurgeReport purgeBucket( GUID bucketGuid, VolumeManager volumeManager, @Nullable BucketPurgeProgressListener listener ) {
        return new GenericBucketPurgeExecutor( this, volumeManager ).purgeBucket( bucketGuid, listener );
    }

    @Override
    public BucketPurgeReport formatBucket( GUID bucketGuid, VolumeManager volumeManager, @Nullable BucketPurgeProgressListener listener ) {
        return new GenericBucketPurgeExecutor( this, volumeManager ).formatBucket( bucketGuid, listener );
    }

    protected UofsDeleteExecutor newDeleteExecutor( VolumeManager volumeManager ) {
        return new UofsDeleteExecutor(
                this,
                this.directFileSystemAccessor,
                this::queryDirectGUIDByPath,
                this::erasePathCache,
                this.newFileDataDeleter( volumeManager ),
                this.newMetadataDeleteOperator(),
                UofsDeletePolicy.strict()
        );
    }

    protected UofsMetadataDeleteOperator newMetadataDeleteOperator() {
        return new UofsMetadataDeleteOperator() {
            @Override
            public void removeFile( GUID guid ) {
                UniformObjectFileSystem.this.removeFileMetadata( guid );
            }

            @Override
            public void removeFolder( GUID guid ) {
                UniformObjectFileSystem.this.removeFolderMetadata( guid );
            }

            @Override
            public void removeInternalSymbolic( GUID guid ) {
                UniformObjectFileSystem.this.removeInternalSymbolicMetadata( guid );
            }

            @Override
            public void removeExternalSymbolic( GUID guid ) {
                UniformObjectFileSystem.this.removeExternalSymbolicMetadata( guid );
            }

            @Override
            public void unlink( GUID parentGuid, GUID childGuid ) {
                UniformObjectFileSystem.this.unlinkMetadata( parentGuid, childGuid );
            }
        };
    }

    protected UofsFileDataDeleter newFileDataDeleter( VolumeManager volumeManager ) {
        if ( volumeManager == null ) {
            return this::deleteFileIndex;
        }
        return fileNode -> this.deleteFileData( fileNode, volumeManager );
    }

    protected void removeFileMetadata( GUID guid ) {
        this.imperialTree.purge( guid );
        this.fileManipulator.remove( guid );
        this.imperialTree.removeCachePath( guid );
    }

    protected void removeFolderMetadata( GUID guid ) {
        this.imperialTree.purge( guid );
        this.folderManipulator.remove( guid );
        this.imperialTree.removeCachePath( guid );
    }

    protected void removeInternalSymbolicMetadata( GUID guid ) {
        this.imperialTree.purge( guid );
        this.symbolicManipulator.remove( guid );
        this.imperialTree.removeCachePath( guid );
    }

    protected void removeExternalSymbolicMetadata( GUID guid ) {
        this.imperialTree.purge( guid );
        this.fileMasterManipulator.getExternalSymbolicManipulator().remove( guid );
        this.imperialTree.removeCachePath( guid );
    }

    protected void unlinkMetadata( GUID parentGuid, GUID childGuid ) {
        if ( parentGuid == null || childGuid == null ) {
            return;
        }
        this.imperialTree.removeInheritance( childGuid, parentGuid );
        this.imperialTree.removeCachePath( childGuid );
    }

    protected void deleteFileIndex( FileNode fileNode ) {
        if ( fileNode != null ) {
            this.deleteFileChunks( fileNode.getGuid() );
        }
    }

    protected void deleteFileData( FileNode fileNode, VolumeManager volumeManager ) {
        try {
            this.newFatFileStore( fileNode, volumeManager ).delete( fileNode );
        }
        catch ( IOException e ) {
            throw new IllegalStateException( "Failed to delete UOFS file data: " + fileNode.getGuid(), e );
        }
    }

    protected FatFileStore newFatFileStore( FileNode fileNode, VolumeManager volumeManager ) {
        FatChunkStore chunkStore = new TitanFatChunkStore( volumeManager );
        VolumeSpaceAllocator allocator = new LinearVolumeSpaceAllocator(
                this.fileMasterManipulator.getFileChunkLocationManipulator()
        );
        GUID writeVolumeGuid = this.resolveWriteVolumeGuid( fileNode );
        return new TitanFatFileStore(
                this,
                this.fatChunkInstrument,
                chunkStore,
                allocator,
                volumeManager,
                fileNode.getBucketGuid(),
                writeVolumeGuid,
                this.journalInstrument,
                JournalType.DELETE
        );
    }

    protected GUID resolveWriteVolumeGuid( FileNode fileNode ) {
        if ( fileNode.getBucketGuid() != null ) {
            Bucket bucket = this.bucketInstrument.get( fileNode.getBucketGuid() );
            if ( bucket != null && bucket.getVolumeGuid() != null ) {
                return bucket.getVolumeGuid();
            }
        }
        return GUIDs.GUID128( this.getConfig().getDefaultVolumeGuid() );
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
        return this.affirmTreeNodeByPath( path, cnSup, nsSup, null );
    }

    protected FileTreeNode affirmTreeNodeByPath( String path, Class<? > cnSup, Class<? > nsSup, GUID bucketGuid ) {
        List<String> parts = this.pathResolver.resolvePathParts( path );
        String currentPath = "";
        GUID parentGuid = GUIDs.Dummy128();

        FileTreeNode node = this.queryElement( path );
        if( node != null ) {
            return node;
        }

        FileTreeNode ret = null;
        for( int i = 0; i < parts.size(); ++i ){
            String part = parts.get( i );
            currentPath = currentPath + ( i > 0 ? this.getConfig().getPathNameSeparator() : "" ) + part;
            node = this.queryElement( currentPath );
            if ( node == null){
                GUID effectiveBucketGuid = this.resolveCreationBucketGuid( bucketGuid, parentGuid, currentPath );
                if ( i == parts.size() - 1 && cnSup != null ){
                    FileNode fileNode = (FileNode) this.dynamicFactory.optNewInstance( cnSup, new Object[]{ this } );
                    fileNode.setName( part );
                    fileNode.setBucketGuid( effectiveBucketGuid );
                    GUID guid = this.put( fileNode );
                    this.affirmOwnedNode( parentGuid, guid );
                    return fileNode;
                }
                else {
                    Folder folder = (Folder) this.dynamicFactory.optNewInstance( nsSup, new Object[]{ this } );
                    folder.setName(part);
                    folder.setBucketGuid( effectiveBucketGuid );
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

    public FileNode affirmFileNode( String path, GUID bucketGuid ) {
        FileNode fileNode = (FileNode) this.affirmTreeNodeByPath( path, GenericFileNode.class, GenericFolder.class, bucketGuid );
        return fileNode;
    }

    @Override
    public Folder affirmFolder( String path ) {
        Folder folder = (Folder) this.affirmTreeNodeByPath(path, null, GenericFolder.class);
        return folder;
    }

    public Folder affirmFolder( String path, GUID bucketGuid ) {
        Folder folder = (Folder) this.affirmTreeNodeByPath( path, null, GenericFolder.class, bucketGuid );
        return folder;
    }

    @Override
    public ElementNode affirmFolderElement( String path ) {
        return this.affirmFolderElement( path, null );
    }

    public ElementNode affirmFolderElement( String path, GUID bucketGuid ) {
        GUID directGuid = this.queryDirectGUIDByPath( path );
        if ( directGuid != null ) {
            ElementNode node = (ElementNode) this.get( directGuid );
            if ( node instanceof ExternalSymbolic ) {
                ElementNode externalNode = this.directFileSystemAccessor.queryElement( path );
                if ( externalNode instanceof ExternalFolder ) {
                    return externalNode;
                }
            }
            if ( node.evinceFolder() == null ) {
                throw new IllegalArgumentException( "UOFS path is not a folder: " + path );
            }
            return node;
        }

        ElementNode externalNode = this.directFileSystemAccessor.queryElement( path );
        if ( externalNode instanceof ExternalFolder ) {
            return externalNode;
        }
        if ( externalNode instanceof ExternalFile ) {
            ExternalFile externalFile = (ExternalFile) externalNode;
            if ( externalFile.exists() ) {
                throw new IllegalArgumentException( "UOFS path is a native external file: " + path );
            }
            return this.directFileSystemAccessor.affirmFolder( path );
        }

        return this.affirmFolder( path, bucketGuid );
    }

    protected GUID resolveCreationBucketGuid( GUID bucketGuid, GUID parentGuid, String currentPath ) {
        if ( bucketGuid != null ) {
            return bucketGuid;
        }
        GUID inheritedBucketGuid = this.resolveBucketGuidFromParent( parentGuid );
        if ( inheritedBucketGuid != null ) {
            return inheritedBucketGuid;
        }
        return this.resolveBucketGuidFromRootPath( currentPath );
    }

    protected GUID resolveBucketGuidFromParent( GUID parentGuid ) {
        if ( parentGuid == null || Objects.equals( parentGuid, GUIDs.Dummy128() ) ) {
            return null;
        }
        FileTreeNode parent = this.get( parentGuid );
        if ( parent instanceof ElementNode ) {
            return ( (ElementNode) parent ).getBucketGuid();
        }
        return null;
    }

    protected GUID resolveBucketGuidFromRootPath( String currentPath ) {
        if ( this.bucketInstrument == null || !StringUtils.isNotBlank( currentPath ) ) {
            return null;
        }
        String root = currentPath;
        int pathSeparator = root.indexOf( this.getConfig().getPathNameSeparator() );
        if ( pathSeparator >= 0 ) {
            root = root.substring( 0, pathSeparator );
        }
        int bucketSeparator = root.indexOf( '@' );
        Bucket bucket = null;
        if ( bucketSeparator > 0 && bucketSeparator < root.length() - 1 ) {
            bucket = this.bucketInstrument.getByUserIdentifierAndBucket(
                    root.substring( 0, bucketSeparator ),
                    root.substring( bucketSeparator + 1 )
            );
        }
        if ( bucket == null ) {
            bucket = this.bucketInstrument.getByBucketIdentifier( root );
        }
        return bucket == null ? null : bucket.getGuid();
    }

    protected void erasePathCache( String path ) {
        if ( this.globalPathGuidCacheQuerier == null ) {
            return;
        }
        String key = DefaultCacheConstants.FilePathCacheNS + path;
        this.globalPathGuidCacheQuerier.erase( key );
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

        ElementNode elementNode = this.queryElement( treePath );
        if ( elementNode instanceof ExternalSymbolic ) {
            ElementNode externalElement = this.directFileSystemAccessor.queryElement( treePath );
            if ( externalElement != null ) {
                elementNode = externalElement;
            }
        }
        if ( elementNode != null && elementNode.evinceSymbolic() != null ) {
            elementNode = this.queryElement( elementNode.evinceSymbolic().getReparsedPoint() );
        }
        if ( elementNode instanceof ExternalFolder ) {
            throw new IllegalArgumentException( "UOFS path is a native external folder: " + path );
        }
        if ( elementNode instanceof ExternalFile ) {
            ExternalFile externalFile = (ExternalFile) elementNode;
            if ( option == UFileOpenOption.READ && !externalFile.exists() ) {
                throw new IllegalArgumentException( "UOFS native external file not found: " + path );
            }
            if ( option == UFileOpenOption.CREATE && externalFile.exists() ) {
                throw new IllegalArgumentException( "UOFS native external file already exists: " + path );
            }
            return new NativeExternalFileChannel( externalFile, option );
        }

        FileNode fileNode;
        if ( option == UFileOpenOption.READ || option == UFileOpenOption.APPEND ) {
            if ( !( elementNode instanceof FileNode ) ) {
                throw new IllegalArgumentException( "UOFS file not found: " + path );
            }
            fileNode = (FileNode) elementNode;
        }
        else if ( option == UFileOpenOption.CREATE ) {
            if ( this.queryElement( treePath ) != null ) {
                throw new IllegalArgumentException( "UOFS file already exists: " + path );
            }
            fileNode = this.affirmFileNode( treePath, bucket.getGuid() );
        }
        else if ( option == UFileOpenOption.CREATE_OVERWRITE ) {
            fileNode = this.affirmFileNode( treePath, bucket.getGuid() );
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
    public void renameFile( String filePath, String newFileName ) {
        ElementNode elementNode = this.queryElement(filePath);
        elementNode.setName( newFileName );

        FileSystemOperator operator = (FileSystemOperator)this.operatorFactory.getOperator(elementNode.getMetaType());
        operator.rename( elementNode.getGuid(), newFileName );
    }

}
