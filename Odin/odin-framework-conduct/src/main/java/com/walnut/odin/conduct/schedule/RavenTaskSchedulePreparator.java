package com.walnut.odin.conduct.schedule;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.task.InstanceEventType;
import com.pinecone.hydra.task.TaskInstanceExecState;
import com.pinecone.hydra.task.kom.UniformTaskInstrument;
import com.pinecone.hydra.task.kom.entity.TaskElement;
import com.pinecone.hydra.task.kom.instance.InstanceEntry;
import com.pinecone.hydra.task.kom.source.TaskNodeManipulator;
import com.pinecone.hydra.task.marshal.TaskScheduleCycle;
import com.pinecone.slime.meta.TableIndex64Meta;

import com.walnut.odin.atlas.graph.RuntimeAtlasInstrument;
import com.walnut.odin.conduct.entity.GenericInstanceEvent;
import com.walnut.odin.conduct.entity.GenericInstanceExec;
import com.walnut.odin.conduct.entity.InstanceEvent;
import com.walnut.odin.conduct.entity.InstanceExec;
import com.walnut.odin.conduct.schedule.entity.ScheduledTaskInstanceFrame;
import com.walnut.odin.conduct.schedule.entity.ScheduledTaskInstanceLineage;
import com.walnut.odin.conduct.schedule.entity.TaskScheduleContext;
import com.walnut.odin.conduct.schedule.lineage.RavenTaskInstanceLineageFreezer;
import com.walnut.odin.conduct.schedule.lineage.TaskInstanceLineageFreezer;
import com.walnut.odin.task.CentralizedTaskInstrument;
import com.walnut.odin.task.RavenTask;
import com.walnut.odin.task.RavenTaskConfig;
import com.walnut.odin.task.RavenTaskInstance;
import com.walnut.odin.task.TaskDeploymentMethod;
import com.walnut.odin.task.mapper.InstanceLineageAdjacentMapper;
import com.walnut.odin.task.mapper.InstanceLineageNodeMapper;
import com.walnut.odin.task.mapper.InstanceEventMapper;
import com.walnut.odin.task.mapper.InstanceExecMapper;
import com.walnut.odin.task.source.RavenTaskMasterManipulator;
import com.walnut.odin.task.source.ScheduleManipulator;
import com.walnut.odin.task.troll.GenericRavenTaskInstance;
import com.walnut.odin.task.troll.LaunchFeature;
import com.walnut.odin.task.troll.TaskExecutionLauncher;

public class RavenTaskSchedulePreparator implements TaskSchedulePreparator {

    public static final Collection<TaskScheduleCycle> DailyTaskScheduleCycles = List.of(
            TaskScheduleCycle.Month, TaskScheduleCycle.Week, TaskScheduleCycle.Day
    );

    public static final Collection<TaskScheduleCycle> HourlyTaskScheduleCycles = List.of(
            TaskScheduleCycle.Hour
    );

    public static final Collection<TaskScheduleCycle> FastTaskScheduleCycles = List.of(
            TaskScheduleCycle.Minute
    );

    private Logger log = LoggerFactory.getLogger( this.getClass() );

    private GuidAllocator                 mGuidAllocator;
    private RavenTaskConfig               mRavenTaskConfig;
    private int                           mnScanThreadCount;
    private long                          mnScanIdWindow;
    private long                          mnFastLookAheadSeconds;
    private long                          mnFastCatchUpLimitMinutes;
    private int                           mnFastMaxInstancesPerTask;

    private UniformTaskScheduler          mTaskScheduler;
    private TaskExecutionLauncher         mTaskExecutionLauncher;
    private UniformTaskInstrument         mUniformTaskInstrument;
    private RuntimeAtlasInstrument        mRuntimeAtlasInstrument;
    private CentralizedTaskInstrument     mCentralizedTaskInstrument;

    private RavenTaskMasterManipulator    mRavenTaskMasterManipulator;
    private TaskNodeManipulator           mTaskNodeManipulator;
    private ScheduleManipulator           mScheduleManipulator;
    private InstanceLineageNodeMapper       mInstanceLineageNodeMapper;
    private InstanceLineageAdjacentMapper   mInstanceLineageAdjacentMapper;
    private InstanceExecMapper            mInstanceExecMapper;
    private InstanceEventMapper           mInstanceEventMapper;

