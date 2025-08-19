package com.walnut.sparta.uis.console.domain.entity;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import java.time.LocalDateTime;

public interface ITags extends Pinenut {
    Long getId();

    void setId( Long id );

    String getTagName();

    void setTagName( String tagName );

    String getTagCategory();

    void setTagCategory( String tagCategory );

    String getDescription();

    void setDescription( String description );

    LocalDateTime getCreateTime();

    void setCreateTime( LocalDateTime createTime );

    GUID getTagGuid();

    void setTagGuid( GUID tagGuid );
}