package com.walnut.odin.conduct.lifecycle;

import java.time.LocalDateTime;
import java.util.Collection;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.task.TaskInstanceStatus;
import com.pinecone.hydra.task.kom.instance.InstanceEntry;
import com.pinecone.hydra.task.kom.instance.InstanceInstrument;
import com.walnut.odin.conduct.entity.GenericInstanceEvent;
import com.walnut.odin.conduct.entity.InstanceEvent;
import com.walnut.odin.task.mapper.InstanceEventMapper;

public class KernelTaskInstanceLifecycleExaminer implements TaskInstanceLifecycleExaminer {

    protected InstanceInstrument mInstanceInstrument;
    protected InstanceEventMapper mInstanceEventMapper;
    protected GuidAllocator mGuidAllocator;
    protected TaskInstanceEventHookRegistry mEventHookRegistry;

    public KernelTaskInstanceLifecycleExaminer(
            InstanceInstrument instanceInstrument, InstanceEventMapper instanceEventMapper
    ) {
        this.mInstanceInstrument = instanceInstrument;
        this.mInstanceEventMapper = instanceEventMapper;
        this.mGuidAllocator = instanceInstrument.getTaskInstrument().getGuidAllocator();
        this.mEventHookRegistry = new KernelTaskInstanceEventHookRegistry();
    }

    @Override
    public TaskInstanceEventHookRegistry eventHookRegistry() {
        return this.mEventHookRegistry;
    }

    protected void publishTransition( TaskInstanceTransitionResult result ) {
        if ( result == null || !result.isSucceeded() ) {
            return;
        }
        this.recordTransitionEvent( result.getTransition() );
        this.mEventHookRegistry.dispatch( result );
    }

    protected InstanceEvent buildInstanceEvent(
            InstanceEntry instance, TaskInstanceTransitionReason reason, String state, String eventContext
    ) {
        if ( instance == null || instance.getGuid() == null ) {
            return null;
        }

        InstanceEvent event = new GenericInstanceEvent();
        event.setGuid( this.mGuidAllocator.nextGUID() );
        event.setTaskGuid( instance.getTaskGuid() );
        event.setInstanceGuid( instance.getGuid() );
        event.setInstanceName( instance.getInstanceName() );
        event.setRetryTimes( instance.getRetryTimes() );
        event.setSequenceCnt( instance.getSequenceCnt() );
        event.setCurrentRetryNumber( instance.getRetryCnt() );
        event.setEventType( reason == null ? "Unknown" : reason.getEventType() );
        event.setState( state == null ? "" : state );
        event.setExecTime( LocalDateTime.now() );
        event.setEventContext( eventContext == null ? "{}" : eventContext );
        return event;
    }

    protected void insertInstanceEvent(
            InstanceEntry instance, TaskInstanceTransitionReason reason, String state, String eventContext
    ) {
        InstanceEvent event = this.buildInstanceEvent( instance, reason, state, eventContext );
        if ( event == null ) {
            return;
        }
        this.mInstanceEventMapper.insert( event );
    }

    protected void recordTransitionEvent( TaskInstanceTransition transition ) {
        InstanceEntry instance = this.mInstanceInstrument.getInstanceEntry( transition.getInstanceGuid() );
        TaskInstanceTransitionReason reason = transition.getReason();
        String state = transition.getToStatus() == null ? "" : transition.getToStatus().getName();
        this.insertInstanceEvent( instance, reason, state, transition.getEventContext() );
    }

    @Override
    public void recordInstanceEvent(
            InstanceEntry instance,
            TaskInstanceTransitionReason reason,
            String eventContext
    ) {
        String state = reason == null ? "Unknown" : reason.getEventType();
        this.recordInstanceEvent( instance, reason, state, eventContext );
    }

    @Override
    public void recordInstanceEvent(
            InstanceEntry instance,
            TaskInstanceTransitionReason reason,
            String state,
            String eventContext
    ) {
        this.insertInstanceEvent( instance, reason, state, eventContext );
    }

