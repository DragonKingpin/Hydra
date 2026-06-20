package com.pinecone.hydra.storage.file.journal;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

import java.time.LocalDateTime;

public interface JournalItem extends Pinenut {
    Long getId();
    void setId( Long id );
    GUID getGuid();
    void setGuid( GUID guid );
    GUID getBucketGuid();
    void setBucketGuid( GUID bucketGuid );
    GUID getJournalGuid();
    void setJournalGuid( GUID journalGuid );
    JournalItemType getItemType();
    void setItemType( JournalItemType itemType );
    JournalItemStatus getItemStatus();
    void setItemStatus( JournalItemStatus itemStatus );
    GUID getTargetGuid();
    void setTargetGuid( GUID targetGuid );
    GUID getFileGuid();
    void setFileGuid( GUID fileGuid );
    GUID getChunkGuid();
    void setChunkGuid( GUID chunkGuid );
    GUID getLocationGuid();
    void setLocationGuid( GUID locationGuid );
    GUID getVolumeGuid();
    void setVolumeGuid( GUID volumeGuid );
    String getObjectKey();
    void setObjectKey( String objectKey );
    long getVolumeOffset();
    void setVolumeOffset( long volumeOffset );
    long getLengthBytes();
    void setLengthBytes( long lengthBytes );
    int getOrdinalNo();
    void setOrdinalNo( int ordinalNo );
    String getOldPayload();
    void setOldPayload( String oldPayload );
    String getNewPayload();
    void setNewPayload( String newPayload );
    String getExtConfig();
    void setExtConfig( String extConfig );
    LocalDateTime getCreateTime();
    void setCreateTime( LocalDateTime createTime );
    LocalDateTime getUpdateTime();
    void setUpdateTime( LocalDateTime updateTime );
}
