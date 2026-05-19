package com.pinecone.hydra.storage.volume.core;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

import java.time.LocalDateTime;

public class VolumeMount implements Pinenut {
    protected long          mnEnumId;
    protected GUID          mGuid;
    protected GUID          mVolumeGuid;
    protected String        mszOwnerName;
    protected String        mszBucketName;
    protected GUID          mBucketGuid;
    protected String        mszMountPath;
    protected String        mszMountType;
    protected String        mszStatus;
    protected String        mszExtConfig;
    protected LocalDateTime mCreateTime;
    protected LocalDateTime mUpdateTime;

    public long getEnumId() { return this.mnEnumId; }
    public void setEnumId( long enumId ) { this.mnEnumId = enumId; }
    public GUID getGuid() { return this.mGuid; }
    public void setGuid( GUID guid ) { this.mGuid = guid; }
    public GUID getVolumeGuid() { return this.mVolumeGuid; }
    public void setVolumeGuid( GUID volumeGuid ) { this.mVolumeGuid = volumeGuid; }
    public String getOwnerName() { return this.mszOwnerName; }
    public void setOwnerName( String ownerName ) { this.mszOwnerName = ownerName; }
    public String getBucketName() { return this.mszBucketName; }
    public void setBucketName( String bucketName ) { this.mszBucketName = bucketName; }
    public GUID getBucketGuid() { return this.mBucketGuid; }
    public void setBucketGuid( GUID bucketGuid ) { this.mBucketGuid = bucketGuid; }
    public String getMountPath() { return this.mszMountPath; }
    public void setMountPath( String mountPath ) { this.mszMountPath = mountPath; }
    public String getMountType() { return this.mszMountType; }
    public void setMountType( String mountType ) { this.mszMountType = mountType; }
    public String getStatus() { return this.mszStatus; }
    public void setStatus( String status ) { this.mszStatus = status; }
    public String getExtConfig() { return this.mszExtConfig; }
    public void setExtConfig( String extConfig ) { this.mszExtConfig = extConfig; }
    public LocalDateTime getCreateTime() { return this.mCreateTime; }
    public void setCreateTime( LocalDateTime createTime ) { this.mCreateTime = createTime; }
    public LocalDateTime getUpdateTime() { return this.mUpdateTime; }
    public void setUpdateTime( LocalDateTime updateTime ) { this.mUpdateTime = updateTime; }
}

