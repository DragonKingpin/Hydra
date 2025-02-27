package com.pinecone.hydra.storage.file;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.file.entity.Cluster;
import com.pinecone.hydra.storage.file.entity.ClusterPage;
import com.pinecone.hydra.storage.file.entity.FSNodeAllotment;
import com.pinecone.hydra.storage.file.entity.FileNode;
import com.pinecone.hydra.storage.file.entity.FileTreeNode;
import com.pinecone.hydra.storage.file.entity.Folder;
import com.pinecone.hydra.storage.file.entity.ElementNode;
import com.pinecone.hydra.storage.file.entity.RemoteCluster;
import com.pinecone.hydra.storage.file.source.FileMasterManipulator;
import com.pinecone.hydra.storage.file.transmit.exporter.FileExportEntity;
import com.pinecone.hydra.storage.file.transmit.receiver.FileReceiveEntity;
import com.pinecone.hydra.storage.volume.VolumeManager;
import com.pinecone.hydra.system.ko.kom.ReparseKOMTree;
import com.pinecone.hydra.unit.imperium.entity.EntityNode;
import com.pinecone.hydra.unit.imperium.entity.ReparseLinkNode;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;

import java.io.IOException;
import java.util.List;
import java.util.TreeMap;

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
    FileTreeNode getSelf( GUID guid );

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

    TreeMap<Long, Cluster > getClustersByFileGuid( GUID guid );

    List<RemoteCluster> fetchClustersPageByFileGuid( GUID fileGuid, long offset, int pageSize );

    ClusterPage fetchClustersByFileGuid( GUID fileGuid, int pageSize );

    ClusterPage fetchClustersByFileGuid( GUID fileGuid );

    Cluster getLastCluster(GUID guid );

    void setFolderVolumeMapping(GUID folderGuid, GUID volumeGuid );
    GUID getMappingVolume(GUID folderGuid );

    GUID getMappingVolume(String path );

    Cluster getClusterByFileWithId(GUID fileGuid, long segId );


    void receive(  FileReceiveEntity entity ) throws IOException;
    void receive( FileReceiveEntity entity, Number offset, Number endSize )throws IOException;
    void randomReceive( FileReceiveEntity entity, Number offset, Number endSize ) throws  IOException;

    void export( FileExportEntity entity ) throws IOException;
    void export( FileExportEntity entity, Number offset, Number endSize );

    FileMasterManipulator  getFileMasterManipulator();

    void updateCluster( FileNode fileNode, long segId );

    void deleteCluster( FileNode fileNode, long segId );

    long countFileCluster( GUID fileGuid );

    void renameFile( String filePath, String newFileName );
}
