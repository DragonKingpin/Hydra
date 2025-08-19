package com.walnut.sparta.uis.console.domain.entity;


import com.pinecone.framework.util.id.GUID;
import java.time.LocalDateTime;

public class Tags implements ITags {

    private Long id;

    private String tagName;

    private String tagCategory;

    private String description;

    private LocalDateTime createTime;

    private GUID tagGuid;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId( Long id ) {
        this.id = id;
    }

    @Override
    public String getTagName() {
        return this.tagName;
    }

    @Override
    public void setTagName( String tagName ) {
        this.tagName = tagName;
    }

    @Override
    public String getTagCategory() {
        return this.tagCategory;
    }

    @Override
    public void setTagCategory( String tagCategory ) {
        this.tagCategory = tagCategory;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public void setDescription( String description ) {
        this.description = description;
    }

    @Override
    public LocalDateTime getCreateTime() {
        return this.createTime;
    }

    @Override
    public void setCreateTime( LocalDateTime createTime ) {
        this.createTime = createTime;
    }

    @Override
    public GUID getTagGuid() {
        return this.tagGuid;
    }

    @Override
    public void setTagGuid( GUID tagGuid ) {
        this.tagGuid = tagGuid;
    }
}

