package com.walnut.sparta.uis.console.domain.entity;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import java.time.LocalDateTime;

public interface IntelligenceTopic extends Pinenut {
    Long getId();

    void setId( Long id );

    GUID getTopicGuid();

    void setTopicGuid(GUID topicGuid);

    String getTopicName();

    void setTopicName(String topicName);

    String getTopicPath();

    void setTopicPath(String topicPath);

    GUID getParentGuid();

    void setParentGuid(GUID parentGuid);

    LocalDateTime getCreateTime();

    void setCreateTime( LocalDateTime createTime );

    LocalDateTime getUpdateTime();

    void setUpdateTime( LocalDateTime updateTime );
}