package com.pinecone.hydra.file.entity;

import com.pinecone.framework.util.id.GUID;

import java.time.LocalDateTime;

public interface FileNode extends ElementNode{
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

    FileMeta getFileMeta();
    void setFileMeta(FileMeta fileMeta);
    GUID getDataAffinityGuid();


    @Override
    default FileNode evinceFileNode() {
        return this;
    }
    void fragmentation( long size );
    void removeFrame();
}
