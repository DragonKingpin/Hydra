package com.pinecone.hydra.registry;

import com.pinecone.framework.system.Nullable;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.registry.entity.ConfigNode;
import com.pinecone.hydra.registry.entity.Namespace;
import com.pinecone.hydra.registry.entity.Properties;
import com.pinecone.hydra.registry.entity.Property;
import com.pinecone.hydra.registry.entity.RegistryTreeNode;
import com.pinecone.hydra.registry.entity.TextValue;
import com.pinecone.hydra.system.ko.KOMInstrument;
import com.pinecone.hydra.unit.udtt.entity.TreeNode;

import java.util.List;

public interface KOMRegistry extends Registry, KOMInstrument {

    String getPath( GUID guid );

    String getFullName( GUID guid );

    GUID put( TreeNode treeNode );

    RegistryTreeNode get( GUID guid );

    RegistryTreeNode get( GUID guid, int depth );

    RegistryTreeNode getSelf( GUID guid );

    Properties getProperties( GUID guid );

    Namespace getNamespace( GUID guid );

    GUID queryGUIDByPath( String path );

    GUID queryGUIDByFN  ( String fullName );

    List<Property > fetchProperties( GUID guid );

    TextValue getTextValue( GUID guid );

    ConfigNode getConfigNode( GUID guid );



    void putProperty( Property property, GUID configNodeGuid );

    void putTextValue( GUID guid, String text, String format );

    void updateProperty( @Nullable GUID configNodeGuid, Property property );

    default void updateProperty( Property property ) {
        this.updateProperty( null, property );
    }

    void updateTextValue( TextValue textValue, GUID configNodeGuid );

    void updateTextValue( GUID guid, String text, String format );



    void remove( GUID guid );

    void removeReparseLink( GUID guid );

    void removeProperty( GUID guid, String key );

    void removeTextValue( GUID guid );



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


    void copyPropertiesTo( GUID sourceGuid, GUID destinationGuid );

    void copyTextValueTo( GUID sourceGuid, GUID destinationGuid );
}
