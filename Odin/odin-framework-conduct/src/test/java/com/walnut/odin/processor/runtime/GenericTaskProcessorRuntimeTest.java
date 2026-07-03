package com.walnut.odin.processor.runtime;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.pinecone.framework.util.id.Identification;
import com.pinecone.hydra.proc.UProcess;
import com.walnut.odin.conduct.CollectiveTaskRegiment;
import com.walnut.odin.dispatch.PipelineLaunchReport;
import com.walnut.odin.dispatch.TaskDispatchException;
import com.walnut.odin.dispatch.TaskDispatcher;
import com.walnut.odin.dispatch.TaskExecutionProcessor;
import com.walnut.odin.dispatch.TaskLaunchContext;
import com.walnut.odin.dispatch.entity.GenericTaskProcessorEntity;
import com.walnut.odin.dispatch.entity.TaskProcessorEntity;
import com.walnut.odin.processor.anonymous.AnonymousTaskProcessorRegistry;
import com.walnut.odin.processor.anonymous.GenericAnonymousTaskProcessorMetadataParser;
import com.walnut.odin.processor.anonymous.RavenAnonymousTaskProcessorRegistry;
import com.walnut.odin.processor.event.GenericTaskProcessorEventHookRegistry;
import com.walnut.odin.processor.event.TaskProcessorEstablishment;
import com.walnut.odin.processor.event.TaskProcessorEvent;
import com.walnut.odin.processor.event.TaskProcessorEventHookRegistry;
import com.walnut.odin.processor.event.TaskProcessorEventType;
import com.walnut.odin.processor.metadata.TaskProcessorRegisterMetadataSpec;
import com.walnut.odin.task.RavenTaskInstance;
import com.walnut.odin.task.troll.InstanceLaunchException;
import com.walnut.odin.task.troll.LaunchFeature;
import com.walnut.odin.task.troll.TaskExecutionLauncher;

class GenericTaskProcessorRuntimeTest {

    @Test
    void anonymousProcessorRegistersOutsideDispatchPool() {
        StubDispatcher dispatcher = new StubDispatcher();
        GenericTaskProcessorRuntime runtime = new GenericTaskProcessorRuntime(
                dispatcher,
                new GenericAnonymousTaskProcessorMetadataParser()
        );
        List<TaskProcessorEvent> events = new ArrayList<>();
        dispatcher.processorEventHookRegistry().addHooker( events::add );

        Map<String, String> metadata = new LinkedHashMap<>();
        metadata.put(
                TaskProcessorRegisterMetadataSpec.KEY_ESTABLISHMENT,
                TaskProcessorRegisterMetadataSpec.ESTABLISHMENT_ANONYMOUS
        );
        metadata.put( TaskProcessorRegisterMetadataSpec.KEY_ALIAS, "heist-private-worker-1" );
        metadata.put( TaskProcessorRegisterMetadataSpec.KEY_BIZ_PATH, "/infra/odin/heist/manhattan" );
        metadata.put( TaskProcessorRegisterMetadataSpec.KEY_EXEC_CAPS, "[\"JVM\",\"SCRIPT\"]" );

        TaskProcessorRegisterResult result = runtime.register(
                GenericTaskProcessorRegisterContext.of( "missing-db-processor", 2048L, metadata )
        );

        Assertions.assertTrue( result.isAccepted() );
        Assertions.assertEquals( TaskProcessorEstablishment.Anonymous, result.getEstablishment() );
        Assertions.assertNotNull( dispatcher.anonymousProcessorRegistry().getByClientId( 2048L ) );
        Assertions.assertEquals(
                "/infra/odin/heist/manhattan",
                dispatcher.anonymousProcessorRegistry().getByClientId( 2048L ).getBizPath()
        );
        Assertions.assertTrue( dispatcher.fetchProcessors().isEmpty() );
        Assertions.assertFalse( dispatcher.registerIncorporatedCalled );
        Assertions.assertTrue(
                events.stream().anyMatch( event ->
                        event.getType() == TaskProcessorEventType.Registered
                                && event.getEstablishment() == TaskProcessorEstablishment.Anonymous
                )
        );
    }

