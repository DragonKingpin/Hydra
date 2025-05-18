package com.pinecone.hydra.system.ko.kom;

import com.pinecone.framework.system.executum.Processum;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.system.ko.CascadeInstrument;
import com.pinecone.hydra.unit.imperium.entity.EntityNode;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;

import java.util.Collection;
import java.util.List;

public interface KOMInstrument extends CascadeInstrument {
    @Override
    KOMInstrument parent();

    @Override
    default void setTargetingName( String name ) {
        CascadeInstrument.super.setTargetingName( name );
    }

    String getPath( GUID objectGuid );

    String querySystemKernelObjectPath( GUID objectGuid ) ;

    String getFullName( GUID objectGuid );

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

    boolean contains( GUID nodeGuid );

    GUID put( TreeNode treeNode );

    TreeNode get( GUID objectGuid );

    GUID queryGUIDByNS( String path, String szBadSep, String szTargetSep );

    TreeNode get( GUID guid, int depth );

    TreeNode getAsRootDepth( GUID guid );

    void remove( GUID guid );

    void remove( String path );

    Collection<TreeNode > getChildren( GUID guid );

    Collection<GUID > fetchChildrenGuids( GUID guid );

    Object queryEntityHandleByNS( String path, String szBadSep, String szTargetSep );

    EntityNode queryNode( String path );

    TreeNode queryTreeNode( String path );

    List<? extends TreeNode > fetchRoot();

    void rename( GUID guid, String name );

    Processum getSuperiorProcess();

}
