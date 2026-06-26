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
    protected TaskInstantaneousMode mMode = TaskInstantaneousMode.Immediate;
    protected boolean       mbAllowLineageBypass;
    protected boolean       mbAllowInstantaneousDepartureBypass;

    public TaskInstantaneousContext toContext() {
        TaskInstantaneousContext context = new TaskInstantaneousContext();
        context.setTaskGuid( this.mTaskGuid );
        context.setExpectTime( this.mExpectTime );
        context.setFireTime( this.mFireTime );
        context.setBusinessTimeEpoch( this.mBusinessTimeEpoch );
        context.setProcessorName( this.mszProcessorName );
        context.setAllowAsymmetricImage( this.mbAllowAsymmetricImage );
        context.setMode( this.mMode );
        context.setAllowLineageBypass( this.mbAllowLineageBypass );
        context.setAllowInstantaneousDepartureBypass( this.mbAllowInstantaneousDepartureBypass );
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
        this.mszProcessorName = normalizeProcessorName( szProcessorName );
    }

    protected static String normalizeProcessorName( String processorName ) {
        if ( processorName == null ) {
            return null;
        }

        String szProcessorName = processorName.trim();
        if ( szProcessorName.isEmpty() || "auto".equalsIgnoreCase( szProcessorName ) ) {
            return null;
        }
        return szProcessorName;
    }

    public boolean isAllowAsymmetricImage() {
        return this.mbAllowAsymmetricImage;
    }

    public void setAllowAsymmetricImage( boolean allowAsymmetricImage ) {
        this.mbAllowAsymmetricImage = allowAsymmetricImage;
    }

    public TaskInstantaneousMode getMode() {
        return this.mMode;
    }

    public void setMode( TaskInstantaneousMode mode ) {
        this.mMode = mode;
    }

    public boolean isAllowLineageBypass() {
        return this.mbAllowLineageBypass;
    }

    public void setAllowLineageBypass( boolean allowLineageBypass ) {
        this.mbAllowLineageBypass = allowLineageBypass;
    }

    public boolean isAllowInstantaneousDepartureBypass() {
        return this.mbAllowInstantaneousDepartureBypass;
    }

    public void setAllowInstantaneousDepartureBypass( boolean allowInstantaneousDepartureBypass ) {
        this.mbAllowInstantaneousDepartureBypass = allowInstantaneousDepartureBypass;
    }

}
