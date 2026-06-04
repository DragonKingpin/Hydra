package com.acorn.skynet.device.grpc.client;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import com.pinecone.framework.system.prototype.Pinenut;

public class GrpcDeviceClientStateSynchronizer implements Pinenut {

    protected static final long RetryDelayMillis0 = 500L;

    protected static final long RetryDelayMillis1 = 1000L;

    protected static final long RetryDelayMillis2 = 2000L;

    protected static final long AsyncSynchronizeQuietMillis = 1000L;

    protected final GrpcDeviceClientTransport client;

    protected final ReentrantLock syncLock;

    protected final Condition syncFinishedCondition;

    protected final Condition retryCondition;

    protected boolean synchronizing;

    protected boolean resyncRequested;

    public GrpcDeviceClientStateSynchronizer( GrpcDeviceClientTransport client ) {
        this.client = client;
        this.syncLock = new ReentrantLock();
        this.syncFinishedCondition = this.syncLock.newCondition();
        this.retryCondition = this.syncLock.newCondition();
    }

    public void requestSynchronize( String reason ) {
        if ( !this.startSynchronizing() ) {
            this.requestResynchronize();
            return;
        }

        Thread syncThread = new Thread( () -> {
            this.awaitAsyncSynchronizeQuietWindow();
            this.runSynchronizeLoop( reason );
        }, "skynet-grpc-device-lifecycle-sync" );
        syncThread.setDaemon( true );
        syncThread.start();
    }

    public boolean synchronizeBlocking( String reason ) {
        if ( !this.startSynchronizing() ) {
            this.requestResynchronize();
            this.awaitSynchronizingFinished();
            return this.client.isLifecycleReady();
        }

        return this.runSynchronizeLoop( reason );
    }

    protected boolean startSynchronizing() {
        this.syncLock.lock();
        try {
            if ( this.synchronizing ) {
                this.resyncRequested = true;
                return false;
            }
            this.synchronizing = true;
            return true;
        }
        finally {
            this.syncLock.unlock();
        }
    }

    protected void requestResynchronize() {
        this.syncLock.lock();
        try {
            this.resyncRequested = true;
            this.retryCondition.signalAll();
        }
        finally {
            this.syncLock.unlock();
        }
    }

    protected void awaitSynchronizingFinished() {
        this.syncLock.lock();
        try {
            while ( this.synchronizing ) {
                this.syncFinishedCondition.await();
            }
        }
        catch ( InterruptedException e ) {
            Thread.currentThread().interrupt();
        }
        finally {
            this.syncLock.unlock();
        }
    }

    protected boolean runSynchronizeLoop( String reason ) {
        int failureCount = 0;
        try {
            while ( !this.client.isTerminated() ) {
                this.syncLock.lock();
                try {
                    this.resyncRequested = false;
                }
                finally {
                    this.syncLock.unlock();
                }

                if ( this.client.synchronizeLifecycleStateOnce( reason ) ) {
                    failureCount = 0;
                    this.syncLock.lock();
                    try {
                        if ( !this.resyncRequested ) {
                            return true;
                        }
                    }
                    finally {
                        this.syncLock.unlock();
                    }
                    continue;
                }

                this.awaitBeforeRetry( failureCount );
                ++failureCount;
            }
            return false;
        }
        finally {
            this.syncLock.lock();
            try {
                this.synchronizing = false;
                this.syncFinishedCondition.signalAll();
            }
            finally {
                this.syncLock.unlock();
            }
        }
    }

    protected long retryDelayMillis( int failureCount ) {
        if ( failureCount <= 0 ) {
            return RetryDelayMillis0;
        }
        if ( failureCount == 1 ) {
            return RetryDelayMillis1;
        }
        return RetryDelayMillis2;
    }

    protected void awaitBeforeRetry( int failureCount ) {
        long delayMillis = this.retryDelayMillis( failureCount );
        this.syncLock.lock();
        try {
            if ( this.resyncRequested ) {
                return;
            }
            this.retryCondition.await( delayMillis, TimeUnit.MILLISECONDS );
        }
        catch ( InterruptedException e ) {
            Thread.currentThread().interrupt();
        }
        finally {
            this.syncLock.unlock();
        }
    }

    protected void awaitAsyncSynchronizeQuietWindow() {
        long nanos = TimeUnit.MILLISECONDS.toNanos( AsyncSynchronizeQuietMillis );
        this.syncLock.lock();
        try {
            while ( nanos > 0L ) {
                try {
                    nanos = this.retryCondition.awaitNanos( nanos );
                }
                catch ( InterruptedException e ) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
        finally {
            this.syncLock.unlock();
        }
    }
}
