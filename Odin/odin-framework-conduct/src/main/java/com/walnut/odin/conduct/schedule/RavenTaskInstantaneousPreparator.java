package com.walnut.odin.conduct.schedule;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pinecone.framework.util.StringUtils;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.system.ko.MetaPersistenceException;
import com.pinecone.hydra.task.InstanceEventType;
import com.pinecone.hydra.task.TaskInstanceExecState;
import com.pinecone.hydra.task.kom.UniformTaskInstrument;
import com.pinecone.hydra.task.kom.entity.TaskElement;
import com.pinecone.hydra.task.kom.instance.InstanceEntry;
import com.pinecone.hydra.task.marshal.TaskScheduleType;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;
import com.pinecone.hydra.unit.vgraph.entity.GraphNode;
import com.walnut.odin.atlas.graph.RuntimeAtlasInstrument;
import com.walnut.odin.conduct.entity.GenericInstanceEvent;
import com.walnut.odin.conduct.entity.GenericInstanceExec;
import com.walnut.odin.conduct.entity.InstanceEvent;
import com.walnut.odin.conduct.entity.InstanceExec;
import com.walnut.odin.conduct.schedule.entity.ScheduledTaskInstanceFrame;
import com.walnut.odin.conduct.schedule.entity.ScheduledTaskInstanceLineage;
import com.walnut.odin.conduct.schedule.entity.TaskInstantaneousContext;
import com.walnut.odin.conduct.schedule.entity.TaskInstantaneousMode;
import com.walnut.odin.conduct.schedule.entity.TaskInstantaneousPrepareResult;
import com.walnut.odin.conduct.schedule.entity.TaskScheduleContext;
import com.walnut.odin.conduct.schedule.lineage.RavenTaskInstanceLineageFreezer;
import com.walnut.odin.conduct.schedule.lineage.TaskInstanceLineageFreezer;
import com.walnut.odin.task.CentralizedTaskInstrument;
import com.walnut.odin.task.RavenTask;
import com.walnut.odin.task.RavenTaskInstance;
import com.walnut.odin.task.TaskDeploymentMethod;
import com.walnut.odin.task.mapper.InstanceAtlasAdjacentMapper;
import com.walnut.odin.task.mapper.InstanceAtlasNodeMapper;
import com.walnut.odin.task.mapper.InstanceEventMapper;
import com.walnut.odin.task.mapper.InstanceExecMapper;
import com.walnut.odin.task.source.RavenTaskMasterManipulator;
import com.walnut.odin.task.source.ScheduleManipulator;
import com.walnut.odin.task.troll.LaunchFeature;
import com.walnut.odin.task.troll.TaskExecutionLauncher;

public class RavenTaskInstantaneousPreparator implements TaskInstantaneousPreparator {

    protected Logger                       log = LoggerFactory.getLogger( this.getClass() );

    protected UniformTaskScheduler         mTaskScheduler;
    protected TaskExecutionLauncher        mTaskExecutionLauncher;
    protected UniformTaskInstrument        mUniformTaskInstrument;
    protected RuntimeAtlasInstrument       mRuntimeAtlasInstrument;
    protected CentralizedTaskInstrument    mCentralizedTaskInstrument;

    protected GuidAllocator                mGuidAllocator;
    protected RavenTaskMasterManipulator   mRavenTaskMasterManipulator;
    protected ScheduleManipulator          mScheduleManipulator;
    protected InstanceAtlasNodeMapper      mInstanceAtlasNodeMapper;
    protected InstanceAtlasAdjacentMapper  mInstanceAtlasAdjacentMapper;
    protected InstanceExecMapper           mInstanceExecMapper;
    protected InstanceEventMapper          mInstanceEventMapper;

    protected TaskScheduleTimeResolver     mTaskScheduleTimeResolver;
    protected TaskInstanceLineageFreezer   mTaskInstanceLineageFreezer;

