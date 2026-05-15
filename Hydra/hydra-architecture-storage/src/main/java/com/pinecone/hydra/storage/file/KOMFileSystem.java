package com.pinecone.hydra.storage.file;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.StorageConfig;
import com.pinecone.hydra.storage.file.entity.FSNodeAllotment;
import com.pinecone.hydra.storage.file.entity.FileNode;
import com.pinecone.hydra.storage.file.entity.FileTreeNode;
import com.pinecone.hydra.storage.file.entity.Folder;
import com.pinecone.hydra.storage.file.entity.ElementNode;
import com.pinecone.hydra.storage.file.fat.FatChunkInstrument;
import com.pinecone.hydra.storage.file.fat.entity.FileChunk;
import com.pinecone.hydra.storage.file.fat.entity.FileChunkLocation;
import com.pinecone.hydra.storage.file.fat.service.ChunkSlice;
import com.pinecone.hydra.storage.file.source.FileMasterManipulator;
import com.pinecone.hydra.storage.file.transmit.channel.UFileChannel;
import com.pinecone.hydra.storage.file.transmit.channel.UFileOpenOption;
import com.pinecone.hydra.storage.volume.VolumeManager;
import com.pinecone.hydra.system.ko.kom.ReparseKOMTree;
import com.pinecone.hydra.unit.imperium.entity.EntityNode;
import com.pinecone.hydra.unit.imperium.entity.ReparseLinkNode;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;

import java.io.IOException;
import java.util.List;

public interface KOMFileSystem extends ReparseKOMTree {
    FileSystemConfig  KernelFileSystemConfig = new KernelFileSystemConfig();

    @Override
    String getPath( GUID guid );

    @Override
    String getFullName( GUID guid );

    @Override
    GUID put( TreeNode treeNode );

    @Override
    FileTreeNode get(GUID guid );

    @Override
    FileTreeNode get( GUID guid, int depth );

    void update( FileTreeNode node);

    @Override
    FileTreeNode getAsRootDepth( GUID guid );

    FileNode getFileNode(GUID guid );

    Folder getFolder(GUID guid );

    @Override
    GUID queryGUIDByPath( String path );

    @Override
    GUID queryGUIDByFN  ( String fullName );

    @Override
    FileSystemConfig getConfig();


    //todo update方法

    @Override
    void remove( GUID guid );

    @Override
    void removeReparseLink( GUID guid );


    @Override
    List<TreeNode > getChildren( GUID guid );

    @Override
    void rename( GUID guid, String name );

    default void rename( String path, String name ) {
        this.rename( this.assertPath( path ), name );
    }

    @Override
    default GUID assertPath( String path, String pathType ) throws IllegalArgumentException {
        GUID guid      = this.queryGUIDByPath( path );
        if( guid == null ) {
            throw new IllegalArgumentException( "Undefined " + pathType + " '" + path + "'" );
        }

        return guid;
    }

    @Override
    default GUID assertPath( String path ) throws IllegalArgumentException {
        return this.assertPath( path, "path" );
    }

    List<TreeNode > getAllTreeNode();



    /** 断言，确保节点唯一拥有关系*/
    @Override
    void affirmOwnedNode( GUID parentGuid, GUID childGuid  );

    FileNode  affirmFileNode( String path );

    Folder    affirmFolder( String path);

    @Override
    void newHardLink    ( GUID sourceGuid, GUID targetGuid );

    /** set affinityParentGuid for child.*/
    void setDataAffinityGuid ( GUID childGuid, GUID affinityParentGuid  );

    default void setDataAffinity ( String childPath, String parentPath ) {
        GUID childGuid      = this.assertPath( childPath );
        GUID parentGuid     = this.assertPath( parentPath );
        if( childGuid == parentGuid ) {
            throw new IllegalArgumentException( "Cyclic path detected '" + childPath + "'" );
        }

        this.setDataAffinityGuid( childGuid, parentGuid );
    }

    Object querySelector                  ( String szSelector );

    ElementNode queryElement(String path);

    @Override
    void remove(String path);

    @Override
    EntityNode queryNode(String path);

    @Override
    ReparseLinkNode queryReparseLink(String path);

    List<TreeNode> selectByName(String name);

    void moveTo(String sourcePath, String destinationPath);

    void move(String sourcePath, String destinationPath);

    void copy(String sourcePath, String destinationPath, VolumeManager volumeManager) throws IOException;

    void directCopy( String sourcePath, String destinationPath ) throws IOException;

    @Override
    List<FileTreeNode> fetchRoot();

    Object querySelectorJ(String szSelector);

    List querySelectorAll(String szSelector);

    FSNodeAllotment getFSNodeAllotment();

    List<FileChunk> getChunksByFileGuid( GUID fileGuid );

    List<FileChunkLocation> getChunkLocations( GUID chunkGuid );

    List<ChunkSlice> fetchChunkSlices( GUID fileGuid, long offset, long length );

    long countFileChunks( GUID fileGuid );

    void deleteFileChunks( GUID fileGuid );

    void setFolderVolumeMapping(GUID folderGuid, GUID volumeGuid );
    GUID getMappingVolume(GUID folderGuid );

    GUID getMappingVolume(String path );

    default UFileChannel open( String path, UFileOpenOption option ) throws IOException {
        throw new UnsupportedOperationException( "UOFS channel open requires a Titan VolumeManager in this round" );
    }

    UFileChannel open(
            String path,
            UFileOpenOption option,
            com.pinecone.hydra.storage.volume.VolumeManager volumeManager
    ) throws IOException;

    default UFileChannel open( FileNode fileNode, UFileOpenOption option ) throws IOException {
        throw new UnsupportedOperationException( "UOFS channel open requires a Titan VolumeManager in this round" );
    }

    UFileChannel open(
            FileNode fileNode,
            UFileOpenOption option,
            com.pinecone.hydra.storage.volume.VolumeManager volumeManager
    ) throws IOException;

    FileMasterManipulator  getFileMasterManipulator();

    FatChunkInstrument getFatChunkInstrument();

    void renameFile( String filePath, String newFileName );
}
