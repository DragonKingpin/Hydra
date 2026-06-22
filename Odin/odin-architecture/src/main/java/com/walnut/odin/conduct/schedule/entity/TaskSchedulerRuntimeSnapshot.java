package com.walnut.odin.conduct.schedule.entity;

import java.time.LocalDateTime;

import com.pinecone.framework.system.prototype.Pinenut;

public class TaskSchedulerRuntimeSnapshot implements Pinenut {

    protected TaskSchedulerCycleEngineConfigSnapshot mConfig;
    protected boolean                                mRunning;
    protected boolean                                mPulsing;
    protected long                                   mPulseSeq;
    protected long                                   mSkippedPulseCount;
    protected LocalDateTime                          mLastPulseStartTime;
    protected LocalDateTime                          mLastPulseFinishTime;
    protected String                                 mLastPulseErrorClass;
    protected String                                 mLastPulseErrorMessage;
    protected long                                   mLastHourlyPulseMillis;
    protected long                                   mLastDailyPulseMillis;
    protected long                                   mLastRecoveryPulseMillis;
    protected String                                 mCycleEngineThreadName;
    protected String                                 mCycleEngineThreadState;
    protected boolean                                mCycleEngineThreadAlive;

    public TaskSchedulerCycleEngineConfigSnapshot getConfig() {
        return this.mConfig;
    }

    public void setConfig( TaskSchedulerCycleEngineConfigSnapshot config ) {
        this.mConfig = config;
    }

    public boolean isRunning() {
        return this.mRunning;
    }

    public void setRunning( boolean running ) {
        this.mRunning = running;
    }

    public boolean isPulsing() {
        return this.mPulsing;
    }

    public void setPulsing( boolean pulsing ) {
        this.mPulsing = pulsing;
    }

    public long getPulseSeq() {
        return this.mPulseSeq;
    }

    public void setPulseSeq( long pulseSeq ) {
        this.mPulseSeq = pulseSeq;
    }

    public long getSkippedPulseCount() {
        return this.mSkippedPulseCount;
    }

    public void setSkippedPulseCount( long skippedPulseCount ) {
        this.mSkippedPulseCount = skippedPulseCount;
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
        return this.mLastPulseErrorClass;
    }

    public void setLastPulseErrorClass( String lastPulseErrorClass ) {
        this.mLastPulseErrorClass = lastPulseErrorClass;
    }

    public String getLastPulseErrorMessage() {
        return this.mLastPulseErrorMessage;
    }

    public void setLastPulseErrorMessage( String lastPulseErrorMessage ) {
        this.mLastPulseErrorMessage = lastPulseErrorMessage;
    }

    public long getLastHourlyPulseMillis() {
        return this.mLastHourlyPulseMillis;
    }

    public void setLastHourlyPulseMillis( long lastHourlyPulseMillis ) {
        this.mLastHourlyPulseMillis = lastHourlyPulseMillis;
    }

    public long getLastDailyPulseMillis() {
        return this.mLastDailyPulseMillis;
    }

    public void setLastDailyPulseMillis( long lastDailyPulseMillis ) {
        this.mLastDailyPulseMillis = lastDailyPulseMillis;
    }

    public long getLastRecoveryPulseMillis() {
        return this.mLastRecoveryPulseMillis;
    }

    public void setLastRecoveryPulseMillis( long lastRecoveryPulseMillis ) {
        this.mLastRecoveryPulseMillis = lastRecoveryPulseMillis;
    }

    public String getCycleEngineThreadName() {
        return this.mCycleEngineThreadName;
    }

    public void setCycleEngineThreadName( String cycleEngineThreadName ) {
        this.mCycleEngineThreadName = cycleEngineThreadName;
    }

    public String getCycleEngineThreadState() {
        return this.mCycleEngineThreadState;
    }

    public void setCycleEngineThreadState( String cycleEngineThreadState ) {
        this.mCycleEngineThreadState = cycleEngineThreadState;
    }

    public boolean isCycleEngineThreadAlive() {
        return this.mCycleEngineThreadAlive;
    }

    public void setCycleEngineThreadAlive( boolean cycleEngineThreadAlive ) {
        this.mCycleEngineThreadAlive = cycleEngineThreadAlive;
    }
}
