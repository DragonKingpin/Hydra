package com.walnut.odin.formation.dto;

import java.time.LocalDateTime;

import com.pinecone.framework.system.prototype.Pinenut;

public class FormationSchedulerRuntimeSnapshot implements Pinenut {
    protected boolean       mbEnabled;
    protected boolean       mbFormationEnabled;
    protected String        mszMode;
    protected String        mszPartitionName;
    protected String        mszNodeId;
    protected boolean       mbRunning;
    protected boolean       mbPulsing;
    protected long          mnStartupDelayMillis;
    protected long          mnTickMillis;
    protected long          mnRecoveryPulseMillis;
    protected boolean       mbAllowOverlappedPulse;
    protected long          mnGracefulShutdownMillis;
    protected boolean       mbPulseLogEnabled;
    protected long          mnSlowPulseMillis;
    protected long          mnPulseSeq;
    protected long          mnSkippedPulseCount;
    protected LocalDateTime mLastPulseStartTime;
    protected LocalDateTime mLastPulseFinishTime;
    protected String        mszLastPulseErrorClass;
    protected String        mszLastPulseErrorMessage;

    public boolean isEnabled() {
        return this.mbEnabled;
    }

    public void setEnabled( boolean enabled ) {
        this.mbEnabled = enabled;
    }

    public boolean isFormationEnabled() {
        return this.mbFormationEnabled;
    }

    public void setFormationEnabled( boolean formationEnabled ) {
        this.mbFormationEnabled = formationEnabled;
    }

    public String getMode() {
        return this.mszMode;
    }

    public void setMode( String mode ) {
        this.mszMode = mode;
    }

    public String getPartitionName() {
        return this.mszPartitionName;
    }

    public void setPartitionName( String partitionName ) {
        this.mszPartitionName = partitionName;
    }

    public String getNodeId() {
        return this.mszNodeId;
    }

    public void setNodeId( String nodeId ) {
        this.mszNodeId = nodeId;
    }

    public boolean isRunning() {
        return this.mbRunning;
    }

    public void setRunning( boolean running ) {
        this.mbRunning = running;
    }

    public boolean isPulsing() {
        return this.mbPulsing;
    }

    public void setPulsing( boolean pulsing ) {
        this.mbPulsing = pulsing;
    }

    public long getStartupDelayMillis() {
        return this.mnStartupDelayMillis;
    }

    public void setStartupDelayMillis( long startupDelayMillis ) {
        this.mnStartupDelayMillis = startupDelayMillis;
    }

    public long getTickMillis() {
        return this.mnTickMillis;
    }

    public void setTickMillis( long tickMillis ) {
        this.mnTickMillis = tickMillis;
    }

    public long getRecoveryPulseMillis() {
        return this.mnRecoveryPulseMillis;
    }

    public void setRecoveryPulseMillis( long recoveryPulseMillis ) {
        this.mnRecoveryPulseMillis = recoveryPulseMillis;
    }

    public boolean isAllowOverlappedPulse() {
        return this.mbAllowOverlappedPulse;
    }

    public void setAllowOverlappedPulse( boolean allowOverlappedPulse ) {
        this.mbAllowOverlappedPulse = allowOverlappedPulse;
    }

    public long getGracefulShutdownMillis() {
        return this.mnGracefulShutdownMillis;
    }

    public void setGracefulShutdownMillis( long gracefulShutdownMillis ) {
        this.mnGracefulShutdownMillis = gracefulShutdownMillis;
    }

    public boolean isPulseLogEnabled() {
        return this.mbPulseLogEnabled;
    }

    public void setPulseLogEnabled( boolean pulseLogEnabled ) {
        this.mbPulseLogEnabled = pulseLogEnabled;
    }

    public long getSlowPulseMillis() {
        return this.mnSlowPulseMillis;
    }

    public void setSlowPulseMillis( long slowPulseMillis ) {
        this.mnSlowPulseMillis = slowPulseMillis;
    }

    public long getPulseSeq() {
        return this.mnPulseSeq;
    }

    public void setPulseSeq( long pulseSeq ) {
        this.mnPulseSeq = pulseSeq;
    }

    public long getSkippedPulseCount() {
        return this.mnSkippedPulseCount;
    }

    public void setSkippedPulseCount( long skippedPulseCount ) {
        this.mnSkippedPulseCount = skippedPulseCount;
    }

    public LocalDateTime getLastPulseStartTime() {
        return this.mLastPulseStartTime;
    }

    public void setLastPulseStartTime( LocalDateTime lastPulseStartTime ) {
        this.mLastPulseStartTime = lastPulseStartTime;
    }

    public LocalDateTime getLastPulseFinishTime() {
        return this.mLastPulseFinishTime;
    }

    public void setLastPulseFinishTime( LocalDateTime lastPulseFinishTime ) {
        this.mLastPulseFinishTime = lastPulseFinishTime;
    }

    public String getLastPulseErrorClass() {
        return this.mszLastPulseErrorClass;
    }

    public void setLastPulseErrorClass( String lastPulseErrorClass ) {
        this.mszLastPulseErrorClass = lastPulseErrorClass;
    }

    public String getLastPulseErrorMessage() {
        return this.mszLastPulseErrorMessage;
    }

    public void setLastPulseErrorMessage( String lastPulseErrorMessage ) {
        this.mszLastPulseErrorMessage = lastPulseErrorMessage;
    }
}
