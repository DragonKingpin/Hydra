package com.walnut.odin.formation.dispatch;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import com.walnut.odin.formation.FormationConfig;
import com.walnut.odin.formation.dto.FormationDispatcherRuntimeSnapshot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocalFormationDispatcher implements FormationDispatcher {

    private static final Logger log = LoggerFactory.getLogger( LocalFormationDispatcher.class );

    protected FormationConfig   mConfig;
    protected ThreadPoolExecutor mExecutor;
    protected AtomicBoolean     mRunning = new AtomicBoolean( false );
    protected AtomicLong        mAcceptedCount = new AtomicLong( 0L );
    protected AtomicLong        mCompletedCount = new AtomicLong( 0L );
    protected AtomicLong        mFailedCount = new AtomicLong( 0L );
    protected AtomicLong        mRejectedCount = new AtomicLong( 0L );

    public LocalFormationDispatcher( FormationConfig config ) {
        this.mConfig = config;
    }

    @Override
    public void startup() {
        if ( !this.mConfig.isFormationDispatcherEnabled() ) {
            log.info( "[OdinFormation] [DispatcherDisabled] <Pass>" );
            return;
        }
        if ( !this.mRunning.compareAndSet( false, true ) ) {
            return;
        }

        int nWorkerCount = Math.max( 1, this.mConfig.getFormationDispatcherWorkerThreadCount() );
        int nQueueCapacity = Math.max( 1, this.mConfig.getFormationDispatcherQueueCapacity() );
        this.mExecutor = new ThreadPoolExecutor(
                nWorkerCount,
                nWorkerCount,
                0L,
                TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>( nQueueCapacity ),
                runnable -> {
                    Thread thread = new Thread( runnable, "odin-formation-dispatcher-worker" );
                    thread.setDaemon( true );
                    return thread;
                },
                ( runnable, executor ) -> {
                    this.mRejectedCount.incrementAndGet();
                    throw new RejectedExecutionException( "Odin formation dispatcher queue is full." );
                }
        );
        log.info(
                "[OdinFormation] [DispatcherStarted] (Workers: `{}`, QueueCapacity: `{}`) <Ready>",
                nWorkerCount,
                nQueueCapacity
        );
    }

    @Override
    public void shutdown() {
        if ( !this.mRunning.compareAndSet( true, false ) ) {
            return;
        }

        ThreadPoolExecutor executor = this.mExecutor;
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
        log.info( "[OdinFormation] [DispatcherStopped] <Done>" );
    }

    @Override
    public boolean offer( Runnable command ) {
        if ( command == null || !this.mRunning.get() || this.mExecutor == null ) {
            this.mRejectedCount.incrementAndGet();
            return false;
        }

        try {
            this.mExecutor.execute( () -> {
                try {
                    command.run();
                    this.mCompletedCount.incrementAndGet();
                }
                catch ( Throwable e ) {
                    this.mFailedCount.incrementAndGet();
                    log.error( "[OdinFormation] [DispatchCommandFailed] <Failed>", e );
                }
            } );
            this.mAcceptedCount.incrementAndGet();
            return true;
        }
        catch ( RuntimeException e ) {
            this.mRejectedCount.incrementAndGet();
            return false;
        }
    }

    @Override
    public FormationDispatcherRuntimeSnapshot retrieveRuntimeSnapshot() {
        FormationDispatcherRuntimeSnapshot snapshot = new FormationDispatcherRuntimeSnapshot();
        snapshot.setRunning( this.mRunning.get() );
        snapshot.setWorkerThreadCount( Math.max( 1, this.mConfig.getFormationDispatcherWorkerThreadCount() ) );
        snapshot.setAcceptedCount( this.mAcceptedCount.get() );
        snapshot.setCompletedCount( this.mCompletedCount.get() );
        snapshot.setFailedCount( this.mFailedCount.get() );
        snapshot.setRejectedCount( this.mRejectedCount.get() );
        ThreadPoolExecutor executor = this.mExecutor;
        if ( executor != null ) {
            snapshot.setQueueSize( executor.getQueue().size() );
        }
        return snapshot;
    }
}
