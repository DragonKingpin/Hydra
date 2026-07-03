package com.walnut.odin.conduct.schedule.entity;

import java.time.LocalDateTime;

import com.pinecone.framework.system.prototype.Pinenut;

public class TaskSchedulerEngineRuntimeSnapshot implements Pinenut {

    protected String        mEngineName;
    protected boolean       mEnabled;
    protected boolean       mRunning;
    protected boolean       mPulsing;
    protected long          mPulseSeq;
    protected long          mSkippedPulseCount;
    protected LocalDateTime mLastPulseStartTime;
    protected LocalDateTime mLastPulseFinishTime;
    protected LocalDateTime mLastPulseTime;
    protected String        mLastPulseErrorClass;
    protected String        mLastPulseErrorMessage;
    protected long          mLastHourlyPulseMillis;
    protected long          mLastDailyPulseMillis;
    protected long          mLastRecoveryPulseMillis;
    protected String        mThreadName;
    protected String        mThreadState;
    protected boolean       mThreadAlive;
    protected boolean       mSupportsPulse;
    protected boolean       mSupportsDailyPulse;
    protected long          mStartupDelayMillis;
    protected long          mTickMillis;
    protected long          mPulseMillis;
    protected long          mHourlyPulseMillis;
    protected long          mDailyPulseMillis;
    protected long          mRecoveryPulseMillis;
    protected boolean       mAllowOverlappedPulse;
    protected long          mGracefulShutdownMillis;
    protected boolean       mPulseLogEnabled;
    protected long          mSlowPulseMillis;
    protected int           mScanThreadCount;
    protected long          mScanIdWindow;
    protected int           mMaxInstancesPerPulse;
    protected long          mPrepareLeadSecondsMinute;
    protected long          mPrepareLeadSecondsHour;
    protected long          mPrepareLeadSecondsDaily;
    protected long          mPrepareCatchUpWindowMinutesMinute;
    protected long          mPrepareCatchUpWindowMinutesHour;
    protected long          mPrepareCatchUpWindowMinutesDaily;
    protected int           mPrepareMaxInstancesPerPulseMinute;
    protected int           mPrepareMaxInstancesPerPulseHour;
    protected int           mPrepareMaxInstancesPerPulseDaily;

    public String getEngineName() {
        return this.mEngineName;
    }

    public void setEngineName( String engineName ) {
        this.mEngineName = engineName;
    }

    public boolean isEnabled() {
        return this.mEnabled;
    }