    @Test
    void incorporatedProcessorUsesDispatchPool() {
        StubDispatcher dispatcher = new StubDispatcher();
        GenericTaskProcessorRuntime runtime = new GenericTaskProcessorRuntime(
                dispatcher,
                new GenericAnonymousTaskProcessorMetadataParser()
        );
        List<TaskProcessorEvent> events = new ArrayList<>();
        dispatcher.processorEventHookRegistry().addHooker( events::add );

        Map<String, String> metadata = new LinkedHashMap<>();
        metadata.put( TaskProcessorRegisterMetadataSpec.KEY_EXEC_CAPS, "[\"SCRIPT\"]" );
        metadata.put( TaskProcessorRegisterMetadataSpec.KEY_ALIAS, "client-alias" );

        TaskProcessorRegisterResult result = runtime.register(
                GenericTaskProcessorRegisterContext.of( "incorporated-processor", 4096L, metadata )
        );

        Assertions.assertTrue( result.isAccepted() );
        Assertions.assertEquals( TaskProcessorEstablishment.Incorporated, result.getEstablishment() );
        Assertions.assertTrue( dispatcher.registerIncorporatedCalled );
        Assertions.assertEquals( 1, dispatcher.fetchProcessors().size() );
        Assertions.assertNull( dispatcher.anonymousProcessorRegistry().getByClientId( 4096L ) );

        TaskProcessorEvent registered = events.stream()
                .filter( event -> event.getType() == TaskProcessorEventType.Registered )
                .findFirst()
                .orElseThrow();
        Assertions.assertEquals(
                TaskProcessorRegisterMetadataSpec.ESTABLISHMENT_INCORPORATED,
                registered.getMetadata().get( TaskProcessorRegisterMetadataSpec.KEY_ESTABLISHMENT )
        );
        Assertions.assertEquals(
                "ANY",
                registered.getMetadata().get( TaskProcessorRegisterMetadataSpec.KEY_EXEC_CAPS )
        );
        Assertions.assertEquals(
                "incorporated-processor",
                registered.getMetadata().get( TaskProcessorRegisterMetadataSpec.KEY_NAME )
        );
        Assertions.assertEquals(
                "incorporated-processor-queue",
                registered.getMetadata().get( TaskProcessorRegisterMetadataSpec.KEY_QUEUE_NAME )
        );
        Assertions.assertEquals(
                "client-alias",
                registered.getMetadata().get( TaskProcessorRegisterMetadataSpec.KEY_ALIAS )
        );
    }

    @Test
    void unregisterCleansBothRuntimePools() {
        StubDispatcher dispatcher = new StubDispatcher();
        GenericTaskProcessorRuntime runtime = new GenericTaskProcessorRuntime(
                dispatcher,
                new GenericAnonymousTaskProcessorMetadataParser()
        );

        Map<String, String> metadata = new LinkedHashMap<>();
        metadata.put(
                TaskProcessorRegisterMetadataSpec.KEY_ESTABLISHMENT,
                TaskProcessorRegisterMetadataSpec.ESTABLISHMENT_ANONYMOUS
        );
        runtime.register( GenericTaskProcessorRegisterContext.of( "anonymous", 2048L, metadata ) );
        runtime.register( GenericTaskProcessorRegisterContext.of( "incorporated", 4096L, Collections.emptyMap() ) );

        TaskProcessorUnregisterResult anonymousResult = runtime.unregister( 2048L );
        TaskProcessorUnregisterResult incorporatedResult = runtime.unregister( 4096L );

        Assertions.assertNotNull( anonymousResult.getAnonymousRegistration() );
        Assertions.assertNull( dispatcher.anonymousProcessorRegistry().getByClientId( 2048L ) );
        Assertions.assertNotNull( incorporatedResult.getIncorporatedProcessor() );
        Assertions.assertTrue( dispatcher.fetchProcessors().isEmpty() );
    }

    @Test
    void unregisterIncorporatedProcessorEmitsLastAuthoritativeMetadata() {
        StubDispatcher dispatcher = new StubDispatcher();
        GenericTaskProcessorRuntime runtime = new GenericTaskProcessorRuntime(
                dispatcher,
                new GenericAnonymousTaskProcessorMetadataParser()
        );
        List<TaskProcessorEvent> events = new ArrayList<>();
        dispatcher.processorEventHookRegistry().addHooker( events::add );

        runtime.register( GenericTaskProcessorRegisterContext.of( "incorporated", 4096L, Collections.emptyMap() ) );
        runtime.unregister( 4096L );

        TaskProcessorEvent detached = events.stream()
                .filter( event -> event.getType() == TaskProcessorEventType.Detached )
                .findFirst()
                .orElseThrow();
        Assertions.assertEquals(
                "ANY",
                detached.getMetadata().get( TaskProcessorRegisterMetadataSpec.KEY_EXEC_CAPS )
        );
        Assertions.assertEquals(
                "incorporated",
                detached.getMetadata().get( TaskProcessorRegisterMetadataSpec.KEY_NAME )
        );
    }