    private TaskInstanceLineageFreezer    mTaskInstanceLineageFreezer;
    private TaskScheduleTimeResolver      mTaskScheduleTimeResolver;

    private ExecutorService               mExecutorService;

    protected interface ScheduleTaskBatchHandler {

        Collection<TaskElement> handle( Collection<TaskElement> elements, LocalDateTime scanTargetTime );

    }

    public RavenTaskSchedulePreparator( UniformTaskScheduler taskScheduler ) {
        this.mTaskScheduler                = taskScheduler;
        this.mRavenTaskConfig              = taskScheduler.ravenTaskConfig();
        this.mnScanThreadCount             = this.mRavenTaskConfig.getScheduleScanThreadCount();
        this.mnScanIdWindow                = this.mRavenTaskConfig.getScheduleScanIdWindow();
        this.mnFastLookAheadSeconds        = 60;
        this.mnFastCatchUpLimitMinutes     = 10;
        this.mnFastMaxInstancesPerTask     = 1;

        this.mRuntimeAtlasInstrument       = taskScheduler.atlasInstrument();
        this.mTaskExecutionLauncher        = taskScheduler.taskExecutionLauncher();
        this.mCentralizedTaskInstrument    = taskScheduler.taskInstrument();
        this.mUniformTaskInstrument        = this.mCentralizedTaskInstrument.getUniformTaskInstrument();

        this.mGuidAllocator                = this.mCentralizedTaskInstrument.getGuidAllocator();

        this.mRavenTaskMasterManipulator   = this.mCentralizedTaskInstrument.getRavenTaskMasterManipulator();
        this.mTaskNodeManipulator          = this.mRavenTaskMasterManipulator.getTaskMasterManipulator().getTaskNodeManipulator();
        this.mScheduleManipulator          = this.mRavenTaskMasterManipulator.getScheduleManipulator();
        this.mInstanceLineageNodeMapper      = this.mScheduleManipulator.getInstanceLineageNodeMapper();
        this.mInstanceLineageAdjacentMapper  = this.mScheduleManipulator.getInstanceLineageAdjacentMapper();
        this.mInstanceExecMapper           = this.mScheduleManipulator.getInstanceExecMapper();
        this.mInstanceEventMapper          = this.mScheduleManipulator.getInstanceEventMapper();

        this.mTaskScheduleTimeResolver     = new TaskScheduleTimeResolver();
        this.mTaskInstanceLineageFreezer   = new RavenTaskInstanceLineageFreezer(
                this.mGuidAllocator,
                this.mRuntimeAtlasInstrument,
                this.mInstanceLineageNodeMapper,
                this.mInstanceLineageAdjacentMapper
        );
        this.mExecutorService              = Executors.newFixedThreadPool( this.mnScanThreadCount * 2 );

        log.info( "[Odin] [CrucialSchedulerComponentLifecycle] (RavenTaskSchedulePreparator Construction) <Done>" );
    }


    protected TaskScheduleContext prepareTaskScheduleTimeOffset( TaskElement element, LocalDateTime targetTime ) {
        TaskScheduleContext context = new TaskScheduleContext( element, targetTime );

        TaskScheduleCycle cycle = element.getScheduleCycle();
        String            cron  = element.getScheduleCron();
        if ( cycle == null ) {
            return context;
        }

        if ( cron == null || cron.isBlank() ) {
            String defaultCron = ScheduleCronHelper.generateDefaultCron( cycle );
            element.setScheduleCron( defaultCron );
            cron = defaultCron;
        }

        LocalDateTime next = element.getNextScheduleTime();
        context.setThisScheduleTime( next );
        if ( next == null ) {
            LocalDateTime firstFireTime = ScheduleCronHelper.computeNextByCron( cron, targetTime.minusSeconds( 1 ) );

            if ( firstFireTime == null ) {
                return context;
            }

            LocalDateTime advanced = ScheduleCronHelper.computeNextByCron( cron, firstFireTime );
            context.setThisScheduleTime( firstFireTime );
            context.setNextScheduleTime( advanced );
            element.setNextScheduleTime( advanced );
            return context;
        }


        LocalDateTime advanced = ScheduleCronHelper.computeNextByCron( cron, next );
        if ( advanced == null ) {
            return context;
        }

        if ( !advanced.equals( next ) ) {
            element.setNextScheduleTime( advanced );
            context.setNextScheduleTime( advanced );
        }

        return context;
    }

