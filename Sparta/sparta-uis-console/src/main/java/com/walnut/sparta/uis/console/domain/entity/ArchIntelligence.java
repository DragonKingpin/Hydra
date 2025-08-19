package com.walnut.sparta.uis.console.domain.entity;

import com.pinecone.framework.util.id.GUID;
import java.time.LocalDateTime;

public class ArchIntelligence implements Intelligence {

    private Long id;

    private GUID intelligenceGuid;

    private String title;

    private String summary;

    private String sourceUrl;

    private LocalDateTime collectTime;

    private LocalDateTime updateTime;

    private String topicName;

    private GUID topicGuid;

    private LocalDateTime ttl;

    private String status;

    private String contentHash;

    public ArchIntelligence() {
    }

    public ArchIntelligence(Long id, GUID intelligenceGuid, String title, String summary, String sourceUrl, LocalDateTime collectTime, LocalDateTime updateTime, String topicName, GUID topicGuid, LocalDateTime ttl, String status, String contentHash) {
        this.id = id;
        this.intelligenceGuid = intelligenceGuid;
        this.title = title;
        this.summary = summary;
        this.sourceUrl = sourceUrl;
        this.collectTime = collectTime;
        this.updateTime = updateTime;
        this.topicName = topicName;
        this.topicGuid = topicGuid;
        this.ttl = ttl;
        this.status = status;
        this.contentHash = contentHash;
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
    public GUID getIntelligenceGuid() {
        return this.intelligenceGuid;
    }

    @Override
    public void setIntelligenceGuid(GUID intelligenceGuid) {
        this.intelligenceGuid = intelligenceGuid;
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getSummary() {
        return this.summary;
    }

    @Override
    public void setSummary(String summary) {
        this.summary = summary;
    }

    @Override
    public String getSourceUrl() {
        return this.sourceUrl;
    }

    @Override
    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    @Override
    public LocalDateTime getCollectTime() {
        return this.collectTime;
    }

    @Override
    public void setCollectTime(LocalDateTime collectTime) {
        this.collectTime = collectTime;
    }

    @Override
    public LocalDateTime getUpdateTime() {
        return this.updateTime;
    }

    @Override
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String getTopicName() {
        return this.topicName;
    }

    @Override
    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public GUID getTopicGuid() {
        return this.topicGuid;
    }

    public void setTopicGuid(GUID topicGuid) {
        this.topicGuid = topicGuid;
    }

    @Override
    public LocalDateTime getTtl() {
        return this.ttl;
    }

    @Override
    public void setTtl(LocalDateTime ttl) {
        this.ttl = ttl;
    }

    @Override
    public String getStatus() {
        return this.status;
    }

    @Override
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String getContentHash() {
        return this.contentHash;
    }

    @Override
    public void setContentHash(String contentHash) {
        this.contentHash = contentHash;
    }
}