package com.pinecone.hydra.registry;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.template.UniformTemplateRenderer;
import com.pinecone.hydra.registry.entity.ConfigNode;
import com.pinecone.hydra.registry.entity.Property;
import com.pinecone.hydra.registry.entity.RegistryTreeNode;
import com.pinecone.hydra.registry.entity.TextValue;
import com.pinecone.hydra.unit.udtt.entity.TreeNode;

import java.util.List;

public interface DistributedRegistry extends Registry {

    String getPath( GUID guid );

    GUID put( TreeNode treeNode );

    RegistryTreeNode get(GUID guid );

    RegistryTreeNode getThis( GUID guid );

    RegistryTreeNode getNodeByPath( String path );

    GUID getGUIDByPath( String path );

    void putProperties( Property property, GUID configNodeGuid );

    void putTextValue( GUID guid,String text,String type );

    void remove( GUID guid );

    void updateProperty( Property property, GUID configNodeGuid );

    void updateTextValue( GUID guid,String text,String type );

    List<Property > getProperties( GUID guid );

    List<Property > getProperties( String path );

    TextValue getTextValue( GUID guid );

    void removeProperty( GUID guid,String key );


    ConfigNode getConfigNodeByGuid( GUID guid );


    // getConfig( String path );

    // getPropertyConfig( String path );

    // TextConfigNode getTextConfig( String path );

    // getTextConfig( String path );

    // getNamespace( String path );

    void removeTextValue( GUID guid );

    List<TreeNode > getChildConf( GUID guid );

    List<TreeNode > selectByName( String name );

    void rename( String name,GUID guid );

    List<TreeNode > getAllTreeNode();

    void insertRegistryTreeNode( GUID parentGuid, GUID childGuid );
}
