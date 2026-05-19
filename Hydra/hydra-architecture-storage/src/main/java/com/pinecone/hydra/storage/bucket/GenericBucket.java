package com.pinecone.hydra.storage.bucket;

import com.pinecone.framework.util.id.GUID;

import java.time.LocalDateTime;

public class GenericBucket implements Bucket {
    protected long          id;
    protected GUID          guid;
    protected String        bucketIdentifier;
    protected String        userIdentifier;
    protected String        bucketName;
    protected GUID          volumeGuid;
    protected String        status;
    protected String        extConfig;
    protected LocalDateTime createTime;
    protected LocalDateTime updateTime;

    public GenericBucket() {
    }

    public GenericBucket( GUID guid, String userIdentifier, String bucketName, GUID volumeGuid ) {
        this( guid, defaultBucketIdentifier( userIdentifier, bucketName ), userIdentifier, bucketName, volumeGuid );
    }

    public GenericBucket( GUID guid, String bucketIdentifier, String userIdentifier, String bucketName, GUID volumeGuid ) {
        this.guid           = guid;
        this.bucketIdentifier = bucketIdentifier;
        this.userIdentifier = userIdentifier;
        this.bucketName     = bucketName;
        this.volumeGuid     = volumeGuid;
        this.status         = "READY";
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
    public String getBucketIdentifier() {
        return this.bucketIdentifier;
    }

    @Override
    public void setBucketIdentifier( String bucketIdentifier ) {
        this.bucketIdentifier = bucketIdentifier;
    }

    @Override
    public String getUserIdentifier() {
        return this.userIdentifier;
    }

    @Override
    public void setUserIdentifier( String userIdentifier ) {
        this.userIdentifier = userIdentifier;
    }

    @Override
    public String getBucketName() {
        return this.bucketName;
    }

    @Override
    public void setBucketName( String bucketName ) {
        this.bucketName = bucketName;
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
    public String getStatus() {
        return this.status;
    }

    @Override
    public void setStatus( String status ) {
        this.status = status;
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

    protected static String defaultBucketIdentifier( String userIdentifier, String bucketName ) {
        return String.valueOf( userIdentifier ) + "_" + String.valueOf( bucketName );
    }
}
