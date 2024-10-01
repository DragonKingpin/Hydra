package com.pinecone.hydra.registry;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.json.JSONObject;
import com.pinecone.framework.util.uoi.UOI;
import com.pinecone.hydra.registry.entity.ConfigNode;
import com.pinecone.hydra.registry.entity.NamespaceNode;
import com.pinecone.hydra.registry.entity.Properties;
import com.pinecone.hydra.registry.entity.Property;
import com.pinecone.hydra.registry.entity.RegistryTreeNode;
import com.pinecone.hydra.registry.entity.TextConfigNode;
import com.pinecone.hydra.registry.entity.TextValue;
import com.pinecone.hydra.unit.udtt.entity.TreeNode;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface DistributedRegistry extends Registry {

    String getPath( GUID guid );

    String getFullName( GUID guid );

    GUID put( TreeNode treeNode );

    RegistryTreeNode get( GUID guid );

    RegistryTreeNode getThis( GUID guid );

    RegistryTreeNode getNodeByPath( String path );

    Properties getProperties( GUID guid );

    Properties getProperties( String path );

    NamespaceNode getNamespaceNode( GUID guid );

    NamespaceNode getNamespaceNode( String path );

    GUID queryGUIDByPath( String path );

    GUID queryGUIDByFN  ( String fullName );

    void putProperty( Property property, GUID configNodeGuid );

    void putTextValue( GUID guid, String text, String format );

    void remove( GUID guid );

    void remove( String path );

    void updateProperty( Property property, GUID configNodeGuid );

    void updateTextValue( TextValue textValue, GUID configNodeGuid );

    void updateTextValue( GUID guid, String text, String format );

    Collection<Property > fetchProperties( GUID guid );

    Collection<Property > fetchProperties( String path );

    TextValue getTextValue( GUID guid );

    TextValue getTextValue( String path );

    void removeProperty( GUID guid, String key );


    ConfigNode getConfigNode( GUID guid );


    void removeTextValue( GUID guid );

    List<TreeNode > getChildren( GUID guid );

    List<TreeNode > selectByName( String name );

    void rename( GUID guid, String name );

    List<TreeNode > getAllTreeNode();

    void insertRegistryTreeNode( GUID parentGuid, GUID childGuid );



    NamespaceNode  affirmNamespace        ( String path );

    Properties     affirmProperties       ( String path );

    TextConfigNode affirmTextConfig       ( String path );

    Properties putProperties              ( String path, Map<String, Object > properties );

    TextConfigNode putTextValue           ( String path, String format, String value );



    Object querySelector                  ( String szSelector );
}
