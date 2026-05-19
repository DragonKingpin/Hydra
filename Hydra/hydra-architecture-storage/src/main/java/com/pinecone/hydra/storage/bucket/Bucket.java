package com.pinecone.hydra.storage.bucket;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

import java.time.LocalDateTime;

public interface Bucket extends Pinenut {
    long getId();

    void setId( long id );

    GUID getGuid();

    void setGuid( GUID guid );

    String getBucketIdentifier();

    void setBucketIdentifier( String bucketIdentifier );

    String getUserIdentifier();

    void setUserIdentifier( String userIdentifier );

    String getBucketName();

    void setBucketName( String bucketName );

    GUID getVolumeGuid();

    void setVolumeGuid( GUID volumeGuid );

    String getStatus();

    void setStatus( String status );

    String getExtConfig();

    void setExtConfig( String extConfig );

    LocalDateTime getCreateTime();

    void setCreateTime( LocalDateTime createTime );

    LocalDateTime getUpdateTime();

    void setUpdateTime( LocalDateTime updateTime );
}
