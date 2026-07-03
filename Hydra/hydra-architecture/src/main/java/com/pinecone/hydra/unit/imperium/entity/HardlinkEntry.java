package com.pinecone.hydra.unit.imperium.entity;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

import java.time.LocalDateTime;

public class HardlinkEntry implements Pinenut {
    protected GUID          targetNodeGuid;
    protected GUID          parentNodeGuid;
    protected String        linkedType;
    protected String        tagName;
    protected GUID          tagGuid;
    protected LocalDateTime createTime;
    protected LocalDateTime updateTime;

    public GUID getTargetNodeGuid() {
        return this.targetNodeGuid;
    }

    public void setTargetNodeGuid( GUID targetNodeGuid ) {
        this.targetNodeGuid = targetNodeGuid;
    }

    public GUID getParentNodeGuid() {
        return this.parentNodeGuid;
    }

    public void setParentNodeGuid( GUID parentNodeGuid ) {
        this.parentNodeGuid = parentNodeGuid;
    }

    public String getLinkedType() {
        return this.linkedType;
    }

    public void setLinkedType( String linkedType ) {
        this.linkedType = linkedType;
    }

    public String getTagName() {
        return this.tagName;
    }

    public void setTagName( String tagName ) {
        this.tagName = tagName;
    }

    public GUID getTagGuid() {
        return this.tagGuid;
    }

    public void setTagGuid( GUID tagGuid ) {
        this.tagGuid = tagGuid;
    }

    public LocalDateTime getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime( LocalDateTime createTime ) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime( LocalDateTime updateTime ) {
        this.updateTime = updateTime;
    }
}
