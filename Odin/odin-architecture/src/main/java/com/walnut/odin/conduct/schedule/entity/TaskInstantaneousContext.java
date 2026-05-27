package com.walnut.odin.conduct.schedule.entity;

import java.time.LocalDateTime;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.walnut.odin.task.troll.LaunchFeature;

public class TaskInstantaneousContext implements Pinenut {

    protected GUID                mTaskGuid;
    protected LocalDateTime       mExpectTime;
    protected LocalDateTime       mFireTime;
    protected LocalDateTime       mBusinessTimeEpoch;
    protected String              mszProcessorName;
    protected boolean             mbAllowAsymmetricImage = true;

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

    public LaunchFeature toLaunchFeature() {
        LaunchFeature launchFeature = new LaunchFeature();
        launchFeature.withAllowAsymmetricImage( this.mbAllowAsymmetricImage );

        if ( this.mszProcessorName != null && !this.mszProcessorName.isEmpty() ) {
            launchFeature.withProcessorDesignated( this.mszProcessorName );
        }

        if ( this.mBusinessTimeEpoch != null ) {
            launchFeature.setBizTimeEpoch( this.mBusinessTimeEpoch );
        }

        return launchFeature;
    }

}
