package com.walnut.sparta.uis.console.domain.entity;

import com.pinecone.framework.util.id.GUID;
import java.time.LocalDateTime;

public class ArchIntelligenceTopic implements IntelligenceTopic {

    private Long id;

    private GUID topicGuid;

    private String topicName;

    private String topicPath;

    private GUID parentGuid;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    public ArchIntelligenceTopic() {
    }

    public ArchIntelligenceTopic(GUID topicGuid, String topicName, GUID parentGuid, String topicPath, LocalDateTime createTime, LocalDateTime updateTime, Long id) {
        this.topicGuid = topicGuid;
        this.topicName = topicName;
        this.parentGuid = parentGuid;
        this.topicPath = topicPath;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.id = id;
    }

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public GUID getTopicGuid() {
        return this.topicGuid;
    }

    @Override
    public void setTopicGuid(GUID topicGuid) {
        this.topicGuid = topicGuid;
    }

    @Override
    public String getTopicName() {
        return this.topicName;
    }

    @Override
    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    @Override
    public String getTopicPath() {
        return this.topicPath;
    }

    @Override
    public void setTopicPath(String topicPath) {
        this.topicPath = topicPath;
    }

    @Override
    public GUID getParentGuid() {
        return this.parentGuid;
    }

    @Override
    public void setParentGuid(GUID parentGuid) {
        this.parentGuid = parentGuid;
    }

    @Override
    public LocalDateTime getCreateTime() {
        return this.createTime;
    }

    @Override
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    @Override
    public LocalDateTime getUpdateTime() {
        return this.updateTime;
    }

    @Override
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}