package com.walnut.odin.conduct.schedule;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pinecone.hydra.task.TaskInstanceStatus;
import com.pinecone.hydra.task.kom.instance.InstanceEntry;
import com.pinecone.hydra.task.kom.instance.InstanceInstrument;
import com.pinecone.hydra.task.kom.instance.TaskInstanceQuery;
import com.pinecone.hydra.task.marshal.TaskScheduleType;
import com.walnut.odin.conduct.schedule.entity.TaskSchedulerEngineRuntimeSnapshot;
import com.walnut.odin.conduct.recovery.TaskSchedulerReconciler;
import com.walnut.odin.task.RavenTaskConfig;
import com.walnut.odin.task.troll.LaunchFeature;

public class RavenInstantaneousEngine implements InstantaneousEngine {

    protected static final List<TaskInstanceStatus> RecoverableStatuses = List.of(
            TaskInstanceStatus.New,
            TaskInstanceStatus.ResourceWait,
            TaskInstanceStatus.DepartureStandby,
            TaskInstanceStatus.ProcessCreating
    );
    protected static final List<TaskScheduleType> RecoverableScheduleTypes = List.of(
            TaskScheduleType.Manual,
            TaskScheduleType.Temporary
    );

    protected Logger log = LoggerFactory.getLogger( this.getClass() );

    protected UniformTaskScheduler       mTaskScheduler;
    protected RavenTaskConfig            mRavenTaskConfig;
    protected InstanceInstrument         mInstanceInstrument;
    protected TaskSchedulerReconciler    mTaskSchedulerReconciler;
    protected ScheduledExecutorService   mExecutor;
    protected AtomicBoolean              mRunning = new AtomicBoolean( false );
    protected AtomicBoolean              mPulsing = new AtomicBoolean( false );
    protected AtomicLong                 mPulseSeq = new AtomicLong( 0L );
    protected volatile LocalDateTime     mLastPulseTime;
    protected volatile Thread            mEngineThread;
    protected volatile Throwable         mLastPulseError;

    public RavenInstantaneousEngine( UniformTaskScheduler taskScheduler, TaskSchedulerReconciler reconciler ) {
        this.mTaskScheduler = taskScheduler;
        this.mRavenTaskConfig = taskScheduler.ravenTaskConfig();
        this.mInstanceInstrument = taskScheduler.instanceInstrument();
        this.mTaskSchedulerReconciler = reconciler;
    }

    @Override
    public UniformTaskScheduler taskScheduler() {
        return this.mTaskScheduler;
    }

    @Override
    public void startService() {
        if ( !this.mRavenTaskConfig.isInstantaneousEngineEnabled() ) {
            this.log.info( "[OdinInstantaneousEngine] [Disabled] <Pass>" );
            return;
        }
        if ( !this.mRunning.compareAndSet( false, true ) ) {
            return;
        }

        long nStartupDelayMillis = Math.max( 0L, this.mRavenTaskConfig.getInstantaneousEngineStartupDelayMillis() );
        long nPulseMillis = Math.max( 1L, this.mRavenTaskConfig.getInstantaneousEnginePulseMillis() );
        this.mExecutor = Executors.newSingleThreadScheduledExecutor( runnable -> {
            Thread thread = new Thread( runnable, "odin-task-instantaneous-engine" );
            thread.setDaemon( true );
            this.mEngineThread = thread;
            return thread;
        } );
        this.mExecutor.scheduleWithFixedDelay(
                this::pulse,
                nStartupDelayMillis,
                nPulseMillis,
                TimeUnit.MILLISECONDS
        );
        this.log.info(
                "[OdinInstantaneousEngine] [Started] (PulseMillis: `{}`, StartupDelayMillis: `{}`) <Ready>",
                nPulseMillis,
                nStartupDelayMillis
        );
    }

