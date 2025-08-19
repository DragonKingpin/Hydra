package com.walnut.sparta.uis.console.domain.dto;

import com.pinecone.framework.util.id.GUID;

import java.time.LocalDateTime;

public class IntelligenceDto {

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

    public IntelligenceDto(String title, String summary, String sourceUrl, LocalDateTime collectTime, LocalDateTime updateTime, String topicName, GUID topicGuid, LocalDateTime ttl, String status, String contentHash) {
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

    public IntelligenceDto() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public LocalDateTime getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(LocalDateTime collectTime) {
        this.collectTime = collectTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public GUID getTopicGuid() {
        return topicGuid;
    }

    public void setTopicGuid(GUID topicGuid) {
        this.topicGuid = topicGuid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getContentHash() {
        return contentHash;
    }

    public void setContentHash(String contentHash) {
        this.contentHash = contentHash;
    }

    public LocalDateTime getTtl() {
        return ttl;
    }

    public void setTtl(LocalDateTime ttl) {
        this.ttl = ttl;
    }
}
