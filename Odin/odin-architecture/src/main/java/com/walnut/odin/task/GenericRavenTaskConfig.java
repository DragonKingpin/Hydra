package com.walnut.odin.task;


import com.pinecone.framework.util.json.JSONObject;
import com.pinecone.hydra.system.ko.ArchKernelObjectConfig;

public class GenericRavenTaskConfig extends ArchKernelObjectConfig implements RavenTaskConfig {

    protected String mszInstanceTitleTimeFormat = RavenTaskConstants.InstanceTitleTimeFormat;
    protected String mszDefaultDateTimeFormat   = RavenTaskConstants.DefaultDateTimeFormat;

    protected int    mnScheduleScanThreadCount  = RavenTaskConstants.ScheduleScanThreadCount;
    protected long   mnScheduleScanIdWindow     = RavenTaskConstants.ScheduleScanIdWindow;

    protected String mszSchedulePartitionName   = "__DEFAULT__";
    protected JSONObject mScheduleGlobalAllocatorConfig;

    protected boolean mbSchedulerEnabled                    = true;
    protected boolean mbSchedulerCycleEngineEnabled         = false;
    protected String  mszSchedulerMode                      = "single-master";
    protected String  mszSchedulerNodeId                    = "odin-master-001";
    protected long    mnScheduleCycleEngineStartupDelayMillis = 3000L;
    protected long    mnScheduleCycleEngineTickMillis       = 1000L;
    protected long    mnScheduleCycleEngineHourlyPulseMillis = 10000L;
    protected long    mnScheduleCycleEngineDailyPulseMillis = 60000L;
    protected long    mnScheduleCycleEngineRecoveryPulseMillis = 5000L;
    protected boolean mbScheduleCycleEngineAllowOverlappedPulse = false;
    protected long    mnScheduleCycleEngineGracefulShutdownMillis = 10000L;
    protected boolean mbScheduleCycleEnginePulseLogEnabled  = true;
    protected long    mnScheduleCycleEngineSlowPulseMillis  = 5000L;
    protected long    mnSchedulePrepareLeadSecondsMinute    = 60L;
    protected long    mnSchedulePrepareLeadSecondsHour      = 3600L;
    protected long    mnSchedulePrepareLeadSecondsDaily     = 14400L;
    protected long    mnSchedulePrepareCatchUpWindowMinutesMinute = 10L;
    protected long    mnSchedulePrepareCatchUpWindowMinutesHour   = 10L;
    protected long    mnSchedulePrepareCatchUpWindowMinutesDaily  = 10L;
    protected int     mnSchedulePrepareMaxInstancesPerPulseMinute = 10;
    protected int     mnSchedulePrepareMaxInstancesPerPulseHour   = 6;
    protected int     mnSchedulePrepareMaxInstancesPerPulseDaily  = 3;
    protected boolean mbInstantaneousEngineEnabled                 = true;
    protected long    mnInstantaneousEngineStartupDelayMillis      = 1000L;
    protected long    mnInstantaneousEnginePulseMillis             = 3000L;
    protected long    mnInstantaneousEngineScanIdWindow            = 1000L;
    protected int     mnInstantaneousEngineMaxInstancesPerPulse    = 200;
    protected boolean mbInstantaneousEnginePulseLogEnabled         = true;
    protected long    mnInstantaneousEngineSlowPulseMillis         = 3000L;
    protected boolean mbPatrolWatchdogEnabled                     = true;
    protected boolean mbPatrolWatchdogRunningProcessAliveEnabled  = true;
    protected long    mnPatrolWatchdogPulseMillis                 = 5000L;
    protected long    mnPatrolWatchdogStartupObservationMillis    = 30000L;
    protected long    mnPatrolWatchdogRunningLostGraceMillis      = 30000L;
    protected long    mnPatrolWatchdogScanIdWindow                = 1000L;

    public GenericRavenTaskConfig() {
        super();
    }

