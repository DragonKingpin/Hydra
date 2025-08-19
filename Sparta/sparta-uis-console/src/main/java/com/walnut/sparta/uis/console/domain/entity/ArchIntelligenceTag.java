package com.walnut.sparta.uis.console.domain.entity;

import com.pinecone.framework.util.id.GUID;

import java.time.LocalDateTime;

public class ArchIntelligenceTag implements IntelligenceTag{

    private Long id;

    private GUID intelligenceGuid;

    private GUID tagGuid;

    private LocalDateTime createTime;

    public ArchIntelligenceTag() {
    }

    public ArchIntelligenceTag(Long id, GUID intelligenceGuid, GUID tagGuid, LocalDateTime createTime) {
        this.id = id;
        this.intelligenceGuid = intelligenceGuid;
        this.tagGuid = tagGuid;
        this.createTime = createTime;
    }

    @Override
    public Long getId() {
        return this.id;
    }
    @Override
    public void setId( Long id ) {
        this.id = id;
    }
    @Override
    public GUID getIntelligenceGuid() {
        return this.intelligenceGuid;
    }
    @Override
    public void setIntelligenceGuid( GUID intelligenceGuid ) {
        this.intelligenceGuid = intelligenceGuid;
    }
    @Override
    public GUID getTagGuid() {
        return this.tagGuid;
    }
    @Override
    public void setTagGuid( GUID tagGuid ) {
        this.tagGuid = tagGuid;
    }
    @Override
    public LocalDateTime getCreateTime() {
        return this.createTime;
    }
    @Override
    public void setCreateTime( LocalDateTime createTime ) {
        this.createTime = createTime;
    }
}
