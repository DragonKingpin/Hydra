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
import com.walnut.odin.conduct.RegimentJoinRejectionException;
import com.walnut.odin.dispatch.entity.TaskProcessorEntity;
import com.walnut.odin.task.RavenTaskInstance;
import com.walnut.odin.task.source.TaskProcessorManipulator;
import com.walnut.odin.task.mapper.InstanceExecMapper;
import com.walnut.odin.task.troll.InstanceLaunchException;
import com.walnut.odin.task.troll.LaunchFeature;
import com.walnut.odin.task.troll.TaskExecutionLauncher;


public class RavenTaskDispatcher implements TaskDispatcher {

    protected final Logger log = LoggerFactory.getLogger( this.getClass() );

    protected final ReentrantLock                        mLock;

    protected final Map<String, TaskExecutionProcessor>  mProcessors;
    protected final Map<Long, TaskExecutionProcessor>    mClientProcessorsIndex;
    protected final Map<Identification, TaskProcPair>    mAffinityTable;


    protected TaskProcessorManipulator  mTaskProcessorManipulator;
    protected InstanceExecMapper        mInstanceExecMapper;
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
        this.mInstanceExecMapper         = regiment.taskInstrument().getRavenTaskMasterManipulator().getScheduleManipulator().getInstanceExecMapper();
    }

    public RavenTaskDispatcher( CollectiveTaskRegiment regiment ) {
        this( regiment, new AdaptiveCapacityDispatchStrategy() );
    }


    @Override
    public TaskExecutionLauncher taskExecutionLauncher() {
        return this.mTaskExecutionLauncher;
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

        if ( entity.isLocal() ) {
            throw new RegimentJoinRejectionException( "Local processor `" + szProcessorName + "` cannot bind RPC control client." );
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
            TaskExecutionProcessor processor = this.mProcessors.remove( szProcessorName );
            if ( processor != null ) {
                this.mClientProcessorsIndex.remove( processor.getControlClientId() );
            }
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
    public TaskExecutionProcessor getProcessorByName( String szProcessorName ) {
        this.mLock.lock();
        try {
            return this.mProcessors.get( szProcessorName );
        }
        finally {
            this.mLock.unlock();
        }
    }

    @Override
    public TaskExecutionProcessor getProcessorByClientId( long nClientId ) {
        this.mLock.lock();
        try {
            return this.mClientProcessorsIndex.get( nClientId );
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
    public PipelineLaunchReport pipeCreate( Collection<TaskLaunchContext> contexts ) throws InstanceLaunchException, TaskDispatchException {
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

        return this.executeScheme( plan, true, false );
    }

    @Override
    public PipelineLaunchReport pipeCreatePrepared( Collection<TaskLaunchContext> contexts ) throws InstanceLaunchException, TaskDispatchException {
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

        return this.executeScheme( plan, true, true );
    }

    @Override
    public PipelineLaunchReport pipeLaunch( Collection<TaskLaunchContext> contexts ) throws InstanceLaunchException, TaskDispatchException {
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

        return this.executeScheme( plan, false, false );
    }

    @Override
    public PipelineLaunchReport pipeLaunchPrepared( Collection<TaskLaunchContext> contexts ) throws InstanceLaunchException, TaskDispatchException {
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

        return this.executeScheme( plan, false, true );
    }

    @Override
    public PipelineLaunchReport pipeStartPrepared( Collection<TaskLaunchContext> contexts ) throws InstanceLaunchException, TaskDispatchException {
        Map<TaskExecutionProcessor, Collection<TaskLaunchContext>> plan = this.buildBoundScheme( contexts );
        return this.executeStartScheme( plan );
    }

    protected Map<TaskExecutionProcessor, Collection<TaskLaunchContext>> buildBoundScheme(
            Collection<TaskLaunchContext> contexts
    ) throws TaskDispatchException {
        Map<TaskExecutionProcessor, Collection<TaskLaunchContext>> plan = new LinkedHashMap<>();
        if ( contexts == null || contexts.isEmpty() ) {
            return plan;
        }

        this.mLock.lock();
        try {
            for ( TaskLaunchContext context : contexts ) {
                TaskExecutionProcessor processor = null;
                String szProcessorName = context.getAffinityProcessorName();
                if ( szProcessorName != null ) {
                    processor = this.mProcessors.get( szProcessorName );
                }
                if ( processor == null ) {
                    TaskProcPair pair = this.mAffinityTable.get( context.getTaskId() );
                    if ( pair != null ) {
                        processor = pair.processor;
                    }
                }
                if ( processor == null ) {
                    throw new TaskDispatchException(
                            "No bound processor found for prepared start, taskId=" + context.getTaskId()
                    );
                }
                plan.computeIfAbsent( processor, k -> new ArrayList<>() ).add( context );
            }
        }
        finally {
            this.mLock.unlock();
        }

        return plan;
    }

    protected PipelineLaunchReport executeScheme(
            Map<TaskExecutionProcessor, Collection<TaskLaunchContext>> scheme, boolean bCreation, boolean bPrepared
    ) throws InstanceLaunchException, TaskDispatchException {
        List<UProcess> launched = new ArrayList<>();
        List<TaskLaunchContext> consumed = new ArrayList<>();
        List<TaskLaunchContext> waiting  = new ArrayList<>();

        for ( Map.Entry<TaskExecutionProcessor, Collection<TaskLaunchContext>> entry : scheme.entrySet() ) {
            TaskExecutionProcessor processor = entry.getKey();
            Collection<TaskLaunchContext> assigned = entry.getValue();
            for ( TaskLaunchContext context : assigned ) {
                context.setAffinityProcessorName( processor.getName() );
                this.mAffinityTable.put( context.getTaskId(), new TaskProcPair( processor, context ) );
            }

            PipelineLaunchReport report;

            if ( bCreation && bPrepared ) {
                report = processor.pipeCreatePrepared( assigned );
            }
            else if ( bCreation ) {
                report = processor.pipeCreate( assigned );
            }
            else if ( bPrepared ) {
                report = processor.pipeLaunchPrepared( assigned );
            }
            else {
                report = processor.pipeLaunch( assigned );
            }

            launched.addAll( report.launchedProcesses() );
            consumed.addAll( report.launchedContext() );
            waiting.addAll( report.waitingContext() );
            this.recordExecutedProcessors( report.launchedContext(), processor );
        }

        return DefaultPipelineLaunchReport.executed(
                null,
                launched,
                consumed,
                waiting
        );
    }

    protected PipelineLaunchReport executeStartScheme(
            Map<TaskExecutionProcessor, Collection<TaskLaunchContext>> scheme
    ) throws TaskDispatchException {
        List<UProcess> launched = new ArrayList<>();
        List<TaskLaunchContext> consumed = new ArrayList<>();
        List<TaskLaunchContext> waiting  = new ArrayList<>();

        for ( Map.Entry<TaskExecutionProcessor, Collection<TaskLaunchContext>> entry : scheme.entrySet() ) {
            TaskExecutionProcessor processor = entry.getKey();
            Collection<TaskLaunchContext> assigned = entry.getValue();
            PipelineLaunchReport report = processor.pipeStartPrepared( assigned );

            launched.addAll( report.launchedProcesses() );
            consumed.addAll( report.launchedContext() );
            waiting.addAll( report.waitingContext() );
            this.recordExecutedProcessors( report.launchedContext(), processor );
        }

        return DefaultPipelineLaunchReport.executed(
                null,
                launched,
                consumed,
                waiting
        );
    }

    protected void recordExecutedProcessors(
            Collection<TaskLaunchContext> contexts, TaskExecutionProcessor processor
    ) {
        if ( contexts == null || contexts.isEmpty() ) {
            return;
        }
        for ( TaskLaunchContext context : contexts ) {
            this.recordExecutedProcessor( context, processor );
        }
    }

    protected void recordExecutedProcessor( TaskLaunchContext context, TaskExecutionProcessor processor ) {
        if ( context == null || processor == null || context.getTaskInstance() == null ) {
            return;
        }
        if ( context.getLaunchedProcess() == null ) {
            return;
        }
        if ( context.getTaskInstance().getInstanceEntry() == null ) {
            return;
        }
        if ( context.getTaskInstance().getInstanceEntry().getGuid() == null ) {
            return;
        }
        this.mInstanceExecMapper.updateExecutedProcessorByInstanceGuidAndRetry(
                context.getTaskInstance().getInstanceEntry().getGuid(),
                context.getTaskInstance().getInstanceEntry().getRetryCnt(),
                processor.getName()
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