    protected String resolveScheduleCron( TaskElement element ) {
        TaskScheduleCycle cycle = element.getScheduleCycle();
        String cron = element.getScheduleCron();
        if ( cycle == null ) {
            return cron;
        }

        if ( cron == null || cron.isBlank() ) {
            cron = ScheduleCronHelper.generateDefaultCron( cycle );
            element.setScheduleCron( cron );
        }
        return cron;
    }

    protected Collection<TaskScheduleContext> prepareFastTaskScheduleTimeOffsets(
            TaskElement element, LocalDateTime targetTime, LocalDateTime lookAheadTarget
    ) {
        Collection<TaskScheduleContext> contexts = new ArrayList<>();
        String cron = this.resolveScheduleCron( element );
        if ( cron == null || cron.isBlank() ) {
            return contexts;
        }

        LocalDateTime fireTime = element.getNextScheduleTime();
        if ( fireTime == null ) {
            fireTime = ScheduleCronHelper.computeNextByCron( cron, targetTime.minusSeconds( 1 ) );
        }

        if ( fireTime == null ) {
            return contexts;
        }

        LocalDateTime catchUpFloor = targetTime.minusMinutes( this.mnFastCatchUpLimitMinutes );
        boolean skippedStaleFireTimes = false;
        if ( fireTime.isBefore( catchUpFloor ) ) {
            fireTime = ScheduleCronHelper.computeNextByCron( cron, catchUpFloor.minusSeconds( 1 ) );
            skippedStaleFireTimes = true;
        }

        if ( fireTime == null ) {
            return contexts;
        }

        if ( fireTime.isAfter( lookAheadTarget ) ) {
            if ( skippedStaleFireTimes ) {
                element.setNextScheduleTime( fireTime );
                this.mTaskNodeManipulator.update( element );
            }
            return contexts;
        }

        int nPrepared = 0;
        while ( !fireTime.isAfter( lookAheadTarget ) && nPrepared < this.mnFastMaxInstancesPerTask ) {
            LocalDateTime next = ScheduleCronHelper.computeNextByCron( cron, fireTime );
            TaskScheduleContext context = new TaskScheduleContext( element, targetTime );
            context.setThisScheduleTime( fireTime );
            context.setNextScheduleTime( next );
            contexts.add( context );

            if ( next == null ) {
                break;
            }

            fireTime = next;
            nPrepared++;
        }

        return contexts;
    }

    protected boolean isParentInstanceLineageResolvable( TaskScheduleContext context, Set<GUID> batchTaskGuids ) {
        TaskElement element = context.getElement();
        List<GUID> parentTaskGuids = this.mRuntimeAtlasInstrument.fetchParentTaskGuids( element.getGuid() );
        if ( parentTaskGuids == null || parentTaskGuids.isEmpty() ) {
            return true;
        }

        LocalDateTime businessTime = this.mTaskScheduleTimeResolver.resolveBusinessTime( element, context.getThisScheduleTime() );
        for ( GUID parentTaskGuid : parentTaskGuids ) {
            TaskElement parentElement = this.mRuntimeAtlasInstrument.queryTaskElementByGuid( parentTaskGuid );
            if ( parentElement == null ) {
                return false;
            }
            if ( batchTaskGuids.contains( parentElement.getGuid() ) ) {
                continue;
            }
            if ( businessTime == null ) {
                if ( this.mInstanceLineageNodeMapper.queryByTaskGuidAndExpectTime( parentElement.getGuid(), context.getThisScheduleTime() ) != null ) {
                    continue;
                }
                return false;
            }
            if ( this.mInstanceLineageNodeMapper.queryByTaskGuidAndBusinessTime( parentElement.getGuid(), businessTime ) == null ) {
                return false;
            }
        }

        return true;
    }

