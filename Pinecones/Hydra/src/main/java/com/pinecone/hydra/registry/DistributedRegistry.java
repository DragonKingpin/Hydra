package com.pinecone.hydra.registry;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.registry.entity.Properties;
import com.pinecone.hydra.unit.udtt.entity.TreeNode;

public interface DistributedRegistry extends Registry {

    String getPath( GUID guid );

    GUID insert( TreeNode treeNode );

    TreeNode get( GUID guid );

    TreeNode getThis( GUID guid );

    TreeNode getNodeByPath( String path );

    void insertProperties( Properties properties,GUID configNodeGuid );

    void insertTextValue( GUID guid,String text,String type );

    void remove( GUID guid );

}
