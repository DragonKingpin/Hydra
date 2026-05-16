package com.pinecone.hydra.storage.file.fat.entity;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

import java.time.LocalDateTime;

public interface FileChunkLocation extends Pinenut {
    long getId();

    void setId( long id );

    GUID getGuid();

    void setGuid( GUID guid );

    GUID getChunkGuid();

    void setChunkGuid( GUID chunkGuid );

    GUID getBucketGuid();

    void setBucketGuid( GUID bucketGuid );

    int getReplicaNo();

    void setReplicaNo( int replicaNo );

    GUID getVolumeGuid();

    void setVolumeGuid( GUID volumeGuid );

    FileChunkLocationType getLocationType();

    void setLocationType( FileChunkLocationType locationType );

    String getObjectKey();

    void setObjectKey( String objectKey );

    long getObjectOffset();

    void setObjectOffset( long objectOffset );

    long getVolumeOffset();

    void setVolumeOffset( long volumeOffset );

    long getLengthBytes();

    void setLengthBytes( long lengthBytes );

    String getStorageKey();

    void setStorageKey( String storageKey );

    long getVersion();

    void setVersion( long version );

    String getExtConfig();

    void setExtConfig( String extConfig );

    LocalDateTime getCreateTime();

    void setCreateTime( LocalDateTime createTime );

    LocalDateTime getUpdateTime();

    void setUpdateTime( LocalDateTime updateTime );
}
