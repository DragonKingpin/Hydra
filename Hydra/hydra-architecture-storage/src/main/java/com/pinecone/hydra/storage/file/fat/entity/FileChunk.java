package com.pinecone.hydra.storage.file.fat.entity;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

import java.time.LocalDateTime;

public interface FileChunk extends Pinenut {
    long getId();

    void setId( long id );

    GUID getGuid();

    void setGuid( GUID guid );

    GUID getFileGuid();

    void setFileGuid( GUID fileGuid );

    long getChunkIndex();

    void setChunkIndex( long chunkIndex );

    long getLogicalOffset();

    void setLogicalOffset( long logicalOffset );

    long getChunkSize();

    void setChunkSize( long chunkSize );

    long getValidSize();

    void setValidSize( long validSize );

    Long getCrc32();

    void setCrc32( Long crc32 );

    Long getChecksum();

    void setChecksum( Long checksum );

    String getExtConfig();

    void setExtConfig( String extConfig );

    LocalDateTime getCreateTime();

    void setCreateTime( LocalDateTime createTime );

    LocalDateTime getUpdateTime();

    void setUpdateTime( LocalDateTime updateTime );
}
