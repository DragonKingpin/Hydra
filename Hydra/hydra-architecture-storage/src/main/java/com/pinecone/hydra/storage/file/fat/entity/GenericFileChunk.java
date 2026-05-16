package com.pinecone.hydra.storage.file.fat.entity;

import com.pinecone.framework.util.id.GUID;

import java.time.LocalDateTime;

public class GenericFileChunk implements FileChunk {
    protected long          id;
    protected GUID          guid;
    protected GUID          fileGuid;
    protected GUID          bucketGuid;
    protected long          chunkIndex;
    protected long          logicalOffset;
    protected long          chunkSize;
    protected long          validSize;
    protected Long          crc32;
    protected Long          checksum;
    protected String        extConfig;
    protected LocalDateTime createTime;
    protected LocalDateTime updateTime;

    public GenericFileChunk() {
    }

    public GenericFileChunk( GUID guid, GUID fileGuid, long chunkIndex, long logicalOffset, long chunkSize, long validSize ) {
        this.guid          = guid;
        this.fileGuid      = fileGuid;
        this.chunkIndex    = chunkIndex;
        this.logicalOffset = logicalOffset;
        this.chunkSize     = chunkSize;
        this.validSize     = validSize;
    }

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public void setId( long id ) {
        this.id = id;
    }

    @Override
    public GUID getGuid() {
        return this.guid;
    }

    @Override
    public void setGuid( GUID guid ) {
        this.guid = guid;
    }

    @Override
    public GUID getFileGuid() {
        return this.fileGuid;
    }

    @Override
    public void setFileGuid( GUID fileGuid ) {
        this.fileGuid = fileGuid;
    }

    @Override
    public GUID getBucketGuid() {
        return this.bucketGuid;
    }

    @Override
    public void setBucketGuid( GUID bucketGuid ) {
        this.bucketGuid = bucketGuid;
    }

    @Override
    public long getChunkIndex() {
        return this.chunkIndex;
    }

    @Override
    public void setChunkIndex( long chunkIndex ) {
        this.chunkIndex = chunkIndex;
    }

    @Override
    public long getLogicalOffset() {
        return this.logicalOffset;
    }

    @Override
    public void setLogicalOffset( long logicalOffset ) {
        this.logicalOffset = logicalOffset;
    }

    @Override
    public long getChunkSize() {
        return this.chunkSize;
    }

    @Override
    public void setChunkSize( long chunkSize ) {
        this.chunkSize = chunkSize;
    }

    @Override
    public long getValidSize() {
        return this.validSize;
    }

    @Override
    public void setValidSize( long validSize ) {
        this.validSize = validSize;
    }

    @Override
    public Long getCrc32() {
        return this.crc32;
    }

    @Override
    public void setCrc32( Long crc32 ) {
        this.crc32 = crc32;
    }

    @Override
    public Long getChecksum() {
        return this.checksum;
    }

    @Override
    public void setChecksum( Long checksum ) {
        this.checksum = checksum;
    }

    @Override
    public String getExtConfig() {
        return this.extConfig;
    }

    @Override
    public void setExtConfig( String extConfig ) {
        this.extConfig = extConfig;
    }

    @Override
    public LocalDateTime getCreateTime() {
        return this.createTime;
    }

    @Override
    public void setCreateTime( LocalDateTime createTime ) {
        this.createTime = createTime;
    }

    @Override
    public LocalDateTime getUpdateTime() {
        return this.updateTime;
    }

    @Override
    public void setUpdateTime( LocalDateTime updateTime ) {
        this.updateTime = updateTime;
    }
}
