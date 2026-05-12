package com.walnut.odin.conduct.schedule;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.StringUtils;
import com.pinecone.hydra.system.ko.MetaPersistenceException;
import com.pinecone.hydra.task.InstanceEventType;
import com.pinecone.hydra.task.TaskInstanceExecState;
import com.pinecone.hydra.task.TaskInstanceStatus;
import com.pinecone.hydra.task.kom.UniformTaskInstrument;
import com.pinecone.hydra.task.kom.instance.InstanceEntry;
import com.pinecone.hydra.task.kom.instance.InstanceInstrument;
import com.pinecone.hydra.task.kom.source.TaskNodeManipulator;
import com.pinecone.slime.meta.TableIndexMeta;
import com.walnut.odin.atlas.graph.RuntimeAtlasInstrument;
import com.walnut.odin.conduct.entity.GenericInstanceEvent;
import com.walnut.odin.conduct.entity.GenericInstanceExec;
import com.walnut.odin.conduct.entity.InstanceEvent;
import com.walnut.odin.conduct.entity.InstanceExec;
import com.walnut.odin.conduct.schedule.entity.DependencyBlockage;
import com.walnut.odin.conduct.schedule.entity.DepartureChecklist;
import com.walnut.odin.conduct.schedule.entity.ScheduleFittingContext;
import com.walnut.odin.dispatch.TaskLaunchContext;
import com.walnut.odin.task.CentralizedTaskInstrument;
import com.walnut.odin.task.RavenTaskConfig;
import com.walnut.odin.task.RavenTaskInstance;
import com.walnut.odin.task.mapper.InstanceAtlasNodeMapper;
import com.walnut.odin.task.source.RavenTaskMasterManipulator;
import com.walnut.odin.task.source.ScheduleManipulator;
import com.walnut.odin.task.troll.GenericRavenTaskInstance;
import com.walnut.odin.task.troll.LaunchFeature;
import com.walnut.odin.task.troll.TaskExecutionLauncher;

public class RavenInstanceScheduleImpetus implements InstanceScheduleImpetus {

    private Logger log = LoggerFactory.getLogger( this.getClass() );

    private RavenTaskConfig            mRavenTaskConfig;
    private int                        mnScanThreadCount;
    private long                       mnScanIdWindow;

    private UniformTaskScheduler       mTaskScheduler;
    private TaskExecutionLauncher      mTaskExecutionLauncher;
    private UniformTaskInstrument      mUniformTaskInstrument;
    private RuntimeAtlasInstrument     mRuntimeAtlasInstrument;
    private InstanceInstrument         mInstanceInstrument;
    private CentralizedTaskInstrument  mCentralizedTaskInstrument;

    private RavenTaskMasterManipulator mRavenTaskMasterManipulator;
    private TaskNodeManipulator        mTaskNodeManipulator;
    private ScheduleManipulator        mScheduleManipulator;
    private InstanceAtlasNodeMapper    mInstanceAtlasNodeMapper;

    private InstanceScheduleAllocator  mInstanceScheduleAllocator;
    private ExecutorService            mExecutorService;

    public RavenInstanceScheduleImpetus( UniformTaskScheduler taskScheduler ) {
        this.mTaskScheduler              = taskScheduler;
        this.mRavenTaskConfig            = taskScheduler.ravenTaskConfig();
        this.mnScanThreadCount           = this.mRavenTaskConfig.getScheduleScanThreadCount();
        this.mnScanIdWindow              = this.mRavenTaskConfig.getScheduleScanIdWindow();

        this.mRuntimeAtlasInstrument     = taskScheduler.atlasInstrument();
        this.mTaskExecutionLauncher      = taskScheduler.taskExecutionLauncher();
        this.mCentralizedTaskInstrument  = taskScheduler.taskInstrument();
        this.mUniformTaskInstrument      = this.mCentralizedTaskInstrument.getUniformTaskInstrument();
        this.mInstanceInstrument         = taskScheduler.instanceInstrument();

        this.mRavenTaskMasterManipulator = this.mCentralizedTaskInstrument.getRavenTaskMasterManipulator();
        this.mTaskNodeManipulator        = this.mRavenTaskMasterManipulator.getTaskMasterManipulator().getTaskNodeManipulator();
        this.mScheduleManipulator        = this.mRavenTaskMasterManipulator.getScheduleManipulator();
        this.mInstanceAtlasNodeMapper    = this.mScheduleManipulator.getInstanceAtlasNodeMapper();

        this.mInstanceScheduleAllocator  = taskScheduler.instanceScheduleAllocator();
        this.mExecutorService            = Executors.newFixedThreadPool( this.mnScanThreadCount * 2 );

        log.info( "[Odin] [CrucialSchedulerComponentLifecycle] (RavenInstanceScheduleImpetus Construction) <Done>" );
    }