    @Override
    public TaskInstanceTransitionResult transit( TaskInstanceTransition transition ) {
        Collection<?> fromStatuses = transition.getFromStatuses();
        int nAffectedRows;
        if ( fromStatuses == null || fromStatuses.isEmpty() ) {
            return TaskInstanceTransitionResult.of( transition, 0 );
        }
        nAffectedRows = this.mInstanceInstrument.transitStatusInMonotonic(
                transition.getInstanceGuid(), transition.getFromStatuses(), transition.getToStatus()
        );

        TaskInstanceTransitionResult result = TaskInstanceTransitionResult.of( transition, nAffectedRows );
        if ( result.isSucceeded() ) {
            this.publishTransition( result );
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
        int nAffectedRows = this.mInstanceInstrument.transitStatusInMonotonicWithFields(
                instanceGuid, transition.getFromStatuses(), toStatus, scheduleTime, null, null, null, null
        );
        TaskInstanceTransitionResult result = TaskInstanceTransitionResult.of( transition, nAffectedRows );
        if ( result.isSucceeded() ) {
            this.publishTransition( result );
        }
        return result;
    }

    @Override
    public TaskInstanceTransitionResult transitAnyWithScheduleTime(
            GUID instanceGuid,
            Collection<TaskInstanceStatus> fromStatuses,
            TaskInstanceStatus toStatus,
            TaskInstanceTransitionReason reason,
            LocalDateTime scheduleTime
    ) {
        TaskInstanceTransition transition = TaskInstanceTransition.ofAny( instanceGuid, fromStatuses, toStatus, reason );
        int nAffectedRows = this.mInstanceInstrument.transitStatusInMonotonicWithFields(
                instanceGuid, transition.getFromStatuses(), toStatus, scheduleTime, null, null, null, null
        );
        TaskInstanceTransitionResult result = TaskInstanceTransitionResult.of( transition, nAffectedRows );
        if ( result.isSucceeded() ) {
            this.publishTransition( result );
        }
        return result;
    }

    @Override
    public TaskInstanceTransitionResult transitAny(
            GUID instanceGuid, Collection<TaskInstanceStatus> fromStatuses, TaskInstanceStatus toStatus, TaskInstanceTransitionReason reason
    ) {
        return this.transit( TaskInstanceTransition.ofAny( instanceGuid, fromStatuses, toStatus, reason ) );
    }

    @Override
    public TaskInstanceTransitionResult transitAnyWithRuntimeFields(
            GUID instanceGuid,
            Collection<TaskInstanceStatus> fromStatuses,
            TaskInstanceStatus toStatus,
            TaskInstanceTransitionReason reason,
            LocalDateTime latestStartTime,
            LocalDateTime latestEndTime,
            LocalDateTime finishTime,
            String errorCause
    ) {
        TaskInstanceTransition transition = TaskInstanceTransition.ofAny( instanceGuid, fromStatuses, toStatus, reason );
        int nAffectedRows = this.mInstanceInstrument.transitStatusInMonotonicWithFields(
                instanceGuid, fromStatuses, toStatus, null, latestStartTime, latestEndTime, finishTime, errorCause
        );
        TaskInstanceTransitionResult result = TaskInstanceTransitionResult.of( transition, nAffectedRows );
        if ( result.isSucceeded() ) {
            this.publishTransition( result );
        }
        return result;
    }

    @Override
    public TaskInstanceTransitionResult transitCurrentRetryWithRuntimeFields(
            GUID instanceGuid,
            int sequenceCnt,
            int retryCnt,
            Collection<TaskInstanceStatus> fromStatuses,
            TaskInstanceStatus toStatus,
            TaskInstanceTransitionReason reason,
            LocalDateTime latestStartTime,
            LocalDateTime latestEndTime,
            LocalDateTime finishTime,
            String errorCause
    ) {
        return this.transitCurrentRetryWithRuntimeFields(
                instanceGuid,
                sequenceCnt,
                retryCnt,
                fromStatuses,
                toStatus,
                reason,
                latestStartTime,
                latestEndTime,
                finishTime,
                errorCause,
                "{}"
        );
    }

    @Override
    public TaskInstanceTransitionResult transitCurrentRetryWithRuntimeFields(
            GUID instanceGuid,
            int sequenceCnt,
            int retryCnt,
            Collection<TaskInstanceStatus> fromStatuses,
            TaskInstanceStatus toStatus,
            TaskInstanceTransitionReason reason,
            LocalDateTime latestStartTime,
            LocalDateTime latestEndTime,
            LocalDateTime finishTime,
            String errorCause,
            String eventContext
    ) {
        return this.transitCurrentRetryWithScheduleAndRuntimeFields(
                instanceGuid,
                sequenceCnt,
                retryCnt,
                fromStatuses,
                toStatus,
                reason,
                null,
                latestStartTime,
                latestEndTime,
                finishTime,
                errorCause,
                eventContext
        );
    }

    @Override
    public TaskInstanceTransitionResult transitCurrentRetryWithScheduleAndRuntimeFields(
            GUID instanceGuid,
            int sequenceCnt,
            int retryCnt,
            Collection<TaskInstanceStatus> fromStatuses,
            TaskInstanceStatus toStatus,
            TaskInstanceTransitionReason reason,
            LocalDateTime scheduleTime,
            LocalDateTime latestStartTime,
            LocalDateTime latestEndTime,
            LocalDateTime finishTime,
            String errorCause,
            String eventContext
    ) {
        TaskInstanceTransition transition = TaskInstanceTransition.ofAny( instanceGuid, fromStatuses, toStatus, reason )
                .withEventContext( eventContext );
        int nAffectedRows = this.mInstanceInstrument.transitStatusInMonotonicWithFieldsGuarded(
                instanceGuid, sequenceCnt, retryCnt, fromStatuses, toStatus, scheduleTime, latestStartTime, latestEndTime, finishTime, errorCause
        );
        TaskInstanceTransitionResult result = TaskInstanceTransitionResult.of( transition, nAffectedRows );
        if ( result.isSucceeded() ) {
            this.publishTransition( result );
        }
        return result;
    }
}
