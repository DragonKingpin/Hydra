package com.walnut.odin.dispatch;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import com.pinecone.framework.util.id.Identification;
import com.pinecone.hydra.deploy.Server;

public abstract class ArchTaskExecutionI32Queue implements TaskExecutionQueue {

    protected String mszName;
    protected Server mDeployClusterServer;
    protected String mszClusterPath;
    protected String mszClusterName;
    protected int    mnControlClientId;

    protected volatile int mnCapacity;
    protected volatile int mnMaxCapacity;
    protected volatile int mnMinCapacity;
    protected volatile int mnUsedCapacity;
    protected volatile int mnRuntimeInstanceCapacity;

    protected final Deque<TaskLaunchContext>               mWaitingQueue;
    protected final Map<Identification, TaskLaunchContext> mRunningInstances;

    protected final ReentrantLock mLock;

    protected ArchTaskExecutionI32Queue() {
        this.mWaitingQueue     = new ArrayDeque<>();
        this.mRunningInstances = new HashMap<>();
        this.mLock             = new ReentrantLock();
    }


    public void offer( Collection<TaskLaunchContext> contexts ) {
        this.mLock.lock();
        try {
            for ( TaskLaunchContext context : contexts ) {
                this.mWaitingQueue.addLast( context );
            }
        }
        finally {
            this.mLock.unlock();
        }
    }

    public void offer( TaskLaunchContext context ) {
        this.mLock.lock();
        try {
            this.mWaitingQueue.addLast( context );
        }
        finally {
            this.mLock.unlock();
        }
    }

    public Collection<TaskLaunchContext> consume( int n, boolean bForce ) {
        this.mLock.lock();
        try {
            if ( n <= 0 ) {
                return Collections.emptyList();
            }

            int nFreeCapacity = this.pendingCapacity();
            if ( nFreeCapacity <= 0 ) {
                return Collections.emptyList();
            }

            int nAllowedByRuntime;
            if ( bForce ) {
                nAllowedByRuntime = n;
            }
            else {
                nAllowedByRuntime = this.mnRuntimeInstanceCapacity > 0 ? this.mnRuntimeInstanceCapacity : n;
            }

            int nConsume = Math.min(
                    Math.min( n, nAllowedByRuntime ),
                    Math.min( nFreeCapacity, this.mWaitingQueue.size() )
            );

            if ( nConsume <= 0 ) {
                return Collections.emptyList();
            }

            List<TaskLaunchContext> result = new ArrayList<>( nConsume );
            while ( nConsume > 0 ) {
                TaskLaunchContext context = this.mWaitingQueue.pollFirst();
                if ( context == null ) {
                    break;
                }

                Identification id = context.getTaskInstance().getId();

                this.mRunningInstances.put( id, context );
                ++this.mnUsedCapacity;

                result.add( context );
                --nConsume;
            }

            return result;
        }
        finally {
            this.mLock.unlock();
        }
    }

    public Collection<TaskLaunchContext> consume( int n ) {
        return this.consume( n, false );
    }

    public Collection<TaskLaunchContext> consume() {
        return this.consume( this.mnRuntimeInstanceCapacity, false );
    }

    public Collection<TaskLaunchContext> pipeConsume( Collection<TaskLaunchContext> products ) {
        this.mLock.lock();
        try {
            if ( products == null || products.isEmpty() ) {
                return Collections.emptyList();
            }

            int nFreeCapacity = this.pendingCapacity();
            if ( nFreeCapacity <= 0 ) {
                for ( TaskLaunchContext context : products ) {
                    this.mWaitingQueue.addLast( context );
                }
                return Collections.emptyList();
            }

            int nAllowedByRuntime = this.mnRuntimeInstanceCapacity > 0 ? this.mnRuntimeInstanceCapacity : products.size();
            int nConsume = Math.min(
                    Math.min( products.size(), nAllowedByRuntime ),
                    nFreeCapacity
            );

            if ( nConsume <= 0 ) {
                for ( TaskLaunchContext context : products ) {
                    this.mWaitingQueue.addLast( context );
                }
                return Collections.emptyList();
            }

            List<TaskLaunchContext> consumed = new ArrayList<>( nConsume );

            int nIndex = 0;
            for ( TaskLaunchContext context : products ) {
                if ( nIndex < nConsume ) {

                    Identification id = context.getTaskInstance().getId();

                    this.mRunningInstances.put( id, context );
                    ++this.mnUsedCapacity;

                    consumed.add( context );
                    ++nIndex;
                }
                else {
                    this.mWaitingQueue.addLast( context );
                }
            }

            return consumed;
        }
        finally {
            this.mLock.unlock();
        }
    }

    public Collection<TaskLaunchContext> runningInstances() {
        this.mLock.lock();
        try {
            return Collections.unmodifiableCollection( this.mRunningInstances.values() );
        }
        finally {
            this.mLock.unlock();
        }
    }

