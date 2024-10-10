package com.pinecone.hydra.file.entity;

import com.pinecone.framework.util.id.GUID;

import java.time.LocalDateTime;

public abstract class ArchElementNode implements ElementNode{
    protected long                    enumId;
    protected GUID                    guid;
    protected LocalDateTime           createTime;
    protected LocalDateTime           updateTime;
    protected String                  name;

    protected FileSystemAttributes fileSystemAttributes;


    public long getEnumId() {
        return enumId;
    }


    public void setEnumId(long enumId) {
        this.enumId = enumId;
    }


    public GUID getGuid() {
        return guid;
    }


    public void setGuid(GUID guid) {
        this.guid = guid;
    }


    public LocalDateTime getCreateTime() {
        return createTime;
    }


    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }


    public LocalDateTime getUpdateTime() {
        return updateTime;
    }


    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public FileSystemAttributes getAttributes() {
        return fileSystemAttributes;
    }


    public void setAttributes(FileSystemAttributes fileSystemAttributes) {
        this.fileSystemAttributes = fileSystemAttributes;
    }

    public String toString() {
        return "ArchElementNode{enumId = " + enumId + ", guid = " + guid + ", createTime = " + createTime + ", updateTime = " + updateTime + ", name = " + name + ", attributes = " + fileSystemAttributes + "}";
    }
}
