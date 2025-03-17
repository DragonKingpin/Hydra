package com.pinecone.hydra.task.kom.entity;


import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.task.kom.source.TaskFamilyNode;

import java.time.LocalDateTime;
import java.util.Set;

public interface ServoElement extends ElementNode, TaskFamilyNode {
    Set<String > UnbeanifiedKeys = Set.of( "distributedTreeNode" );

    long getEnumId();
    void setEnumId( long id );

    GUID getGuid();
    void setGuid( GUID guid );

    GUID getMetaGuid();
    void setMetaGuid( GUID metaGuid );

    String getName();
    void setName( String name );

    String getPath();
    void setPath( String path );

    String getType();
    void setType( String type );

    String getAlias();
    void setAlias( String alias );

    String getResourceType();
    void setResourceType( String resourceType );

    LocalDateTime getCreateTime();
    void setCreateTime( LocalDateTime createTime );

    LocalDateTime getUpdateTime();
    void setUpdateTime( LocalDateTime updateTime );
}
