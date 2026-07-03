package com.walnut.odin.formation;

import com.pinecone.hydra.system.ko.KernelObjectConfig;

public interface FormationConfig extends KernelObjectConfig {

    boolean isFormationEnabled();

    String getFormationMode();

    String getFormationPartitionName();

    String getFormationNodeId();

    boolean isFormationSchedulerEnabled();

    long getFormationSchedulerStartupDelayMillis();

    long getFormationSchedulerTickMillis();

    long getFormationSchedulerRecoveryPulseMillis();

    boolean isFormationSchedulerAllowOverlappedPulse();

    long getFormationSchedulerGracefulShutdownMillis();

    boolean isFormationSchedulerPulseLogEnabled();

    long getFormationSchedulerSlowPulseMillis();

    boolean isFormationDispatcherEnabled();

    int getFormationDispatcherWorkerThreadCount();

    int getFormationDispatcherQueueCapacity();

    int getFormationDispatcherPollBatchSize();

    long getFormationDispatcherOfferTimeoutMillis();

    long getFormationDispatcherIdleSleepMillis();

    String getFormationDefaultStrategyType();

    long getFormationFixedPageSize();

    long getFormationFixedFrameSize();

    long getFormationFixedMaximumInflightPage();

    long getFormationFixedLeaseSeconds();

    boolean isFormationRecoveryEnabled();

    boolean isFormationStartupReconcileEnabled();

    long getFormationExpireLeaseSeconds();

    int getFormationRecoveryBatchSize();

    boolean isFormationManualTaskOnly();
}
