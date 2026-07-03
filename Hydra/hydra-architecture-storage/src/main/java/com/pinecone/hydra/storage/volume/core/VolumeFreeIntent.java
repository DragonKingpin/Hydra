package com.pinecone.hydra.storage.volume.core;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

import java.time.LocalDateTime;

public class VolumeFreeIntent implements Pinenut {
    protected long                   mnEnumId;
    protected GUID                   mGuid;
    protected GUID                   mVolumeGuid;
    protected long                   mnVolumeOffset;
    protected long                   mnLengthBytes;
    protected GUID                   mSourceLocationGuid;
    protected VolumeFreeIntentStatus mStatus;
    protected String                 mszMessage;
    protected LocalDateTime          mCreateTime;
    protected LocalDateTime          mUpdateTime;

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

    public GUID getVolumeGuid() {
        return this.mVolumeGuid;
    }

    public void setVolumeGuid( GUID volumeGuid ) {
        this.mVolumeGuid = volumeGuid;
    }

    public long getVolumeOffset() {
        return this.mnVolumeOffset;
    }

    public void setVolumeOffset( long volumeOffset ) {
        this.mnVolumeOffset = volumeOffset;
    }

    public long getLengthBytes() {
        return this.mnLengthBytes;
    }

    public void setLengthBytes( long lengthBytes ) {
        this.mnLengthBytes = lengthBytes;
    }

    public GUID getSourceLocationGuid() {
        return this.mSourceLocationGuid;
    }

    public void setSourceLocationGuid( GUID sourceLocationGuid ) {
        this.mSourceLocationGuid = sourceLocationGuid;
    }

    public VolumeFreeIntentStatus getStatus() {
        return this.mStatus;
    }

    public void setStatus( VolumeFreeIntentStatus status ) {
        this.mStatus = status;
    }

    public String getMessage() {
        return this.mszMessage;
    }

    public void setMessage( String message ) {
        this.mszMessage = message;
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