    public GenericRavenTaskConfig( JSONObject main ) {
        super( main.optJSONObject( "kernelConfig" ) );
        JSONObject config = main.optJSONObject( "kernelConfig" );
        JSONObject schedulerConfig = main.optJSONObject( "scheduler" );
        JSONObject cycleEngineConfig = this.optJSONObject( schedulerConfig, "cycleEngine" );
        JSONObject prepareLeadSecondsConfig = this.optJSONObject( cycleEngineConfig, "prepareLeadSeconds" );
        JSONObject prepareCatchUpWindowMinutesConfig = this.optJSONObject( cycleEngineConfig, "prepareCatchUpWindowMinutes" );
        JSONObject prepareMaxInstancesPerPulseConfig = this.optJSONObject( cycleEngineConfig, "prepareMaxInstancesPerPulse" );
        JSONObject instantaneousEngineConfig = this.optJSONObject( schedulerConfig, "instantaneousEngine" );
        JSONObject patrolWatchdogConfig = this.optJSONObject( schedulerConfig, "patrolWatchdog" );
        JSONObject patrolWatchdogRulesConfig = this.optJSONObject( patrolWatchdogConfig, "rules" );
        JSONObject runningProcessAliveConfig = this.optJSONObject( patrolWatchdogRulesConfig, "runningProcessAlive" );

        this.mszInstanceTitleTimeFormat = this.optString( config, "instanceTitleTimeFormat", RavenTaskConstants.InstanceTitleTimeFormat );
        this.mszDefaultDateTimeFormat   = this.optString( config, "defaultDateTimeFormat", RavenTaskConstants.DefaultDateTimeFormat );

        this.mnScheduleScanThreadCount  = (int) this.optLong( config, "scheduleScanThreadCount", RavenTaskConstants.ScheduleScanThreadCount );
        this.mnScheduleScanIdWindow     = this.optLong( config, "scheduleScanIdWindow", RavenTaskConstants.ScheduleScanIdWindow );

        this.mbSchedulerEnabled                    = this.optBoolean( schedulerConfig, "enable", true );
        this.mszSchedulerMode                      = this.optString( schedulerConfig, "mode", "single-master" );
        this.mszSchedulePartitionName              = this.optString( schedulerConfig, "partitionName", "__DEFAULT__" );
        this.mScheduleGlobalAllocatorConfig        = this.optJSONObject( schedulerConfig, "globalAllocator" );

        this.mbSchedulerCycleEngineEnabled         = this.optBoolean( cycleEngineConfig, "enable", false );
        this.mszSchedulerNodeId                    = this.optString( cycleEngineConfig, "nodeId", "odin-master-001" );
        this.mnScheduleCycleEngineStartupDelayMillis = this.optLong( cycleEngineConfig, "startupDelayMillis", 3000L );
        this.mnScheduleCycleEngineTickMillis       = this.optLong( cycleEngineConfig, "tickMillis", 1000L );
        this.mnScheduleCycleEngineHourlyPulseMillis = this.optLong( cycleEngineConfig, "hourlyPulseMillis", 10000L );
        this.mnScheduleCycleEngineDailyPulseMillis = this.optLong( cycleEngineConfig, "dailyPulseMillis", 60000L );
        this.mnScheduleCycleEngineRecoveryPulseMillis = this.optLong( cycleEngineConfig, "recoveryPulseMillis", 5000L );
        this.mbScheduleCycleEngineAllowOverlappedPulse = this.optBoolean( cycleEngineConfig, "allowOverlappedPulse", false );
        this.mnScheduleCycleEngineGracefulShutdownMillis = this.optLong( cycleEngineConfig, "gracefulShutdownMillis", 10000L );
        this.mbScheduleCycleEnginePulseLogEnabled  = this.optBoolean( cycleEngineConfig, "enablePulseLog", true );
        this.mnScheduleCycleEngineSlowPulseMillis  = this.optLong( cycleEngineConfig, "slowPulseMillis", 5000L );
        this.mnSchedulePrepareLeadSecondsMinute    = this.optLong( prepareLeadSecondsConfig, "minute", 60L );
        this.mnSchedulePrepareLeadSecondsHour      = this.optLong( prepareLeadSecondsConfig, "hour", 3600L );
        this.mnSchedulePrepareLeadSecondsDaily     = this.optLong( prepareLeadSecondsConfig, "daily", 14400L );
        this.mnSchedulePrepareCatchUpWindowMinutesMinute = this.optNonNegativeLong(
                prepareCatchUpWindowMinutesConfig, "minute", 10L, "scheduler.cycleEngine.prepareCatchUpWindowMinutes.minute"
        );
        this.mnSchedulePrepareCatchUpWindowMinutesHour = this.optNonNegativeLong(
                prepareCatchUpWindowMinutesConfig, "hour", 10L, "scheduler.cycleEngine.prepareCatchUpWindowMinutes.hour"
        );
        this.mnSchedulePrepareCatchUpWindowMinutesDaily = this.optNonNegativeLong(
                prepareCatchUpWindowMinutesConfig, "daily", 10L, "scheduler.cycleEngine.prepareCatchUpWindowMinutes.daily"
        );
        this.mnSchedulePrepareMaxInstancesPerPulseMinute = (int) this.optLong( prepareMaxInstancesPerPulseConfig, "minute", 10L );
        this.mnSchedulePrepareMaxInstancesPerPulseHour   = (int) this.optLong( prepareMaxInstancesPerPulseConfig, "hour", 6L );
        this.mnSchedulePrepareMaxInstancesPerPulseDaily  = (int) this.optLong( prepareMaxInstancesPerPulseConfig, "daily", 3L );
        this.mbInstantaneousEngineEnabled                = this.optBoolean( instantaneousEngineConfig, "enable", true );
        this.mnInstantaneousEngineStartupDelayMillis     = this.optLong( instantaneousEngineConfig, "startupDelayMillis", 1000L );
        this.mnInstantaneousEnginePulseMillis            = this.optLong( instantaneousEngineConfig, "pulseMillis", 3000L );
        this.mnInstantaneousEngineScanIdWindow           = this.optLong( instantaneousEngineConfig, "scanIdWindow", 1000L );
        this.mnInstantaneousEngineMaxInstancesPerPulse   = (int) this.optLong( instantaneousEngineConfig, "maxInstancesPerPulse", 200L );
        this.mbInstantaneousEnginePulseLogEnabled        = this.optBoolean( instantaneousEngineConfig, "enablePulseLog", true );
        this.mnInstantaneousEngineSlowPulseMillis        = this.optLong( instantaneousEngineConfig, "slowPulseMillis", 3000L );
        this.mbPatrolWatchdogEnabled                    = this.optBoolean( patrolWatchdogConfig, "enable", true );
        this.mnPatrolWatchdogPulseMillis                = this.optLong( patrolWatchdogConfig, "pulseMillis", 5000L );
        this.mnPatrolWatchdogStartupObservationMillis   = this.optLong( patrolWatchdogConfig, "startupObservationMillis", 30000L );
        this.mnPatrolWatchdogRunningLostGraceMillis     = this.optLong( patrolWatchdogConfig, "runningLostGraceMillis", 30000L );
        this.mnPatrolWatchdogScanIdWindow               = this.optLong( patrolWatchdogConfig, "scanIdWindow", 1000L );
        this.mbPatrolWatchdogRunningProcessAliveEnabled = this.optBoolean( runningProcessAliveConfig, "enable", true );
    }

