package com.pinecone.hydra.file.entity;

import com.pinecone.hydra.file.source.LocalFrameManipulator;

import java.time.LocalDateTime;

public interface LocalFrame extends Frame {
    LocalDateTime getCreateTime();
    void setCreateTime(LocalDateTime createTime);

    LocalDateTime getUpdateTime();
    void setUpdateTime(LocalDateTime updateTime);

    String getSourceName();
    void setSourceName(String sourceName);

    @Override
    default LocalFrame evinceLocalFrame() {
        return this;
    }

    void setLocalFrameManipulator(LocalFrameManipulator localFrameManipulator);
}
