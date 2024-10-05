package com.pinecone.hydra.file.entity;

import com.pinecone.framework.util.id.GUID;

import java.time.LocalDateTime;

public interface FileNode extends FileTreeNode{
    long getEnumId();

    void setEnumId(long enumId);

    GUID getGuid();

    void setGuid( GUID guid );

    LocalDateTime getCreateTime();

    void setCreateTime( LocalDateTime createTime );

    LocalDateTime getUpdateTime();

    void setUpdateTime( LocalDateTime updateTime );

    String getName();

    void setName( String name );

    LocalDateTime getDeletedTime();
    void setDeletedTime(LocalDateTime deletedTime);

    long getChecksum();
    void setChecksum(long checksum);

    int getParityCheck();
    void setParityCheck(int parityCheck);

    long getSize();
    void setSize(long size);

    void copyValueTo(GUID destinationGuid );
    void copyTo    (GUID destinationGuid);

    @Override
    default FileNode evinceFileNode() {
        return this;
    }
}