    protected JSONObject optJSONObject( JSONObject config, String key ) {
        if ( config == null ) {
            return null;
        }
        return config.optJSONObject( key );
    }

    protected String optString( JSONObject config, String key, String defaultValue ) {
        if ( config == null ) {
            return defaultValue;
        }
        return config.optString( key, defaultValue );
    }

    protected boolean optBoolean( JSONObject config, String key, boolean defaultValue ) {
        if ( config == null ) {
            return defaultValue;
        }
        return config.optBoolean( key, defaultValue );
    }

    protected long optLong( JSONObject config, String key, long defaultValue ) {
        if ( config == null ) {
            return defaultValue;
        }
        return config.optLong( key, defaultValue );
    }

    protected long optNonNegativeLong( JSONObject config, String key, long defaultValue, String configPath ) {
        long value = this.optLong( config, key, defaultValue );
        if ( value < 0L ) {
            throw new IllegalArgumentException( "Config `" + configPath + "` must not be negative." );
        }
        return value;
    }

    @Override
    public String getInstanceTitleTimeFormat() {
        return this.mszInstanceTitleTimeFormat;
    }

    @Override
    public String getDefaultDateTimeFormat() {
        return this.mszDefaultDateTimeFormat;
    }

    @Override
    public int getScheduleScanThreadCount() {
        return this.mnScheduleScanThreadCount;
    }

    @Override
    public long getScheduleScanIdWindow() {
        return this.mnScheduleScanIdWindow;
    }

    @Override
    public JSONObject getScheduleGlobalAllocatorConfig() {
        return this.mScheduleGlobalAllocatorConfig;
    }

    @Override
    public String getSchedulePartitionName() {
        return this.mszSchedulePartitionName;
    }

    @Override
    public boolean isSchedulerEnabled() {
        return this.mbSchedulerEnabled;
    }

    @Override
    public boolean isSchedulerCycleEngineEnabled() {
        return this.mbSchedulerCycleEngineEnabled;
    }

    @Override
    public String getSchedulerMode() {
        return this.mszSchedulerMode;
    }

    @Override
    public String getSchedulerNodeId() {
        return this.mszSchedulerNodeId;
    }

    @Override
    public long getScheduleCycleEngineStartupDelayMillis() {
        return this.mnScheduleCycleEngineStartupDelayMillis;
    }

