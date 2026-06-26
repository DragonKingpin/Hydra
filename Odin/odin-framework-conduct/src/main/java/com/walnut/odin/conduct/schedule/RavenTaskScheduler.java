package com.walnut.odin.conduct.schedule;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import com.pinecone.hydra.system.ko.MetaPersistenceException;
import com.pinecone.hydra.task.kom.UniformTaskInstrument;
import com.pinecone.hydra.task.kom.instance.InstanceInstrument;

import com.walnut.odin.atlas.graph.RuntimeAtlasInstrument;
import com.walnut.odin.conduct.lifecycle.TaskInstanceLifecycleExaminer;
import com.walnut.odin.conduct.recovery.KernelTaskSchedulerReconciler;
import com.walnut.odin.conduct.recovery.TaskSchedulerReconciler;
import com.walnut.odin.conduct.schedule.entity.InstanceDepartureResult;
import com.walnut.odin.conduct.schedule.entity.TaskSchedulerCycleEngineConfigSnapshot;
import com.walnut.odin.conduct.schedule.entity.TaskSchedulerEngineRuntimeSnapshot;
import com.walnut.odin.conduct.schedule.entity.TaskSchedulerIdentitySnapshot;
import com.walnut.odin.conduct.schedule.entity.TaskSchedulerRuntimeSnapshot;
import com.walnut.odin.conduct.schedule.entity.TaskInstantaneousContext;
import com.walnut.odin.conduct.schedule.entity.TaskInstantaneousPrepareResult;
import com.walnut.odin.conduct.schedule.entity.TaskInstantaneousSubmitRequest;
import com.walnut.odin.conduct.schedule.entity.TaskInstantaneousSubmitResult;
import com.walnut.odin.dispatch.TaskDispatchException;
import com.walnut.odin.dispatch.TaskDispatcher;
import com.walnut.odin.patrol.KernelPatrolWatchdog;
import com.walnut.odin.patrol.PatrolWatchdog;
import com.walnut.odin.task.CentralizedTaskInstrument;
import com.walnut.odin.task.RavenTaskConfig;
import com.walnut.odin.task.launch.TaskLaunchFeatureProviderRegistry;
import com.walnut.odin.task.troll.InstanceLaunchException;
import com.walnut.odin.task.troll.TaskExecutionLauncher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RavenTaskScheduler implements UniformTaskScheduler {

    private static final Logger          log = LoggerFactory.getLogger( RavenTaskScheduler.class );

    private RavenTaskConfig              mRavenTaskConfig;

    private InstanceInstrument           mInstanceInstrument;
    private UniformTaskInstrument        mUniformTaskInstrument;
    private RuntimeAtlasInstrument       mRuntimeAtlasInstrument;
    private CentralizedTaskInstrument    mCentralizedTaskInstrument;

    private TaskExecutionLauncher        mTaskExecutionLauncher;
    private TaskInstanceLifecycleExaminer mTaskInstanceLifecycleExaminer;
    private TaskDispatcher               mTaskDispatcher;
    private TaskLaunchFeatureProviderRegistry mTaskLaunchFeatureProviderRegistry;

    private TaskSchedulePreparator       mTaskSchedulePreparator;
    private TaskInstantaneousPreparator  mTaskInstantaneousPreparator;
    private InstanceScheduleImpetus      mInstanceScheduleImpetus;
    private InstanceInstantaneousImpetus mInstanceInstantaneousImpetus;
    private InstantaneousEngine          mInstantaneousEngine;
    private InstanceDepartureGate        mInstanceDepartureGate;

    private InstanceScheduleAllocator    mInstanceScheduleAllocator;
    private TaskSchedulerReconciler      mTaskSchedulerReconciler;
    private PatrolWatchdog               mPatrolWatchdog;
    private String                       mszPartitionName;

    private ScheduledExecutorService     mCycleEngineExecutor;
    private AtomicBoolean                mRuntimeRunning;
    private AtomicBoolean                mCycleEngineRunning;
    private AtomicBoolean                mPulsing;
    private AtomicLong                   mPulseSeq;
    private AtomicLong                   mSkippedPulseCount;

    private volatile LocalDateTime       mLastPulseStartTime;
    private volatile LocalDateTime       mLastPulseFinishTime;
    private volatile Throwable           mLastPulseError;

    private volatile long                mnLastHourlyPulseMillis;
    private volatile long                mnLastDailyPulseMillis;
    private volatile long                mnLastRecoveryPulseMillis;
    private volatile Thread              mCycleEngineThread;

    public RavenTaskScheduler(
            CentralizedTaskInstrument taskInstrument, RuntimeAtlasInstrument atlasInstrument,
            TaskDispatcher dispatcher
    ) {
        log.info( "[Odin] [CrucialSchedulerComponentLifecycle] (RavenTaskScheduler Construction) <Start>" );

        this.mCentralizedTaskInstrument    = taskInstrument;
        this.mUniformTaskInstrument        = taskInstrument.getUniformTaskInstrument();
        this.mInstanceInstrument           = this.mUniformTaskInstrument.getInstanceInstrument();
        this.mRuntimeAtlasInstrument       = atlasInstrument;

        this.mTaskExecutionLauncher        = dispatcher.taskExecutionLauncher();
        this.mTaskInstanceLifecycleExaminer = dispatcher
                .collectiveTaskRegiment()
                .taskInstanceLifecycleExaminer();
        this.mTaskDispatcher               = dispatcher;
        this.mTaskLaunchFeatureProviderRegistry = new TaskLaunchFeatureProviderRegistry();

        this.mRavenTaskConfig              = (RavenTaskConfig) taskInstrument.getConfig();
        this.mszPartitionName              = this.mRavenTaskConfig.getSchedulePartitionName();

        this.mInstanceScheduleAllocator    = new RavenScheduleAllocator( this ); // [1]
        this.mInstanceDepartureGate        = new RavenInstanceDepartureGate( this ); // [2]
        this.mTaskSchedulePreparator       = new RavenTaskSchedulePreparator( this ); // [3]
        this.mTaskInstantaneousPreparator  = new RavenTaskInstantaneousPreparator( this ); // [4]
        this.mInstanceScheduleImpetus      = new RavenInstanceScheduleImpetus( this ); // [5]
        this.mInstanceInstantaneousImpetus = new RavenInstanceInstantaneousImpetus( this ); // [6]
        this.mTaskSchedulerReconciler      = new KernelTaskSchedulerReconciler( this ); // [7]
        this.mInstantaneousEngine          = new RavenInstantaneousEngine( this, this.mTaskSchedulerReconciler ); // [8]
        this.mPatrolWatchdog               = new KernelPatrolWatchdog( this ); // [9]

        this.mRuntimeRunning               = new AtomicBoolean( false );
        this.mCycleEngineRunning           = new AtomicBoolean( false );
        this.mPulsing                      = new AtomicBoolean( false );
        this.mPulseSeq                     = new AtomicLong( 0L );
        this.mSkippedPulseCount            = new AtomicLong( 0L );


        log.info( "[Odin] [CrucialSchedulerComponentLifecycle] (RavenTaskScheduler Construction) <Done>" );
    }


    @Override
    public TaskSchedulePreparator taskSchedulePreparator() {
        return this.mTaskSchedulePreparator;
    }

    @Override
    public TaskInstantaneousPreparator taskInstantaneousPreparator() {
        return this.mTaskInstantaneousPreparator;
    }

    @Override
    public InstanceScheduleImpetus instanceScheduleImpetus() {
        return this.mInstanceScheduleImpetus;
    }

    @Override
    public InstanceInstantaneousImpetus instanceInstantaneousImpetus() {
        return this.mInstanceInstantaneousImpetus;
    }

    @Override
    public InstantaneousEngine instantaneousEngine() {
        return this.mInstantaneousEngine;
    }

    @Override
    public InstanceDepartureGate instanceDepartureGate() {
        return this.mInstanceDepartureGate;
    }

    @Override
    public InstanceScheduleAllocator instanceScheduleAllocator() {
        return this.mInstanceScheduleAllocator;
    }

    @Override
    public PatrolWatchdog patrolWatchdog() {
        return this.mPatrolWatchdog;
    }

    @Override
    public RavenTaskConfig ravenTaskConfig() {
        return this.mRavenTaskConfig;
    }

    @Override
    public CentralizedTaskInstrument taskInstrument() {
        return this.mCentralizedTaskInstrument;
    }

    @Override
    public InstanceInstrument instanceInstrument() {
        return this.mInstanceInstrument;
    }

    @Override
    public RuntimeAtlasInstrument atlasInstrument() {
        return this.mRuntimeAtlasInstrument;
    }

    @Override
    public TaskExecutionLauncher taskExecutionLauncher() {
        return this.mTaskExecutionLauncher;
    }

    @Override
    public TaskInstanceLifecycleExaminer taskInstanceLifecycleExaminer() {
        return this.mTaskInstanceLifecycleExaminer;
    }

    @Override
    public TaskDispatcher taskDispatcher() {
        return this.mTaskDispatcher;
    }

    @Override
    public TaskLaunchFeatureProviderRegistry taskLaunchFeatureProviderRegistry() {
        return this.mTaskLaunchFeatureProviderRegistry;
    }

    @Override
    public String getPartitionName() {
        return this.mszPartitionName;
    }

    @Override
    public void startService() {
        if ( !this.mRavenTaskConfig.isSchedulerEnabled() ) {
            log.info( "[OdinScheduler] [RuntimeDisabled] (Reason: `scheduler-disabled`) <Pass>" );
            return;
        }

        if ( this.mRuntimeRunning.compareAndSet( false, true ) ) {
            this.startRuntimeServices();
            log.info(
                    "[OdinScheduler] [RuntimeStarted] (Mode: `{}`, NodeId: `{}`, Partition: `{}`) <Ready>",
                    this.mRavenTaskConfig.getSchedulerMode(),
                    this.mRavenTaskConfig.getSchedulerNodeId(),
                    this.mszPartitionName
            );
        }
        else {
            log.info( "[OdinScheduler] [RuntimeStarted] (Reason: `already-running`, NodeId: `{}`) <Pass>",
                    this.mRavenTaskConfig.getSchedulerNodeId() );
        }

        this.startCycleEngineIfEnabled();
    }

    @Override
    public void startCycleEngine() {
        this.startCycleEngineIfEnabled();
    }

    protected void startRuntimeServices() {
        this.mTaskSchedulePreparator.startService();
        this.mInstanceScheduleImpetus.startService();
        this.mInstantaneousEngine.startService();
        this.mPatrolWatchdog.startService();
    }

    protected void startCycleEngineIfEnabled() {
        if ( !this.mRavenTaskConfig.isSchedulerEnabled() ) {
            log.info( "[OdinScheduler] [CycleEngineDisabled] (Reason: `scheduler-disabled`) <Pass>" );
            return;
        }

        if ( !this.mRavenTaskConfig.isSchedulerCycleEngineEnabled() ) {
            log.info( "[OdinScheduler] [CycleEngineDisabled] (Reason: `cycle-engine-disabled`, ManualPulse: `true`) <Pass>" );
            return;
        }

        if ( !this.mCycleEngineRunning.compareAndSet( false, true ) ) {
            log.info( "[OdinScheduler] [CycleEngineStarted] (Reason: `already-running`, NodeId: `{}`) <Pass>",
                    this.mRavenTaskConfig.getSchedulerNodeId() );
            return;
        }

        long nStartupDelayMillis = Math.max( 0L, this.mRavenTaskConfig.getScheduleCycleEngineStartupDelayMillis() );
        long nTickMillis = Math.max( 1L, this.mRavenTaskConfig.getScheduleCycleEngineTickMillis() );

        this.traceSchedulerCycleEngineBanner();
        this.mCycleEngineExecutor = Executors.newSingleThreadScheduledExecutor( runnable -> {
            Thread thread = new Thread( runnable, "odin-task-scheduler-cycle-engine" );
            thread.setDaemon( true );
            this.mCycleEngineThread = thread;
            return thread;
        } );
        this.mCycleEngineExecutor.scheduleWithFixedDelay(
                this::cycleEnginePulse,
                nStartupDelayMillis,
                nTickMillis,
                TimeUnit.MILLISECONDS
        );
        log.info(
                "[OdinScheduler] [CycleEngineStarted] (Mode: `{}`, NodeId: `{}`, Partition: `{}`, TickMillis: `{}`, StartupDelayMillis: `{}`) <Ready>",
                this.mRavenTaskConfig.getSchedulerMode(),
                this.mRavenTaskConfig.getSchedulerNodeId(),
                this.mszPartitionName,
                nTickMillis,
                nStartupDelayMillis
        );
    }

    @Override
    public void terminateService() {
        boolean bRuntimeWasRunning = this.mRuntimeRunning.compareAndSet( true, false );
        boolean bCycleWasRunning = this.stopCycleEngineInternal();

        this.stopRuntimeServices();
        if ( !bRuntimeWasRunning && !bCycleWasRunning ) {
            log.info( "[OdinScheduler] [RuntimeStopped] (Reason: `already-stopped`, Partition: `{}`) <Pass>", this.mszPartitionName );
            return;
        }
        log.info( "[OdinScheduler] [RuntimeStopped] (Partition: `{}`) <Done>", this.mszPartitionName );
    }

    protected boolean stopCycleEngineInternal() {
        if ( !this.mCycleEngineRunning.compareAndSet( true, false ) ) {
            return false;
        }

        long nGracefulShutdownMillis = Math.max( 0L, this.mRavenTaskConfig.getScheduleCycleEngineGracefulShutdownMillis() );
        ScheduledExecutorService executor = this.mCycleEngineExecutor;
        this.mCycleEngineExecutor = null;
        if ( executor != null ) {
            executor.shutdown();
            try {
                if ( !executor.awaitTermination( nGracefulShutdownMillis, TimeUnit.MILLISECONDS ) ) {
                    executor.shutdownNow();
                }
            }
            catch ( InterruptedException e ) {
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }

        this.mCycleEngineThread = null;

        log.info(
                "[OdinScheduler] [CycleEngineStopped] (PulseCount: `{}`, SkippedCount: `{}`, LastPulseTime: `{}`) <Done>",
                this.mPulseSeq.get(),
                this.mSkippedPulseCount.get(),
                this.mLastPulseFinishTime
        );
        return true;
    }

    @Override
    public void stopCycleEngine() {
        this.stopCycleEngineInternal();
    }

    protected void stopRuntimeServices() {
        long nGracefulShutdownMillis = Math.max( 0L, this.mRavenTaskConfig.getScheduleCycleEngineGracefulShutdownMillis() );
        this.mTaskSchedulePreparator.terminateService( nGracefulShutdownMillis );
        this.mInstanceScheduleImpetus.terminateService( nGracefulShutdownMillis );
        this.mInstantaneousEngine.terminateService( nGracefulShutdownMillis );
        this.mPatrolWatchdog.terminateService();
    }

    @Override
    public boolean isRunning() {
        return this.mCycleEngineRunning.get();
    }

    @Override
    public TaskSchedulerRuntimeSnapshot runtimeSnapshot() {
        TaskSchedulerRuntimeSnapshot snapshot = new TaskSchedulerRuntimeSnapshot();
        TaskSchedulerCycleEngineConfigSnapshot config = this.configSnapshot();
        TaskSchedulerEngineRuntimeSnapshot cycleEngine = this.cycleEngineSnapshot();

        snapshot.setIdentity( this.identitySnapshot() );
        snapshot.setCycleEngine( cycleEngine );
        snapshot.setInstantaneousEngine( this.mInstantaneousEngine.runtimeSnapshot() );

        snapshot.setConfig( config );
        snapshot.setRunning( cycleEngine.isRunning() );
        snapshot.setPulsing( cycleEngine.isPulsing() );
        snapshot.setPulseSeq( cycleEngine.getPulseSeq() );
        snapshot.setSkippedPulseCount( cycleEngine.getSkippedPulseCount() );
        snapshot.setLastPulseStartTime( cycleEngine.getLastPulseStartTime() );
        snapshot.setLastPulseFinishTime( cycleEngine.getLastPulseFinishTime() );
        snapshot.setLastPulseErrorClass( cycleEngine.getLastPulseErrorClass() );
        snapshot.setLastPulseErrorMessage( cycleEngine.getLastPulseErrorMessage() );
        snapshot.setLastHourlyPulseMillis( cycleEngine.getLastHourlyPulseMillis() );
        snapshot.setLastDailyPulseMillis( cycleEngine.getLastDailyPulseMillis() );
        snapshot.setLastRecoveryPulseMillis( cycleEngine.getLastRecoveryPulseMillis() );
        snapshot.setCycleEngineThreadName( cycleEngine.getThreadName() );
        snapshot.setCycleEngineThreadState( cycleEngine.getThreadState() );
        snapshot.setCycleEngineThreadAlive( cycleEngine.isThreadAlive() );
        return snapshot;
    }

    protected TaskSchedulerIdentitySnapshot identitySnapshot() {
        TaskSchedulerIdentitySnapshot snapshot = new TaskSchedulerIdentitySnapshot();
        snapshot.setSchedulerEnabled( this.mRavenTaskConfig.isSchedulerEnabled() );
        snapshot.setSchedulerMode( this.mRavenTaskConfig.getSchedulerMode() );
        snapshot.setNodeId( this.mRavenTaskConfig.getSchedulerNodeId() );
        snapshot.setPartitionName( this.mszPartitionName );
        return snapshot;
    }

    protected TaskSchedulerEngineRuntimeSnapshot cycleEngineSnapshot() {
        TaskSchedulerEngineRuntimeSnapshot snapshot = new TaskSchedulerEngineRuntimeSnapshot();
        snapshot.setEngineName( "CycleEngine" );
        snapshot.setEnabled( this.mRavenTaskConfig.isSchedulerCycleEngineEnabled() );
        snapshot.setRunning( this.mCycleEngineRunning.get() );
        snapshot.setPulsing( this.mPulsing.get() );
        snapshot.setPulseSeq( this.mPulseSeq.get() );
        snapshot.setSkippedPulseCount( this.mSkippedPulseCount.get() );
        snapshot.setLastPulseStartTime( this.mLastPulseStartTime );
        snapshot.setLastPulseFinishTime( this.mLastPulseFinishTime );
        snapshot.setLastHourlyPulseMillis( this.mnLastHourlyPulseMillis );
        snapshot.setLastDailyPulseMillis( this.mnLastDailyPulseMillis );
        snapshot.setLastRecoveryPulseMillis( this.mnLastRecoveryPulseMillis );
        snapshot.setSupportsPulse( true );
        snapshot.setSupportsDailyPulse( true );
        snapshot.setStartupDelayMillis( this.mRavenTaskConfig.getScheduleCycleEngineStartupDelayMillis() );
        snapshot.setTickMillis( this.mRavenTaskConfig.getScheduleCycleEngineTickMillis() );
        snapshot.setHourlyPulseMillis( this.mRavenTaskConfig.getScheduleCycleEngineHourlyPulseMillis() );
        snapshot.setDailyPulseMillis( this.mRavenTaskConfig.getScheduleCycleEngineDailyPulseMillis() );
        snapshot.setRecoveryPulseMillis( this.mRavenTaskConfig.getScheduleCycleEngineRecoveryPulseMillis() );
        snapshot.setAllowOverlappedPulse( this.mRavenTaskConfig.isScheduleCycleEngineAllowOverlappedPulse() );
        snapshot.setGracefulShutdownMillis( this.mRavenTaskConfig.getScheduleCycleEngineGracefulShutdownMillis() );
        snapshot.setPulseLogEnabled( this.mRavenTaskConfig.isScheduleCycleEnginePulseLogEnabled() );
        snapshot.setSlowPulseMillis( this.mRavenTaskConfig.getScheduleCycleEngineSlowPulseMillis() );
        snapshot.setScanThreadCount( this.mRavenTaskConfig.getScheduleScanThreadCount() );
        snapshot.setScanIdWindow( this.mRavenTaskConfig.getScheduleScanIdWindow() );
        snapshot.setPrepareLeadSecondsMinute( this.mRavenTaskConfig.getSchedulePrepareLeadSecondsMinute() );
        snapshot.setPrepareLeadSecondsHour( this.mRavenTaskConfig.getSchedulePrepareLeadSecondsHour() );
        snapshot.setPrepareLeadSecondsDaily( this.mRavenTaskConfig.getSchedulePrepareLeadSecondsDaily() );
        snapshot.setPrepareCatchUpWindowMinutesMinute( this.mRavenTaskConfig.getSchedulePrepareCatchUpWindowMinutesMinute() );
        snapshot.setPrepareCatchUpWindowMinutesHour( this.mRavenTaskConfig.getSchedulePrepareCatchUpWindowMinutesHour() );
        snapshot.setPrepareCatchUpWindowMinutesDaily( this.mRavenTaskConfig.getSchedulePrepareCatchUpWindowMinutesDaily() );
        snapshot.setPrepareMaxInstancesPerPulseMinute( this.mRavenTaskConfig.getSchedulePrepareMaxInstancesPerPulseMinute() );
        snapshot.setPrepareMaxInstancesPerPulseHour( this.mRavenTaskConfig.getSchedulePrepareMaxInstancesPerPulseHour() );
        snapshot.setPrepareMaxInstancesPerPulseDaily( this.mRavenTaskConfig.getSchedulePrepareMaxInstancesPerPulseDaily() );

        Throwable lastPulseError = this.mLastPulseError;
        if ( lastPulseError != null ) {
            snapshot.setLastPulseErrorClass( lastPulseError.getClass().getName() );
            snapshot.setLastPulseErrorMessage( lastPulseError.getMessage() );
        }

        Thread cycleEngineThread = this.mCycleEngineThread;
        if ( cycleEngineThread != null ) {
            snapshot.setThreadName( cycleEngineThread.getName() );
            snapshot.setThreadState( cycleEngineThread.getState().name() );
            snapshot.setThreadAlive( cycleEngineThread.isAlive() );
        }
        return snapshot;
    }

    protected TaskSchedulerCycleEngineConfigSnapshot configSnapshot() {
        TaskSchedulerCycleEngineConfigSnapshot snapshot = new TaskSchedulerCycleEngineConfigSnapshot();
        snapshot.setSchedulerEnabled( this.mRavenTaskConfig.isSchedulerEnabled() );
        snapshot.setCycleEngineEnabled( this.mRavenTaskConfig.isSchedulerCycleEngineEnabled() );
        snapshot.setSchedulerMode( this.mRavenTaskConfig.getSchedulerMode() );
        snapshot.setNodeId( this.mRavenTaskConfig.getSchedulerNodeId() );
        snapshot.setPartitionName( this.mszPartitionName );
        snapshot.setScanThreadCount( this.mRavenTaskConfig.getScheduleScanThreadCount() );
        snapshot.setScanIdWindow( this.mRavenTaskConfig.getScheduleScanIdWindow() );
        snapshot.setStartupDelayMillis( this.mRavenTaskConfig.getScheduleCycleEngineStartupDelayMillis() );
        snapshot.setTickMillis( this.mRavenTaskConfig.getScheduleCycleEngineTickMillis() );
        snapshot.setHourlyPulseMillis( this.mRavenTaskConfig.getScheduleCycleEngineHourlyPulseMillis() );
        snapshot.setDailyPulseMillis( this.mRavenTaskConfig.getScheduleCycleEngineDailyPulseMillis() );
        snapshot.setRecoveryPulseMillis( this.mRavenTaskConfig.getScheduleCycleEngineRecoveryPulseMillis() );
        snapshot.setAllowOverlappedPulse( this.mRavenTaskConfig.isScheduleCycleEngineAllowOverlappedPulse() );
        snapshot.setGracefulShutdownMillis( this.mRavenTaskConfig.getScheduleCycleEngineGracefulShutdownMillis() );
        snapshot.setPulseLogEnabled( this.mRavenTaskConfig.isScheduleCycleEnginePulseLogEnabled() );
        snapshot.setSlowPulseMillis( this.mRavenTaskConfig.getScheduleCycleEngineSlowPulseMillis() );
        snapshot.setPrepareLeadSecondsMinute( this.mRavenTaskConfig.getSchedulePrepareLeadSecondsMinute() );
        snapshot.setPrepareLeadSecondsHour( this.mRavenTaskConfig.getSchedulePrepareLeadSecondsHour() );
        snapshot.setPrepareLeadSecondsDaily( this.mRavenTaskConfig.getSchedulePrepareLeadSecondsDaily() );
        snapshot.setPrepareCatchUpWindowMinutesMinute( this.mRavenTaskConfig.getSchedulePrepareCatchUpWindowMinutesMinute() );
        snapshot.setPrepareCatchUpWindowMinutesHour( this.mRavenTaskConfig.getSchedulePrepareCatchUpWindowMinutesHour() );
        snapshot.setPrepareCatchUpWindowMinutesDaily( this.mRavenTaskConfig.getSchedulePrepareCatchUpWindowMinutesDaily() );
        snapshot.setPrepareMaxInstancesPerPulseMinute( this.mRavenTaskConfig.getSchedulePrepareMaxInstancesPerPulseMinute() );
        snapshot.setPrepareMaxInstancesPerPulseHour( this.mRavenTaskConfig.getSchedulePrepareMaxInstancesPerPulseHour() );
        snapshot.setPrepareMaxInstancesPerPulseDaily( this.mRavenTaskConfig.getSchedulePrepareMaxInstancesPerPulseDaily() );
        return snapshot;
    }

    @Override
    public void pulseSchedule() {
        this.pulseSchedule( LocalDateTime.now() );
    }

    @Override
    public void pulseSchedule( LocalDateTime pulseTime ) {
        if ( pulseTime == null ) {
            pulseTime = LocalDateTime.now();
        }

        if ( !this.enterPulse( "manual" ) ) {
            return;
        }

        try {
            this.executeSchedulePulse( pulseTime, true, false, true );
        }
        finally {
            this.exitPulse();
        }
    }

    protected void executeSchedulePulse( LocalDateTime pulseTime, boolean hourly, boolean daily, boolean recovery ) {
        if ( recovery ) {
            this.mTaskSchedulerReconciler.reconcileLightweight( pulseTime );
        }
        if ( hourly ) {
            this.mTaskSchedulePreparator.prepareHourlySchedulableTasksAndWait( pulseTime );
        }
        this.mTaskSchedulePreparator.prepareFastSchedulableTasksAndWait( pulseTime );
        if ( daily ) {
            this.mTaskSchedulePreparator.prepareDailySchedulableTasksAndWait( pulseTime );
        }
        this.mInstanceScheduleImpetus.impelPrelaunchInstances( pulseTime );
        this.mInstanceScheduleImpetus.impelPreparedStandbyInstances( pulseTime );
    }

    protected void cycleEnginePulse() {
        if ( !this.mCycleEngineRunning.get() ) {
            return;
        }

        if ( !this.enterPulse( "cycle-engine" ) ) {
            return;
        }

        long nPulseId = this.mPulseSeq.incrementAndGet();
        long nStartMillis = System.currentTimeMillis();
        LocalDateTime pulseTime = LocalDateTime.now();
        this.mLastPulseStartTime = pulseTime;

        boolean hourly = this.shouldRunHourlyPulse( nStartMillis );
        boolean daily = this.shouldRunDailyPulse( nStartMillis );
        boolean recovery = this.shouldRunRecoveryPulse( nStartMillis );

        try {
            this.executeSchedulePulse( pulseTime, hourly, daily, recovery );
            this.mLastPulseError = null;
            this.mLastPulseFinishTime = LocalDateTime.now();
            this.logPulseDone( nPulseId, nStartMillis, hourly, daily, recovery );
        }
        catch ( Throwable e ) {
            this.mLastPulseError = e;
            this.mLastPulseFinishTime = LocalDateTime.now();
            log.error(
                    "[OdinScheduler] [CyclePulseFailed] (PulseId: `{}`, NodeId: `{}`, Partition: `{}`) <Failed>",
                    nPulseId,
                    this.mRavenTaskConfig.getSchedulerNodeId(),
                    this.mszPartitionName,
                    e
            );
        }
        finally {
            this.exitPulse();
        }
    }

    protected boolean enterPulse( String reason ) {
        if ( this.mRavenTaskConfig.isScheduleCycleEngineAllowOverlappedPulse() ) {
            return true;
        }

        if ( this.mPulsing.compareAndSet( false, true ) ) {
            return true;
        }

        long nSkipped = this.mSkippedPulseCount.incrementAndGet();
        log.info(
                "[OdinScheduler] [PulseSkipped] (Reason: `already-pulsing`, Trigger: `{}`, SkippedCount: `{}`) <Skipped>",
                reason,
                nSkipped
        );
        return false;
    }

    protected void exitPulse() {
        if ( !this.mRavenTaskConfig.isScheduleCycleEngineAllowOverlappedPulse() ) {
            this.mPulsing.set( false );
        }
    }

    protected boolean shouldRunHourlyPulse( long nowMillis ) {
        long nIntervalMillis = this.mRavenTaskConfig.getScheduleCycleEngineHourlyPulseMillis();
        if ( nIntervalMillis <= 0L ) {
            return false;
        }
        if ( this.mnLastHourlyPulseMillis > 0L && nowMillis - this.mnLastHourlyPulseMillis < nIntervalMillis ) {
            return false;
        }
        this.mnLastHourlyPulseMillis = nowMillis;
        return true;
    }

    protected boolean shouldRunDailyPulse( long nowMillis ) {
        long nIntervalMillis = this.mRavenTaskConfig.getScheduleCycleEngineDailyPulseMillis();
        if ( nIntervalMillis <= 0L ) {
            return false;
        }
        if ( this.mnLastDailyPulseMillis > 0L && nowMillis - this.mnLastDailyPulseMillis < nIntervalMillis ) {
            return false;
        }
        this.mnLastDailyPulseMillis = nowMillis;
        return true;
    }

    protected boolean shouldRunRecoveryPulse( long nowMillis ) {
        long nIntervalMillis = this.mRavenTaskConfig.getScheduleCycleEngineRecoveryPulseMillis();
        if ( nIntervalMillis <= 0L ) {
            return false;
        }
        if ( this.mnLastRecoveryPulseMillis > 0L && nowMillis - this.mnLastRecoveryPulseMillis < nIntervalMillis ) {
            return false;
        }
        this.mnLastRecoveryPulseMillis = nowMillis;
        return true;
    }

    protected void logPulseDone( long pulseId, long startMillis, boolean hourly, boolean daily, boolean recovery ) {
        long nDurationMillis = System.currentTimeMillis() - startMillis;
        long nSlowPulseMillis = this.mRavenTaskConfig.getScheduleCycleEngineSlowPulseMillis();
        if ( nSlowPulseMillis > 0L && nDurationMillis >= nSlowPulseMillis ) {
            log.warn(
                    "[OdinScheduler] [SlowCyclePulse] (PulseId: `{}`, DurationMs: `{}`, SlowPulseMillis: `{}`, Hourly: `{}`, Daily: `{}`, Recovery: `{}`) <Warn>",
                    pulseId,
                    nDurationMillis,
                    nSlowPulseMillis,
                    hourly,
                    daily,
                    recovery
            );
            return;
        }
        if ( !this.mRavenTaskConfig.isScheduleCycleEnginePulseLogEnabled() ) {
            return;
        }
        log.info(
                "[OdinScheduler] [CyclePulse] (PulseId: `{}`, DurationMs: `{}`, Hourly: `{}`, Daily: `{}`, Recovery: `{}`) <Done>",
                pulseId,
                nDurationMillis,
                hourly,
                daily,
                recovery
        );
    }

    protected void traceSchedulerCycleEngineBanner() {
        log.info( "---------------------------------------------------------------" );
        log.info( " Bean Nuts Acorn Odin Scheduler Cycle Engine" );
        log.info( " Mode       : {}", this.mRavenTaskConfig.getSchedulerMode() );
        log.info( " Partition  : {}", this.mszPartitionName );
        log.info( " Node       : {}", this.mRavenTaskConfig.getSchedulerNodeId() );
        log.info( " Tick       : {} ms", Math.max( 1L, this.mRavenTaskConfig.getScheduleCycleEngineTickMillis() ) );
        log.info( " Hourly     : {} ms", this.mRavenTaskConfig.getScheduleCycleEngineHourlyPulseMillis() );
        log.info( " Daily      : {} ms", this.mRavenTaskConfig.getScheduleCycleEngineDailyPulseMillis() );
        log.info( " Recovery   : {} ms", this.mRavenTaskConfig.getScheduleCycleEngineRecoveryPulseMillis() );
        log.info( "---------------------------------------------------------------" );
    }

    @Override
    public void pulseScheduleDaily( LocalDateTime pulseTime ) {
        if ( pulseTime == null ) {
            pulseTime = LocalDateTime.now();
        }

        if ( !this.enterPulse( "manual-daily" ) ) {
            return;
        }

        try {
            this.mTaskSchedulePreparator.prepareDailySchedulableTasksAndWait( pulseTime );
        }
        finally {
            this.exitPulse();
        }
    }

    @Override
    public TaskInstantaneousSubmitResult submitInstantaneousTask( TaskInstantaneousSubmitRequest request )
            throws MetaPersistenceException, InstanceLaunchException, TaskDispatchException {
        if ( request == null ) {
            throw new IllegalArgumentException( "TaskInstantaneousSubmitRequest is null." );
        }

        LocalDateTime now = LocalDateTime.now();
        if ( request.getExpectTime() == null ) {
            request.setExpectTime( now );
        }
        if ( request.getFireTime() == null ) {
            request.setFireTime( request.getExpectTime() );
        }
        if ( request.getBusinessTimeEpoch() == null ) {
            request.setBusinessTimeEpoch( request.getExpectTime() );
        }

        TaskInstantaneousContext context = request.toContext();
        TaskInstantaneousPrepareResult prepareResult = this.mTaskInstantaneousPreparator.prepare( context );
        InstanceDepartureResult departureResult = this.mInstanceInstantaneousImpetus.impel(
                List.of( prepareResult.getInstance().getInstanceEntry() ),
                request.getFireTime(),
                context.toLaunchFeature()
        );

        return new TaskInstantaneousSubmitResult( request, prepareResult, departureResult );
    }


}
