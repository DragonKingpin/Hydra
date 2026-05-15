package com.pinecone.hydra.storage.file.fat.entity;

import com.pinecone.framework.util.id.GUID;

import java.time.LocalDateTime;

public class GenericFileChunkLocation implements FileChunkLocation {
    protected long          id;
    protected GUID          guid;
    protected GUID          chunkGuid;
    protected int           replicaNo;
    protected GUID          volumeGuid;
    protected FileChunkLocationType locationType;
    protected String        objectKey;
    protected long          objectOffset;
    protected long          volumeOffset;
    protected long          lengthBytes;
    protected String        storageKey;
    protected long          version;
    protected String        extConfig;
    protected LocalDateTime createTime;
    protected LocalDateTime updateTime;

    public GenericFileChunkLocation() {
    }

    public GenericFileChunkLocation( GUID guid, GUID chunkGuid, GUID volumeGuid, long volumeOffset, long lengthBytes ) {
        this.guid         = guid;
        this.chunkGuid    = chunkGuid;
        this.replicaNo    = 0;
        this.volumeGuid   = volumeGuid;
        this.locationType = FileChunkLocationType.VOLUME_BLOCK_EXTENT;
        this.volumeOffset = volumeOffset;
        this.lengthBytes  = lengthBytes;
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
    public GUID getChunkGuid() {
        return this.chunkGuid;
    }

    @Override
    public void setChunkGuid( GUID chunkGuid ) {
        this.chunkGuid = chunkGuid;
    }

    @Override
    public int getReplicaNo() {
        return this.replicaNo;
    }

    @Override
    public void setReplicaNo( int replicaNo ) {
        this.replicaNo = replicaNo;
    }

    @Override
    public GUID getVolumeGuid() {
        return this.volumeGuid;
    }

    @Override
    public void setVolumeGuid( GUID volumeGuid ) {
        this.volumeGuid = volumeGuid;
    }

    @Override
    public FileChunkLocationType getLocationType() {
        return this.locationType;
    }

    @Override
    public void setLocationType( FileChunkLocationType locationType ) {
        this.locationType = locationType;
    }

    @Override
    public String getObjectKey() {
        return this.objectKey;
    }

    @Override
    public void setObjectKey( String objectKey ) {
        this.objectKey = objectKey;
    }

    @Override
    public long getObjectOffset() {
        return this.objectOffset;
    }

    @Override
    public void setObjectOffset( long objectOffset ) {
        this.objectOffset = objectOffset;
    }

    @Override
    public long getVolumeOffset() {
        return this.volumeOffset;
    }

    @Override
    public void setVolumeOffset( long volumeOffset ) {
        this.volumeOffset = volumeOffset;
    }

    @Override
    public long getLengthBytes() {
        return this.lengthBytes;
    }

    @Override
    public void setLengthBytes( long lengthBytes ) {
        this.lengthBytes = lengthBytes;
    }

    @Override
    public String getStorageKey() {
        return this.storageKey;
    }

    @Override
    public void setStorageKey( String storageKey ) {
        this.storageKey = storageKey;
    }

    @Override
    public long getVersion() {
        return this.version;
    }

    @Override
    public void setVersion( long version ) {
        this.version = version;
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
