package com.walnut.sparta.uis.console.domain.dto;

import com.pinecone.framework.util.id.GUID;

public class IntelligenceQueryDTO {

    private int page;

    private int size;

    private GUID topicGuid;

    private GUID tagGuid;

    private String status;

    public IntelligenceQueryDTO() {
    }

    public IntelligenceQueryDTO(int page, int size) {
        this.page = page;
        this.size = size;
    }

    public GUID getTopicGuid() {
        return topicGuid;
    }

    public void setTopicGuid(GUID topicGuid) {
        this.topicGuid = topicGuid;
    }

    public GUID getTagGuid() {
        return tagGuid;
    }

    public void setTagGuid(GUID tagGuid) {
        this.tagGuid = tagGuid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }


}