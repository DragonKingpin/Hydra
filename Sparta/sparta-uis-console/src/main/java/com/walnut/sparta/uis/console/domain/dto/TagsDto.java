package com.walnut.sparta.uis.console.domain.dto;

import java.time.LocalDateTime;

public class TagsDto {
    private String tagName;

    private String tagCategory;

    private String description;

    private LocalDateTime createTime;

    public TagsDto() {
    }

    public TagsDto(String tagName, String tagCategory, String description, LocalDateTime createTime) {
        this.tagName = tagName;
        this.tagCategory = tagCategory;
        this.description = description;
        this.createTime = createTime;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getTagCategory() {
        return tagCategory;
    }

    public void setTagCategory(String tagCategory) {
        this.tagCategory = tagCategory;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}
