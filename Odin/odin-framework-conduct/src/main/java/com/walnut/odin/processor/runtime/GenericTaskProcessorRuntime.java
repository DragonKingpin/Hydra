package com.walnut.odin.processor.runtime;

import com.walnut.odin.dispatch.TaskExecutionProcessor;
import com.walnut.odin.dispatch.TaskDispatcher;
import com.walnut.odin.dispatch.entity.TaskProcessorEntity;
import com.walnut.odin.processor.anonymous.AnonymousTaskProcessorMetadataParser;
import com.walnut.odin.processor.anonymous.AnonymousTaskProcessorRegistration;
import com.walnut.odin.processor.anonymous.AnonymousTaskProcessorRegistry;
import com.walnut.odin.processor.event.GenericTaskProcessorEvent;
import com.walnut.odin.processor.event.TaskProcessorEstablishment;
import com.walnut.odin.processor.event.TaskProcessorEventHookRegistry;
import com.walnut.odin.processor.event.TaskProcessorEventType;
import com.walnut.odin.processor.runtime.TaskProcessorRegisterContext;
import com.walnut.odin.processor.runtime.TaskProcessorRegisterResult;
import com.walnut.odin.processor.runtime.TaskProcessorRuntime;
import com.walnut.odin.processor.runtime.TaskProcessorUnregisterResult;

public class GenericTaskProcessorRuntime implements TaskProcessorRuntime {

    protected TaskDispatcher                       mTaskDispatcher;
    protected AnonymousTaskProcessorMetadataParser mAnonymousMetadataParser;

    public GenericTaskProcessorRuntime(
            TaskDispatcher taskDispatcher,
            AnonymousTaskProcessorMetadataParser anonymousMetadataParser
    ) {
        this.mTaskDispatcher          = taskDispatcher;
        this.mAnonymousMetadataParser = anonymousMetadataParser;
    }

    @Override
    public TaskProcessorRegisterResult register( TaskProcessorRegisterContext context ) {
        TaskProcessorEstablishment establishment = this.resolveEstablishment( context );
        this.dispatchEvent( TaskProcessorEventType.JoinRequested, establishment, context, null, null );

        try {
            if ( establishment == TaskProcessorEstablishment.Anonymous ) {
                AnonymousTaskProcessorRegistration registration = this.anonymousProcessorRegistry().register(
                        context.getNodeName(),
                        context.getClientId(),
                        context.getMetadata()
                );
                this.dispatchEvent( TaskProcessorEventType.JoinAccepted, establishment, context, null, null );
                this.dispatchEvent( TaskProcessorEventType.Registered, establishment, context, null, null );
                return GenericTaskProcessorRegisterResult.acceptedAnonymous( registration );
            }

            TaskProcessorEntity entity = this.mTaskDispatcher.registerIncorporatedProcessor(
                    context.getNodeName(),
                    context.getClientId(),
                    context.getMetadata()
            );
            String szProcessorGuid = this.getProcessorGuid( entity );
            this.dispatchEvent( TaskProcessorEventType.JoinAccepted, establishment, context, szProcessorGuid, null );
            this.dispatchEvent( TaskProcessorEventType.Registered, establishment, context, szProcessorGuid, null );
            return GenericTaskProcessorRegisterResult.acceptedIncorporated( entity );
        }
        catch ( RuntimeException e ) {
            this.dispatchEvent( TaskProcessorEventType.JoinRejected, establishment, context, null, e.getMessage() );
            throw e;
        }
    }

    @Override
    public TaskProcessorUnregisterResult unregister( long nClientId ) {
        TaskExecutionProcessor incorporatedProcessor = this.mTaskDispatcher.removeIncorporatedProcessorByClientId( nClientId );
        AnonymousTaskProcessorRegistration anonymousRegistration = this.anonymousProcessorRegistry().unregister( nClientId );

        if ( incorporatedProcessor != null ) {
            this.dispatchEvent(
                    TaskProcessorEventType.Detached,
                    TaskProcessorEstablishment.Incorporated,
                    nClientId,
                    incorporatedProcessor.getName(),
                    null,
                    null,
                    null
            );
            this.dispatchEvent(
                    TaskProcessorEventType.Unregistered,
                    TaskProcessorEstablishment.Incorporated,
                    nClientId,
                    incorporatedProcessor.getName(),
                    null,
                    null,
                    null
            );
        }

        if ( anonymousRegistration != null ) {
            this.dispatchEvent(
                    TaskProcessorEventType.Detached,
                    TaskProcessorEstablishment.Anonymous,
                    nClientId,
                    anonymousRegistration.getNodeName(),
                    anonymousRegistration.getMetadata(),
                    null,
                    null
            );
            this.dispatchEvent(
                    TaskProcessorEventType.Unregistered,
                    TaskProcessorEstablishment.Anonymous,
                    nClientId,
                    anonymousRegistration.getNodeName(),
                    anonymousRegistration.getMetadata(),
                    null,
                    null
            );
        }

        return GenericTaskProcessorUnregisterResult.of( incorporatedProcessor, anonymousRegistration );
    }

    @Override
    public AnonymousTaskProcessorRegistry anonymousProcessorRegistry() {
        return this.mTaskDispatcher.anonymousProcessorRegistry();
    }

    @Override
    public TaskProcessorEventHookRegistry eventHookRegistry() {
        return this.mTaskDispatcher.processorEventHookRegistry();
    }

    protected TaskProcessorEstablishment resolveEstablishment( TaskProcessorRegisterContext context ) {
        if ( context != null && this.mAnonymousMetadataParser != null && this.mAnonymousMetadataParser.isAnonymous( context.getMetadata() ) ) {
            return TaskProcessorEstablishment.Anonymous;
        }
        return TaskProcessorEstablishment.Incorporated;
    }

    protected String getProcessorGuid( TaskProcessorEntity entity ) {
        if ( entity == null || entity.getGuid() == null ) {
            return null;
        }
        return entity.getGuid().toString();
    }

    protected void dispatchEvent(
            TaskProcessorEventType type,
            TaskProcessorEstablishment establishment,
            TaskProcessorRegisterContext context,
            String szProcessorGuid,
            String szReason
    ) {
        if ( context == null ) {
            return;
        }
        this.dispatchEvent(
                type,
                establishment,
                context.getClientId(),
                context.getNodeName(),
                context.getMetadata(),
                szProcessorGuid,
                szReason
        );
    }

    protected void dispatchEvent(
            TaskProcessorEventType type,
            TaskProcessorEstablishment establishment,
            long nClientId,
            String szNodeName,
            java.util.Map<String, String> metadata,
            String szProcessorGuid,
            String szReason
    ) {
        this.eventHookRegistry().dispatch(
                GenericTaskProcessorEvent.of(
                        type,
                        establishment,
                        nClientId,
                        szNodeName,
                        szProcessorGuid,
                        metadata,
                        szReason
                )
        );
    }
}
