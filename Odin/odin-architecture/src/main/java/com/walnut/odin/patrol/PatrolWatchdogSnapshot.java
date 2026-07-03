package com.walnut.odin.patrol;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.pinecone.framework.system.prototype.Pinenut;

public class PatrolWatchdogSnapshot implements Pinenut {

    protected String                         mszWatchdogName;
    protected boolean                        mbServiceRunning;
    protected boolean                        mbEnabled;
    protected boolean                        mbPatrolling;
    protected long                           mnPulseMillis;
    protected long                           mnStartupObservationMillis;
    protected long                           mnRunningLostGraceMillis;
    protected long                           mnScanIdWindow;
    protected LocalDateTime                  mStartupTime;
    protected LocalDateTime                  mLastPatrolStartTime;
    protected LocalDateTime                  mLastPatrolFinishTime;
    protected int                            mnLastScannedCount;
    protected int                            mnLastFailedCount;
    protected List<PatrolWatchdogRuleSnapshot> mRules = new ArrayList<>();

    public String getWatchdogName() {
        return this.mszWatchdogName;
    }

    public void setWatchdogName( String watchdogName ) {
        this.mszWatchdogName = watchdogName;
    }

    public boolean isServiceRunning() {
        return this.mbServiceRunning;
    }

    public void setServiceRunning( boolean serviceRunning ) {
        this.mbServiceRunning = serviceRunning;
    }

    public boolean isEnabled() {
        return this.mbEnabled;
    }

    public void setEnabled( boolean enabled ) {
        this.mbEnabled = enabled;
    }

    public boolean isPatrolling() {
        return this.mbPatrolling;
    }

    public void setPatrolling( boolean patrolling ) {
        this.mbPatrolling = patrolling;
    }

    public long getPulseMillis() {
        return this.mnPulseMillis;
    }

    public void setPulseMillis( long pulseMillis ) {
        this.mnPulseMillis = pulseMillis;
    }

    public long getStartupObservationMillis() {
        return this.mnStartupObservationMillis;
    }

    public void setStartupObservationMillis( long startupObservationMillis ) {
        this.mnStartupObservationMillis = startupObservationMillis;
    }

    public long getRunningLostGraceMillis() {
        return this.mnRunningLostGraceMillis;
    }

    public void setRunningLostGraceMillis( long runningLostGraceMillis ) {
        this.mnRunningLostGraceMillis = runningLostGraceMillis;
    }

    public long getScanIdWindow() {
        return this.mnScanIdWindow;
    }

    public void setScanIdWindow( long scanIdWindow ) {
        this.mnScanIdWindow = scanIdWindow;
    }

    public LocalDateTime getStartupTime() {
        return this.mStartupTime;
    }

    public void setStartupTime( LocalDateTime startupTime ) {
        this.mStartupTime = startupTime;
    }

    public LocalDateTime getLastPatrolStartTime() {
        return this.mLastPatrolStartTime;
    }

    public void setLastPatrolStartTime( LocalDateTime lastPatrolStartTime ) {
        this.mLastPatrolStartTime = lastPatrolStartTime;
    }

    public LocalDateTime getLastPatrolFinishTime() {
        return this.mLastPatrolFinishTime;
    }

    public void setLastPatrolFinishTime( LocalDateTime lastPatrolFinishTime ) {
        this.mLastPatrolFinishTime = lastPatrolFinishTime;
    }

    public int getLastScannedCount() {
        return this.mnLastScannedCount;
    }

    public void setLastScannedCount( int lastScannedCount ) {
        this.mnLastScannedCount = lastScannedCount;
    }

    public int getLastFailedCount() {
        return this.mnLastFailedCount;
    }

    public void setLastFailedCount( int lastFailedCount ) {
        this.mnLastFailedCount = lastFailedCount;
    }

    public List<PatrolWatchdogRuleSnapshot> getRules() {
        return this.mRules;
    }

    public void setRules( List<PatrolWatchdogRuleSnapshot> rules ) {
        if ( rules == null ) {
            this.mRules = new ArrayList<>();
            return;
        }
        this.mRules = rules;
    }
}