    public RavenTaskInstantaneousPreparator( UniformTaskScheduler taskScheduler ) {
        this.mTaskScheduler                = taskScheduler;
        this.mRuntimeAtlasInstrument       = taskScheduler.atlasInstrument();
        this.mTaskExecutionLauncher        = taskScheduler.taskExecutionLauncher();
        this.mCentralizedTaskInstrument    = taskScheduler.taskInstrument();
        this.mUniformTaskInstrument        = this.mCentralizedTaskInstrument.getUniformTaskInstrument();

        this.mGuidAllocator                = this.mCentralizedTaskInstrument.getGuidAllocator();
        this.mRavenTaskMasterManipulator   = this.mCentralizedTaskInstrument.getRavenTaskMasterManipulator();
        this.mScheduleManipulator          = this.mRavenTaskMasterManipulator.getScheduleManipulator();
        this.mInstanceAtlasNodeMapper      = this.mScheduleManipulator.getInstanceAtlasNodeMapper();
        this.mInstanceAtlasAdjacentMapper  = this.mScheduleManipulator.getInstanceAtlasAdjacentMapper();
        this.mInstanceExecMapper           = this.mScheduleManipulator.getInstanceExecMapper();
        this.mInstanceEventMapper          = this.mScheduleManipulator.getInstanceEventMapper();

        this.mTaskScheduleTimeResolver     = new TaskScheduleTimeResolver();
        this.mTaskInstanceLineageFreezer   = new RavenTaskInstanceLineageFreezer(
                this.mGuidAllocator,
                this.mRuntimeAtlasInstrument,
                this.mInstanceAtlasNodeMapper,
                this.mInstanceAtlasAdjacentMapper
        );
    }

    @Override
    public UniformTaskScheduler taskScheduler() {
        return this.mTaskScheduler;
    }

    protected RavenTask resolveTask( GUID taskGuid ) {
        if ( taskGuid == null ) {
            throw new IllegalArgumentException( "Task guid is null." );
        }

        TreeNode treeNode = this.mCentralizedTaskInstrument.get( taskGuid );
        if ( !(treeNode instanceof TaskElement) ) {
            throw new IllegalArgumentException( "Object node `" + taskGuid + "` is not task." );
        }

        return this.mCentralizedTaskInstrument.constructTask( (TaskElement) treeNode );
    }

    protected boolean isImmediateMode( TaskInstantaneousContext context ) {
        return context.getMode() == null || context.getMode() == TaskInstantaneousMode.Immediate;
    }

    protected boolean shouldBypassLineage( TaskInstantaneousContext context ) {
        TaskInstantaneousMode mode = context.getMode();
        return context.isAllowLineageBypass()
                || mode == TaskInstantaneousMode.Debug
                || mode == TaskInstantaneousMode.Temporary;
    }

    protected TaskScheduleType resolveInstanceScheduleType(
            TaskElement element, TaskInstantaneousContext context
    ) {
        if ( !this.isImmediateMode( context ) ) {
            return TaskScheduleType.Temporary;
        }

        TaskScheduleType scheduleType = element.getScheduleType();
        if ( scheduleType == TaskScheduleType.Manual
                || scheduleType == TaskScheduleType.Cycle
                || scheduleType == TaskScheduleType.Temporary ) {
            return scheduleType;
        }

        throw new IllegalStateException(
                "Task `" + element.getName() + "` schedule type `" + scheduleType + "` cannot be run immediately."
        );
    }

    protected void assertImmediateTaskRunnable( TaskElement element, TaskInstantaneousContext context ) {
        if ( !this.isImmediateMode( context ) ) {
            return;
        }

        if ( !element.isEnable() ) {
            throw new IllegalStateException( "Task `" + element.getName() + "` is disabled." );
        }

        this.resolveInstanceScheduleType( element, context );
    }