    protected boolean shouldCheckDependency( InstanceEntry instance ) {
        if ( instance == null ) {
            return false;
        }

        TaskInstanceStatus status = instance.getInstanceStatus();
        return status == TaskInstanceStatus.New || status == TaskInstanceStatus.DependencyWait;
    }

    protected void updateInstanceStatus( InstanceEntry instance, TaskInstanceStatus status ) throws MetaPersistenceException {
        if ( instance == null || status == null ) {
            return;
        }

        if ( instance.getInstanceStatus() == status ) {
            return;
        }

        instance.setInstanceStatus( status );
        this.mInstanceInstrument.updateInstance( instance );
    }

    protected Collection<GUID> fetchDependencyCheckInstanceGuids( Collection<InstanceEntry> instances ) {
        Collection<GUID> ids = new ArrayList<>();
        if ( instances == null || instances.isEmpty() ) {
            return ids;
        }

        for ( InstanceEntry instance : instances ) {
            if ( !this.shouldCheckDependency( instance ) ) {
                continue;
            }

            GUID guid = instance.getGuid();
            if ( guid == null ) {
                continue;
            }

            ids.add( guid );
        }

        return ids;
    }

    protected DependencyBlockageIndex fetchDependencyBlockageIndex( Collection<InstanceEntry> instances ) {
        Collection<GUID> ids = this.fetchDependencyCheckInstanceGuids( instances );
        if ( ids.isEmpty() ) {
            return new DependencyBlockageIndex( List.of() );
        }

        Collection<DependencyBlockage> blockages = this.mInstanceAtlasNodeMapper.fetchDependencyBlockages(
                ids,
                TaskInstanceStatus.Finished.getName()
        );
        return new DependencyBlockageIndex( blockages );
    }

    protected DepartureChecklist prelaunch_check_instance(
            InstanceEntry that, DependencyBlockageIndex dependencyBlockageIndex
    ) throws MetaPersistenceException {
        DepartureChecklist checklist = new DepartureChecklist( that );
        if ( that == null || that.getGuid() == null ) {
            checklist.setInterceptedStatus( TaskInstanceStatus.Error );
            checklist.setPreDepartureLastStatus( TaskInstanceStatus.Error );
            return checklist;
        }

        TaskInstanceStatus status = that.getInstanceStatus();
        if ( status == null ) {
            checklist.setInterceptedStatus( TaskInstanceStatus.Error );
            checklist.setPreDepartureLastStatus( TaskInstanceStatus.Error );
            return checklist;
        }

        if ( status == TaskInstanceStatus.DepartureStandby ) {
            checklist.setPreDepartureLastStatus( TaskInstanceStatus.DepartureStandby );
            return checklist;
        }

        if ( status == TaskInstanceStatus.ResourceWait ) {
            checklist.setPreDepartureLastStatus( TaskInstanceStatus.ResourceWait );
            return checklist;
        }

        if ( !this.shouldCheckDependency( that ) ) {
            checklist.setInterceptedStatus( status );
            checklist.setPreDepartureLastStatus( status );
            return checklist;
        }

        Collection<DependencyBlockage> blockages = dependencyBlockageIndex.fetchBlockages( that.getGuid() );
        if ( blockages == null || blockages.isEmpty() ) {
            this.updateInstanceStatus( that, TaskInstanceStatus.ResourceWait );
            checklist.setPreDepartureLastStatus( TaskInstanceStatus.ResourceWait );
            return checklist;
        }

        for ( DependencyBlockage blockage : blockages ) {
            if ( blockage == null ) {
                continue;
            }
            checklist.addDependentInstanceId( blockage.getDependentInstanceGuid() );
        }

        this.updateInstanceStatus( that, TaskInstanceStatus.DependencyWait );
        checklist.setInterceptedStatus( TaskInstanceStatus.DependencyWait );
        checklist.setPreDepartureLastStatus( TaskInstanceStatus.DependencyWait );
        return checklist;
    }

    protected void traceDiscardedInstances( Collection<InstanceEntry> discardedInstances ) {
        if ( discardedInstances == null || discardedInstances.isEmpty() ) {
            return;
        }

        for ( InstanceEntry discardedInstance : discardedInstances ) {
            log.info(
                    "[DiscardInstance] ( Task `{}`, Instance `{}` ) has been discarded.",
                    discardedInstance.getTaskName(), discardedInstance.getInstanceName()
            );
            // TODO, Sophisticate upgradation.
        }
    }