    private static class StubDispatcher implements TaskDispatcher {

        private final AnonymousTaskProcessorRegistry anonymousRegistry;
        private final TaskProcessorEventHookRegistry eventHookRegistry;
        private final Map<Long, TaskExecutionProcessor> incorporatedByClientId;

        private boolean registerIncorporatedCalled;

        private StubDispatcher() {
            this.anonymousRegistry = new RavenAnonymousTaskProcessorRegistry();
            this.eventHookRegistry = new GenericTaskProcessorEventHookRegistry();
            this.incorporatedByClientId = new LinkedHashMap<>();
        }

        @Override
        public TaskExecutionLauncher taskExecutionLauncher() {
            return null;
        }

        @Override
        public CollectiveTaskRegiment collectiveTaskRegiment() {
            return null;
        }

        @Override
        public void registerProcessor( TaskExecutionProcessor processor ) {
        }

        @Override
        public TaskProcessorEntity registerProcessor( String szProcessorName, long nClientId ) throws IllegalArgumentException {
            return this.registerIncorporatedProcessor( szProcessorName, nClientId, null );
        }

        @Override
        public TaskProcessorEntity registerProcessor(
                String szProcessorName, long nClientId, Map<String, String> metadata
        ) throws IllegalArgumentException {
            return this.registerIncorporatedProcessor( szProcessorName, nClientId, metadata );
        }

        @Override
        public TaskProcessorEntity registerIncorporatedProcessor(
                String szProcessorName, long nClientId, Map<String, String> metadata
        ) throws IllegalArgumentException {
            this.registerIncorporatedCalled = true;
            GenericTaskProcessorEntity entity = new GenericTaskProcessorEntity();
            entity.setName( szProcessorName );
            entity.setControlClientId( nClientId );
            entity.setClusterPath( "/test" );
            entity.setClusterName( "test" );
            entity.setExecCaps( "ANY" );
            entity.setEnable( true );
            entity.setQueueName( szProcessorName + "-queue" );
            entity.setQueueMaxCapacity( 1 );
            entity.setQueueRuntimeInstanceCapacity( 1 );
            this.incorporatedByClientId.put( nClientId, new StubTaskExecutionProcessor( entity ) );
            return entity;
        }

        @Override
        public void unregisterProcessor( String szProcessorName ) {
        }

        @Override
        public void unregisterProcessor( long nClientId ) {
            this.removeIncorporatedProcessorByClientId( nClientId );
        }

        @Override
        public TaskExecutionProcessor removeIncorporatedProcessorByClientId( long nClientId ) {
            return this.incorporatedByClientId.remove( nClientId );
        }

        @Override
        public AnonymousTaskProcessorRegistry anonymousProcessorRegistry() {
            return this.anonymousRegistry;
        }

        @Override
        public TaskProcessorEventHookRegistry processorEventHookRegistry() {
            return this.eventHookRegistry;
        }

        @Override
        public TaskProcessorRuntime processorRuntime() {
            return new GenericTaskProcessorRuntime( this, new GenericAnonymousTaskProcessorMetadataParser() );
        }

        @Override
        public Collection<TaskExecutionProcessor> fetchProcessors() {
            return new ArrayList<>( this.incorporatedByClientId.values() );
        }

        @Override
        public TaskExecutionProcessor getProcessorByName( String szProcessorName ) {
            return this.incorporatedByClientId.values().stream()
                    .filter( processor -> szProcessorName.equals( processor.getName() ) )
                    .findFirst()
                    .orElse( null );
        }

        @Override
        public TaskExecutionProcessor getProcessorByClientId( long nClientId ) {
            return this.incorporatedByClientId.get( nClientId );
        }

        @Override
        public void setProcessorAffinity( String szProcessorName, TaskLaunchContext launchContext ) {
        }

        @Override
        public TaskExecutionProcessor getAffinityTasks( Identification taskId ) {
            return null;
        }

        @Override
        public TaskExecutionProcessor getAffinityTask( TaskLaunchContext launchContext ) {
            return null;
        }

        @Override
        public Collection<TaskLaunchContext> queryAffinityTasks( String szProcessorName ) {
            return Collections.emptyList();
        }

        @Override
        public PipelineLaunchReport pipeCreate( Collection<TaskLaunchContext> contexts ) throws InstanceLaunchException, TaskDispatchException {
            return null;
        }

