package com.walnut.odin.conduct.schedule.entity;

import java.time.LocalDateTime;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public class TaskInstantaneousSubmitRequest implements Pinenut {

    protected GUID          mTaskGuid;
    protected LocalDateTime mExpectTime;
    protected LocalDateTime mFireTime;
    protected LocalDateTime mBusinessTimeEpoch;
    protected String        mszProcessorName;
    protected boolean       mbAllowAsymmetricImage = true;

    public TaskInstantaneousContext toContext() {
        TaskInstantaneousContext context = new TaskInstantaneousContext();
        context.setTaskGuid( this.mTaskGuid );
        context.setExpectTime( this.mExpectTime );
        context.setFireTime( this.mFireTime );
        context.setBusinessTimeEpoch( this.mBusinessTimeEpoch );
        context.setProcessorName( this.mszProcessorName );
        context.setAllowAsymmetricImage( this.mbAllowAsymmetricImage );
        return context;
    }

    public GUID getTaskGuid() {
        return this.mTaskGuid;
    }

    public void setTaskGuid( GUID taskGuid ) {
        this.mTaskGuid = taskGuid;
    }

    public LocalDateTime getExpectTime() {
        return this.mExpectTime;
    }

    public void setExpectTime( LocalDateTime expectTime ) {
        this.mExpectTime = expectTime;
    }

    public LocalDateTime getFireTime() {
        return this.mFireTime;
    }

    public void setFireTime( LocalDateTime fireTime ) {
        this.mFireTime = fireTime;
    }

    public LocalDateTime getBusinessTimeEpoch() {
        return this.mBusinessTimeEpoch;
    }

    public void setBusinessTimeEpoch( LocalDateTime businessTimeEpoch ) {
        this.mBusinessTimeEpoch = businessTimeEpoch;
    }

    public String getProcessorName() {
        return this.mszProcessorName;
    }

    public void setProcessorName( String szProcessorName ) {
        this.mszProcessorName = szProcessorName;
    }

    public boolean isAllowAsymmetricImage() {
        return this.mbAllowAsymmetricImage;
    }

    public void setAllowAsymmetricImage( boolean allowAsymmetricImage ) {
        this.mbAllowAsymmetricImage = allowAsymmetricImage;
    }

}
