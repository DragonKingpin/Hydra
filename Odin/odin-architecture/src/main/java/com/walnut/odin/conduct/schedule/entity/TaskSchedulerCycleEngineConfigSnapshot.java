package com.walnut.odin.conduct.schedule.entity;

import com.pinecone.framework.system.prototype.Pinenut;

public class TaskSchedulerCycleEngineConfigSnapshot implements Pinenut {

    protected boolean mSchedulerEnabled;
    protected boolean mCycleEngineEnabled;
    protected String  mSchedulerMode;
    protected String  mNodeId;
    protected String  mPartitionName;
    protected int     mScanThreadCount;
    protected long    mScanIdWindow;
    protected long    mStartupDelayMillis;
    protected long    mTickMillis;
    protected long    mHourlyPulseMillis;
    protected long    mDailyPulseMillis;
    protected long    mRecoveryPulseMillis;
    protected boolean mAllowOverlappedPulse;
    protected long    mGracefulShutdownMillis;
    protected boolean mPulseLogEnabled;
    protected long    mSlowPulseMillis;
    protected long    mPrepareLeadSecondsMinute;
    protected long    mPrepareLeadSecondsHour;
    protected long    mPrepareLeadSecondsDaily;
    protected long    mPrepareCatchUpWindowMinutesMinute;
    protected long    mPrepareCatchUpWindowMinutesHour;
    protected long    mPrepareCatchUpWindowMinutesDaily;
    protected int     mPrepareMaxInstancesPerPulseMinute;
    protected int     mPrepareMaxInstancesPerPulseHour;
    protected int     mPrepareMaxInstancesPerPulseDaily;

    public boolean isSchedulerEnabled() {
        return this.mSchedulerEnabled;
    }

    public void setSchedulerEnabled( boolean schedulerEnabled ) {
        this.mSchedulerEnabled = schedulerEnabled;
    }

    public boolean isCycleEngineEnabled() {
        return this.mCycleEngineEnabled;
    }

    public void setCycleEngineEnabled( boolean cycleEngineEnabled ) {
        this.mCycleEngineEnabled = cycleEngineEnabled;
    }

    public String getSchedulerMode() {
        return this.mSchedulerMode;
    }

    public void setSchedulerMode( String schedulerMode ) {
        this.mSchedulerMode = schedulerMode;
    }

    public String getNodeId() {
        return this.mNodeId;
    }

    public void setNodeId( String nodeId ) {
        this.mNodeId = nodeId;
    }

    public String getPartitionName() {
        return this.mPartitionName;
    }

    public void setPartitionName( String partitionName ) {
        this.mPartitionName = partitionName;
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
