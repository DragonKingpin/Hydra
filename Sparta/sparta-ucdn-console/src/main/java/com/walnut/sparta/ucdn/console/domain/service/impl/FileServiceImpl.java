package com.walnut.sparta.ucdn.console.domain.service.impl;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.file.KOMFileSystem;
import com.pinecone.hydra.storage.file.entity.ClusterPage;
import com.pinecone.hydra.storage.file.entity.FileNode;
import com.pinecone.hydra.storage.file.entity.FileTreeNode;
import com.pinecone.hydra.storage.file.entity.Folder;
import com.pinecone.hydra.storage.file.entity.LocalCluster;
import com.pinecone.hydra.storage.volume.UniformVolumeManager;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;
import com.walnut.sparta.ucdn.console.domain.service.FileService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.List;

@Service
public class FileServiceImpl implements FileService {

    @Resource
    private KOMFileSystem primaryFileSystem;

    @Resource
    private UniformVolumeManager primaryVolume;

    @Override
    public void remove(GUID fileGuid){
        FileTreeNode fileTreeNode = this.primaryFileSystem.get(fileGuid);
        if( fileTreeNode instanceof Folder){
            Folder folder = (Folder) fileTreeNode;
            List<TreeNode> children = this.primaryFileSystem.getChildren(folder.getGuid());
            for( TreeNode treeNode : children ){
                this.remove( treeNode.getGuid() );
            }
        }else if( fileTreeNode instanceof FileNode){
            FileNode fileNode = (FileNode) fileTreeNode;
            ClusterPage clusterPage = this.primaryFileSystem.fetchClustersByFileGuid( fileNode.getGuid() );
            long fileClusterNum = clusterPage.getClusters();
            for( long i = 0; i < fileClusterNum; i++ ){
                LocalCluster frame = clusterPage.getLocalCluster( i );
                try {
                    this.primaryVolume.removeStorageObject( frame );
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
