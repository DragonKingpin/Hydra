package com.walnut.odin.conduct.lifecycle;

import java.time.LocalDateTime;
import java.util.Collection;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.task.TaskInstanceStatus;
import com.pinecone.hydra.task.kom.instance.InstanceEntry;
import com.pinecone.hydra.task.kom.instance.InstanceInstrument;
import com.walnut.odin.conduct.entity.GenericInstanceEvent;
import com.walnut.odin.conduct.entity.InstanceEvent;
import com.walnut.odin.task.mapper.InstanceEventMapper;

public class KernelTaskInstanceLifecycleInstrument implements TaskInstanceLifecycleInstrument, Pinenut {

    protected InstanceInstrument mInstanceInstrument;
    protected InstanceEventMapper mInstanceEventMapper;
    protected GuidAllocator mGuidAllocator;

    public KernelTaskInstanceLifecycleInstrument(
            InstanceInstrument instanceInstrument, InstanceEventMapper instanceEventMapper
    ) {
        this.mInstanceInstrument = instanceInstrument;
        this.mInstanceEventMapper = instanceEventMapper;
        this.mGuidAllocator = instanceInstrument.getTaskInstrument().getGuidAllocator();
    }

    protected void traceEvent( TaskInstanceTransition transition ) {
        InstanceEntry instance = this.mInstanceInstrument.getInstanceEntry( transition.getInstanceGuid() );
        if ( instance == null ) {
            return;
        }

        TaskInstanceTransitionReason reason = transition.getReason();
        InstanceEvent event = new GenericInstanceEvent();
        event.setGuid( this.mGuidAllocator.nextGUID() );
        event.setTaskGuid( instance.getTaskGuid() );
        event.setInstanceGuid( instance.getGuid() );
        event.setInstanceName( instance.getInstanceName() );
        event.setRetryTimes( instance.getRetryCnt() );
        event.setCurrentRetryNumber( instance.getRetryCnt() );
        event.setEventType( reason == null ? "Unknown" : reason.getEventType() );
        event.setState( transition.getToStatus().getName() );
        event.setExecTime( LocalDateTime.now() );
        event.setEventContext( transition.getEventContext() == null ? "{}" : transition.getEventContext() );
        this.mInstanceEventMapper.insert( event );
    }

    @Override
    public TaskInstanceTransitionResult transit( TaskInstanceTransition transition ) {
        Collection<?> fromStatuses = transition.getFromStatuses();
        int nAffectedRows;
        if ( fromStatuses == null || fromStatuses.isEmpty() ) {
            return TaskInstanceTransitionResult.of( transition, 0 );
        }
        if ( fromStatuses.size() == 1 ) {
            nAffectedRows = this.mInstanceInstrument.transitStatus(
                    transition.getInstanceGuid(), transition.getFromStatuses().iterator().next(), transition.getToStatus()
            );
        }
        else {
            nAffectedRows = this.mInstanceInstrument.transitStatusIn(
                    transition.getInstanceGuid(), transition.getFromStatuses(), transition.getToStatus()
            );
        }

        TaskInstanceTransitionResult result = TaskInstanceTransitionResult.of( transition, nAffectedRows );
        if ( result.isSucceeded() ) {
            this.traceEvent( transition );
        }
        return result;
    }

    @Override
    public TaskInstanceTransitionResult transit(
            GUID instanceGuid, TaskInstanceStatus fromStatus, TaskInstanceStatus toStatus, TaskInstanceTransitionReason reason
    ) {
        return this.transit( TaskInstanceTransition.of( instanceGuid, fromStatus, toStatus, reason ) );
    }

    @Override
    public TaskInstanceTransitionResult transitWithScheduleTime(
            GUID instanceGuid, TaskInstanceStatus fromStatus, TaskInstanceStatus toStatus,
            TaskInstanceTransitionReason reason, LocalDateTime scheduleTime
    ) {
        TaskInstanceTransition transition = TaskInstanceTransition.of( instanceGuid, fromStatus, toStatus, reason );
        int nAffectedRows = this.mInstanceInstrument.transitStatusWithScheduleTime(
                instanceGuid, fromStatus, toStatus, scheduleTime
        );
        TaskInstanceTransitionResult result = TaskInstanceTransitionResult.of( transition, nAffectedRows );
        if ( result.isSucceeded() ) {
            this.traceEvent( transition );
        }
        return result;
    }

    @Override
    public TaskInstanceTransitionResult transitAny(
            GUID instanceGuid, Collection<TaskInstanceStatus> fromStatuses, TaskInstanceStatus toStatus, TaskInstanceTransitionReason reason
    ) {
        return this.transit( TaskInstanceTransition.ofAny( instanceGuid, fromStatuses, toStatus, reason ) );
    }
}
