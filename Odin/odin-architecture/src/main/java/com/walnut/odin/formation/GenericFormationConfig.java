package com.walnut.odin.formation;

import com.pinecone.framework.util.json.JSONObject;
import com.pinecone.hydra.system.ko.ArchKernelObjectConfig;

public class GenericFormationConfig extends ArchKernelObjectConfig implements FormationConfig {

    protected boolean mbFormationEnabled = true;
    protected String  mszFormationMode = "single-master";
    protected String  mszFormationPartitionName = "__DEFAULT__";
    protected String  mszFormationNodeId = "odin-formation-001";

    protected boolean mbFormationSchedulerEnabled = true;
    protected long    mnFormationSchedulerStartupDelayMillis = 3000L;
    protected long    mnFormationSchedulerTickMillis = 1000L;
    protected long    mnFormationSchedulerRecoveryPulseMillis = 5000L;
    protected boolean mbFormationSchedulerAllowOverlappedPulse = false;
    protected long    mnFormationSchedulerGracefulShutdownMillis = 10000L;
    protected boolean mbFormationSchedulerPulseLogEnabled = true;
    protected long    mnFormationSchedulerSlowPulseMillis = 5000L;

    protected boolean mbFormationDispatcherEnabled = true;
    protected int     mnFormationDispatcherWorkerThreadCount = 4;
    protected int     mnFormationDispatcherQueueCapacity = 1024;
    protected int     mnFormationDispatcherPollBatchSize = 16;
    protected long    mnFormationDispatcherOfferTimeoutMillis = 1000L;
    protected long    mnFormationDispatcherIdleSleepMillis = 50L;

    protected String  mszFormationDefaultStrategyType = FormationStrategyType.FixedPage.name();
    protected long    mnFormationFixedPageSize = 5L;
    protected long    mnFormationFixedFrameSize = 1L;
    protected long    mnFormationFixedMaximumInflightPage = 1L;
    protected long    mnFormationFixedLeaseSeconds = 300L;

    protected boolean mbFormationRecoveryEnabled = true;
    protected boolean mbFormationStartupReconcileEnabled = true;
    protected long    mnFormationExpireLeaseSeconds = 600L;
    protected int     mnFormationRecoveryBatchSize = 64;

    protected boolean mbFormationManualTaskOnly = true;

    public GenericFormationConfig() {
        super();
    }

