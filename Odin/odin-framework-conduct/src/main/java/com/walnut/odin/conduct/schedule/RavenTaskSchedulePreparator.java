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
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.task.InstanceEventType;
import com.pinecone.hydra.task.TaskInstanceExecState;
import com.pinecone.hydra.task.kom.UniformTaskInstrument;
import com.pinecone.hydra.task.kom.entity.TaskElement;
import com.pinecone.hydra.task.kom.instance.InstanceEntry;
import com.pinecone.hydra.task.kom.source.TaskNodeManipulator;
import com.pinecone.hydra.task.marshal.TaskScheduleCycle;
import com.pinecone.slime.meta.TableIndex64Meta;

import com.walnut.odin.atlas.graph.RuntimeAtlasInstrument;
import com.walnut.odin.conduct.entity.GenericInstanceExec;
import com.walnut.odin.conduct.entity.InstanceExec;
import com.walnut.odin.conduct.lifecycle.TaskInstanceTransitionReason;
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

    private RavenTaskConfig               mRavenTaskConfig;
    private int                           mnScanThreadCount;
    private long                          mnScanIdWindow;
    private long                          mnMinutePrepareCatchUpWindowMinutes;
    private long                          mnHourPrepareCatchUpWindowMinutes;
    private long                          mnDailyPrepareCatchUpWindowMinutes;
    private long                          mnMinutePrepareLeadSeconds;
    private long                          mnHourPrepareLeadSeconds;
    private long                          mnDailyPrepareLeadSeconds;
    private int                           mnMinutePrepareMaxInstancesPerPulse;
    private int                           mnHourPrepareMaxInstancesPerPulse;
    private int                           mnDailyPrepareMaxInstancesPerPulse;

    private UniformTaskScheduler          mTaskScheduler;
    private TaskExecutionLauncher         mTaskExecutionLauncher;
    private UniformTaskInstrument         mUniformTaskInstrument;
    private RuntimeAtlasInstrument        mRuntimeAtlasInstrument;
    private CentralizedTaskInstrument     mCentralizedTaskInstrument;

    private RavenTaskMasterManipulator    mRavenTaskMasterManipulator;
    private TaskNodeManipulator           mTaskNodeManipulator;
    private ScheduleManipulator           mScheduleManipulator;
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
        this.mnMinutePrepareLeadSeconds    = this.mRavenTaskConfig.getSchedulePrepareLeadSecondsMinute();
        this.mnHourPrepareLeadSeconds      = this.mRavenTaskConfig.getSchedulePrepareLeadSecondsHour();
        this.mnDailyPrepareLeadSeconds     = this.mRavenTaskConfig.getSchedulePrepareLeadSecondsDaily();
        this.mnMinutePrepareCatchUpWindowMinutes = this.mRavenTaskConfig.getSchedulePrepareCatchUpWindowMinutesMinute();
        this.mnHourPrepareCatchUpWindowMinutes   = this.mRavenTaskConfig.getSchedulePrepareCatchUpWindowMinutesHour();
        this.mnDailyPrepareCatchUpWindowMinutes  = this.mRavenTaskConfig.getSchedulePrepareCatchUpWindowMinutesDaily();
        this.mnMinutePrepareMaxInstancesPerPulse = this.mRavenTaskConfig.getSchedulePrepareMaxInstancesPerPulseMinute();
        this.mnHourPrepareMaxInstancesPerPulse   = this.mRavenTaskConfig.getSchedulePrepareMaxInstancesPerPulseHour();
        this.mnDailyPrepareMaxInstancesPerPulse  = this.mRavenTaskConfig.getSchedulePrepareMaxInstancesPerPulseDaily();

        this.mRuntimeAtlasInstrument       = taskScheduler.atlasInstrument();
        this.mTaskExecutionLauncher        = taskScheduler.taskExecutionLauncher();
        this.mCentralizedTaskInstrument    = taskScheduler.taskInstrument();
        this.mUniformTaskInstrument        = this.mCentralizedTaskInstrument.getUniformTaskInstrument();

        this.mRavenTaskMasterManipulator   = this.mCentralizedTaskInstrument.getRavenTaskMasterManipulator();
        this.mTaskNodeManipulator          = this.mRavenTaskMasterManipulator.getTaskMasterManipulator().getTaskNodeManipulator();
        this.mScheduleManipulator          = this.mRavenTaskMasterManipulator.getScheduleManipulator();
        this.mInstanceExecMapper           = this.mScheduleManipulator.getInstanceExecMapper();
        this.mInstanceEventMapper          = this.mScheduleManipulator.getInstanceEventMapper();

        this.mTaskScheduleTimeResolver     = new TaskScheduleTimeResolver();
        this.mTaskInstanceLineageFreezer   = new RavenTaskInstanceLineageFreezer( this.mRuntimeAtlasInstrument );
        this.startService();

        log.info( "[Odin] [CrucialSchedulerComponentLifecycle] (RavenTaskSchedulePreparator Construction) <Done>" );
    }

    @Override
    public synchronized void startService() {
        if ( this.mExecutorService != null && !this.mExecutorService.isShutdown() ) {
            return;
        }
        this.mExecutorService = Executors.newFixedThreadPool( this.mnScanThreadCount * 2 );
    }

    @Override
    public synchronized void terminateService( long nGracefulShutdownMillis ) {
        ExecutorService executor = this.mExecutorService;
        this.mExecutorService = null;
        if ( executor == null ) {
            return;
        }

        executor.shutdown();
        try {
            if ( !executor.awaitTermination( Math.max( 0L, nGracefulShutdownMillis ), TimeUnit.MILLISECONDS ) ) {
                executor.shutdownNow();
            }
        }
        catch ( InterruptedException e ) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
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

    protected Collection<TaskScheduleContext> prepareTaskScheduleTimeOffsets(
            TaskElement element,
            LocalDateTime pulseTime,
            LocalDateTime prepareUntil,
            int nMaxInstancesPerPulse,
            long nCatchUpWindowMinutes
    ) {
        Collection<TaskScheduleContext> contexts = new ArrayList<>();
        String cron = this.resolveScheduleCron( element );
        if ( cron == null || cron.isBlank() ) {
            return contexts;
        }

        long nCatchUpMinutes = Math.max( 0L, nCatchUpWindowMinutes );
        LocalDateTime fireTime = element.getNextScheduleTime();
        if ( fireTime == null ) {
            LocalDateTime searchStartTime = pulseTime.minusMinutes( nCatchUpMinutes );
            fireTime = ScheduleCronHelper.computeLatestByCronBeforeOrAt( cron, searchStartTime, pulseTime );
            if ( fireTime == null ) {
                fireTime = ScheduleCronHelper.computeNextByCron( cron, pulseTime.minusSeconds( 1 ) );
                if ( fireTime == null || fireTime.isAfter( prepareUntil ) ) {
                    if ( fireTime != null ) {
                        element.setNextScheduleTime( fireTime );
                        this.persistTaskScheduleOffset( element );
                    }
                    return contexts;
                }
            }
        }

        LocalDateTime catchUpFloor = pulseTime.minusMinutes( nCatchUpMinutes );
        boolean skippedStaleFireTimes = false;
        if ( fireTime.isBefore( catchUpFloor ) ) {
            fireTime = ScheduleCronHelper.computeNextByCron( cron, catchUpFloor.minusSeconds( 1 ) );
            skippedStaleFireTimes = true;
        }

        if ( fireTime == null ) {
            return contexts;
        }

        if ( fireTime.isAfter( prepareUntil ) ) {
            if ( skippedStaleFireTimes ) {
                element.setNextScheduleTime( fireTime );
                this.persistTaskScheduleOffset( element );
            }
            return contexts;
        }

        int nPrepared = 0;
        int nMax = Math.max( 1, nMaxInstancesPerPulse );
        while ( !fireTime.isAfter( prepareUntil ) && nPrepared < nMax ) {
            LocalDateTime next = ScheduleCronHelper.computeNextByCron( cron, fireTime );
            TaskScheduleContext context = new TaskScheduleContext( element, pulseTime );
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

    protected LocalDateTime lineageTime( TaskScheduleContext context ) {
        if ( context == null || context.getElement() == null ) {
            return null;
        }
        LocalDateTime businessTime = this.mTaskScheduleTimeResolver.resolveBusinessTime(
                context.getElement(), context.getThisScheduleTime()
        );
        if ( businessTime != null ) {
            return businessTime;
        }
        return context.getThisScheduleTime();
    }

    protected boolean isParentInstanceLineageResolvable( TaskScheduleContext context, Set<LineageReadyKey> batchLineageKeys ) {
        TaskElement element = context.getElement();
        List<GUID> parentTaskGuids = this.mRuntimeAtlasInstrument.fetchParentTaskGuids( element.getGuid() );
        if ( parentTaskGuids == null || parentTaskGuids.isEmpty() ) {
            return true;
        }

        LocalDateTime businessTime = this.mTaskScheduleTimeResolver.resolveBusinessTime( element, context.getThisScheduleTime() );
        LocalDateTime lineageTime = this.lineageTime( context );
        for ( GUID parentTaskGuid : parentTaskGuids ) {
            TaskElement parentElement = this.mRuntimeAtlasInstrument.queryTaskElementByGuid( parentTaskGuid );
            if ( parentElement == null ) {
                return false;
            }
            if ( batchLineageKeys.contains( new LineageReadyKey( parentElement.getGuid(), lineageTime ) ) ) {
                continue;
            }
            if ( !this.mRuntimeAtlasInstrument.isParentInstanceLineageResolvable(
                    element.getGuid(), parentElement.getGuid(), context.getThisScheduleTime(), businessTime
            ) ) {
                return false;
            }
        }

        return true;
    }

    protected Collection<TaskScheduleContext> filterLineageResolvableContexts( Collection<TaskScheduleContext> contexts ) {
        Collection<TaskScheduleContext> result = new ArrayList<>();
        Set<LineageReadyKey> batchLineageKeys = new HashSet<>();
        for ( TaskScheduleContext context : contexts ) {
            batchLineageKeys.add( new LineageReadyKey( context.getElement().getGuid(), this.lineageTime( context ) ) );
        }

        for ( TaskScheduleContext context : contexts ) {
            if ( this.isParentInstanceLineageResolvable( context, batchLineageKeys ) ) {
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
            this.persistTaskScheduleOffset( element );
        }
    }

    protected void persistTaskScheduleOffset( TaskElement element ) {
        int nUpdated = this.mTaskNodeManipulator.updateScheduleOffsetIfEnabled(
                element.getGuid(),
                element.getScheduleCron(),
                element.getNextScheduleTime()
        );
        if ( nUpdated <= 0 ) {
            log.info(
                    "[TaskSchedulerLifecycle] Skip schedule offset persist because task is no longer enabled. "
                            + "(Task: `{}`, Guid: `{}`)",
                    element.getName(),
                    element.getGuid()
            );
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
        int nSequenceCnt = instance.getInstanceEntry().getSequenceCnt();
        int nRetryCnt = instance.getInstanceEntry().getRetryCnt();
        if ( this.mInstanceExecMapper.queryByInstanceGuidAndRetry( instanceGuid, nSequenceCnt, nRetryCnt ) != null ) {
            return;
        }

        InstanceExec exec = new GenericInstanceExec();
        exec.setTaskGuid( element.getGuid() );
        exec.setInstanceGuid( instanceGuid );
        exec.setTaskName( instance.getOwnedTask().getName() );
        exec.setInstanceName( instance.getInstanceEntry().getInstanceName() );
        exec.setProcessorQueue( "default" );
        exec.setAffinityProcessor( instance.getInstanceEntry().getAffinityProcessor() );
        exec.setDesignatedProcessor( instance.getInstanceEntry().getDesignatedProcessor() );
        exec.setImagePath( instance.getInstanceEntry().getImagePath() );
        exec.setClusterName( "local_cluster" );
        exec.setExecState( TaskInstanceExecState.Submitted.getName() );
        exec.setSequenceCnt( nSequenceCnt );
        exec.setCurrentRetryNumber( nRetryCnt );
        exec.setRetryTimes( instance.getInstanceEntry().getRetryTimes() );
        this.mInstanceExecMapper.insert( exec );
    }

    protected void ensureTaskEventTimeReady( TaskScheduleContext context, RavenTaskInstance instance ) {
        GUID instanceGuid = instance.getInstanceEntry().getGuid();
        int nSequenceCnt = instance.getInstanceEntry().getSequenceCnt();
        int nRetryCnt = instance.getInstanceEntry().getRetryCnt();
        String eventState = InstanceEventType.TaskTimeReady.getName();
        if ( this.mInstanceEventMapper.queryByInstanceGuidAndState( instanceGuid, nSequenceCnt, nRetryCnt, eventState ) != null ) {
            return;
        }

        this.mTaskScheduler.taskInstanceLifecycleExaminer().recordInstanceEvent(
                instance.getInstanceEntry(),
                TaskInstanceTransitionReason.TimeReady,
                eventState,
                "{}"
        );
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
        return this.prepareScheduleTasks(
                elements,
                targetTime,
                targetTime,
                1,
                this.mnMinutePrepareCatchUpWindowMinutes
        );
    }

    protected Collection<TaskElement> prepareScheduleTasks(
            Collection<TaskElement> elements,
            LocalDateTime pulseTime,
            LocalDateTime prepareUntil,
            int nMaxInstancesPerPulse,
            long nCatchUpWindowMinutes
    ) {
        if ( elements == null || elements.isEmpty() ) {
            return elements;
        }

        Collection<TaskScheduleContext> contexts = new ArrayList<>();
        for ( TaskElement element : elements ) {
            contexts.addAll( this.prepareTaskScheduleTimeOffsets(
                    element,
                    pulseTime,
                    prepareUntil,
                    nMaxInstancesPerPulse,
                    nCatchUpWindowMinutes
            ) );
        }

        this.prepareTaskInstances( contexts, pulseTime );
        Debug.traceSyn( elements );
        return elements;
    }

    @Override
    public UniformTaskScheduler taskScheduler() {
        return this.mTaskScheduler;
    }

    protected static class LineageReadyKey {
        private final GUID taskGuid;
        private final LocalDateTime lineageTime;

        public LineageReadyKey( GUID taskGuid, LocalDateTime lineageTime ) {
            this.taskGuid = taskGuid;
            this.lineageTime = lineageTime;
        }

        @Override
        public boolean equals( Object o ) {
            if ( this == o ) {
                return true;
            }
            if ( !( o instanceof LineageReadyKey ) ) {
                return false;
            }
            LineageReadyKey that = (LineageReadyKey) o;
            return java.util.Objects.equals( this.taskGuid, that.taskGuid )
                    && java.util.Objects.equals( this.lineageTime, that.lineageTime );
        }

        @Override
        public int hashCode() {
            return java.util.Objects.hash( this.taskGuid, this.lineageTime );
        }
    }


    protected void prepareSchedulableTasksAndWait(
            Collection<TaskScheduleCycle> cycles, LocalDateTime targetTime, ScheduleTaskBatchHandler handler
    ) {
        this.startService();

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
        if ( targetTime == null ) {
            targetTime = LocalDateTime.now();
        }

        LocalDateTime pulseTime = targetTime;
        LocalDateTime prepareUntil = pulseTime.plusSeconds( Math.max( 0L, this.mnDailyPrepareLeadSeconds ) );
        this.prepareSchedulableTasksAndWait(
                DailyTaskScheduleCycles,
                prepareUntil,
                ( elements, ignored ) -> this.prepareScheduleTasks(
                        elements,
                        pulseTime,
                        prepareUntil,
                        this.mnDailyPrepareMaxInstancesPerPulse,
                        this.mnDailyPrepareCatchUpWindowMinutes
                )
        );
    }

    @Override
    public void prepareHourlySchedulableTasksAndWait( LocalDateTime targetTime ) {
        if ( targetTime == null ) {
            targetTime = LocalDateTime.now();
        }

        LocalDateTime pulseTime = targetTime;
        LocalDateTime prepareUntil = pulseTime.plusSeconds( Math.max( 0L, this.mnHourPrepareLeadSeconds ) );
        this.prepareSchedulableTasksAndWait(
                HourlyTaskScheduleCycles,
                prepareUntil,
                ( elements, ignored ) -> this.prepareScheduleTasks(
                        elements,
                        pulseTime,
                        prepareUntil,
                        this.mnHourPrepareMaxInstancesPerPulse,
                        this.mnHourPrepareCatchUpWindowMinutes
                )
        );
    }

    @Override
    public void prepareFastSchedulableTasksAndWait( LocalDateTime targetTime ) {
        if ( targetTime == null ) {
            targetTime = LocalDateTime.now();
        }

        LocalDateTime pulseTime = targetTime;
        LocalDateTime prepareUntil = pulseTime.plusSeconds( Math.max( 0L, this.mnMinutePrepareLeadSeconds ) );
        this.prepareSchedulableTasksAndWait(
                FastTaskScheduleCycles,
                prepareUntil,
                ( elements, ignored ) -> this.prepareScheduleTasks(
                        elements,
                        pulseTime,
                        prepareUntil,
                        this.mnMinutePrepareMaxInstancesPerPulse,
                        this.mnMinutePrepareCatchUpWindowMinutes
                )
        );
    }

    @Override
    public List<TaskElement> fetchSchedulableTasksInRange( long idMin, long idMax, Collection<TaskScheduleCycle> cycles, LocalDateTime targetTime ) {
        return this.mTaskNodeManipulator.fetchSchedulableTasksInRange( idMin, idMax, cycles, targetTime );
    }

}
