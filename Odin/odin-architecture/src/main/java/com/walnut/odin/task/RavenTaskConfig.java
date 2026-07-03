package com.walnut.odin.task;

import com.pinecone.framework.util.json.JSONObject;
import com.pinecone.hydra.system.ko.KernelObjectConfig;

public interface RavenTaskConfig extends KernelObjectConfig {

    String getInstanceTitleTimeFormat();

    String getDefaultDateTimeFormat();

    int getScheduleScanThreadCount();

    long getScheduleScanIdWindow();

    JSONObject getScheduleGlobalAllocatorConfig();

    String getSchedulePartitionName();

    boolean isSchedulerEnabled();

    boolean isSchedulerCycleEngineEnabled();

    String getSchedulerMode();

    String getSchedulerNodeId();

    long getScheduleCycleEngineStartupDelayMillis();

    long getScheduleCycleEngineTickMillis();

    long getScheduleCycleEngineHourlyPulseMillis();

    long getScheduleCycleEngineDailyPulseMillis();

    long getScheduleCycleEngineRecoveryPulseMillis();

    boolean isScheduleCycleEngineAllowOverlappedPulse();

    long getScheduleCycleEngineGracefulShutdownMillis();

    boolean isScheduleCycleEnginePulseLogEnabled();

    long getScheduleCycleEngineSlowPulseMillis();

    long getSchedulePrepareLeadSecondsMinute();

    long getSchedulePrepareLeadSecondsHour();

    long getSchedulePrepareLeadSecondsDaily();

    long getSchedulePrepareCatchUpWindowMinutesMinute();

    long getSchedulePrepareCatchUpWindowMinutesHour();

    long getSchedulePrepareCatchUpWindowMinutesDaily();

    int getSchedulePrepareMaxInstancesPerPulseMinute();

    int getSchedulePrepareMaxInstancesPerPulseHour();

    int getSchedulePrepareMaxInstancesPerPulseDaily();

    boolean isInstantaneousEngineEnabled();

    long getInstantaneousEngineStartupDelayMillis();

    long getInstantaneousEnginePulseMillis();

    long getInstantaneousEngineScanIdWindow();

    int getInstantaneousEngineMaxInstancesPerPulse();

    boolean isInstantaneousEnginePulseLogEnabled();

    long getInstantaneousEngineSlowPulseMillis();

    boolean isPatrolWatchdogEnabled();

    boolean isPatrolWatchdogRunningProcessAliveEnabled();

    long getPatrolWatchdogPulseMillis();

    long getPatrolWatchdogStartupObservationMillis();

    long getPatrolWatchdogRunningLostGraceMillis();

    long getPatrolWatchdogScanIdWindow();

}