    public GenericFormationConfig( JSONObject main ) {
        super( main == null ? null : main.optJSONObject( "formation" ) );
        JSONObject formationConfig = this.optJSONObject( main, "formation" );
        JSONObject schedulerConfig = this.optJSONObject( formationConfig, "scheduler" );
        JSONObject dispatcherConfig = this.optJSONObject( formationConfig, "dispatcher" );
        JSONObject strategyConfig = this.optJSONObject( formationConfig, "strategy" );
        JSONObject fixedPageConfig = this.optJSONObject( strategyConfig, "fixedPage" );
        JSONObject recoveryConfig = this.optJSONObject( formationConfig, "recovery" );
        JSONObject guardConfig = this.optJSONObject( formationConfig, "guard" );

        this.mbFormationEnabled = this.optBoolean( formationConfig, "enable", true );
        this.mszFormationMode = this.optString( formationConfig, "mode", "single-master" );
        this.mszFormationPartitionName = this.optString( formationConfig, "partitionName", "__DEFAULT__" );
        this.mszFormationNodeId = this.optString( formationConfig, "nodeId", "odin-formation-001" );

        this.mbFormationSchedulerEnabled = this.optBoolean( schedulerConfig, "enable", true );
        this.mnFormationSchedulerStartupDelayMillis = this.optLong( schedulerConfig, "startupDelayMillis", 3000L );
        this.mnFormationSchedulerTickMillis = this.optLong( schedulerConfig, "tickMillis", 1000L );
        this.mnFormationSchedulerRecoveryPulseMillis = this.optLong( schedulerConfig, "recoveryPulseMillis", 5000L );
        this.mbFormationSchedulerAllowOverlappedPulse = this.optBoolean( schedulerConfig, "allowOverlappedPulse", false );
        this.mnFormationSchedulerGracefulShutdownMillis = this.optLong( schedulerConfig, "gracefulShutdownMillis", 10000L );
        this.mbFormationSchedulerPulseLogEnabled = this.optBoolean( schedulerConfig, "enablePulseLog", true );
        this.mnFormationSchedulerSlowPulseMillis = this.optLong( schedulerConfig, "slowPulseMillis", 5000L );

        this.mbFormationDispatcherEnabled = this.optBoolean( dispatcherConfig, "enable", true );
        this.mnFormationDispatcherWorkerThreadCount = (int)this.optLong( dispatcherConfig, "workerThreadCount", 4L );
        this.mnFormationDispatcherQueueCapacity = (int)this.optLong( dispatcherConfig, "queueCapacity", 1024L );
        this.mnFormationDispatcherPollBatchSize = (int)this.optLong( dispatcherConfig, "pollBatchSize", 16L );
        this.mnFormationDispatcherOfferTimeoutMillis = this.optLong( dispatcherConfig, "offerTimeoutMillis", 1000L );
        this.mnFormationDispatcherIdleSleepMillis = this.optLong( dispatcherConfig, "idleSleepMillis", 50L );

        this.mszFormationDefaultStrategyType = this.optString( strategyConfig, "defaultType", FormationStrategyType.FixedPage.name() );
        this.mnFormationFixedPageSize = this.optLong( fixedPageConfig, "pageSize", 5L );
        this.mnFormationFixedFrameSize = this.optLong( fixedPageConfig, "frameSize", 1L );
        this.mnFormationFixedMaximumInflightPage = this.optLong( fixedPageConfig, "maximumInflightPage", 1L );
        this.mnFormationFixedLeaseSeconds = this.optLong( fixedPageConfig, "leaseSeconds", 300L );

        this.mbFormationRecoveryEnabled = this.optBoolean( recoveryConfig, "enable", true );
        this.mbFormationStartupReconcileEnabled = this.optBoolean( recoveryConfig, "startupReconcile", true );
        this.mnFormationExpireLeaseSeconds = this.optLong( recoveryConfig, "expireLeaseSeconds", 600L );
        this.mnFormationRecoveryBatchSize = (int)this.optLong( recoveryConfig, "recoveryBatchSize", 64L );

        this.mbFormationManualTaskOnly = this.optBoolean( guardConfig, "manualTaskOnly", true );
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

    @Override public boolean isFormationEnabled() { return this.mbFormationEnabled; }
    @Override public String getFormationMode() { return this.mszFormationMode; }
    @Override public String getFormationPartitionName() { return this.mszFormationPartitionName; }
    @Override public String getFormationNodeId() { return this.mszFormationNodeId; }
    @Override public boolean isFormationSchedulerEnabled() { return this.mbFormationSchedulerEnabled; }
    @Override public long getFormationSchedulerStartupDelayMillis() { return this.mnFormationSchedulerStartupDelayMillis; }
    @Override public long getFormationSchedulerTickMillis() { return this.mnFormationSchedulerTickMillis; }
    @Override public long getFormationSchedulerRecoveryPulseMillis() { return this.mnFormationSchedulerRecoveryPulseMillis; }
    @Override public boolean isFormationSchedulerAllowOverlappedPulse() { return this.mbFormationSchedulerAllowOverlappedPulse; }
    @Override public long getFormationSchedulerGracefulShutdownMillis() { return this.mnFormationSchedulerGracefulShutdownMillis; }
    @Override public boolean isFormationSchedulerPulseLogEnabled() { return this.mbFormationSchedulerPulseLogEnabled; }
    @Override public long getFormationSchedulerSlowPulseMillis() { return this.mnFormationSchedulerSlowPulseMillis; }
    @Override public boolean isFormationDispatcherEnabled() { return this.mbFormationDispatcherEnabled; }
    @Override public int getFormationDispatcherWorkerThreadCount() { return this.mnFormationDispatcherWorkerThreadCount; }
    @Override public int getFormationDispatcherQueueCapacity() { return this.mnFormationDispatcherQueueCapacity; }
    @Override public int getFormationDispatcherPollBatchSize() { return this.mnFormationDispatcherPollBatchSize; }
    @Override public long getFormationDispatcherOfferTimeoutMillis() { return this.mnFormationDispatcherOfferTimeoutMillis; }
    @Override public long getFormationDispatcherIdleSleepMillis() { return this.mnFormationDispatcherIdleSleepMillis; }
    @Override public String getFormationDefaultStrategyType() { return this.mszFormationDefaultStrategyType; }
    @Override public long getFormationFixedPageSize() { return this.mnFormationFixedPageSize; }
    @Override public long getFormationFixedFrameSize() { return this.mnFormationFixedFrameSize; }
    @Override public long getFormationFixedMaximumInflightPage() { return this.mnFormationFixedMaximumInflightPage; }
    @Override public long getFormationFixedLeaseSeconds() { return this.mnFormationFixedLeaseSeconds; }
    @Override public boolean isFormationRecoveryEnabled() { return this.mbFormationRecoveryEnabled; }
    @Override public boolean isFormationStartupReconcileEnabled() { return this.mbFormationStartupReconcileEnabled; }
    @Override public long getFormationExpireLeaseSeconds() { return this.mnFormationExpireLeaseSeconds; }
    @Override public int getFormationRecoveryBatchSize() { return this.mnFormationRecoveryBatchSize; }
    @Override public boolean isFormationManualTaskOnly() { return this.mbFormationManualTaskOnly; }
}
