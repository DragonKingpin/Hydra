package com.walnut.sparta.uis.console.domain.entity;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import java.time.LocalDateTime;

public interface Intelligence extends Pinenut {
    Long getId();

    void setId(Long id);

    GUID getIntelligenceGuid();

    void setIntelligenceGuid( GUID intelligenceGuid );

    String getTitle();

    void setTitle( String title );

    String getSummary();

    void setSummary( String summary )
            ;
    String getSourceUrl();

    void setSourceUrl( String sourceUrl );

    LocalDateTime getCollectTime();

    void setCollectTime( LocalDateTime collectTime );

    LocalDateTime getUpdateTime();

    void setUpdateTime( LocalDateTime updateTime );

    String getTopicName();

    void setTopicName( String topicName );

    GUID getTopicGuid();

    void setTopicGuid( GUID topicGuid );

    LocalDateTime getTtl();

    void setTtl( LocalDateTime ttl );

    String getStatus();

    void setStatus( String status );

    String getContentHash();

    void setContentHash( String contentHash );

}