package com.pinecone.hydra.storage.volume.core;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

import java.time.LocalDateTime;

public class VolumeEvent implements Pinenut {
    protected long          mnEnumId;
    protected GUID          mGuid;
    protected GUID          mVolumeGuid;
    protected GUID          mPhysicalGuid;
    protected String        mszEventType;
    protected String        mszEventStatus;
    protected String        mszEventPayload;
    protected LocalDateTime mCreateTime;
    protected LocalDateTime mUpdateTime;

    public long getEnumId() { return this.mnEnumId; }
    public void setEnumId( long enumId ) { this.mnEnumId = enumId; }
    public GUID getGuid() { return this.mGuid; }
    public void setGuid( GUID guid ) { this.mGuid = guid; }
    public GUID getVolumeGuid() { return this.mVolumeGuid; }
    public void setVolumeGuid( GUID volumeGuid ) { this.mVolumeGuid = volumeGuid; }
    public GUID getPhysicalGuid() { return this.mPhysicalGuid; }
    public void setPhysicalGuid( GUID physicalGuid ) { this.mPhysicalGuid = physicalGuid; }
    public String getEventType() { return this.mszEventType; }
    public void setEventType( String eventType ) { this.mszEventType = eventType; }
    public String getEventStatus() { return this.mszEventStatus; }
    public void setEventStatus( String eventStatus ) { this.mszEventStatus = eventStatus; }
    public String getEventPayload() { return this.mszEventPayload; }
    public void setEventPayload( String eventPayload ) { this.mszEventPayload = eventPayload; }
    public LocalDateTime getCreateTime() { return this.mCreateTime; }
    public void setCreateTime( LocalDateTime createTime ) { this.mCreateTime = createTime; }
    public LocalDateTime getUpdateTime() { return this.mUpdateTime; }
    public void setUpdateTime( LocalDateTime updateTime ) { this.mUpdateTime = updateTime; }
}