    protected Collection<TaskScheduleContext> filterLineageResolvableContexts( Collection<TaskScheduleContext> contexts ) {
        Collection<TaskScheduleContext> result = new ArrayList<>();
        Set<GUID> batchTaskGuids = new HashSet<>();
        for ( TaskScheduleContext context : contexts ) {
            batchTaskGuids.add( context.getElement().getGuid() );
        }

        for ( TaskScheduleContext context : contexts ) {
            if ( this.isParentInstanceLineageResolvable( context, batchTaskGuids ) ) {
                result.add( context );
            }
            else {
                log.info(
                        "[TaskSchedulerLifecycle] Skip preparing task `{}` because parent instance lineage is not ready.",
                        context.getElement().getName()
                );
            }
        }
        return result;
    }

    protected void persistPreparedTaskScheduleOffsets( Collection<ScheduledTaskInstanceLineage> lineages ) {
        for ( ScheduledTaskInstanceLineage lineage : lineages ) {
            TaskScheduleContext context = lineage.getContext();
            if ( context.getNextScheduleTime() == null ) {
                continue;
            }
            TaskElement element = context.getElement();
            element.setNextScheduleTime( context.getNextScheduleTime() );
            this.mTaskNodeManipulator.update( element );
        }
    }

    protected ScheduledTaskInstanceFrame prepareInstance( TaskScheduleContext context, RavenTask task, LocalDateTime targetTime ) {
        TaskElement element = context.getElement();
        LocalDateTime expectTime = context.getThisScheduleTime();
        LocalDateTime businessTime = this.mTaskScheduleTimeResolver.resolveBusinessTime( element, expectTime );
        if ( businessTime != null ) {
            InstanceEntry existing = this.mUniformTaskInstrument.getInstanceInstrument().queryInstanceByTaskGuidAndBusinessTime(
                    element.getGuid(), businessTime
            );
            if ( existing != null ) {
                return new ScheduledTaskInstanceFrame( context, new GenericRavenTaskInstance( existing, task ), false );
            }
        }

        RavenTaskInstance that = task.createInstance();
        LaunchFeature feature = this.prepareLaunchFeature( element, expectTime );
        InstanceEntry it = that.getInstanceEntry();
        it.setExpectTime( expectTime );
        it.setBusinessTime( businessTime );
        it.setFireTime( targetTime );

        this.mTaskExecutionLauncher.initializeInstance( that, feature );  // 这里会完成实例插入
        return new ScheduledTaskInstanceFrame( context, that, true );
    }

    protected LaunchFeature prepareLaunchFeature( TaskElement element, LocalDateTime expectTime ) {
        LaunchFeature feature = new LaunchFeature();
        LocalDateTime bizTimeEpoch = expectTime;
        if ( bizTimeEpoch == null ) {
            bizTimeEpoch = LocalDateTime.now();
        }
        feature.setBizTimeEpoch( bizTimeEpoch );

        feature.setAllowAsymmetricImage( true );
        if ( TaskDeploymentMethod.isAuthoritative( element.getDeploymentMethod() ) ) {
            feature.setAllowAsymmetricImage( false );
        }

        return feature;
    }

    protected void ensureTaskExec( TaskScheduleContext context, RavenTaskInstance instance ) {
        TaskElement element = context.getElement();
        GUID instanceGuid = instance.getInstanceEntry().getGuid();
        int nRetryCnt = instance.getInstanceEntry().getRetryCnt();
        if ( this.mInstanceExecMapper.queryByInstanceGuidAndRetry( instanceGuid, nRetryCnt ) != null ) {
            return;
        }

        InstanceExec exec = new GenericInstanceExec();
        exec.setTaskGuid( element.getGuid() );
        exec.setInstanceGuid( instanceGuid );
        exec.setTaskName( instance.getOwnedTask().getName() );
        exec.setInstanceName( instance.getInstanceEntry().getInstanceName() );
        exec.setProcessorQueue( "default" );
        exec.setImagePath( instance.getInstanceEntry().getImagePath() );
        exec.setClusterName( "local_cluster" );
        exec.setExecState( TaskInstanceExecState.Submitted.getName() );
        exec.setCurrentRetryNumber( nRetryCnt );
        exec.setRetryTimes( nRetryCnt );
        this.mInstanceExecMapper.insert( exec );
    }

