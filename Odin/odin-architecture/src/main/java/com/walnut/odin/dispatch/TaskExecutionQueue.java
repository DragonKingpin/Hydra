package com.walnut.odin.dispatch;

import java.util.Collection;

import com.pinecone.framework.util.id.Identification;

public interface TaskExecutionQueue extends TaskQueueMeta {

    void applyMaxCapacity( int maxCapacity );

    void applyMinCapacity( int minCapacity );

    void applyRuntimeInstanceCapacity( int capacity );

    boolean isUsageCriticalCapacity();


    void offer( Collection<TaskLaunchContext> contexts ) throws TaskDispatchException;

    void offer( TaskLaunchContext context ) throws TaskDispatchException;

    Collection<TaskLaunchContext> consume( int n, boolean bForce, TaskInstanceConsumer consumer ) throws TaskConsumeException;

    Collection<TaskLaunchContext> consume( int n, TaskInstanceConsumer consumer ) throws TaskConsumeException;

    Collection<TaskLaunchContext> consume( TaskInstanceConsumer consumer ) throws TaskConsumeException;

    Collection<TaskLaunchContext> pipeConsume( Collection<TaskLaunchContext> products, TaskInstanceConsumer consumer ) throws TaskDispatchException, TaskConsumeException;

    Collection<TaskLaunchContext> runningInstances();

    void markTerminated( Identification id );

    Collection<TaskLaunchContext> recycleTerminated( Collection<Identification> terminatedIds );

    Collection<TaskLaunchContext> consumePending( TaskInstanceConsumer consumer ) throws TaskConsumeException;

    Collection<TaskLaunchContext> shiftPipeline( Collection<Identification> terminatedIds, TaskInstanceConsumer consumer ) throws TaskConsumeException;

    int pendingCapacity();


    int waitingSize();

    int runningSize();

    boolean isFull();

    boolean isIdle();


    Collection<TaskLaunchContext> drainAllWaiting();

    TaskLaunchContext getRunningContextById( Identification id );

}
