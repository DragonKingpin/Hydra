package com.walnut.odin.dispatch;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pinecone.hydra.proc.UProcess;
import com.walnut.odin.conduct.CollectiveTaskRegiment;
import com.walnut.odin.dispatch.entity.TaskProcessorEntity;
import com.walnut.odin.task.RavenTaskInstance;
import com.walnut.odin.task.source.TaskProcessorManipulator;
import com.walnut.odin.task.troll.InstanceLaunchException;
import com.walnut.odin.task.troll.LaunchFeature;
import com.walnut.odin.task.troll.TaskExecutionElevator;


public class RavenTaskDispatcher implements TaskDispatcher {

    protected final Logger log = LoggerFactory.getLogger( this.getClass() );

    protected final ReentrantLock mLock;

    protected final Map<String, TaskExecutionProcessor>  mProcessors;

    protected final Map<Long, TaskExecutionProcessor>    mClientProcessorsIndex;

    protected final Map<String, List<TaskLaunchContext>> mAffinityTable;

    protected TaskProcessorManipulator  mTaskProcessorManipulator;

    protected DispatchStrategy          mDispatchStrategy;

    protected TaskExecutionElevator     mTaskExecutionElevator;

    protected CollectiveTaskRegiment    mCollectiveTaskRegiment;

    public RavenTaskDispatcher( CollectiveTaskRegiment regiment, DispatchStrategy strategy ) {
        this.mLock                       = new ReentrantLock();
        this.mProcessors                 = new LinkedHashMap<>();
        this.mAffinityTable              = new HashMap<>();
        this.mDispatchStrategy           = strategy;
        this.mClientProcessorsIndex      = new HashMap<>();
        this.mCollectiveTaskRegiment     = regiment;
        this.mTaskExecutionElevator      = regiment.taskExecutionElevator();
        this.mTaskProcessorManipulator   = regiment.taskInstrument().getRavenTaskMasterManipulator().getTaskProcessorManipulator();
    }

    public RavenTaskDispatcher( CollectiveTaskRegiment regiment ) {
        this( regiment, new AdaptiveCapacityDispatchStrategy() );
    }

    @Override
    public void registerProcessor( TaskExecutionProcessor processor ) {
        this.mLock.lock();
        try {
            this.mProcessors.put( processor.getName(), processor );
            this.mClientProcessorsIndex.put( processor.getControlClientId(), processor );
            this.log.info( "Registered processor, name:`{}`, clientId:`{}` ", processor.getName(), processor.getControlClientId() );
        }
        finally {
            this.mLock.unlock();
        }
    }

    @Override
    public TaskProcessorEntity registerProcessor( String szProcessorName, long nClientId ) throws IllegalArgumentException {
        TaskProcessorEntity entity = this.mTaskProcessorManipulator.selectByProcessorName( szProcessorName );
        if ( entity == null ) {
            throw new IllegalArgumentException( szProcessorName + " not found" );
        }

        entity.setControlClientId( nClientId );
        TaskExecutionProcessor processor = new RavenTaskExecutionProcessor( entity, this.mTaskExecutionElevator );
        this.registerProcessor( processor );
        return entity;
    }

    @Override
    public void unregisterProcessor( String szProcessorName ) {
        this.mLock.lock();
        try {
            this.mProcessors.remove( szProcessorName );
            this.mAffinityTable.remove( szProcessorName );
            this.log.info( "Unregistered processor, name:`{}`", szProcessorName );
        }
        finally {
            this.mLock.unlock();
        }
    }

    @Override
    public void unregisterProcessor( long nClientId ) {
        TaskExecutionProcessor processor = null;
        this.mLock.lock();
        try {
            processor = this.mClientProcessorsIndex.remove( nClientId );
        }
        finally {
            this.mLock.unlock();

            if ( processor != null ) {
                this.unregisterProcessor( processor.getName() );
                this.log.info( "Unregistered processor, name:`{}`, clientId:`{}` ", processor.getName(), processor.getControlClientId() );
            }
        }
    }

