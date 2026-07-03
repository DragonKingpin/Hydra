package com.acorn.redqueen.service.registry.grpc.client;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import com.pinecone.framework.system.prototype.Pinenut;

public class GrpcServiceClientStateSynchronizer implements Pinenut {

    protected static final long RetryDelayMillis0 = 500L;

    protected static final long RetryDelayMillis1 = 1000L;

    protected static final long RetryDelayMillis2 = 2000L;

    protected static final long AsyncSynchronizeQuietMillis = 1000L;

    protected GrpcServiceClientTransport mClient;

    protected ReentrantLock mSyncLock;

    protected Condition mSyncFinishedCondition;

    protected Condition mRetryCondition;

    protected boolean mbSynchronizing;

    protected boolean mbResyncRequested;

    public GrpcServiceClientStateSynchronizer( GrpcServiceClientTransport client ) {
        this.mClient = client;
        this.mSyncLock = new ReentrantLock();
        this.mSyncFinishedCondition = this.mSyncLock.newCondition();
        this.mRetryCondition = this.mSyncLock.newCondition();
    }

    public void requestSynchronize( String szReason ) {
        if ( !this.startSynchronizing() ) {
            this.requestResynchronize();
            return;
        }

        Thread syncThread = new Thread( () -> {
            this.awaitAsyncSynchronizeQuietWindow();
            this.runSynchronizeLoop( szReason );
        }, "redqueen-grpc-service-control-sync" );
        syncThread.setDaemon( true );
        syncThread.start();
    }

    public boolean synchronizeBlocking( String szReason ) {
        if ( !this.startSynchronizing() ) {
            this.requestResynchronize();
            this.awaitSynchronizingFinished();
            return this.mClient.isControlReady();
        }

        return this.runSynchronizeLoop( szReason );
    }

    protected boolean startSynchronizing() {
        this.mSyncLock.lock();
        try {
            if ( this.mbSynchronizing ) {
                this.mbResyncRequested = true;
                return false;
            }
            this.mbSynchronizing = true;
            return true;
        }
        finally {
            this.mSyncLock.unlock();
        }
    }

    protected void requestResynchronize() {
        this.mSyncLock.lock();
        try {
            this.mbResyncRequested = true;
            this.mRetryCondition.signalAll();
        }
        finally {
            this.mSyncLock.unlock();
        }
    }

    protected void awaitSynchronizingFinished() {
        this.mSyncLock.lock();
        try {
            while ( this.mbSynchronizing ) {
                this.mSyncFinishedCondition.await();
            }
        }
        catch ( InterruptedException e ) {
            Thread.currentThread().interrupt();
        }
        finally {
            this.mSyncLock.unlock();
        }
    }

    protected boolean runSynchronizeLoop( String szReason ) {
        int nFailureCount = 0;
        try {
            while ( !this.mClient.isTerminated() ) {
                this.mSyncLock.lock();
                try {
                    this.mbResyncRequested = false;
                }
                finally {
                    this.mSyncLock.unlock();
                }

                if ( this.mClient.synchronizeControlStateOnce( szReason ) ) {
                    nFailureCount = 0;
                    this.mSyncLock.lock();
                    try {
                        if ( !this.mbResyncRequested ) {
                            return true;
                        }
                    }
                    finally {
                        this.mSyncLock.unlock();
                    }
                    continue;
                }

                this.awaitBeforeRetry( nFailureCount );
                ++nFailureCount;
            }
            return false;
        }
        finally {
            this.mSyncLock.lock();
            try {
                this.mbSynchronizing = false;
                this.mSyncFinishedCondition.signalAll();
            }
            finally {
                this.mSyncLock.unlock();
            }
        }
    }

    protected long retryDelayMillis( int nFailureCount ) {
        if ( nFailureCount <= 0 ) {
            return RetryDelayMillis0;
        }
        if ( nFailureCount == 1 ) {
            return RetryDelayMillis1;
        }
        return RetryDelayMillis2;
    }

    protected void awaitBeforeRetry( int nFailureCount ) {
        long nDelayMillis = this.retryDelayMillis( nFailureCount );
        this.mSyncLock.lock();
        try {
            if ( this.mbResyncRequested ) {
                return;
            }
            this.mRetryCondition.await( nDelayMillis, TimeUnit.MILLISECONDS );
        }
        catch ( InterruptedException e ) {
            Thread.currentThread().interrupt();
        }
        finally {
            this.mSyncLock.unlock();
        }
    }

    protected void awaitAsyncSynchronizeQuietWindow() {
        long nNanos = TimeUnit.MILLISECONDS.toNanos( AsyncSynchronizeQuietMillis );
        this.mSyncLock.lock();
        try {
            while ( nNanos > 0L ) {
                try {
                    nNanos = this.mRetryCondition.awaitNanos( nNanos );
                }
                catch ( InterruptedException e ) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
        finally {
            this.mSyncLock.unlock();
        }
    }

}



