package com.pinecone.hydra.registry;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.registry.entity.ConfigNode;
import com.pinecone.hydra.registry.entity.NamespaceNode;
import com.pinecone.hydra.registry.entity.Properties;
import com.pinecone.hydra.registry.entity.Property;
import com.pinecone.hydra.registry.entity.RegistryTreeNode;
import com.pinecone.hydra.registry.entity.TextValue;
import com.pinecone.hydra.unit.udtt.entity.TreeNode;

import java.util.List;

public interface DistributedRegistry extends Registry {

    String getPath( GUID guid );

    String getFullName( GUID guid );

    GUID put( TreeNode treeNode );

    RegistryTreeNode get( GUID guid );

    RegistryTreeNode getThis( GUID guid );

    Properties getProperties( GUID guid );

    NamespaceNode getNamespaceNode( GUID guid );

    GUID queryGUIDByPath( String path );

    GUID queryGUIDByFN  ( String fullName );

    List<Property > fetchProperties( GUID guid );

    TextValue getTextValue( GUID guid );

    ConfigNode getConfigNode( GUID guid );




    void putProperty( Property property, GUID configNodeGuid );

    void putTextValue( GUID guid, String text, String format );

    void updateProperty( Property property, GUID configNodeGuid );

    void updateTextValue( TextValue textValue, GUID configNodeGuid );

    void updateTextValue( GUID guid, String text, String format );



    void remove( GUID guid );

    void removeProperty( GUID guid, String key );

    void removeTextValue( GUID guid );



    List<TreeNode > getChildren( GUID guid );

    void rename( GUID guid, String name );

    List<TreeNode > getAllTreeNode();



    /** 断言，确保节点唯一拥有关系*/
    void affirmOwnedNode( GUID parentGuid, GUID childGuid  );

    void newHardLink    ( GUID sourceGuid, GUID targetGuid );

    /** 断言，确保节点唯一拥有关系*/
    void setDataAffinityGuid ( GUID childGuid, GUID parentGuid  );




    Object querySelector                  ( String szSelector );
}
