package com.pinecone.hydra.storage.file.journal;

import com.pinecone.framework.util.id.GUID;

import java.time.LocalDateTime;

public class GenericJournal implements Journal {
    protected Long id;
    protected GUID guid;
    protected JournalType journalType;
    protected JournalStatus journalStatus;
    protected GUID fileGuid;
    protected String path;
    protected GUID operatorGuid;
    protected LocalDateTime beginTime;
    protected LocalDateTime commitTime;
    protected String errorMessage;
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
    public JournalType getJournalType() { return this.journalType; }
    @Override
    public void setJournalType( JournalType journalType ) { this.journalType = journalType; }
    @Override
    public JournalStatus getJournalStatus() { return this.journalStatus; }
    @Override
    public void setJournalStatus( JournalStatus journalStatus ) { this.journalStatus = journalStatus; }
    @Override
    public GUID getFileGuid() { return this.fileGuid; }
    @Override
    public void setFileGuid( GUID fileGuid ) { this.fileGuid = fileGuid; }
    @Override
    public String getPath() { return this.path; }
    @Override
    public void setPath( String path ) { this.path = path; }
    @Override
    public GUID getOperatorGuid() { return this.operatorGuid; }
    @Override
    public void setOperatorGuid( GUID operatorGuid ) { this.operatorGuid = operatorGuid; }
    @Override
    public LocalDateTime getBeginTime() { return this.beginTime; }
    @Override
    public void setBeginTime( LocalDateTime beginTime ) { this.beginTime = beginTime; }
    @Override
    public LocalDateTime getCommitTime() { return this.commitTime; }
    @Override
    public void setCommitTime( LocalDateTime commitTime ) { this.commitTime = commitTime; }
    @Override
    public String getErrorMessage() { return this.errorMessage; }
    @Override
    public void setErrorMessage( String errorMessage ) { this.errorMessage = errorMessage; }
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