    protected void ensureTaskEventTimeReady( TaskScheduleContext context, RavenTaskInstance instance ) {
        TaskElement element = context.getElement();
        GUID instanceGuid = instance.getInstanceEntry().getGuid();
        String eventState = InstanceEventType.TaskTimeReady.getName();
        if ( this.mInstanceEventMapper.queryByInstanceGuidAndState( instanceGuid, eventState ) != null ) {
            return;
        }

        InstanceEvent event = new GenericInstanceEvent();
        event.setGuid( this.mGuidAllocator.nextGUID() );
        event.setTaskGuid( element.getGuid() );
        event.setInstanceGuid( instanceGuid );
        event.setInstanceName( instance.getInstanceEntry().getInstanceName() );
        event.setRetryTimes( instance.getInstanceEntry().getRetryCnt() );
        event.setCurrentRetryNumber( instance.getInstanceEntry().getRetryCnt() );
        event.setEventType( instance.getTaskType() );
        event.setState( eventState );
        event.setExecTime( LocalDateTime.now() );
        event.setEventContext( "{}" );
        this.mScheduleManipulator.getInstanceEventMapper().insert( event );
    }

    protected void prepareTaskInstances( Collection<TaskScheduleContext> contexts, LocalDateTime targetTime ) {
        Collection<ScheduledTaskInstanceFrame> frames = new ArrayList<>();
        contexts = this.filterLineageResolvableContexts( contexts );
        if ( contexts.isEmpty() ) {
            return;
        }

        for ( TaskScheduleContext context : contexts ) {
            TaskElement element = context.getElement();

            RavenTask task = this.mCentralizedTaskInstrument.constructTask( element );
            frames.add( this.prepareInstance( context, task, targetTime ) );
        }

        Collection<ScheduledTaskInstanceLineage> lineages = this.mTaskInstanceLineageFreezer.freeze( frames );

        Collection<ScheduledTaskInstanceLineage> preparedLineages = new ArrayList<>();
        for ( ScheduledTaskInstanceLineage lineage : lineages ) {
            try {
                this.ensureTaskExec( lineage.getContext(), lineage.getInstance() );
                this.ensureTaskEventTimeReady( lineage.getContext(), lineage.getInstance() );
                preparedLineages.add( lineage );
            }
            catch ( RuntimeException e ) {
                log.error(
                        "[TaskSchedulerLifecycle] Preparing task instance failed. (Task: `{}`, Instance: `{}`) <Skipped>",
                        lineage.getContext().getElement().getName(),
                        lineage.getInstance().getInstanceEntry().getInstanceName(),
                        e
                );
            }
        }

        this.persistPreparedTaskScheduleOffsets( preparedLineages );
    }

    protected Collection<TaskElement> prepareScheduleTasks( Collection<TaskElement> elements, LocalDateTime targetTime ) {
        if ( elements == null || elements.isEmpty() ) {
            return elements;
        }

        Collection<TaskScheduleContext> contexts = new ArrayList<>();
        for ( TaskElement element : elements ) {
            TaskScheduleContext context = this.prepareTaskScheduleTimeOffset( element, targetTime );
            contexts.add( context );
        }

        this.prepareTaskInstances( contexts, targetTime );
        Debug.traceSyn( elements );
        return elements;
    }

    protected Collection<TaskElement> prepareFastScheduleTasks(
            Collection<TaskElement> elements, LocalDateTime targetTime, LocalDateTime lookAheadTarget
    ) {
        if ( elements == null || elements.isEmpty() ) {
            return elements;
        }

        Collection<TaskScheduleContext> contexts = new ArrayList<>();
        for ( TaskElement element : elements ) {
            contexts.addAll( this.prepareFastTaskScheduleTimeOffsets( element, targetTime, lookAheadTarget ) );
        }

        this.prepareTaskInstances( contexts, targetTime );
        Debug.traceSyn( elements );
        return elements;
    }

