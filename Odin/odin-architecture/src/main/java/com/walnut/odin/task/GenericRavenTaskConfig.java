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

    public GenericRavenTaskConfig() {
        super();
    }

    public GenericRavenTaskConfig( JSONObject main ) {
        super( main.optJSONObject( "kernelConfig" ) );
        JSONObject config = main.optJSONObject( "kernelConfig" );
        JSONObject schedulerConfig = main.optJSONObject( "scheduler" );
        JSONObject cycleEngineConfig = this.optJSONObject( schedulerConfig, "cycleEngine" );

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

}
