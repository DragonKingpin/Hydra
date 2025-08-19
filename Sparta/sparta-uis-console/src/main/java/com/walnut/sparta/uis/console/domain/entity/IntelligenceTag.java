package com.walnut.sparta.uis.console.domain.entity;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

import java.time.LocalDateTime;

public interface IntelligenceTag extends Pinenut {
    Long getId();

    void setId(Long id);

    GUID getIntelligenceGuid();

    void setIntelligenceGuid(GUID intelligenceGuid );

    GUID getTagGuid();

    void setTagGuid( GUID tagGuid );

    LocalDateTime getCreateTime();

    void setCreateTime( LocalDateTime createTime );
}