    public void markTerminated( Identification id ) {
        this.mLock.lock();
        try {
            TaskLaunchContext context = this.mRunningInstances.remove( id );
            if ( context != null ) {
                this.mnUsedCapacity--;
            }
        }
        finally {
            this.mLock.unlock();
        }
    }

    public Collection<TaskLaunchContext> recycleTerminated( Collection<Identification> terminatedIds ) {
        this.mLock.lock();
        try {
            if ( terminatedIds == null || terminatedIds.isEmpty() ) {
                return Collections.emptyList();
            }

            List<TaskLaunchContext> recycled = new ArrayList<>( terminatedIds.size() );
            for ( Identification id : terminatedIds ) {
                TaskLaunchContext context = this.mRunningInstances.remove( id );
                if ( context != null ) {
                    --this.mnUsedCapacity;
                    recycled.add( context );
                }
            }

            if ( recycled.isEmpty() ) {
                return Collections.emptyList();
            }
            return recycled;
        }
        finally {
            this.mLock.unlock();
        }
    }

    public Collection<TaskLaunchContext> consumePending() {
        return this.consume( this.mnRuntimeInstanceCapacity, false );
    }

    public Collection<TaskLaunchContext> shiftPipeline( Collection<Identification> terminatedIds ) {
        // Recycle terminated instances first to release capacity.
        // The returned collection only represents newly consumed contexts.
        // Recycled instances are intentionally not part of the return value,
        // since this method models a "release-then-refill" pipeline step.
        // Callers must not rely on the return value to infer recycle results.
        this.recycleTerminated( terminatedIds );

        return this.consumePending();
    }

    public int pendingCapacity() {
        return this.mnCapacity - this.mnUsedCapacity;
    }


    public int waitingSize() {
        this.mLock.lock();
        try {
            return this.mWaitingQueue.size();
        }
        finally {
            this.mLock.unlock();
        }
    }

    public int runningSize() {
        this.mLock.lock();
        try {
            return this.mRunningInstances.size();
        }
        finally {
            this.mLock.unlock();
        }
    }

    public boolean isFull() {
        this.mLock.lock();
        try {
            return this.mnUsedCapacity >= this.mnCapacity;
        }
        finally {
            this.mLock.unlock();
        }
    }

    public boolean isIdle() {
        this.mLock.lock();
        try {
            return this.mnUsedCapacity == 0 && this.mWaitingQueue.isEmpty();
        }
        finally {
            this.mLock.unlock();
        }
    }


    public Collection<TaskLaunchContext> drainAllWaiting() {
        this.mLock.lock();
        try {
            if ( this.mWaitingQueue.isEmpty() ) {
                return Collections.emptyList();
            }
            List<TaskLaunchContext> drained = new ArrayList<>( this.mWaitingQueue.size() );

            while ( !this.mWaitingQueue.isEmpty() ) {
                TaskLaunchContext context = this.mWaitingQueue.pollFirst();
                if ( context == null ) {
                    break;
                }
                drained.add( context );
            }
            return drained;
        }
        finally {
            this.mLock.unlock();
        }
    }


    @Override
    public void applyCapacity( int nCapacity ) {
        this.mLock.lock();
        try {
            this.mnCapacity = (int) nCapacity;
        }
        finally {
            this.mLock.unlock();
        }
    }

    @Override
    public void applyMaxCapacity( int nMaxCapacity ) {
        this.mLock.lock();
        try {
            this.mnMaxCapacity = (int) nMaxCapacity;
        }
        finally {
            this.mLock.unlock();
        }
    }

    @Override
    public void applyMinCapacity( int nMinCapacity ) {
        this.mLock.lock();
        try {
            this.mnMinCapacity = (int) nMinCapacity;
        }
        finally {
            this.mLock.unlock();
        }
    }

    @Override
    public void applyRuntimeInstanceCapacity( int nCapacity ) {
        this.mLock.lock();
        try {
            this.mnRuntimeInstanceCapacity = nCapacity;
        }
        finally {
            this.mLock.unlock();
        }
    }




    @Override
    public String getName() {
        return this.mszName;
    }

    @Override
    public Server getDeployClusterServer() {
        return this.mDeployClusterServer;
    }

    @Override
    public String getClusterPath() {
        return this.mszClusterPath;
    }

    @Override
    public String getClusterName() {
        return this.mszClusterName;
    }

    @Override
    public int getControlClientId() {
        return this.mnControlClientId;
    }

    @Override
    public int getCapacity() {
        return this.mnCapacity;
    }

    @Override
    public int getMaxCapacity() {
        return this.mnMaxCapacity;
    }

    @Override
    public int getMinCapacity() {
        return this.mnMinCapacity;
    }

    @Override
    public int getUsedCapacity() {
        return this.mnUsedCapacity;
    }

    @Override
    public int getRuntimeInstanceCapacity() {
        return this.mnRuntimeInstanceCapacity;
    }

}