    @Override
    public Collection<TaskExecutionProcessor> fetchProcessors() {
        this.mLock.lock();
        try {
            return Collections.unmodifiableCollection(
                    new ArrayList<>( this.mProcessors.values() )
            );
        }
        finally {
            this.mLock.unlock();
        }
    }

    @Override
    public void setProcessorAffinity( String szProcessorName, TaskLaunchContext launchContext ) {
        this.mLock.lock();
        try {
            List<TaskLaunchContext> list =
                    this.mAffinityTable.computeIfAbsent(
                            szProcessorName, k -> new ArrayList<>()
                    );
            list.add( launchContext );
        }
        finally {
            this.mLock.unlock();
        }
    }

    @Override
    public Collection<TaskLaunchContext> queryAffinityTasks( String szProcessorName ) {
        this.mLock.lock();
        try {
            List<TaskLaunchContext> list = this.mAffinityTable.get( szProcessorName );
            if ( list == null || list.isEmpty() ) {
                return Collections.emptyList();
            }
            return new ArrayList<>( list );
        }
        finally {
            this.mLock.unlock();
        }
    }

    @Override
    public PipelineElevationReport pipeLaunch( Collection<TaskLaunchContext> contexts ) throws InstanceLaunchException, TaskDispatchException {
        Map<TaskExecutionProcessor, Collection<TaskLaunchContext>> plan;

        this.mLock.lock();
        try {
            plan = this.mDispatchStrategy.dispatch(
                    new ArrayList<>( this.mProcessors.values() ), contexts
            );
        }
        finally {
            this.mLock.unlock();
        }

        return this.executeScheme( plan, true );
    }

    @Override
    public PipelineElevationReport pipeElevate( Collection<TaskLaunchContext> contexts ) throws InstanceLaunchException, TaskDispatchException {
        Map<TaskExecutionProcessor, Collection<TaskLaunchContext>> plan;

        this.mLock.lock();
        try {
            plan = this.mDispatchStrategy.dispatch(
                    new ArrayList<>( this.mProcessors.values() ), contexts
            );
        }
        finally {
            this.mLock.unlock();
        }

        return this.executeScheme( plan, false );
    }

    protected PipelineElevationReport executeScheme(
            Map<TaskExecutionProcessor, Collection<TaskLaunchContext>> scheme, boolean bLaunch
    ) throws InstanceLaunchException, TaskDispatchException {
        List<UProcess> launched = new ArrayList<>();
        List<TaskLaunchContext> consumed = new ArrayList<>();
        List<TaskLaunchContext> waiting  = new ArrayList<>();

        for ( Map.Entry<TaskExecutionProcessor, Collection<TaskLaunchContext>> entry : scheme.entrySet() ) {
            TaskExecutionProcessor processor = entry.getKey();
            Collection<TaskLaunchContext> assigned = entry.getValue();

            PipelineElevationReport report;

            if ( bLaunch ) {
                report = processor.pipeLaunch( assigned );
            }
            else {
                report = processor.pipeElevate( assigned );
            }

            launched.addAll( report.launchedProcesses() );
            consumed.addAll( report.launchedContext() );
            waiting.addAll( report.waitingContext() );
        }

        return DefaultPipelineElevationReport.executed(
                null,
                launched,
                consumed,
                waiting
        );
    }

    @Override
    public UProcess launch( RavenTaskInstance instance, LaunchFeature feature ) throws InstanceLaunchException, TaskDispatchException {
        TaskLaunchContext context = TaskLaunchContext.of( instance, feature );
        PipelineElevationReport _r = this.pipeLaunch( List.of( context ) );
        return context.getLaunchedProcess();
    }

    @Override
    public UProcess elevate( RavenTaskInstance instance, LaunchFeature feature ) throws InstanceLaunchException, TaskDispatchException {
        TaskLaunchContext context = TaskLaunchContext.of( instance, feature );
        PipelineElevationReport _r = this.pipeElevate( List.of( context ) );
        return context.getLaunchedProcess();
    }

}