        @Override
        public PipelineLaunchReport pipeCreatePrepared( Collection<TaskLaunchContext> contexts ) throws InstanceLaunchException, TaskDispatchException {
            return null;
        }

        @Override
        public PipelineLaunchReport pipeLaunch( Collection<TaskLaunchContext> contexts ) throws InstanceLaunchException, TaskDispatchException {
            return null;
        }

        @Override
        public PipelineLaunchReport pipeLaunchPrepared( Collection<TaskLaunchContext> contexts ) throws InstanceLaunchException, TaskDispatchException {
            return null;
        }

        @Override
        public PipelineLaunchReport pipeStartPrepared( Collection<TaskLaunchContext> contexts ) throws InstanceLaunchException, TaskDispatchException {
            return null;
        }

        @Override
        public UProcess create( RavenTaskInstance instance, LaunchFeature feature ) throws InstanceLaunchException, TaskDispatchException {
            return null;
        }

        @Override
        public UProcess launch( RavenTaskInstance instance, LaunchFeature feature ) throws InstanceLaunchException, TaskDispatchException {
            return null;
        }
    }

    private static class StubTaskExecutionProcessor implements TaskExecutionProcessor {

        private final TaskProcessorEntity entity;

        private StubTaskExecutionProcessor( TaskProcessorEntity entity ) {
            this.entity = entity;
        }

        @Override
        public String getName() {
            return this.entity.getName();
        }

        @Override
        public com.pinecone.hydra.deploy.Server getDeployClusterServer() {
            return this.entity.getDeployClusterServer();
        }

        @Override
        public String getClusterPath() {
            return this.entity.getClusterPath();
        }

        @Override
        public String getClusterName() {
            return this.entity.getClusterName();
        }

        @Override
        public long getControlClientId() {
            return this.entity.getControlClientId();
        }

        @Override
        public String getExecCaps() {
            return this.entity.getExecCaps();
        }

        @Override
        public com.walnut.odin.dispatch.TaskExecutionQueue getTaskExecutionQueue() {
            return null;
        }

        @Override
        public boolean isLocal() {
            return this.entity.isLocal();
        }

        @Override
        public int getPriority() {
            return this.entity.getPriority();
        }

        @Override
        public boolean isExclusive() {
            return this.entity.isExclusive();
        }

        @Override
        public TaskLaunchContext getTaskLaunchContextByPID( com.pinecone.framework.util.id.GUID pid ) {
            return null;
        }

        @Override
        public int getRunningSize() {
            return 0;
        }

        @Override
        public int getWaitingSize() {
            return 0;
        }

        @Override
        public UProcess directlyCreate( RavenTaskInstance instance, LaunchFeature feature ) {
            return null;
        }

        @Override
        public UProcess directlyCreatePrepared( RavenTaskInstance instance, LaunchFeature feature ) {
            return null;
        }

        @Override
        public UProcess directlyLaunch( RavenTaskInstance instance, LaunchFeature feature ) {
            return null;
        }

        @Override
        public UProcess directlyLaunchPrepared( RavenTaskInstance instance, LaunchFeature feature ) {
            return null;
        }

        @Override
        public UProcess directlyStartPrepared( TaskLaunchContext context ) {
            return null;
        }

        @Override
        public PipelineLaunchReport recycleTerminated( Collection<Identification> terminatedIds ) {
            return null;
        }

        @Override
        public PipelineLaunchReport launchsPending() {
            return null;
        }

        @Override
        public PipelineLaunchReport shiftLaunchsPipeline( Collection<Identification> terminatedIds ) {
            return null;
        }

        @Override
        public PipelineLaunchReport prepare( Collection<TaskLaunchContext> contexts ) {
            return null;
        }

        @Override
        public PipelineLaunchReport pipeCreate( Collection<TaskLaunchContext> contexts ) {
            return null;
        }

        @Override
        public PipelineLaunchReport pipeCreatePrepared( Collection<TaskLaunchContext> contexts ) {
            return null;
        }

        @Override
        public PipelineLaunchReport pipeLaunch( Collection<TaskLaunchContext> contexts ) {
            return null;
        }

        @Override
        public PipelineLaunchReport pipeLaunchPrepared( Collection<TaskLaunchContext> contexts ) {
            return null;
        }

        @Override
        public PipelineLaunchReport pipeStartPrepared( Collection<TaskLaunchContext> contexts ) {
            return null;
        }
    }
}
