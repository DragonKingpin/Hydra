package com.pinecone.hydra.registry;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.registry.entity.Property;
import com.pinecone.hydra.registry.entity.TextValue;
import com.pinecone.hydra.unit.udtt.entity.TreeNode;

import java.util.List;

public interface DistributedRegistry extends Registry {

    String getPath( GUID guid );

    GUID insert( TreeNode treeNode );

    TreeNode get( GUID guid );

    TreeNode getThis( GUID guid );

    TreeNode getNodeByPath( String path );

    void insertProperties(Property property, GUID configNodeGuid );

    void insertTextValue( GUID guid,String text,String type );

    void remove( GUID guid );

    void updateProperty(Property property, GUID configNodeGuid );

    void updateTextValue( GUID guid,String text,String type );

    List<Property> getProperties(GUID guid);

    TextValue getTextValue(GUID guid);

    void removeProperty(GUID guid,String key);

    void removeTextValue(GUID guid);
    List<TreeNode> getChildConf(GUID guid);
    List<TreeNode> selectByName(String name);
    void rename(String name,GUID guid);
    List<TreeNode> getAllTreeNode();
}
