package com.pinecone.hydra.file;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.file.creator.FileSystemCreator;
import com.pinecone.hydra.file.entity.FileNode;
import com.pinecone.hydra.file.entity.FileTreeNode;
import com.pinecone.hydra.file.entity.Folder;
import com.pinecone.hydra.file.entity.ElementNode;
import com.pinecone.hydra.file.entity.Frame;
import com.pinecone.hydra.file.entity.Symbolic;
import com.pinecone.hydra.registry.entity.RegistryTreeNode;
import com.pinecone.hydra.system.ko.KOMInstrument;
import com.pinecone.hydra.unit.udtt.entity.EntityNode;
import com.pinecone.hydra.unit.udtt.entity.ReparseLinkNode;
import com.pinecone.hydra.unit.udtt.entity.TreeNode;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public interface KOMFileSystem extends KOMInstrument {
    FileSystemConfig  KernelFileSystemConfig = new kernelFileSystemConfig();

    String getPath( GUID guid );

    String getFullName( GUID guid );

    GUID put( TreeNode treeNode );

    FileTreeNode get(GUID guid );

    FileTreeNode get( GUID guid, int depth );

    FileTreeNode getSelf( GUID guid );

    FileNode getFileNode(GUID guid );

    Folder getFolder(GUID guid );

    GUID queryGUIDByPath( String path );

    GUID queryGUIDByFN  ( String fullName );


    //todo update方法


    void remove( GUID guid );

    void removeReparseLink( GUID guid );
    void removeFileNode(GUID guid);
    void removeFolder(GUID guid);


    List<TreeNode > getChildren( GUID guid );

    void rename( GUID guid, String name );

    default void rename( String path, String name ) {
        this.rename( this.assertPath( path ), name );
    }

    default GUID assertPath( String path, String pathType ) throws IllegalArgumentException {
        GUID guid      = this.queryGUIDByPath( path );
        if( guid == null ) {
            throw new IllegalArgumentException( "Undefined " + pathType + " '" + path + "'" );
        }

        return guid;
    }

    default GUID assertPath( String path ) throws IllegalArgumentException {
        return this.assertPath( path, "path" );
    }

    List<TreeNode > getAllTreeNode();



    /** 断言，确保节点唯一拥有关系*/
    void affirmOwnedNode( GUID parentGuid, GUID childGuid  );
    FileNode  affirmFileNode( String path );

    Folder    affirmFolder( String path);

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

    void newLinkTag( GUID originalGuid,GUID dirGuid,String tagName );

    void newLinkTag( String originalPath ,String dirPath,String tagName );

    void updateLinkTag( GUID tagGuid,String tagName);


    Object querySelector                  ( String szSelector );

    void copyFileNodeTo( GUID sourceGuid, GUID destinationGuid );
    void copyFolderTo( GUID sourceGuid, GUID destinationGuid );

    ElementNode queryElement(String path);
    void remove(String path);
    EntityNode queryNode(String path);
    ReparseLinkNode queryReparseLink(String path);
    List<TreeNode> selectByName(String name);
    void moveTo(String sourcePath, String destinationPath);
    void move(String sourcePath, String destinationPath);
    void copyTo(String sourcePath, String destinationPath);
    void copy(String sourcePath, String destinationPath);
    List<FileTreeNode> listRoot();
    Object querySelectorJ(String szSelector);
    List querySelectorAll(String szSelector);
    FileSystemCreator getFileSystemCreator();
    TreeMap<Long, Frame> getFrameByFileGuid(GUID guid);
}
