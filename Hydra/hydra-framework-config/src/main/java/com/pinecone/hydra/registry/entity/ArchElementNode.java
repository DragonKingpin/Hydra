package com.pinecone.hydra.registry.entity;

import java.time.LocalDateTime;

import com.pinecone.framework.util.id.GUID;

public abstract class ArchElementNode implements ElementNode {
    protected long                    enumId;
    protected GUID                    guid;
    protected LocalDateTime           createTime;
    protected LocalDateTime           updateTime;
    protected String                  name;

    protected Attributes              attributes;

    @Override
    public long getEnumId() {
        return this.enumId;
    }

    public void setEnumId( long enumId ) {
        this.enumId = enumId;
    }

    @Override
    public GUID getGuid() {
        return this.guid;
    }

    public void setGuid( GUID guid ) {
        this.guid = guid;
    }

    @Override
    public LocalDateTime getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime( LocalDateTime createTime ) {
        this.createTime = createTime;
    }

    @Override
    public LocalDateTime getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime( LocalDateTime updateTime ) {
        this.updateTime = updateTime;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    @Override
    public Attributes getAttributes() {
        return this.attributes;
    }

    public void setAttributes( Attributes attributes ) {
        this.attributes = attributes;
    }
}