    protected void assertBusinessTimeUnique( TaskElement element, LocalDateTime businessTime ) {
        if ( businessTime == null ) {
            return;
        }

        InstanceEntry existing = this.mUniformTaskInstrument.getInstanceInstrument()
                .queryInstanceByTaskGuidAndBusinessTime( element.getGuid(), businessTime );
        if ( existing != null ) {
            throw new IllegalStateException(
                    "Task `" + element.getName() + "` already has instance for business time `" + businessTime + "`."
            );
        }
    }

    protected boolean isParentInstanceLineageResolvable( TaskElement element, LocalDateTime expectTime, LocalDateTime businessTime ) {
        GraphNode graphNode = this.mRuntimeAtlasInstrument.queryGraphNodeByTaskGuid( element.getGuid() );
        if ( graphNode == null ) {
            return true;
        }

        List<GUID> parentIds = this.mRuntimeAtlasInstrument.fetchParentIds( graphNode.getId() );
        if ( parentIds == null || parentIds.isEmpty() ) {
            return true;
        }

        for ( GUID parentId : parentIds ) {
            TaskElement parentElement = this.mRuntimeAtlasInstrument.queryTaskElementByGuid( parentId );
            if ( parentElement == null ) {
                return false;
            }
            if ( businessTime == null ) {
                if ( this.mInstanceAtlasNodeMapper.queryByTaskGuidAndExpectTime( parentElement.getGuid(), expectTime ) != null ) {
                    continue;
                }
                return false;
            }
            if ( this.mInstanceAtlasNodeMapper.queryByTaskGuidAndBusinessTime( parentElement.getGuid(), businessTime ) == null ) {
                return false;
            }
        }

        return true;
    }

    protected void assertLineageResolvable(
            TaskElement element, TaskInstantaneousContext context, LocalDateTime expectTime, LocalDateTime businessTime
    ) {
        if ( this.shouldBypassLineage( context ) ) {
            return;
        }

        if ( !this.isParentInstanceLineageResolvable( element, expectTime, businessTime ) ) {
            throw new IllegalStateException(
                    "Task `" + element.getName() + "` parent instance lineage is not ready."
            );
        }
    }

    protected void ensureTaskExec( RavenTaskInstance instance ) {
        InstanceEntry entry = instance.getInstanceEntry();
        GUID instanceGuid = entry.getGuid();
        int nRetryCnt = entry.getRetryCnt();
        if ( this.mInstanceExecMapper.queryByInstanceGuidAndRetry( instanceGuid, nRetryCnt ) != null ) {
            return;
        }

        InstanceExec exec = new GenericInstanceExec();
        exec.setTaskGuid( entry.getTaskGuid() );
        exec.setInstanceGuid( instanceGuid );
        exec.setTaskName( instance.getOwnedTask().getName() );
        exec.setInstanceName( entry.getInstanceName() );
        exec.setProcessorQueue( "default" );
        exec.setImagePath( entry.getImagePath() );
        exec.setClusterName( "local_cluster" );
        exec.setExecState( TaskInstanceExecState.Submitted.getName() );
        exec.setCurrentRetryNumber( nRetryCnt );
        exec.setRetryTimes( nRetryCnt );
        this.mInstanceExecMapper.insert( exec );
    }

    protected void ensureTaskEventTimeReady( RavenTaskInstance instance ) {
        InstanceEntry entry = instance.getInstanceEntry();
        GUID instanceGuid = entry.getGuid();
        String szEventState = InstanceEventType.TaskTimeReady.getName();
        if ( this.mInstanceEventMapper.queryByInstanceGuidAndState( instanceGuid, szEventState ) != null ) {
            return;
        }

        InstanceEvent event = new GenericInstanceEvent();
        event.setGuid( this.mGuidAllocator.nextGUID() );
        event.setTaskGuid( entry.getTaskGuid() );
        event.setInstanceGuid( instanceGuid );
        event.setInstanceName( entry.getInstanceName() );
        event.setRetryTimes( entry.getRetryCnt() );
        event.setCurrentRetryNumber( entry.getRetryCnt() );
        event.setEventType( instance.getTaskType() );
        event.setState( szEventState );
        event.setExecTime( LocalDateTime.now() );
        event.setEventContext( "{}" );
        this.mScheduleManipulator.getInstanceEventMapper().insert( event );
    }

