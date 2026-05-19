package com.pinecone.hydra.account.entity;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.system.ko.meta.ElementObject;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;

import java.time.LocalDateTime;

public interface ElementNode extends TreeNode, ElementObject {
    long getEnumId();
    void setEnumId( long enumId );

    LocalDateTime getCreateTime();
    void setCreateTime( LocalDateTime createTime );

    LocalDateTime getUpdateTime();
    void setUpdateTime( LocalDateTime updateTime );

    String getName();
    void setName( String name );

    GUID getGuid();
    void setGuid( GUID guid );

    @Override
    default String objectCategoryName() {
        return "Account";
    }
}
