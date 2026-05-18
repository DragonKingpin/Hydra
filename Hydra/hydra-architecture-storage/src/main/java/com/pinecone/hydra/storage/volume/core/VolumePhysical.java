package com.pinecone.hydra.storage.volume.core;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

import java.time.LocalDateTime;

public class VolumePhysical implements Pinenut {
    protected long                 mnEnumId;
    protected GUID                 mGuid;
    protected GUID                 mDeviceGuid;
    protected String               mszName;
    protected VolumePhysicalType   mPhysicalType;
    protected VolumePhysicalStatus mStatus;
    protected String               mszEndpoint;
    protected String               mszRootPath;
    protected long                 mnCapacityBytes;
    protected long                 mnUsedBytes;
    protected String               mszExtConfig;
    protected LocalDateTime        mCreateTime;
    protected LocalDateTime        mUpdateTime;

    public long getEnumId() {
        return this.mnEnumId;
    }

    public void setEnumId( long enumId ) {
        this.mnEnumId = enumId;
    }

    public GUID getGuid() {
        return this.mGuid;
    }

    public void setGuid( GUID guid ) {
        this.mGuid = guid;
    }

    public GUID getDeviceGuid() {
        return this.mDeviceGuid;
    }

    public void setDeviceGuid( GUID deviceGuid ) {
        this.mDeviceGuid = deviceGuid;
    }

    public String getName() {
        return this.mszName;
    }

    public void setName( String name ) {
        this.mszName = name;
    }

    public VolumePhysicalType getPhysicalType() {
        return this.mPhysicalType;
    }

    public void setPhysicalType( VolumePhysicalType physicalType ) {
        this.mPhysicalType = physicalType;
    }

    public VolumePhysicalStatus getStatus() {
        return this.mStatus;
    }

    public void setStatus( VolumePhysicalStatus status ) {
        this.mStatus = status;
    }

    public String getEndpoint() {
        return this.mszEndpoint;
    }

    public void setEndpoint( String endpoint ) {
        this.mszEndpoint = endpoint;
    }

    public String getRootPath() {
        return this.mszRootPath;
    }

    public void setRootPath( String rootPath ) {
        this.mszRootPath = rootPath;
    }

    public long getCapacityBytes() {
        return this.mnCapacityBytes;
    }

    public void setCapacityBytes( long capacityBytes ) {
        this.mnCapacityBytes = capacityBytes;
    }

    public long getUsedBytes() {
        return this.mnUsedBytes;
    }

    public void setUsedBytes( long usedBytes ) {
        this.mnUsedBytes = usedBytes;
    }

    public String getExtConfig() {
        return this.mszExtConfig;
    }

    public void setExtConfig( String extConfig ) {
        this.mszExtConfig = extConfig;
    }

    public LocalDateTime getCreateTime() {
        return this.mCreateTime;
    }

    public void setCreateTime( LocalDateTime createTime ) {
        this.mCreateTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return this.mUpdateTime;
    }

    public void setUpdateTime( LocalDateTime updateTime ) {
        this.mUpdateTime = updateTime;
    }
}