    protected void freezeLineage( TaskElement element, RavenTaskInstance instance, LocalDateTime expectTime ) {
        TaskScheduleContext scheduleContext = new TaskScheduleContext( element, instance.getInstanceEntry().getFireTime() );
        scheduleContext.setThisScheduleTime( expectTime );
        Collection<ScheduledTaskInstanceLineage> lineages = this.mTaskInstanceLineageFreezer.freeze(
                java.util.List.of( new ScheduledTaskInstanceFrame( scheduleContext, instance, true ) )
        );
        if ( lineages.isEmpty() ) {
            this.log.warn(
                    "[TaskInstantaneousPreparator] Instance lineage is empty, instanceGuid:`{}`.",
                    instance.getInstanceEntry().getGuid()
            );
        }
    }

    protected LaunchFeature prepareLaunchFeature( TaskElement element, TaskInstantaneousContext context, LocalDateTime bizTimeEpoch ) {
        LaunchFeature feature = new LaunchFeature();
        feature.setBizTimeEpoch( bizTimeEpoch );
        feature.setAllowAsymmetricImage( context.isAllowAsymmetricImage() );
        feature.setAllowInstantaneousDepartureBypass( context.isAllowInstantaneousDepartureBypass() );

        if ( TaskDeploymentMethod.isAuthoritative( element.getDeploymentMethod() ) ) {
            feature.setAllowAsymmetricImage( false );
        }

        return feature;
    }

    @Override
    public TaskInstantaneousPrepareResult prepare( TaskInstantaneousContext context ) throws MetaPersistenceException {
        if ( context == null ) {
            throw new IllegalArgumentException( "TaskInstantaneousContext is null." );
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expectTime = context.getExpectTime();
        if ( expectTime == null ) {
            expectTime = now;
        }
        LocalDateTime fireTime = context.getFireTime();
        if ( fireTime == null ) {
            fireTime = now;
        }
        LocalDateTime bizTimeEpoch = context.getBusinessTimeEpoch();
        if ( bizTimeEpoch == null ) {
            bizTimeEpoch = expectTime;
        }

        RavenTask task = this.resolveTask( context.getTaskGuid() );
        TaskElement element = task.getTaskElement();
        this.assertImmediateTaskRunnable( element, context );

        LocalDateTime businessTime = this.mTaskScheduleTimeResolver.resolveBusinessTime( element, bizTimeEpoch );
        this.assertBusinessTimeUnique( element, businessTime );
        this.assertLineageResolvable( element, context, expectTime, businessTime );

        RavenTaskInstance instance = task.createInstance();

        InstanceEntry entry = instance.getInstanceEntry();
        entry.setScheduleType( this.resolveInstanceScheduleType( element, context ) );
        entry.setExpectTime( expectTime );
        entry.setFireTime( fireTime );
        entry.setBusinessTime( businessTime );
        if ( StringUtils.isNoneEmpty( context.getProcessorName() ) ) {
            entry.setProcessorName( context.getProcessorName() );
        }

        LaunchFeature feature = this.prepareLaunchFeature( element, context, bizTimeEpoch );

        this.mTaskExecutionLauncher.initializeInstance( instance, feature );
        if ( !this.shouldBypassLineage( context ) ) {
            this.freezeLineage( element, instance, expectTime );
        }
        this.ensureTaskExec( instance );
        this.ensureTaskEventTimeReady( instance );

        return new TaskInstantaneousPrepareResult( context, instance );
    }

}