    @Override
    public UniformTaskScheduler taskScheduler() {
        return this.mTaskScheduler;
    }


    protected void prepareSchedulableTasksAndWait(
            Collection<TaskScheduleCycle> cycles, LocalDateTime targetTime, ScheduleTaskBatchHandler handler
    ) {
        if ( targetTime == null ) {
            targetTime = LocalDateTime.now();
        }

        TableIndex64Meta range = this.mTaskNodeManipulator.selectSchedulableIdRange( cycles, targetTime );
        if ( range == null ) {
            return;
        }

        long idMin = range.getMinId();
        long idMax = range.getMaxId();
        if ( idMin <= 0 || idMax <= 0 || idMax < idMin ) {
            return;
        }

        long cursor = idMin;
        Collection<Future<?>> futures = new ArrayList<>();
        while ( cursor <= idMax ) {
            long windowStart = cursor;
            long windowEnd   = cursor + this.mnScanIdWindow - 1;

            if ( windowEnd > idMax ) {
                windowEnd = idMax;
            }

            final long finalStart = windowStart;
            final long finalEnd   = windowEnd;

            LocalDateTime finalTargetTime = targetTime;
            Future<?> future = this.mExecutorService.submit( () -> {
                try {
                    log.info( "[TaskSchedulerLifecycle] Preparing schedulable tasks (Start: {}, End: {}) <Start>", finalStart, finalEnd );

                    Collection<TaskElement> elements = this.mTaskNodeManipulator.fetchSchedulableTasksInRange(
                            finalStart, finalEnd, cycles, finalTargetTime
                    );
                    if ( elements == null ) {
                        elements = List.of();
                    }

                    elements = handler.handle( elements, finalTargetTime );

                    log.info( "[TaskSchedulerLifecycle] Preparing schedulable tasks (Start: {}, End: {}, Size: {}) <Done>", finalStart, finalEnd, elements.size() );
                }
                catch ( Exception e ) {
                    log.error( "[TaskSchedulerLifecycle] Preparing schedulable tasks (Start: {}, End: {}) <Error>", finalStart, finalEnd, e );
                }
            } );
            futures.add( future );

            cursor = windowEnd + 1;
        }

        for ( Future<?> future : futures ) {
            try {
                future.get();
            }
            catch ( Exception e ) {
                log.error( "[TaskSchedulerLifecycle] Waiting schedulable task preparation <Error>", e );
            }
        }
    }

    @Override
    public void prepareSchedulableTasksAndWait( Collection<TaskScheduleCycle> cycles, LocalDateTime targetTime ) {
        this.prepareSchedulableTasksAndWait( cycles, targetTime, this::prepareScheduleTasks );
    }

    @Override
    public void prepareDailySchedulableTasksAndWait( LocalDateTime targetTime ) {
        this.prepareSchedulableTasksAndWait( DailyTaskScheduleCycles, targetTime );
    }

    @Override
    public void prepareHourlySchedulableTasksAndWait( LocalDateTime targetTime ) {
        this.prepareSchedulableTasksAndWait( HourlyTaskScheduleCycles, targetTime );
    }

    @Override
    public void prepareFastSchedulableTasksAndWait( LocalDateTime targetTime ) {
        if ( targetTime == null ) {
            targetTime = LocalDateTime.now();
        }

        LocalDateTime scanTargetTime = targetTime.plusSeconds( this.mnFastLookAheadSeconds );
        LocalDateTime finalTargetTime = targetTime;
        this.prepareSchedulableTasksAndWait(
                FastTaskScheduleCycles,
                scanTargetTime,
                ( elements, lookAheadTarget ) -> this.prepareFastScheduleTasks( elements, finalTargetTime, lookAheadTarget )
        );
    }

    @Override
    public List<TaskElement> fetchSchedulableTasksInRange( long idMin, long idMax, Collection<TaskScheduleCycle> cycles, LocalDateTime targetTime ) {
        return this.mTaskNodeManipulator.fetchSchedulableTasksInRange( idMin, idMax, cycles, targetTime );
    }

}
