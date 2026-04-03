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

import com.pinecone.framework.util.id.Identification;
import com.pinecone.hydra.proc.UProcess;
import com.walnut.odin.conduct.CollectiveTaskRegiment;
import com.walnut.odin.dispatch.entity.TaskProcessorEntity;
import com.walnut.odin.task.RavenTaskInstance;
import com.walnut.odin.task.source.TaskProcessorManipulator;
import com.walnut.odin.task.troll.InstanceLaunchException;
import com.walnut.odin.task.troll.LaunchFeature;
import com.walnut.odin.task.troll.TaskExecutionLauncher;


public class RavenTaskDispatcher implements TaskDispatcher {

    protected final Logger log = LoggerFactory.getLogger( this.getClass() );

    protected final ReentrantLock mLock;

    protected final Map<String, TaskExecutionProcessor>  mProcessors;

    protected final Map<Long, TaskExecutionProcessor>    mClientProcessorsIndex;

    protected final Map<Identification, TaskProcPair>    mAffinityTable;



    protected TaskProcessorManipulator  mTaskProcessorManipulator;

    protected DispatchStrategy          mDispatchStrategy;

    protected TaskExecutionLauncher     mTaskExecutionLauncher;

    protected CollectiveTaskRegiment    mCollectiveTaskRegiment;

    public RavenTaskDispatcher( CollectiveTaskRegiment regiment, DispatchStrategy strategy ) {
        this.mLock                       = new ReentrantLock();
        this.mProcessors                 = new LinkedHashMap<>();
        this.mAffinityTable              = new HashMap<>();
        this.mDispatchStrategy           = strategy;
        this.mClientProcessorsIndex      = new HashMap<>();
        this.mCollectiveTaskRegiment     = regiment;
        this.mTaskExecutionLauncher      = regiment.taskExecutionLauncher();
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
        TaskExecutionProcessor processor = new RavenTaskExecutionProcessor( entity, this.mTaskExecutionLauncher );
        this.registerProcessor( processor );
        return entity;
    }

    @Override
    public void unregisterProcessor( String szProcessorName ) {
        this.mLock.lock();
        try {
            this.mProcessors.remove( szProcessorName );
            this.mAffinityTable.entrySet().removeIf( entry -> {
                if ( entry.getValue().processor.getName().equals( szProcessorName ) ) {
                    return true;
                }
                return false;
            } );
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
            TaskExecutionProcessor processor = this.mProcessors.get( szProcessorName );
            if ( processor == null ) {
                throw new IllegalArgumentException( "Processor not found: " + szProcessorName );
            }
            this.mAffinityTable.put( launchContext.getTaskId(), new TaskProcPair( processor, launchContext ) );
        }
        finally {
            this.mLock.unlock();
        }
    }

    @Override
    public TaskExecutionProcessor getAffinityTasks( Identification taskId ) {
        TaskProcPair pair = this.mAffinityTable.get( taskId );
        if ( pair != null ) {
            return pair.processor;
        }
        return null;
    }

    @Override
    public Collection<TaskLaunchContext> queryAffinityTasks( String szProcessorName ) {
        this.mLock.lock();
        try {
            Collection<TaskLaunchContext> result = new ArrayList<>();
            for ( TaskProcPair pair : this.mAffinityTable.values() ) {
                if ( pair.processor.getName().equals( szProcessorName ) ) {
                    result.add( pair.launchContext );
                }
            }
            return result;
        }
        finally {
            this.mLock.unlock();
        }
    }

    @Override
    public PipelineLaunchReport pipeCreate(Collection<TaskLaunchContext> contexts ) throws InstanceLaunchException, TaskDispatchException {
        Map<TaskExecutionProcessor, Collection<TaskLaunchContext>> plan;

        this.mLock.lock();
        try {
            plan = this.mDispatchStrategy.dispatch(
                    new ArrayList<>( this.mProcessors.values() ), contexts, this
            );
        }
        finally {
            this.mLock.unlock();
        }

        return this.executeScheme( plan, true );
    }

    @Override
    public PipelineLaunchReport pipeLaunch(Collection<TaskLaunchContext> contexts ) throws InstanceLaunchException, TaskDispatchException {
        Map<TaskExecutionProcessor, Collection<TaskLaunchContext>> plan;

        this.mLock.lock();
        try {
            plan = this.mDispatchStrategy.dispatch(
                    new ArrayList<>( this.mProcessors.values() ), contexts, this
            );
        }
        finally {
            this.mLock.unlock();
        }

        return this.executeScheme( plan, false );
    }

    protected PipelineLaunchReport executeScheme(
            Map<TaskExecutionProcessor, Collection<TaskLaunchContext>> scheme, boolean bCreation
    ) throws InstanceLaunchException, TaskDispatchException {
        List<UProcess> launched = new ArrayList<>();
        List<TaskLaunchContext> consumed = new ArrayList<>();
        List<TaskLaunchContext> waiting  = new ArrayList<>();

        for ( Map.Entry<TaskExecutionProcessor, Collection<TaskLaunchContext>> entry : scheme.entrySet() ) {
            TaskExecutionProcessor processor = entry.getKey();
            Collection<TaskLaunchContext> assigned = entry.getValue();

            PipelineLaunchReport report;

            if ( bCreation ) {
                report = processor.pipeCreate( assigned );
            }
            else {
                report = processor.pipeLaunch( assigned );
            }

            launched.addAll( report.launchedProcesses() );
            consumed.addAll( report.launchedContext() );
            waiting.addAll( report.waitingContext() );
        }

        return DefaultPipelineLaunchReport.executed(
                null,
                launched,
                consumed,
                waiting
        );
    }

    @Override
    public UProcess create( RavenTaskInstance instance, LaunchFeature feature ) throws InstanceLaunchException, TaskDispatchException {
        TaskLaunchContext context = TaskLaunchContext.of( instance, feature );
        PipelineLaunchReport _r = this.pipeCreate( List.of( context ) );
        return context.getLaunchedProcess();
    }

    @Override
    public UProcess launch( RavenTaskInstance instance, LaunchFeature feature ) throws InstanceLaunchException, TaskDispatchException {
        TaskLaunchContext context = TaskLaunchContext.of( instance, feature );
        PipelineLaunchReport _r = this.pipeLaunch( List.of( context ) );
        return context.getLaunchedProcess();
    }


    protected static class TaskProcPair {
        public TaskExecutionProcessor processor;
        public TaskLaunchContext launchContext;

        public TaskProcPair( TaskExecutionProcessor processor, TaskLaunchContext launchContext ) {
            this.processor = processor;
            this.launchContext = launchContext;
        }
    }
}