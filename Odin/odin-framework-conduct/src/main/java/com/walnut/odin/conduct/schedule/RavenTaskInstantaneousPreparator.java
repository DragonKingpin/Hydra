package com.walnut.odin.conduct.schedule;

import java.time.LocalDateTime;
import java.util.Collection;

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
import com.walnut.odin.atlas.graph.RuntimeAtlasInstrument;
import com.walnut.odin.conduct.entity.GenericInstanceEvent;
import com.walnut.odin.conduct.entity.GenericInstanceExec;
import com.walnut.odin.conduct.entity.InstanceEvent;
import com.walnut.odin.conduct.entity.InstanceExec;
import com.walnut.odin.conduct.schedule.entity.ScheduledTaskInstanceFrame;
import com.walnut.odin.conduct.schedule.entity.ScheduledTaskInstanceLineage;
import com.walnut.odin.conduct.schedule.entity.TaskInstantaneousContext;
import com.walnut.odin.conduct.schedule.entity.TaskInstantaneousPrepareResult;
import com.walnut.odin.conduct.schedule.entity.TaskScheduleContext;
import com.walnut.odin.conduct.schedule.lineage.RavenTaskInstanceLineageFreezer;
import com.walnut.odin.conduct.schedule.lineage.TaskInstanceLineageFreezer;
import com.walnut.odin.task.CentralizedTaskInstrument;
import com.walnut.odin.task.RavenTask;
import com.walnut.odin.task.RavenTaskInstance;
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
        RavenTaskInstance instance = task.createInstance();

        InstanceEntry entry = instance.getInstanceEntry();
        entry.setScheduleType( TaskScheduleType.Temporary );
        entry.setExpectTime( expectTime );
        entry.setFireTime( fireTime );
        entry.setBusinessTime( this.mTaskScheduleTimeResolver.resolveBusinessTime( element, bizTimeEpoch ) );
        if ( StringUtils.isNoneEmpty( context.getProcessorName() ) ) {
            entry.setProcessorName( context.getProcessorName() );
        }

        LaunchFeature feature = new LaunchFeature();
        feature.setBizTimeEpoch( bizTimeEpoch );

        this.mTaskExecutionLauncher.initializeInstance( instance, feature );
        this.freezeLineage( element, instance, expectTime );
        this.ensureTaskExec( instance );
        this.ensureTaskEventTimeReady( instance );

        return new TaskInstantaneousPrepareResult( context, instance );
    }

}
