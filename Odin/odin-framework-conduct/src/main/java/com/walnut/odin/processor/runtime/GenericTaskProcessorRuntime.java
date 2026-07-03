package com.walnut.odin.processor.runtime;

import java.util.LinkedHashMap;
import java.util.Map;

import com.walnut.odin.dispatch.TaskExecutionProcessor;
import com.walnut.odin.dispatch.TaskQueueMeta;
import com.walnut.odin.dispatch.TaskDispatcher;
import com.walnut.odin.dispatch.entity.TaskProcessorEntity;
import com.walnut.odin.processor.anonymous.AnonymousTaskProcessorMetadataParser;
import com.walnut.odin.processor.anonymous.AnonymousTaskProcessorRegistration;
import com.walnut.odin.processor.anonymous.AnonymousTaskProcessorRegistry;
import com.walnut.odin.processor.event.GenericTaskProcessorEvent;
import com.walnut.odin.processor.event.TaskProcessorEstablishment;
import com.walnut.odin.processor.event.TaskProcessorEventHookRegistry;
import com.walnut.odin.processor.event.TaskProcessorEventType;
import com.walnut.odin.processor.metadata.TaskProcessorRegisterMetadataSpec;
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
            Map<String, String> metadata = this.incorporatedEventMetadata( entity, context );
            this.dispatchEvent(
                    TaskProcessorEventType.JoinAccepted,
                    establishment,
                    context.getClientId(),
                    context.getNodeName(),
                    metadata,
                    szProcessorGuid,
                    null
            );
            this.dispatchEvent(
                    TaskProcessorEventType.Registered,
                    establishment,
                    context.getClientId(),
                    context.getNodeName(),
                    metadata,
                    szProcessorGuid,
                    null
            );
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
            Map<String, String> metadata = this.incorporatedEventMetadata( incorporatedProcessor );
            this.dispatchEvent(
                    TaskProcessorEventType.Detached,
                    TaskProcessorEstablishment.Incorporated,
                    nClientId,
                    incorporatedProcessor.getName(),
                    metadata,
                    null,
                    null
            );
            this.dispatchEvent(
                    TaskProcessorEventType.Unregistered,
                    TaskProcessorEstablishment.Incorporated,
                    nClientId,
                    incorporatedProcessor.getName(),
                    metadata,
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

    protected Map<String, String> incorporatedEventMetadata(
            TaskProcessorEntity entity,
            TaskProcessorRegisterContext context
    ) {
        Map<String, String> metadata = new LinkedHashMap<>();
        if ( context != null && context.getMetadata() != null ) {
            metadata.putAll( context.getMetadata() );
        }
        this.applyIncorporatedEntityMetadata( metadata, entity );
        return metadata;
    }

    protected Map<String, String> incorporatedEventMetadata( TaskExecutionProcessor processor ) {
        Map<String, String> metadata = new LinkedHashMap<>();
        if ( processor == null ) {
            return metadata;
        }
        this.put( metadata, TaskProcessorRegisterMetadataSpec.KEY_ESTABLISHMENT, TaskProcessorRegisterMetadataSpec.ESTABLISHMENT_INCORPORATED );
        this.put( metadata, TaskProcessorRegisterMetadataSpec.KEY_NAME, processor.getName() );
        this.put( metadata, TaskProcessorRegisterMetadataSpec.KEY_CLUSTER_NAME, processor.getClusterName() );
        this.put( metadata, TaskProcessorRegisterMetadataSpec.KEY_CLUSTER_PATH, processor.getClusterPath() );
        this.put( metadata, TaskProcessorRegisterMetadataSpec.KEY_EXEC_CAPS, processor.getExecCaps() );
        this.put( metadata, TaskProcessorRegisterMetadataSpec.KEY_LOCAL, String.valueOf( processor.isLocal() ) );
        this.put( metadata, TaskProcessorRegisterMetadataSpec.KEY_EXCLUSIVE, String.valueOf( processor.isExclusive() ) );
        this.put( metadata, TaskProcessorRegisterMetadataSpec.KEY_PRIORITY, String.valueOf( processor.getPriority() ) );
        this.applyQueueMetadata( metadata, processor.getTaskExecutionQueue() );
        return metadata;
    }

    protected void applyIncorporatedEntityMetadata( Map<String, String> metadata, TaskProcessorEntity entity ) {
        if ( entity == null ) {
            return;
        }
        this.put( metadata, TaskProcessorRegisterMetadataSpec.KEY_ESTABLISHMENT, TaskProcessorRegisterMetadataSpec.ESTABLISHMENT_INCORPORATED );
        this.put( metadata, TaskProcessorRegisterMetadataSpec.KEY_GUID, this.getProcessorGuid( entity ) );
        this.put( metadata, TaskProcessorRegisterMetadataSpec.KEY_NAME, entity.getName() );
        this.put( metadata, TaskProcessorRegisterMetadataSpec.KEY_CLUSTER_NAME, entity.getClusterName() );
        this.put( metadata, TaskProcessorRegisterMetadataSpec.KEY_CLUSTER_PATH, entity.getClusterPath() );
        this.put( metadata, TaskProcessorRegisterMetadataSpec.KEY_EXEC_CAPS, entity.getExecCaps() );
        this.put( metadata, TaskProcessorRegisterMetadataSpec.KEY_LOCAL, String.valueOf( entity.isLocal() ) );
        this.put( metadata, TaskProcessorRegisterMetadataSpec.KEY_EXCLUSIVE, String.valueOf( entity.isExclusive() ) );
        this.put( metadata, TaskProcessorRegisterMetadataSpec.KEY_ENABLE, String.valueOf( entity.isEnable() ) );
        this.put( metadata, TaskProcessorRegisterMetadataSpec.KEY_PRIORITY, String.valueOf( entity.getPriority() ) );
        this.put( metadata, TaskProcessorRegisterMetadataSpec.KEY_EXTRA_METADATA, entity.getExtraMetadata() );
        this.put( metadata, TaskProcessorRegisterMetadataSpec.KEY_DYNAMIC_METADATA, entity.getDyMetadataCache() );
        this.applyQueueMetadata( metadata, entity.getTaskQueueMeta() );
    }

    protected void applyQueueMetadata( Map<String, String> metadata, TaskQueueMeta queueMeta ) {
        if ( queueMeta == null ) {
            return;
        }
        this.put( metadata, TaskProcessorRegisterMetadataSpec.KEY_QUEUE_NAME, queueMeta.getName() );
        this.put( metadata, TaskProcessorRegisterMetadataSpec.KEY_QUEUE_MAX_CAPACITY, String.valueOf( queueMeta.getMaxCapacity() ) );
        this.put( metadata, TaskProcessorRegisterMetadataSpec.KEY_QUEUE_MIN_CAPACITY, String.valueOf( queueMeta.getMinCapacity() ) );
        this.put(
                metadata,
                TaskProcessorRegisterMetadataSpec.KEY_QUEUE_RUNTIME_INSTANCE_CAPACITY,
                String.valueOf( queueMeta.getRuntimeInstanceCapacity() )
        );
    }

    protected void put( Map<String, String> metadata, String szKey, String szValue ) {
        if ( metadata == null || szKey == null || szValue == null ) {
            return;
        }
        metadata.put( szKey, szValue );
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
