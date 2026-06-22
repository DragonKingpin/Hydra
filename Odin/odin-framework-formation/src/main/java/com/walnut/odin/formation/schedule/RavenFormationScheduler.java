package com.walnut.odin.formation.schedule;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import com.walnut.odin.conduct.schedule.UniformTaskScheduler;
import com.walnut.odin.formation.FormationConfig;
import com.walnut.odin.formation.FormationRunStatus;
import com.walnut.odin.formation.GenericFormationRun;
import com.walnut.odin.formation.dispatch.FormationDispatcher;
import com.walnut.odin.formation.entity.FormationSchedulerRuntimeSnapshot;
import com.walnut.odin.formation.recovery.FormationRunReconciler;
import com.walnut.odin.formation.service.FormationFlowService;
import com.walnut.odin.formation.source.FormationRunMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RavenFormationScheduler implements FormationScheduler {

    private static final Logger log = LoggerFactory.getLogger( RavenFormationScheduler.class );

    protected FormationConfig       mConfig;
    protected FormationRunMapper    mRunMapper;
    protected FormationFlowService  mFlowService;
    protected UniformTaskScheduler  mTaskScheduler;
    protected FormationDispatcher   mDispatcher;
    protected FormationRunReconciler mReconciler;

    protected ScheduledExecutorService mExecutor;
    protected AtomicBoolean            mRunning = new AtomicBoolean( false );
    protected AtomicBoolean            mPulsing = new AtomicBoolean( false );
    protected AtomicLong               mPulseSeq = new AtomicLong( 0L );
    protected AtomicLong               mSkippedPulseCount = new AtomicLong( 0L );

    protected volatile LocalDateTime mLastPulseStartTime;
    protected volatile LocalDateTime mLastPulseFinishTime;
    protected volatile Throwable     mLastPulseError;
    protected volatile long          mnLastRecoveryPulseMillis;

    public RavenFormationScheduler(
            FormationConfig config,
            FormationRunMapper runMapper,
            FormationFlowService flowService,
            UniformTaskScheduler taskScheduler,
            FormationDispatcher dispatcher,
            FormationRunReconciler reconciler
    ) {
        this.mConfig = config;
        this.mRunMapper = runMapper;
        this.mFlowService = flowService;
        this.mTaskScheduler = taskScheduler;
        this.mDispatcher = dispatcher;
        this.mReconciler = reconciler;
    }

    @Override
    public void startup() {
        if ( !this.mConfig.isFormationEnabled() || !this.mConfig.isFormationSchedulerEnabled() ) {
            log.info( "[OdinFormation] [SchedulerDisabled] <Pass>" );
            return;
        }
        if ( !this.mRunning.compareAndSet( false, true ) ) {
            return;
        }

        this.mReconciler.reconcileStartup();
        long nStartupDelayMillis = Math.max( 0L, this.mConfig.getFormationSchedulerStartupDelayMillis() );
        long nTickMillis = Math.max( 1L, this.mConfig.getFormationSchedulerTickMillis() );
        this.mExecutor = Executors.newSingleThreadScheduledExecutor( runnable -> {
            Thread thread = new Thread( runnable, "odin-formation-scheduler" );
            thread.setDaemon( true );
            return thread;
        } );
        this.mExecutor.scheduleWithFixedDelay(
                this::cyclePulse,
                nStartupDelayMillis,
                nTickMillis,
                TimeUnit.MILLISECONDS
        );
        log.info(
                "[OdinFormation] [SchedulerStarted] (Mode: `{}`, NodeId: `{}`, Partition: `{}`) <Ready>",
                this.mConfig.getFormationMode(),
                this.mConfig.getFormationNodeId(),
                this.mConfig.getFormationPartitionName()
        );
    }

    @Override
    public void shutdown() {
        if ( !this.mRunning.compareAndSet( true, false ) ) {
            return;
        }

        ScheduledExecutorService executor = this.mExecutor;
        this.mExecutor = null;
        if ( executor != null ) {
            executor.shutdown();
            try {
                if ( !executor.awaitTermination(
                        Math.max( 0L, this.mConfig.getFormationSchedulerGracefulShutdownMillis() ),
                        TimeUnit.MILLISECONDS
                ) ) {
                    executor.shutdownNow();
                }
            }
            catch ( InterruptedException e ) {
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
        log.info( "[OdinFormation] [SchedulerStopped] (PulseCount: `{}`) <Done>", this.mPulseSeq.get() );
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
        if ( !this.enterPulse( "manual" ) ) {
            return;
        }
        try {
            this.executePulse( pulseTime );
        }
        finally {
            this.exitPulse();
        }
    }

    protected void cyclePulse() {
        if ( !this.mRunning.get() || !this.enterPulse( "cycle" ) ) {
            return;
        }
        long nPulseId = this.mPulseSeq.incrementAndGet();
        long nStartMillis = System.currentTimeMillis();
        LocalDateTime pulseTime = LocalDateTime.now();
        this.mLastPulseStartTime = pulseTime;
        try {
            this.executePulse( pulseTime );
            this.mLastPulseError = null;
            this.mLastPulseFinishTime = LocalDateTime.now();
            this.logPulseDone( nPulseId, nStartMillis );
        }
        catch ( Throwable e ) {
            this.mLastPulseError = e;
            this.mLastPulseFinishTime = LocalDateTime.now();
            log.error( "[OdinFormation] [SchedulerPulseFailed] (PulseId: `{}`) <Failed>", nPulseId, e );
        }
        finally {
            this.exitPulse();
        }
    }

    protected void executePulse( LocalDateTime pulseTime ) {
        if ( this.shouldRunRecoveryPulse() ) {
            this.mReconciler.reconcilePulse( pulseTime );
        }

        List<GenericFormationRun> runs = this.mRunMapper.fetchRunnableRuns(
                Math.max( 1, this.mConfig.getFormationDispatcherPollBatchSize() )
        );
        if ( runs == null || runs.isEmpty() ) {
            return;
        }

        for ( GenericFormationRun run : runs ) {
            if ( run == null || run.getGuid() == null ) {
                continue;
            }
            this.mRunMapper.markRunning( run.getGuid() );
            boolean offered = this.mDispatcher.offer( () -> this.executeRun( run.getGuid() ) );
            if ( !offered ) {
                this.mRunMapper.updateStatus( run.getGuid(), FormationRunStatus.Prepared.getName() );
            }
        }
    }

    protected void executeRun( com.pinecone.framework.util.id.GUID runGuid ) {
        GenericFormationRun run = this.mRunMapper.selectByGuid( runGuid );
        if ( run == null ) {
            return;
        }
        try {
            this.mFlowService.flow( run, this.mTaskScheduler, this.mConfig.getFormationNodeId() );
            GenericFormationRun latest = this.mRunMapper.selectByGuid( runGuid );
            if ( latest != null && latest.getFailedCount() > 0L ) {
                this.mRunMapper.updateStatus( runGuid, FormationRunStatus.Failed.getName() );
            }
            else {
                this.mRunMapper.updateStatus( runGuid, FormationRunStatus.Completed.getName() );
            }
        }
        catch ( Throwable e ) {
            this.mRunMapper.updateStatus( runGuid, FormationRunStatus.Failed.getName() );
            throw e;
        }
    }

    protected boolean enterPulse( String reason ) {
        if ( this.mConfig.isFormationSchedulerAllowOverlappedPulse() ) {
            return true;
        }
        if ( this.mPulsing.compareAndSet( false, true ) ) {
            return true;
        }
        this.mSkippedPulseCount.incrementAndGet();
        log.info( "[OdinFormation] [PulseSkipped] (Reason: `{}`) <Skipped>", reason );
        return false;
    }

    protected void exitPulse() {
        if ( !this.mConfig.isFormationSchedulerAllowOverlappedPulse() ) {
            this.mPulsing.set( false );
        }
    }

    protected boolean shouldRunRecoveryPulse() {
        long nIntervalMillis = this.mConfig.getFormationSchedulerRecoveryPulseMillis();
        if ( nIntervalMillis <= 0L ) {
            return false;
        }
        long nowMillis = System.currentTimeMillis();
        if ( this.mnLastRecoveryPulseMillis > 0L && nowMillis - this.mnLastRecoveryPulseMillis < nIntervalMillis ) {
            return false;
        }
        this.mnLastRecoveryPulseMillis = nowMillis;
        return true;
    }

    protected void logPulseDone( long pulseId, long startMillis ) {
        if ( !this.mConfig.isFormationSchedulerPulseLogEnabled() ) {
            return;
        }
        long nDurationMillis = System.currentTimeMillis() - startMillis;
        if ( this.mConfig.getFormationSchedulerSlowPulseMillis() > 0L
                && nDurationMillis >= this.mConfig.getFormationSchedulerSlowPulseMillis() ) {
            log.warn(
                    "[OdinFormation] [SlowSchedulerPulse] (PulseId: `{}`, DurationMs: `{}`) <Warn>",
                    pulseId,
                    nDurationMillis
            );
            return;
        }
        log.info( "[OdinFormation] [SchedulerPulse] (PulseId: `{}`, DurationMs: `{}`) <Done>", pulseId, nDurationMillis );
    }

    @Override
    public FormationSchedulerRuntimeSnapshot retrieveRuntimeSnapshot() {
        FormationSchedulerRuntimeSnapshot snapshot = new FormationSchedulerRuntimeSnapshot();
        snapshot.setRunning( this.mRunning.get() );
        snapshot.setPulsing( this.mPulsing.get() );
        snapshot.setPulseSeq( this.mPulseSeq.get() );
        snapshot.setSkippedPulseCount( this.mSkippedPulseCount.get() );
        snapshot.setLastPulseStartTime( this.mLastPulseStartTime );
        snapshot.setLastPulseFinishTime( this.mLastPulseFinishTime );
        Throwable lastPulseError = this.mLastPulseError;
        if ( lastPulseError != null ) {
            snapshot.setLastPulseErrorClass( lastPulseError.getClass().getName() );
            snapshot.setLastPulseErrorMessage( lastPulseError.getMessage() );
        }
        return snapshot;
    }
}
