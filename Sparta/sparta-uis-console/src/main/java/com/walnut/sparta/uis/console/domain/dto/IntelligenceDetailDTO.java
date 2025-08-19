package com.walnut.sparta.uis.console.domain.dto;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.walnut.sparta.uis.console.domain.entity.Intelligence;
import com.walnut.sparta.uis.console.domain.entity.IntelligenceTopic;
import com.walnut.sparta.uis.console.domain.entity.Tags;
import java.time.LocalDateTime;
import java.util.List;

public class IntelligenceDetailDTO   {


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

    private String topicPath;

    private List<Tags> tagsList;

    public IntelligenceDetailDTO(Intelligence intelligence, IntelligenceTopic topic) {
        this.intelligenceGuid = intelligence.getIntelligenceGuid();
        this.title = intelligence.getTitle();
        this.summary = intelligence.getSummary();
        this.sourceUrl = intelligence.getSourceUrl();
        this.collectTime = intelligence.getCollectTime();
        this.updateTime = intelligence.getUpdateTime();
        this.topicName = intelligence.getTopicName();
        this.topicGuid = intelligence.getTopicGuid();
        this.ttl = intelligence.getTtl();
        this.status = intelligence.getStatus();
        this.contentHash = intelligence.getContentHash();

        if (topic != null) {
            this.topicPath = topic.getTopicPath();
        }
    }


    public GUID getIntelligenceGuid() {
        return intelligenceGuid;
    }
    public void setIntelligenceGuid(GUID intelligenceGuid) {
        this.intelligenceGuid = intelligenceGuid;
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

    public LocalDateTime getTtl() {
        return ttl;
    }
    public void setTtl(LocalDateTime ttl) {
        this.ttl = ttl;
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

    public String getTopicPath() {
        return topicPath;
    }
    public void setTopicPath(String topicPath) {
        this.topicPath = topicPath;
    }

    public List<Tags> getTagsList() {
        return tagsList;
    }
    public void setTagsList(List<Tags> tagsList) {
        this.tagsList = tagsList;
    }
}