    // [Prelaunch-Stage2] 已完成并行调度配额分配，启动准备程序
    protected Collection<TaskLaunchContext> initializePrelaunchSequence( Collection<InstanceEntry> fittedInstances ) {
        Collection<TaskLaunchContext> li = new ArrayList<>();
        for ( InstanceEntry fittedInstance : fittedInstances ) {
            RavenTaskInstance instance      = new GenericRavenTaskInstance( fittedInstance, this.mCentralizedTaskInstrument );
            LaunchFeature launchFeature     = new LaunchFeature();
            String szProcessor = fittedInstance.getProcessorName();

            if ( StringUtils.isNoneEmpty(szProcessor) ) {
                // Not affinity(best-effort), but designated(compulsory).
                // 这里不是建议分配，而是绑核
                launchFeature.withProcessorDesignated( szProcessor );
            }
            TaskLaunchContext launchContext = TaskLaunchContext.of( instance, launchFeature );

            li.add( launchContext );
        }
        return li;
    }


    protected Collection<TaskLaunchContext> prepareLaunchContexts( ScheduleFittingContext context ) {
        Collection<InstanceEntry> fittedInstances    = context.getFittedInstances();
        Collection<InstanceEntry> discardedInstances = context.getDiscardedInstances();

        Collection<TaskLaunchContext> li = this.initializePrelaunchSequence( fittedInstances );

        this.traceDiscardedInstances( discardedInstances );

        return li;
    }

    protected void fitResourceWaitInstances(
            Collection<InstanceEntry> resourceWaitInstances, Collection<InstanceEntry> departureStandbyInstances
    ) throws MetaPersistenceException {
        if ( resourceWaitInstances == null || resourceWaitInstances.isEmpty() ) {
            return;
        }

        ScheduleFittingContext context = this.mInstanceScheduleAllocator.pipeFitting( resourceWaitInstances );
        Collection<InstanceEntry> fittedInstances = context.getFittedInstances();
        for ( InstanceEntry fittedInstance : fittedInstances ) {
            this.updateInstanceStatus( fittedInstance, TaskInstanceStatus.DepartureStandby );
            departureStandbyInstances.add( fittedInstance );
        }

        this.traceDiscardedInstances( context.getDiscardedInstances() );
    }

    protected Collection<TaskLaunchContext> prepareDepartureLaunchContexts(
            Collection<InstanceEntry> departureStandbyInstances
    ) {
        if ( departureStandbyInstances == null || departureStandbyInstances.isEmpty() ) {
            return List.of();
        }

        return this.initializePrelaunchSequence( departureStandbyInstances );
    }

    protected static class DependencyBlockageIndex {
        private Map<GUID, Collection<DependencyBlockage>> mIndex;

        public DependencyBlockageIndex( Collection<DependencyBlockage> blockages ) {
            this.mIndex = new HashMap<>();
            if ( blockages == null || blockages.isEmpty() ) {
                return;
            }

            for ( DependencyBlockage blockage : blockages ) {
                if ( blockage == null || blockage.getInstanceGuid() == null ) {
                    continue;
                }

                Collection<DependencyBlockage> current = this.mIndex.computeIfAbsent(
                        blockage.getInstanceGuid(),
                        k -> new ArrayList<>()
                );
                current.add( blockage );
            }
        }

        public Collection<DependencyBlockage> fetchBlockages( GUID instanceGuid ) {
            Collection<DependencyBlockage> blockages = this.mIndex.get( instanceGuid );
            if ( blockages == null ) {
                return List.of();
            }
            return blockages;
        }
    }