    @Override
    public long getScheduleCycleEngineTickMillis() {
        return this.mnScheduleCycleEngineTickMillis;
    }

    @Override
    public long getScheduleCycleEngineHourlyPulseMillis() {
        return this.mnScheduleCycleEngineHourlyPulseMillis;
    }

    @Override
    public long getScheduleCycleEngineDailyPulseMillis() {
        return this.mnScheduleCycleEngineDailyPulseMillis;
    }

    @Override
    public long getScheduleCycleEngineRecoveryPulseMillis() {
        return this.mnScheduleCycleEngineRecoveryPulseMillis;
    }

    @Override
    public boolean isScheduleCycleEngineAllowOverlappedPulse() {
        return this.mbScheduleCycleEngineAllowOverlappedPulse;
    }

    @Override
    public long getScheduleCycleEngineGracefulShutdownMillis() {
        return this.mnScheduleCycleEngineGracefulShutdownMillis;
    }

    @Override
    public boolean isScheduleCycleEnginePulseLogEnabled() {
        return this.mbScheduleCycleEnginePulseLogEnabled;
    }

    @Override
    public long getScheduleCycleEngineSlowPulseMillis() {
        return this.mnScheduleCycleEngineSlowPulseMillis;
    }

    @Override
    public long getSchedulePrepareLeadSecondsMinute() {
        return this.mnSchedulePrepareLeadSecondsMinute;
    }

    @Override
    public long getSchedulePrepareLeadSecondsHour() {
        return this.mnSchedulePrepareLeadSecondsHour;
    }

    @Override
    public long getSchedulePrepareLeadSecondsDaily() {
        return this.mnSchedulePrepareLeadSecondsDaily;
    }

    @Override
    public long getSchedulePrepareCatchUpWindowMinutesMinute() {
        return this.mnSchedulePrepareCatchUpWindowMinutesMinute;
    }

    @Override
    public long getSchedulePrepareCatchUpWindowMinutesHour() {
        return this.mnSchedulePrepareCatchUpWindowMinutesHour;
    }

    @Override
    public long getSchedulePrepareCatchUpWindowMinutesDaily() {
        return this.mnSchedulePrepareCatchUpWindowMinutesDaily;
    }

    @Override
    public int getSchedulePrepareMaxInstancesPerPulseMinute() {
        return this.mnSchedulePrepareMaxInstancesPerPulseMinute;
    }

    @Override
    public int getSchedulePrepareMaxInstancesPerPulseHour() {
        return this.mnSchedulePrepareMaxInstancesPerPulseHour;
    }

    @Override
    public int getSchedulePrepareMaxInstancesPerPulseDaily() {
        return this.mnSchedulePrepareMaxInstancesPerPulseDaily;
    }

    @Override
    public boolean isInstantaneousEngineEnabled() {
        return this.mbInstantaneousEngineEnabled;
    }

    @Override
    public long getInstantaneousEngineStartupDelayMillis() {
        return this.mnInstantaneousEngineStartupDelayMillis;
    }

    @Override
    public long getInstantaneousEnginePulseMillis() {
        return this.mnInstantaneousEnginePulseMillis;
    }

    @Override
    public long getInstantaneousEngineScanIdWindow() {
        return this.mnInstantaneousEngineScanIdWindow;
    }

    @Override
    public int getInstantaneousEngineMaxInstancesPerPulse() {
        return this.mnInstantaneousEngineMaxInstancesPerPulse;
    }

    @Override
    public boolean isInstantaneousEnginePulseLogEnabled() {
        return this.mbInstantaneousEnginePulseLogEnabled;
    }

    @Override
    public long getInstantaneousEngineSlowPulseMillis() {
        return this.mnInstantaneousEngineSlowPulseMillis;
    }

    @Override
    public boolean isPatrolWatchdogEnabled() {
        return this.mbPatrolWatchdogEnabled;
    }

    @Override
    public boolean isPatrolWatchdogRunningProcessAliveEnabled() {
        return this.mbPatrolWatchdogRunningProcessAliveEnabled;
    }

    @Override
    public long getPatrolWatchdogPulseMillis() {
        return this.mnPatrolWatchdogPulseMillis;
    }

    @Override
    public long getPatrolWatchdogStartupObservationMillis() {
        return this.mnPatrolWatchdogStartupObservationMillis;
    }

    @Override
    public long getPatrolWatchdogRunningLostGraceMillis() {
        return this.mnPatrolWatchdogRunningLostGraceMillis;
    }

    @Override
    public long getPatrolWatchdogScanIdWindow() {
        return this.mnPatrolWatchdogScanIdWindow;
    }

}
