package com.walnut.odin.conduct.schedule;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pinecone.framework.util.StringUtils;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.system.ko.MetaPersistenceException;
import com.pinecone.hydra.task.TaskInstanceExecState;
import com.pinecone.hydra.task.TaskInstanceStatus;
import com.pinecone.hydra.task.kom.instance.InstanceEntry;
import com.pinecone.hydra.task.kom.instance.InstanceInstrument;
import com.walnut.odin.conduct.lifecycle.KernelTaskInstanceLifecycleInstrument;
import com.walnut.odin.conduct.lifecycle.TaskInstanceLifecycleInstrument;
import com.walnut.odin.conduct.lifecycle.TaskInstanceTransitionReason;
import com.walnut.odin.conduct.lifecycle.TaskInstanceTransitionResult;
import com.walnut.odin.conduct.schedule.entity.DependencyBlockage;
import com.walnut.odin.conduct.schedule.entity.DepartureChecklist;
import com.walnut.odin.conduct.schedule.entity.InstanceDepartureResult;
import com.walnut.odin.conduct.schedule.entity.ScheduleFittingContext;
import com.walnut.odin.dispatch.TaskLaunchContext;
import com.walnut.odin.task.CentralizedTaskInstrument;
import com.walnut.odin.task.RavenTaskInstance;
import com.walnut.odin.task.mapper.InstanceLineageNodeMapper;
import com.walnut.odin.task.mapper.InstanceExecMapper;
import com.walnut.odin.task.source.RavenTaskMasterManipulator;
import com.walnut.odin.task.source.ScheduleManipulator;
import com.walnut.odin.task.troll.GenericRavenTaskInstance;
import com.walnut.odin.task.troll.LaunchFeature;

public class RavenInstanceDepartureGate implements InstanceDepartureGate {

    protected Logger                          log = LoggerFactory.getLogger( this.getClass() );

    protected UniformTaskScheduler            mTaskScheduler;
    protected InstanceInstrument              mInstanceInstrument;
    protected CentralizedTaskInstrument       mCentralizedTaskInstrument;
    protected RavenTaskMasterManipulator      mRavenTaskMasterManipulator;
    protected ScheduleManipulator             mScheduleManipulator;
    protected InstanceLineageNodeMapper         mInstanceLineageNodeMapper;
    protected InstanceExecMapper              mInstanceExecMapper;
    protected InstanceScheduleAllocator       mInstanceScheduleAllocator;
    protected TaskInstanceLifecycleInstrument mTaskInstanceLifecycleInstrument;

    public RavenInstanceDepartureGate( UniformTaskScheduler taskScheduler ) {
        this.mTaskScheduler              = taskScheduler;
        this.mCentralizedTaskInstrument  = taskScheduler.taskInstrument();
        this.mInstanceInstrument         = taskScheduler.instanceInstrument();

        this.mRavenTaskMasterManipulator = this.mCentralizedTaskInstrument.getRavenTaskMasterManipulator();
        this.mScheduleManipulator        = this.mRavenTaskMasterManipulator.getScheduleManipulator();
        this.mInstanceLineageNodeMapper    = this.mScheduleManipulator.getInstanceLineageNodeMapper();
        this.mInstanceExecMapper         = this.mScheduleManipulator.getInstanceExecMapper();

        this.mInstanceScheduleAllocator  = taskScheduler.instanceScheduleAllocator();
        this.mTaskInstanceLifecycleInstrument = new KernelTaskInstanceLifecycleInstrument(
                this.mInstanceInstrument,
                this.mScheduleManipulator.getInstanceEventMapper()
        );
    }

    @Override
    public UniformTaskScheduler taskScheduler() {
        return this.mTaskScheduler;
    }

    protected boolean shouldCheckDependency( InstanceEntry instance ) {
        if ( instance == null ) {
            return false;
        }

        TaskInstanceStatus status = instance.getInstanceStatus();
        return status == TaskInstanceStatus.New || status == TaskInstanceStatus.DependencyWait;
    }