    @Override
    public void terminateService( long gracefulShutdownMillis ) {
        if ( !this.mRunning.compareAndSet( true, false ) ) {
            return;
        }

        ScheduledExecutorService executor = this.mExecutor;
        this.mExecutor = null;
        if ( executor != null ) {
            executor.shutdown();
            try {
                if ( !executor.awaitTermination( Math.max( 0L, gracefulShutdownMillis ), TimeUnit.MILLISECONDS ) ) {
                    executor.shutdownNow();
                }
            }
            catch ( InterruptedException e ) {
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
        this.mEngineThread = null;
        this.log.info( "[OdinInstantaneousEngine] [Stopped] (PulseCount: `{}`) <Done>", this.mPulseSeq.get() );
    }

    @Override
    public void pulse() {
        this.pulse( LocalDateTime.now() );
    }

    @Override
    public void pulse( LocalDateTime pulseTime ) {
        if ( pulseTime == null ) {
            pulseTime = LocalDateTime.now();
        }
        if ( !this.mPulsing.compareAndSet( false, true ) ) {
            return;
        }

        long nPulseId = this.mPulseSeq.incrementAndGet();
        long nStartMillis = System.currentTimeMillis();
        try {
            this.mLastPulseTime = pulseTime;
            this.mTaskSchedulerReconciler.reconcileRetryableTerminalInstances( pulseTime );
            this.impelRecoverableManualInstances( pulseTime );
            this.mLastPulseError = null;
            this.logPulseDone( nPulseId, nStartMillis );
        }
        catch ( Throwable e ) {
            this.mLastPulseError = e;
            this.log.error( "[OdinInstantaneousEngine] [PulseFailed] (PulseId: `{}`) <Failed>", nPulseId, e );
        }
        finally {
            this.mPulsing.set( false );
        }
    }

    protected void impelRecoverableManualInstances( LocalDateTime pulseTime ) {
        for ( TaskInstanceStatus status : RecoverableStatuses ) {
            for ( TaskScheduleType scheduleType : RecoverableScheduleTypes ) {
                this.impelRecoverableManualInstances( status, scheduleType, pulseTime );
            }
        }
    }

    protected void impelRecoverableManualInstances(
            TaskInstanceStatus status, TaskScheduleType scheduleType, LocalDateTime pulseTime
    ) {
        TaskInstanceQuery query = new TaskInstanceQuery();
        query.setRunStatus( status.getName() );
        query.setScheduleType( scheduleType.getName() );
        query.setExpectTimeEnd( pulseTime );
        query.setLimit( Math.max( 1, this.mRavenTaskConfig.getInstantaneousEngineMaxInstancesPerPulse() ) );

        List<InstanceEntry> entries = this.mInstanceInstrument.fetchInstances( query );
        if ( entries == null || entries.isEmpty() ) {
            return;
        }

        for ( InstanceEntry entry : entries ) {
            this.impelRecoverableManualInstance( entry, pulseTime );
        }
    }

    protected void impelRecoverableManualInstance( InstanceEntry entry, LocalDateTime pulseTime ) {
        if ( entry == null || entry.getGuid() == null ) {
            return;
        }

        LaunchFeature feature = new LaunchFeature()
                .withAllowInstantaneousDepartureBypass( true );
        if ( entry.getDesignatedProcessor() != null && !entry.getDesignatedProcessor().trim().isEmpty() ) {
            feature.withProcessorDesignated( entry.getDesignatedProcessor() );
        }

        try {
            this.log.info(
                    "[OdinInstantaneousEngine] [RecoverableManualDispatch] "
                            + "(InstanceGuid: `{}`, TaskGuid: `{}`, Status: `{}`, DesignatedProcessor: `{}`, AffinityProcessor: `{}`) <Start>",
                    entry.getGuid(),
                    entry.getTaskGuid(),
                    entry.getRunStatus(),
                    entry.getDesignatedProcessor(),
                    entry.getAffinityProcessor()
            );
            this.mTaskScheduler.instanceInstantaneousImpetus().impel( List.of( entry ), pulseTime, feature );
        }
        catch ( Exception e ) {
            this.log.warn(
                    "[OdinInstantaneousEngine] [RecoverableManualDispatchFailed] "
                            + "(InstanceGuid: `{}`, TaskGuid: `{}`, Status: `{}`, DesignatedProcessor: `{}`, AffinityProcessor: `{}`) <Waiting>",
                    entry.getGuid(),
                    entry.getTaskGuid(),
                    entry.getRunStatus(),
                    entry.getDesignatedProcessor(),
                    entry.getAffinityProcessor(),
                    e
            );
        }
    }

    protected void logPulseDone( long pulseId, long startMillis ) {
        long nDurationMillis = System.currentTimeMillis() - startMillis;
        long nSlowPulseMillis = this.mRavenTaskConfig.getInstantaneousEngineSlowPulseMillis();
        if ( nSlowPulseMillis > 0L && nDurationMillis >= nSlowPulseMillis ) {
            this.log.warn(
                    "[OdinInstantaneousEngine] [SlowPulse] (PulseId: `{}`, DurationMs: `{}`) <Warn>",
                    pulseId,
                    nDurationMillis
            );
            return;
        }
        if ( !this.mRavenTaskConfig.isInstantaneousEnginePulseLogEnabled() ) {
            return;
        }
        this.log.info(
                "[OdinInstantaneousEngine] [Pulse] (PulseId: `{}`, DurationMs: `{}`) <Done>",
                pulseId,
                nDurationMillis
        );
    }

    @Override
    public boolean isRunning() {
        return this.mRunning.get();
    }

    @Override
    public TaskSchedulerEngineRuntimeSnapshot runtimeSnapshot() {
        TaskSchedulerEngineRuntimeSnapshot snapshot = new TaskSchedulerEngineRuntimeSnapshot();
        snapshot.setEngineName( "InstantaneousEngine" );
        snapshot.setEnabled( this.mRavenTaskConfig.isInstantaneousEngineEnabled() );
        snapshot.setRunning( this.mRunning.get() );
        snapshot.setPulsing( this.mPulsing.get() );
        snapshot.setPulseSeq( this.mPulseSeq.get() );
        snapshot.setLastPulseTime( this.mLastPulseTime );
        snapshot.setSupportsPulse( true );
        snapshot.setSupportsDailyPulse( false );
        snapshot.setStartupDelayMillis( this.mRavenTaskConfig.getInstantaneousEngineStartupDelayMillis() );
        snapshot.setPulseMillis( this.mRavenTaskConfig.getInstantaneousEnginePulseMillis() );
        snapshot.setScanIdWindow( this.mRavenTaskConfig.getInstantaneousEngineScanIdWindow() );
        snapshot.setMaxInstancesPerPulse( this.mRavenTaskConfig.getInstantaneousEngineMaxInstancesPerPulse() );
        snapshot.setPulseLogEnabled( this.mRavenTaskConfig.isInstantaneousEnginePulseLogEnabled() );
        snapshot.setSlowPulseMillis( this.mRavenTaskConfig.getInstantaneousEngineSlowPulseMillis() );

        Thread engineThread = this.mEngineThread;
        if ( engineThread != null ) {
            snapshot.setThreadName( engineThread.getName() );
            snapshot.setThreadState( engineThread.getState().name() );
            snapshot.setThreadAlive( engineThread.isAlive() );
        }

        Throwable lastPulseError = this.mLastPulseError;
        if ( lastPulseError != null ) {
            snapshot.setLastPulseErrorClass( lastPulseError.getClass().getName() );
            snapshot.setLastPulseErrorMessage( lastPulseError.getMessage() );
        }
        return snapshot;
    }
}