    @Override
    public void impelSchedulableInstances( Collection<TaskInstanceStatus> statuses, LocalDateTime targetTime ) {
        if ( targetTime == null ) {
            targetTime = LocalDateTime.now();
        }

        TableIndexMeta range = this.mInstanceInstrument.querySchedulableIdRange( statuses, targetTime );
        if ( range == null ) {
            return;
        }

        long idMin = range.getMinId();
        long idMax = range.getMaxId();
        if ( idMin <= 0 || idMax <= 0 || idMax < idMin ) {
            return;
        }

        long cursor = idMin;
        while ( cursor <= idMax ) {
            long windowStart = cursor;
            long windowEnd   = cursor + this.mnScanIdWindow - 1;

            if ( windowEnd > idMax ) {
                windowEnd = idMax;
            }

            final long finalStart = windowStart;
            final long finalEnd   = windowEnd;

            LocalDateTime finalTargetTime = targetTime;
            this.mExecutorService.submit( () -> {
                try {
                    log.info( "[TaskSchedulerLifecycle] Impelling schedulable instances (Start: {}, End: {}) <Start>", finalStart, finalEnd );


                    Collection<InstanceEntry> entries = this.mInstanceInstrument.fetchSchedulableInstances(
                            finalStart, finalEnd, statuses, finalTargetTime
                    );
                    if ( entries == null || entries.isEmpty() ) {
                        log.info( "[TaskSchedulerLifecycle] Impelling schedulable instances (Start: {}, End: {}, Size: 0) <Done>", finalStart, finalEnd );
                        return;
                    }

                    DependencyBlockageIndex dependencyBlockageIndex = this.fetchDependencyBlockageIndex( entries );
                    Collection<InstanceEntry> resourceWaitInstances = new ArrayList<>();
                    Collection<InstanceEntry> departureStandbyInstances = new ArrayList<>();

                    for ( InstanceEntry entry : entries ) {
                        DepartureChecklist checklist = this.prelaunch_check_instance( entry, dependencyBlockageIndex );
                        if ( checklist.isIntercepted() ) {
                            continue;
                        }

                        TaskInstanceStatus lastStatus = checklist.getPreDepartureLastStatus();
                        if ( lastStatus == TaskInstanceStatus.ResourceWait ) {
                            resourceWaitInstances.add( entry );
                        }
                        else if ( lastStatus == TaskInstanceStatus.DepartureStandby ) {
                            departureStandbyInstances.add( entry );
                        }
                    }

                    this.fitResourceWaitInstances( resourceWaitInstances, departureStandbyInstances );
                    Collection<TaskLaunchContext> launchContexts = this.prepareDepartureLaunchContexts( departureStandbyInstances );
                    if ( !launchContexts.isEmpty() ) {
                        this.mTaskScheduler.taskDispatcher().pipeLaunch( launchContexts );
                    }
                    //elements = this.prepareScheduleTasks( elements, finalTargetTime );

                    log.info( "[TaskSchedulerLifecycle] Impelling schedulable instances (Start: {}, End: {}, Size: {}) <Done>", finalStart, finalEnd, entries.size() );
                }
                catch ( Exception e ) {
                    log.error( "[TaskSchedulerLifecycle] Impelling schedulable instances (Start: {}, End: {}) <Error>", finalStart, finalEnd, e );
                }
            } );

            cursor = windowEnd + 1;
        }
    }

    @Override
    public void impelPrelaunchInstances( LocalDateTime targetTime ) {
        this.impelSchedulableInstances(
                List.of(
                        TaskInstanceStatus.New,          TaskInstanceStatus.DependencyWait,
                        TaskInstanceStatus.ResourceWait, TaskInstanceStatus.DepartureStandby
                ),
                targetTime
        );
    }

    /*protected void processAndFireInstances( List<InstanceEntry> instances ) throws MetaPersistenceException {
        for ( InstanceEntry instance : instances ) {
            try {
                log.info( "GUID: {}, Name: {}", instance.getGuid(), instance.getInstanceName() );
                instance.setInstanceStatus( TaskInstanceStatus.ResourceWait );
                instance.setRunStatus(TaskInstanceStatus.ResourceWait.getName());
                instance.setStartTime( LocalDateTime.now() );
                this.mInstanceInstrument.updateInstance( instance );
                //log.info(this.mInstanceInstrument.getInstanceEntry(instance.getGuid()).getRunStatus());
                InstanceExec execUpdate = new GenericInstanceExec();
                execUpdate.setInstanceGuid( instance.getGuid() );
                execUpdate.setExecState( TaskInstanceExecState.Submitted.getName() );
                this.mScheduleManipulator.getInstanceExecMapper().updateStateByInstanceGuid( execUpdate );

                InstanceEvent event = new GenericInstanceEvent();
                event.setGuid( this.mCentralizedTaskInstrument.getGuidAllocator().nextGUID() );
                event.setTaskGuid( instance.getTaskGuid() );
                event.setInstanceGuid( instance.getGuid() );
                event.setInstanceName( instance.getInstanceName() );
                event.setEventType( instance.getTaskType() );
                event.setState( InstanceEventType.CheckDependencyReady.getName() );
                event.setExecTime( LocalDateTime.now() );
                event.setEventContext( "{}" );
                //    this.mScheduleManipulator.getInstanceEventMapper().insert( event );
                //LaunchFeature feature = new LaunchFeature();
                //  this.mTaskExecutionLauncher.launchLocally( instance, feature );

            }
            catch ( MetaPersistenceException e ) {
                instance.setInstanceStatus( TaskInstanceStatus.Error );
                this.mInstanceInstrument.updateInstance( instance );
            }
        }
    }*/


    @Override
    public UniformTaskScheduler taskScheduler() {
        return this.mTaskScheduler;
    }
}
