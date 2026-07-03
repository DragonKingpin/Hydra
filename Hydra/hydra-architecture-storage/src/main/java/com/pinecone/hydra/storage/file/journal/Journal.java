package com.pinecone.hydra.storage.file.journal;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

import java.time.LocalDateTime;

public interface Journal extends Pinenut {
    Long getId();
    void setId( Long id );

    GUID getGuid();
    void setGuid( GUID guid );

    GUID getBucketGuid();
    void setBucketGuid( GUID bucketGuid );

    JournalType getJournalType();
    void setJournalType( JournalType journalType );

    JournalStatus getJournalStatus();
    void setJournalStatus( JournalStatus journalStatus );

    GUID getFileGuid();
    void setFileGuid( GUID fileGuid );

    String getPath();
    void setPath( String path );

    GUID getOperatorGuid();
    void setOperatorGuid( GUID operatorGuid );

    LocalDateTime getBeginTime();
    void setBeginTime( LocalDateTime beginTime );

    LocalDateTime getCommitTime();
    void setCommitTime( LocalDateTime commitTime );

    String getErrorMessage();
    void setErrorMessage( String errorMessage );

    String getExtConfig();
    void setExtConfig( String extConfig );

    LocalDateTime getCreateTime();
    void setCreateTime( LocalDateTime createTime );

    LocalDateTime getUpdateTime();
    void setUpdateTime( LocalDateTime updateTime );
}
