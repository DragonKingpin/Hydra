package com.pinecone.hydra.unit.udtt;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.registry.entity.ElementNode;
import com.pinecone.hydra.registry.entity.RegistryTreeNode;
import com.pinecone.hydra.unit.udtt.entity.EntityNode;
import com.pinecone.hydra.unit.udtt.entity.ReparseLinkNode;
import com.pinecone.hydra.unit.udtt.entity.TreeNode;

import java.util.List;

public interface KOMTree {
    String getPath( GUID guid );

    String getFullName( GUID guid );

    GUID queryGUIDByPath( String path );

    GUID queryGUIDByFN  ( String fullName );

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

    ReparseLinkNode queryReparseLink(String path );

    GUID put( TreeNode treeNode );

    TreeNode get( GUID guid );

    GUID queryGUIDByNS( String path, String szBadSep, String szTargetSep );

    ElementNode queryElement(String path );

    TreeNode get( GUID guid, int depth );

    TreeNode getSelf( GUID guid );

    void newLinkTag( String originalPath, String dirPath, String tagName );

    void remove( GUID guid );

    void removeReparseLink( GUID guid );

    void remove( String path );

    void affirmOwnedNode( GUID parentGuid, GUID childGuid );

    void newHardLink( GUID sourceGuid, GUID targetGuid );

    void newLinkTag( GUID originalGuid, GUID dirGuid, String tagName );

    void updateLinkTag( GUID tagGuid, String tagName );

    List<TreeNode > getChildren(GUID guid );

    ReparseLinkNode queryReparseLinkByNS( String path, String szBadSep, String szTargetSep );

    Object queryEntityHandleByNS( String path, String szBadSep, String szTargetSep );

    EntityNode queryNode(String path );

    List<TreeNode> listRoot();

    void rename( GUID guid, String name );

}
