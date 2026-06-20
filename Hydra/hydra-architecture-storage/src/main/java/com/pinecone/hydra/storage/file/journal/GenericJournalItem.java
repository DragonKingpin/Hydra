package com.pinecone.hydra.storage.file.journal;

import com.pinecone.framework.util.id.GUID;

import java.time.LocalDateTime;

public class GenericJournalItem implements JournalItem {
    protected Long id;
    protected GUID guid;
    protected GUID bucketGuid;
    protected GUID journalGuid;
    protected JournalItemType itemType;
    protected JournalItemStatus itemStatus;
    protected GUID targetGuid;
    protected GUID fileGuid;
    protected GUID chunkGuid;
    protected GUID locationGuid;
    protected GUID volumeGuid;
    protected String objectKey;
    protected long volumeOffset;
    protected long lengthBytes;
    protected int ordinalNo;
    protected String oldPayload;
    protected String newPayload;
    protected String extConfig;
    protected LocalDateTime createTime;
    protected LocalDateTime updateTime;

    @Override
    public Long getId() { return this.id; }
    @Override
    public void setId( Long id ) { this.id = id; }
    @Override
    public GUID getGuid() { return this.guid; }
    @Override
    public void setGuid( GUID guid ) { this.guid = guid; }
    @Override
    public GUID getBucketGuid() { return this.bucketGuid; }
    @Override
    public void setBucketGuid( GUID bucketGuid ) { this.bucketGuid = bucketGuid; }
    @Override
    public GUID getJournalGuid() { return this.journalGuid; }
    @Override
    public void setJournalGuid( GUID journalGuid ) { this.journalGuid = journalGuid; }
    @Override
    public JournalItemType getItemType() { return this.itemType; }
    @Override
    public void setItemType( JournalItemType itemType ) { this.itemType = itemType; }
    @Override
    public JournalItemStatus getItemStatus() { return this.itemStatus; }
    @Override
    public void setItemStatus( JournalItemStatus itemStatus ) { this.itemStatus = itemStatus; }
    @Override
    public GUID getTargetGuid() { return this.targetGuid; }
    @Override
    public void setTargetGuid( GUID targetGuid ) { this.targetGuid = targetGuid; }
    @Override
    public GUID getFileGuid() { return this.fileGuid; }
    @Override
    public void setFileGuid( GUID fileGuid ) { this.fileGuid = fileGuid; }
    @Override
    public GUID getChunkGuid() { return this.chunkGuid; }
    @Override
    public void setChunkGuid( GUID chunkGuid ) { this.chunkGuid = chunkGuid; }
    @Override
    public GUID getLocationGuid() { return this.locationGuid; }
    @Override
    public void setLocationGuid( GUID locationGuid ) { this.locationGuid = locationGuid; }
    @Override
    public GUID getVolumeGuid() { return this.volumeGuid; }
    @Override
    public void setVolumeGuid( GUID volumeGuid ) { this.volumeGuid = volumeGuid; }
    @Override
    public String getObjectKey() { return this.objectKey; }
    @Override
    public void setObjectKey( String objectKey ) { this.objectKey = objectKey; }
    @Override
    public long getVolumeOffset() { return this.volumeOffset; }
    @Override
    public void setVolumeOffset( long volumeOffset ) { this.volumeOffset = volumeOffset; }
    @Override
    public long getLengthBytes() { return this.lengthBytes; }
    @Override
    public void setLengthBytes( long lengthBytes ) { this.lengthBytes = lengthBytes; }
    @Override
    public int getOrdinalNo() { return this.ordinalNo; }
    @Override
    public void setOrdinalNo( int ordinalNo ) { this.ordinalNo = ordinalNo; }
    @Override
    public String getOldPayload() { return this.oldPayload; }
    @Override
    public void setOldPayload( String oldPayload ) { this.oldPayload = oldPayload; }
    @Override
    public String getNewPayload() { return this.newPayload; }
    @Override
    public void setNewPayload( String newPayload ) { this.newPayload = newPayload; }
    @Override
    public String getExtConfig() { return this.extConfig; }
    @Override
    public void setExtConfig( String extConfig ) { this.extConfig = extConfig; }
    @Override
    public LocalDateTime getCreateTime() { return this.createTime; }
    @Override
    public void setCreateTime( LocalDateTime createTime ) { this.createTime = createTime; }
    @Override
    public LocalDateTime getUpdateTime() { return this.updateTime; }
    @Override
    public void setUpdateTime( LocalDateTime updateTime ) { this.updateTime = updateTime; }
}
