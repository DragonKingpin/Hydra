package com.walnut.odin.formation.dto;

import java.time.LocalDateTime;

import com.pinecone.framework.system.prototype.Pinenut;

public class FormationSchedulerRuntimeSnapshot implements Pinenut {
    protected boolean       mbRunning;
    protected boolean       mbPulsing;
    protected long          mnPulseSeq;
    protected long          mnSkippedPulseCount;
    protected LocalDateTime mLastPulseStartTime;
    protected LocalDateTime mLastPulseFinishTime;
    protected String        mszLastPulseErrorClass;
    protected String        mszLastPulseErrorMessage;

    public boolean isRunning() { return this.mbRunning; }
    public void setRunning( boolean running ) { this.mbRunning = running; }
    public boolean isPulsing() { return this.mbPulsing; }
    public void setPulsing( boolean pulsing ) { this.mbPulsing = pulsing; }
    public long getPulseSeq() { return this.mnPulseSeq; }
    public void setPulseSeq( long pulseSeq ) { this.mnPulseSeq = pulseSeq; }
    public long getSkippedPulseCount() { return this.mnSkippedPulseCount; }
    public void setSkippedPulseCount( long skippedPulseCount ) { this.mnSkippedPulseCount = skippedPulseCount; }
    public LocalDateTime getLastPulseStartTime() { return this.mLastPulseStartTime; }
    public void setLastPulseStartTime( LocalDateTime lastPulseStartTime ) { this.mLastPulseStartTime = lastPulseStartTime; }
    public LocalDateTime getLastPulseFinishTime() { return this.mLastPulseFinishTime; }
    public void setLastPulseFinishTime( LocalDateTime lastPulseFinishTime ) { this.mLastPulseFinishTime = lastPulseFinishTime; }
    public String getLastPulseErrorClass() { return this.mszLastPulseErrorClass; }
    public void setLastPulseErrorClass( String lastPulseErrorClass ) { this.mszLastPulseErrorClass = lastPulseErrorClass; }
    public String getLastPulseErrorMessage() { return this.mszLastPulseErrorMessage; }
    public void setLastPulseErrorMessage( String lastPulseErrorMessage ) { this.mszLastPulseErrorMessage = lastPulseErrorMessage; }
}
