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

public abstract class ArchTaskExecutionI32Queue implements TaskExecutionQueue {

    protected String mszName;

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


    protected void assertOfferCapacityLocked( int nIncoming ) throws QueueBadAllocatedException {
        if ( nIncoming <= 0 ) {
            return;
        }

        int nFuture = this.mnUsedCapacity + this.mWaitingQueue.size() + nIncoming;

        if ( nFuture > this.mnMaxCapacity ) {
            throw new QueueBadAllocatedException(
                    "Queue capacity exceeded. incoming=" + nIncoming +
                            ", used=" + this.mnUsedCapacity +
                            ", waiting=" + this.mWaitingQueue.size() +
                            ", max=" + this.mnMaxCapacity
            );
        }
    }


    @Override
    public void offer( Collection<TaskLaunchContext> contexts ) throws TaskDispatchException {
        this.mLock.lock();
        try {
            this.assertOfferCapacityLocked( contexts.size() );
            for ( TaskLaunchContext context : contexts ) {
                this.mWaitingQueue.addLast( context );
            }
        }
        finally {
            this.mLock.unlock();
        }
    }

    @Override
    public void offer( TaskLaunchContext context ) throws TaskDispatchException {
        this.mLock.lock();
        try {
            this.assertOfferCapacityLocked( 1 );
            this.mWaitingQueue.addLast( context );
        }
        finally {
            this.mLock.unlock();
        }
    }

    @Override
    public Collection<TaskLaunchContext> consume( int n, boolean bForce, TaskInstanceConsumer consumer ) throws TaskConsumeException {
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

                try {
                    consumer.tryConsume( context );
                }
                catch ( TaskConsumeException e ) {
                    ConsumeCompromisedPolice police = consumer.compromisedPolice();

                    switch ( police ) {
                        case EvictionIgnore: {
                            // 丢弃任务，不重新入队
                            --nConsume;
                            continue;
                        }
                        case EvictionException: {
                            e.setEvictionTask( context );
                            throw e;
                        }
                        case BreakException:
                        default: {
                            // 恢复任务到原队列位置（队头）
                            this.mWaitingQueue.addFirst( context );
                            throw e;
                        }
                    }
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

    @Override
    public Collection<TaskLaunchContext> consume( int n, TaskInstanceConsumer consumer ) throws TaskConsumeException {
        return this.consume( n, false, consumer );
    }

    @Override
    public Collection<TaskLaunchContext> consume( TaskInstanceConsumer consumer ) throws TaskConsumeException {
        return this.consume( this.mnRuntimeInstanceCapacity, false, consumer );
    }

    protected void addRemain( Collection<TaskLaunchContext> products, TaskLaunchContext context ) {
        boolean bSkipCurrent = true;
        for ( TaskLaunchContext remain : products ) {
            if ( bSkipCurrent ) {
                if ( remain == context ) {
                    bSkipCurrent = false;
                }
                continue;
            }
            this.mWaitingQueue.addLast( remain );
        }
    }

    @Override
    public Collection<TaskLaunchContext> pipeConsume( Collection<TaskLaunchContext> products, TaskInstanceConsumer consumer ) throws TaskDispatchException, TaskConsumeException {
        this.mLock.lock();
        try {
            if ( products == null || products.isEmpty() ) {
                return Collections.emptyList();
            }

            this.assertOfferCapacityLocked( products.size() );

            int nFreeCapacity = this.pendingCapacity();
            if ( nFreeCapacity <= 0 ) {
                for ( TaskLaunchContext context : products ) {
                    this.mWaitingQueue.addLast( context );
                }
                return Collections.emptyList();
            }

            int nAllowedByRuntime = this.mnRuntimeInstanceCapacity > 0
                    ? this.mnRuntimeInstanceCapacity : products.size();

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
                    try {
                        consumer.tryConsume( context );
                    }
                    catch ( TaskConsumeException e ) {
                        ConsumeCompromisedPolice police = consumer.compromisedPolice();
                        switch ( police ) {
                            case EvictionIgnore: {
                                --nConsume;
                                continue;
                            }
                            case EvictionException: {
                                this.addRemain( products, context );
                                e.setEvictionTask( context );
                                throw e;
                            }
                            case BreakException:
                            default: {
                                // 当前任务未消费，重新入 waiting 队尾
                                this.mWaitingQueue.addLast( context );

                                // 剩余未遍历的 products 全部入队
                                this.addRemain( products, context );
                                throw e;
                            }
                        }
                    }

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

    @Override
    public Collection<TaskLaunchContext> runningInstances() {
        this.mLock.lock();
        try {
            return Collections.unmodifiableCollection( this.mRunningInstances.values() );
        }
        finally {
            this.mLock.unlock();
        }
    }

    @Override
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

    @Override
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

    @Override
    public Collection<TaskLaunchContext> consumePending( TaskInstanceConsumer consumer ) throws TaskConsumeException {
        return this.consume( this.mnRuntimeInstanceCapacity, false, consumer );
    }

    @Override
    public Collection<TaskLaunchContext> shiftPipeline( Collection<Identification> terminatedIds, TaskInstanceConsumer consumer ) throws TaskConsumeException {
        // Recycle terminated instances first to release capacity.
        // The returned collection only represents newly consumed contexts.
        // Recycled instances are intentionally not part of the return value,
        // since this method models a "release-then-refill" pipeline step.
        // Callers must not rely on the return value to infer recycle results.
        this.recycleTerminated( terminatedIds );

        return this.consumePending( consumer );
    }

    @Override
    public int pendingCapacity() {
        return this.mnMaxCapacity - this.mnUsedCapacity;
    }

    @Override
    public TaskLaunchContext getRunningContextById( Identification id ) {
        this.mLock.lock();
        try {
            return this.mRunningInstances.get( id );
        }
        finally {
            this.mLock.unlock();
        }
    }

    @Override
    public int waitingSize() {
        this.mLock.lock();
        try {
            return this.mWaitingQueue.size();
        }
        finally {
            this.mLock.unlock();
        }
    }

    @Override
    public int runningSize() {
        this.mLock.lock();
        try {
            return this.mRunningInstances.size();
        }
        finally {
            this.mLock.unlock();
        }
    }

    @Override
    public boolean isFull() {
        this.mLock.lock();
        try {
            return this.mnUsedCapacity >= this.mnMaxCapacity;
        }
        finally {
            this.mLock.unlock();
        }
    }

    @Override
    public boolean isIdle() {
        this.mLock.lock();
        try {
            return this.mnUsedCapacity == 0 && this.mWaitingQueue.isEmpty();
        }
        finally {
            this.mLock.unlock();
        }
    }


    @Override
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
    public boolean isUsageCriticalCapacity() {
        this.mLock.lock();
        try {
            return this.mnUsedCapacity >= this.mnMinCapacity;
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