    protected TaskInstanceTransitionReason resolveTransitionReason( TaskInstanceStatus status ) {
        if ( status == TaskInstanceStatus.DependencyWait ) {
            return TaskInstanceTransitionReason.DependencyReady;
        }
        if ( status == TaskInstanceStatus.ResourceWait ) {
            return TaskInstanceTransitionReason.DependencyReady;
        }
        if ( status == TaskInstanceStatus.DepartureStandby ) {
            return TaskInstanceTransitionReason.DepartureReady;
        }
        if ( status == TaskInstanceStatus.ProcessCreating ) {
            return TaskInstanceTransitionReason.ProcessCreationClaim;
        }
        return TaskInstanceTransitionReason.Manual;
    }

    protected void updateInstanceStatus( InstanceEntry instance, TaskInstanceStatus status ) throws MetaPersistenceException {
        if ( instance == null || status == null ) {
            return;
        }

        TaskInstanceStatus fromStatus = instance.getInstanceStatus();
        if ( fromStatus == status ) {
            return;
        }

        TaskInstanceTransitionResult result = this.mTaskInstanceLifecycleInstrument.transit(
                instance.getGuid(), fromStatus, status, this.resolveTransitionReason( status )
        );
        if ( result.isSucceeded() ) {
            instance.setInstanceStatus( status );
        }
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

        Collection<DependencyBlockage> blockages = this.mInstanceLineageNodeMapper.fetchDependencyBlockages(
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

        if ( status == TaskInstanceStatus.ProcessCreating ) {
            checklist.setPreDepartureLastStatus( TaskInstanceStatus.ProcessCreating );
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
            this.log.info(
                    "[DiscardInstance] ( Task `{}`, Instance `{}` ) has been discarded.",
                    discardedInstance.getTaskName(), discardedInstance.getInstanceName()
            );
        }
    }

    protected Collection<TaskLaunchContext> initializePrelaunchSequence( Collection<InstanceEntry> fittedInstances ) {
        Collection<TaskLaunchContext> li = new ArrayList<>();
        for ( InstanceEntry fittedInstance : fittedInstances ) {
            RavenTaskInstance instance  = new GenericRavenTaskInstance( fittedInstance, this.mCentralizedTaskInstrument );
            String szInvalidCause = this.validateLaunchContextTask( instance );
            if ( szInvalidCause != null ) {
                this.markInvalidLaunchContext( fittedInstance, szInvalidCause );
                continue;
            }
            LaunchFeature launchFeature = new LaunchFeature();
            String szDesignatedProcessor = fittedInstance.getDesignatedProcessor();

            if ( StringUtils.isNoneEmpty( szDesignatedProcessor ) ) {
                launchFeature.withProcessorDesignated( szDesignatedProcessor );
            }
            TaskLaunchContext launchContext = TaskLaunchContext.of( instance, launchFeature );
            if ( StringUtils.isNoneEmpty( fittedInstance.getAffinityProcessor() ) ) {
                launchContext.setAffinityProcessorName( fittedInstance.getAffinityProcessor() );
            }

            li.add( launchContext );
        }
        return li;
    }

    protected String validateLaunchContextTask( RavenTaskInstance instance ) {
        if ( instance == null ) {
            return "No task instance found for instance departure.";
        }
        try {
            if ( instance.getOwnedTask() == null ) {
                return "No owned task found for instance departure.";
            }
            if ( instance.getOwnedTask().getId() == null ) {
                return "Owned task has no id for instance departure.";
            }
        }
        catch ( RuntimeException e ) {
            String szMessage = e.getMessage();
            if ( szMessage == null ) {
                szMessage = e.getClass().getName();
            }
            return "Owned task is not resolvable for instance departure: " + szMessage;
        }
        return null;
    }

    protected void markInvalidLaunchContext( InstanceEntry instance, String szCause ) {
        if ( instance == null || instance.getGuid() == null ) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        TaskInstanceTransitionResult result = this.mTaskInstanceLifecycleInstrument.transitAnyWithRuntimeFields(
                instance.getGuid(),
                List.of(
                        TaskInstanceStatus.New,
                        TaskInstanceStatus.DependencyWait,
                        TaskInstanceStatus.ResourceWait,
                        TaskInstanceStatus.DepartureStandby,
                        TaskInstanceStatus.ProcessCreating,
                        TaskInstanceStatus.ProcessStandby
                ),
                TaskInstanceStatus.Error,
                TaskInstanceTransitionReason.ProcessCreationFailed,
                null,
                now,
                now,
                szCause
        );
        if ( result.isSucceeded() ) {
            instance.setInstanceStatus( TaskInstanceStatus.Error );
            instance.setErrorCause( szCause );
            instance.setLastEndTime( now );
            instance.setFinishTime( now );
            this.mInstanceExecMapper.updateStateRetryMonotonic(
                    instance.getGuid(),
                    instance.getRetryCnt(),
                    TaskInstanceExecState.Fail.getName(),
                    null,
                    null,
                    now
            );
        }

        this.log.warn(
                "[TaskSchedulerLifecycle] Invalid launch context marked as error "
                        + "(TaskGuid: `{}`, InstanceGuid: `{}`, Cause: `{}`) <Rejected>",
                instance.getTaskGuid(),
                instance.getGuid(),
                szCause
        );
    }

    protected void fitResourceWaitInstances(
            Collection<InstanceEntry> resourceWaitInstances,
            Collection<InstanceEntry> departureStandbyInstances,
            InstanceDepartureResult result
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

        result.getDiscardedInstances().addAll( context.getDiscardedInstances() );
        this.traceDiscardedInstances( context.getDiscardedInstances() );
    }

    protected Collection<TaskLaunchContext> prepareDepartureLaunchContexts(
            Collection<InstanceEntry> departureStandbyInstances, LocalDateTime scheduleTime, InstanceDepartureResult result
    ) throws MetaPersistenceException {
        if ( departureStandbyInstances == null || departureStandbyInstances.isEmpty() ) {
            return List.of();
        }

        Collection<InstanceEntry> claimedInstances = new ArrayList<>();
        for ( InstanceEntry instance : departureStandbyInstances ) {
            TaskInstanceTransitionResult transition = this.mTaskInstanceLifecycleInstrument.transitWithScheduleTime(
                    instance.getGuid(),
                    TaskInstanceStatus.DepartureStandby,
                    TaskInstanceStatus.ProcessCreating,
                    TaskInstanceTransitionReason.ProcessCreationClaim,
                    scheduleTime
            );
            if ( !transition.isSucceeded() ) {
                continue;
            }

            instance.setScheduleTime( scheduleTime );
            instance.setInstanceStatus( TaskInstanceStatus.ProcessCreating );
            claimedInstances.add( instance );
        }

        result.getClaimedInstances().addAll( claimedInstances );
        return this.initializePrelaunchSequence( claimedInstances );
    }

    @Override
    public InstanceDepartureResult prepareDeparture(
            Collection<InstanceEntry> instances, LocalDateTime scheduleTime
    ) throws MetaPersistenceException {
        if ( scheduleTime == null ) {
            scheduleTime = LocalDateTime.now();
        }

        InstanceDepartureResult result = new InstanceDepartureResult();
        if ( instances == null || instances.isEmpty() ) {
            return result;
        }

        DependencyBlockageIndex dependencyBlockageIndex = this.fetchDependencyBlockageIndex( instances );
        Collection<InstanceEntry> resourceWaitInstances = result.getResourceWaitInstances();
        Collection<InstanceEntry> departureStandbyInstances = result.getDepartureStandbyInstances();
        Collection<InstanceEntry> processCreatingInstances = new ArrayList<>();

        for ( InstanceEntry entry : instances ) {
            DepartureChecklist checklist = this.prelaunch_check_instance( entry, dependencyBlockageIndex );
            result.getChecklists().add( checklist );
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
            else if ( lastStatus == TaskInstanceStatus.ProcessCreating ) {
                processCreatingInstances.add( entry );
            }
        }

        this.fitResourceWaitInstances( resourceWaitInstances, departureStandbyInstances, result );
        result.getLaunchContexts().addAll(
                this.prepareDepartureLaunchContexts( departureStandbyInstances, scheduleTime, result )
        );
        result.getLaunchContexts().addAll( this.initializePrelaunchSequence( processCreatingInstances ) );
        return result;
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

}