    public void setEnabled( boolean enabled ) {
        this.mEnabled = enabled;
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

    public LocalDateTime getLastPulseTime() {
        return this.mLastPulseTime;
    }

    public void setLastPulseTime( LocalDateTime lastPulseTime ) {
        this.mLastPulseTime = lastPulseTime;
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

    public String getThreadName() {
        return this.mThreadName;
    }

    public void setThreadName( String threadName ) {
        this.mThreadName = threadName;
    }

    public String getThreadState() {
        return this.mThreadState;
    }

    public void setThreadState( String threadState ) {
        this.mThreadState = threadState;
    }

    public boolean isThreadAlive() {
        return this.mThreadAlive;
    }

    public void setThreadAlive( boolean threadAlive ) {
        this.mThreadAlive = threadAlive;
    }

    public boolean isSupportsPulse() {
        return this.mSupportsPulse;
    }

    public void setSupportsPulse( boolean supportsPulse ) {
        this.mSupportsPulse = supportsPulse;
    }

    public boolean isSupportsDailyPulse() {
        return this.mSupportsDailyPulse;
    }

    public void setSupportsDailyPulse( boolean supportsDailyPulse ) {
        this.mSupportsDailyPulse = supportsDailyPulse;
    }

    public long getStartupDelayMillis() {
        return this.mStartupDelayMillis;
    }

    public void setStartupDelayMillis( long startupDelayMillis ) {
        this.mStartupDelayMillis = startupDelayMillis;
    }

    public long getTickMillis() {
        return this.mTickMillis;
    }

    public void setTickMillis( long tickMillis ) {
        this.mTickMillis = tickMillis;
    }

    public long getPulseMillis() {
        return this.mPulseMillis;
    }

    public void setPulseMillis( long pulseMillis ) {
        this.mPulseMillis = pulseMillis;
    }

    public long getHourlyPulseMillis() {
        return this.mHourlyPulseMillis;
    }

    public void setHourlyPulseMillis( long hourlyPulseMillis ) {
        this.mHourlyPulseMillis = hourlyPulseMillis;
    }

    public long getDailyPulseMillis() {
        return this.mDailyPulseMillis;
    }

    public void setDailyPulseMillis( long dailyPulseMillis ) {
        this.mDailyPulseMillis = dailyPulseMillis;
    }

    public long getRecoveryPulseMillis() {
        return this.mRecoveryPulseMillis;
    }

    public void setRecoveryPulseMillis( long recoveryPulseMillis ) {
        this.mRecoveryPulseMillis = recoveryPulseMillis;
    }

    public boolean isAllowOverlappedPulse() {
        return this.mAllowOverlappedPulse;
    }

    public void setAllowOverlappedPulse( boolean allowOverlappedPulse ) {
        this.mAllowOverlappedPulse = allowOverlappedPulse;
    }

    public long getGracefulShutdownMillis() {
        return this.mGracefulShutdownMillis;
    }

    public void setGracefulShutdownMillis( long gracefulShutdownMillis ) {
        this.mGracefulShutdownMillis = gracefulShutdownMillis;
    }

    public boolean isPulseLogEnabled() {
        return this.mPulseLogEnabled;
    }

    public void setPulseLogEnabled( boolean pulseLogEnabled ) {
        this.mPulseLogEnabled = pulseLogEnabled;
    }

    public long getSlowPulseMillis() {
        return this.mSlowPulseMillis;
    }

    public void setSlowPulseMillis( long slowPulseMillis ) {
        this.mSlowPulseMillis = slowPulseMillis;
    }

    public int getScanThreadCount() {
        return this.mScanThreadCount;
    }

    public void setScanThreadCount( int scanThreadCount ) {
        this.mScanThreadCount = scanThreadCount;
    }

    public long getScanIdWindow() {
        return this.mScanIdWindow;
    }

    public void setScanIdWindow( long scanIdWindow ) {
        this.mScanIdWindow = scanIdWindow;
    }

    public int getMaxInstancesPerPulse() {
        return this.mMaxInstancesPerPulse;
    }

    public void setMaxInstancesPerPulse( int maxInstancesPerPulse ) {
        this.mMaxInstancesPerPulse = maxInstancesPerPulse;
    }

    public long getPrepareLeadSecondsMinute() {
        return this.mPrepareLeadSecondsMinute;
    }

    public void setPrepareLeadSecondsMinute( long prepareLeadSecondsMinute ) {
        this.mPrepareLeadSecondsMinute = prepareLeadSecondsMinute;
    }

    public long getPrepareLeadSecondsHour() {
        return this.mPrepareLeadSecondsHour;
    }

    public void setPrepareLeadSecondsHour( long prepareLeadSecondsHour ) {
        this.mPrepareLeadSecondsHour = prepareLeadSecondsHour;
    }

    public long getPrepareLeadSecondsDaily() {
        return this.mPrepareLeadSecondsDaily;
    }

    public void setPrepareLeadSecondsDaily( long prepareLeadSecondsDaily ) {
        this.mPrepareLeadSecondsDaily = prepareLeadSecondsDaily;
    }

    public long getPrepareCatchUpWindowMinutesMinute() {
        return this.mPrepareCatchUpWindowMinutesMinute;
    }

    public void setPrepareCatchUpWindowMinutesMinute( long prepareCatchUpWindowMinutesMinute ) {
        this.mPrepareCatchUpWindowMinutesMinute = prepareCatchUpWindowMinutesMinute;
    }

    public long getPrepareCatchUpWindowMinutesHour() {
        return this.mPrepareCatchUpWindowMinutesHour;
    }

    public void setPrepareCatchUpWindowMinutesHour( long prepareCatchUpWindowMinutesHour ) {
        this.mPrepareCatchUpWindowMinutesHour = prepareCatchUpWindowMinutesHour;
    }

    public long getPrepareCatchUpWindowMinutesDaily() {
        return this.mPrepareCatchUpWindowMinutesDaily;
    }

    public void setPrepareCatchUpWindowMinutesDaily( long prepareCatchUpWindowMinutesDaily ) {
        this.mPrepareCatchUpWindowMinutesDaily = prepareCatchUpWindowMinutesDaily;
    }

    public int getPrepareMaxInstancesPerPulseMinute() {
        return this.mPrepareMaxInstancesPerPulseMinute;
    }

    public void setPrepareMaxInstancesPerPulseMinute( int prepareMaxInstancesPerPulseMinute ) {
        this.mPrepareMaxInstancesPerPulseMinute = prepareMaxInstancesPerPulseMinute;
    }

    public int getPrepareMaxInstancesPerPulseHour() {
        return this.mPrepareMaxInstancesPerPulseHour;
    }

    public void setPrepareMaxInstancesPerPulseHour( int prepareMaxInstancesPerPulseHour ) {
        this.mPrepareMaxInstancesPerPulseHour = prepareMaxInstancesPerPulseHour;
    }

    public int getPrepareMaxInstancesPerPulseDaily() {
        return this.mPrepareMaxInstancesPerPulseDaily;
    }

    public void setPrepareMaxInstancesPerPulseDaily( int prepareMaxInstancesPerPulseDaily ) {
        this.mPrepareMaxInstancesPerPulseDaily = prepareMaxInstancesPerPulseDaily;
    }
}
