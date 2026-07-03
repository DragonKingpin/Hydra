package com.walnut.odin.conduct.entity;

import java.time.LocalDateTime;
import java.util.Map;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.json.homotype.BeanMapDecoder;

public class GenericInstanceExecAudit implements InstanceExecAudit {

    protected long          id;
    protected GUID          taskGuid;
    protected GUID          instanceGuid;
    protected int           sequenceCnt;
    protected int           currentRetryNumber;
    protected String        auditState;
    protected String        auditType;
    protected String        message;
    protected String        artifactUri;
    protected String        artifactDigest;
    protected String        payload;
    protected LocalDateTime startTime;
    protected LocalDateTime finishTime;
    protected LocalDateTime createTime;
    protected LocalDateTime updateTime;

    public GenericInstanceExecAudit() {
    }

    public GenericInstanceExecAudit( Map<String, Object> joEntity ) {
        BeanMapDecoder.BasicDecoder.decode( this, joEntity );
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
    public GUID getTaskGuid() {
        return this.taskGuid;
    }

    @Override
    public void setTaskGuid( GUID taskGuid ) {
        this.taskGuid = taskGuid;
    }

    @Override
    public GUID getInstanceGuid() {
        return this.instanceGuid;
    }

    @Override
    public void setInstanceGuid( GUID instanceGuid ) {
        this.instanceGuid = instanceGuid;
    }

    @Override
    public int getSequenceCnt() {
        return this.sequenceCnt;
    }

    @Override
    public void setSequenceCnt( int sequenceCnt ) {
        this.sequenceCnt = sequenceCnt;
    }

    @Override
    public int getCurrentRetryNumber() {
        return this.currentRetryNumber;
    }

    @Override
    public void setCurrentRetryNumber( int currentRetryNumber ) {
        this.currentRetryNumber = currentRetryNumber;
    }

    @Override
    public String getAuditState() {
        return this.auditState;
    }

    @Override
    public void setAuditState( String auditState ) {
        this.auditState = auditState;
    }

    @Override
    public String getAuditType() {
        return this.auditType;
    }

    @Override
    public void setAuditType( String auditType ) {
        this.auditType = auditType;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public void setMessage( String message ) {
        this.message = message;
    }

    @Override
    public String getArtifactUri() {
        return this.artifactUri;
    }

    @Override
    public void setArtifactUri( String artifactUri ) {
        this.artifactUri = artifactUri;
    }

    @Override
    public String getArtifactDigest() {
        return this.artifactDigest;
    }

    @Override
    public void setArtifactDigest( String artifactDigest ) {
        this.artifactDigest = artifactDigest;
    }

    @Override
    public String getPayload() {
        return this.payload;
    }

    @Override
    public void setPayload( String payload ) {
        this.payload = payload;
    }

    @Override
    public LocalDateTime getStartTime() {
        return this.startTime;
    }

    @Override
    public void setStartTime( LocalDateTime startTime ) {
        this.startTime = startTime;
    }

    @Override
    public LocalDateTime getFinishTime() {
        return this.finishTime;
    }

    @Override
    public void setFinishTime( LocalDateTime finishTime ) {
        this.finishTime = finishTime;
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
}
