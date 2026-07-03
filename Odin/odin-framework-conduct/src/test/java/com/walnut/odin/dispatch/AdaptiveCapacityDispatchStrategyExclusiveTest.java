package com.walnut.odin.dispatch;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.Identification;
import com.pinecone.hydra.deploy.Server;
import com.pinecone.hydra.proc.UProcess;
import com.walnut.odin.conduct.CollectiveTaskRegiment;
import com.walnut.odin.processor.anonymous.AnonymousTaskProcessorRegistry;
import com.walnut.odin.processor.anonymous.RavenAnonymousTaskProcessorRegistry;
import com.walnut.odin.processor.event.GenericTaskProcessorEventHookRegistry;
import com.walnut.odin.processor.event.TaskProcessorEventHookRegistry;
import com.walnut.odin.processor.runtime.GenericTaskProcessorRuntime;
import com.walnut.odin.processor.runtime.TaskProcessorRuntime;
import com.walnut.odin.task.RavenTaskInstance;
import com.walnut.odin.task.troll.LaunchFeature;
import com.walnut.odin.task.troll.TaskExecutionLauncher;

class AdaptiveCapacityDispatchStrategyExclusiveTest {

    @Test
    void normalDispatchSkipsExclusiveProcessor() throws Exception {
        AdaptiveCapacityDispatchStrategy strategy = new AdaptiveCapacityDispatchStrategy();
        StubProcessor normal = new StubProcessor( "normal", false, 4 );
        StubProcessor exclusive = new StubProcessor( "exclusive", true, 4 );

        TaskLaunchContext context = context( "task-normal", new LaunchFeature() );
        Map<TaskExecutionProcessor, Collection<TaskLaunchContext>> plan = strategy.dispatch(
                List.of( exclusive, normal ),
                List.of( context ),
                new StubDispatcher()
        );

        Assertions.assertSame( normal, onlyProcessor( plan ) );
    }

    @Test
    void affinityDispatchCanUseExclusiveProcessor() throws Exception {
        AdaptiveCapacityDispatchStrategy strategy = new AdaptiveCapacityDispatchStrategy();
        StubProcessor normal = new StubProcessor( "normal", false, 4 );
        StubProcessor exclusive = new StubProcessor( "exclusive", true, 4 );

        TaskLaunchContext context = context( "task-affinity", new LaunchFeature() );
        context.setAffinityProcessorName( "exclusive" );

        Map<TaskExecutionProcessor, Collection<TaskLaunchContext>> plan = strategy.dispatch(
                List.of( normal, exclusive ),
                List.of( context ),
                new StubDispatcher()
        );

        Assertions.assertSame( exclusive, onlyProcessor( plan ) );
    }

    @Test
    void designatedDispatchCanUseExclusiveProcessor() throws Exception {
        AdaptiveCapacityDispatchStrategy strategy = new AdaptiveCapacityDispatchStrategy();
        StubProcessor normal = new StubProcessor( "normal", false, 4 );
        StubProcessor exclusive = new StubProcessor( "exclusive", true, 4 );

        TaskLaunchContext context = context(
                "task-designated",
                new LaunchFeature().withProcessorDesignated( "exclusive" )
        );

        Map<TaskExecutionProcessor, Collection<TaskLaunchContext>> plan = strategy.dispatch(
                List.of( normal, exclusive ),
                List.of( context ),
                new StubDispatcher()
        );

        Assertions.assertSame( exclusive, onlyProcessor( plan ) );
    }

    @Test
    void designatedMissingProcessorStillFails() {
        AdaptiveCapacityDispatchStrategy strategy = new AdaptiveCapacityDispatchStrategy();
        StubProcessor normal = new StubProcessor( "normal", false, 4 );

        TaskLaunchContext context = context(
                "task-designated-missing",
                new LaunchFeature().withProcessorDesignated( "missing" )
        );

        TaskDispatchException exception = Assertions.assertThrows(
                TaskDispatchException.class,
                () -> strategy.dispatch(
                        List.of( normal ),
                        List.of( context ),
                        new StubDispatcher()
                )
        );

        Assertions.assertTrue( exception.getMessage().contains( "Designated processor `missing` not available." ) );
    }

    private static TaskExecutionProcessor onlyProcessor(
            Map<TaskExecutionProcessor, Collection<TaskLaunchContext>> plan
    ) {
        Assertions.assertEquals( 1, plan.size() );
        return plan.keySet().iterator().next();
    }

    private static TaskLaunchContext context( String taskName, LaunchFeature feature ) {
        return TaskLaunchContext.of( proxyRavenTaskInstance( taskName ), feature );
    }

    private static RavenTaskInstance proxyRavenTaskInstance( String taskName ) {
        return (RavenTaskInstance) java.lang.reflect.Proxy.newProxyInstance(
                RavenTaskInstance.class.getClassLoader(),
                new Class<?>[]{ RavenTaskInstance.class },
                ( proxy, method, args ) -> {
                    String szMethodName = method.getName();
                    if ( "getExecArch".equals( szMethodName ) ) {
                        return ExecutionArchitecture.ANY.name();
                    }
                    if ( "getName".equals( szMethodName ) ) {
                        return taskName;
                    }
                    if ( "toString".equals( szMethodName ) ) {
                        return taskName;
                    }
                    return null;
                }
        );
    }

    private static class StubDispatcher implements TaskDispatcher {

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
        public com.walnut.odin.dispatch.entity.TaskProcessorEntity registerProcessor(
                String szProcessorName, long nClientId
        ) {
            return this.registerProcessor( szProcessorName, nClientId, null );
        }

        @Override
        public com.walnut.odin.dispatch.entity.TaskProcessorEntity registerProcessor(
                String szProcessorName, long nClientId, Map<String, String> metadata
        ) {
            return null;
        }

        @Override
        public com.walnut.odin.dispatch.entity.TaskProcessorEntity registerIncorporatedProcessor(
                String szProcessorName, long nClientId, Map<String, String> metadata
        ) {
            return null;
        }

        @Override
        public void unregisterProcessor( String szProcessorName ) {
        }

        @Override
        public void unregisterProcessor( long nClientId ) {
        }

        @Override
        public TaskExecutionProcessor removeIncorporatedProcessorByClientId( long nClientId ) {
            return null;
        }

        @Override
        public AnonymousTaskProcessorRegistry anonymousProcessorRegistry() {
            return new RavenAnonymousTaskProcessorRegistry();
        }

        @Override
        public TaskProcessorEventHookRegistry processorEventHookRegistry() {
            return new GenericTaskProcessorEventHookRegistry();
        }

        @Override
        public TaskProcessorRuntime processorRuntime() {
            return new GenericTaskProcessorRuntime( this, null );
        }

        @Override
        public Collection<TaskExecutionProcessor> fetchProcessors() {
            return Collections.emptyList();
        }

        @Override
        public TaskExecutionProcessor getProcessorByName( String szProcessorName ) {
            return null;
        }

        @Override
        public TaskExecutionProcessor getProcessorByClientId( long nClientId ) {
            return null;
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

        @Override
        public UProcess create( RavenTaskInstance instance, LaunchFeature feature ) {
            return null;
        }

        @Override
        public UProcess launch( RavenTaskInstance instance, LaunchFeature feature ) {
            return null;
        }
    }

    private static class StubProcessor implements TaskExecutionProcessor {

        private final String name;
        private final boolean exclusive;
        private final TaskExecutionQueue queue;

        private StubProcessor( String name, boolean exclusive, int capacity ) {
            this.name = name;
            this.exclusive = exclusive;
            this.queue = new GenericI32TaskQueue( new StubTaskQueueMeta( name + "-queue", capacity ) );
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public Server getDeployClusterServer() {
            return null;
        }

        @Override
        public String getClusterPath() {
            return "/dev/null";
        }

        @Override
        public String getClusterName() {
            return "dev";
        }

        @Override
        public long getControlClientId() {
            return 0;
        }

        @Override
        public String getExecCaps() {
            return "[\"ANY\"]";
        }

        @Override
        public TaskExecutionQueue getTaskExecutionQueue() {
            return this.queue;
        }

        @Override
        public boolean isLocal() {
            return false;
        }

        @Override
        public int getPriority() {
            return 0;
        }

        @Override
        public boolean isExclusive() {
            return this.exclusive;
        }

        @Override
        public TaskLaunchContext getTaskLaunchContextByPID( GUID pid ) {
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

    private static class StubTaskQueueMeta implements TaskQueueMeta {

        private final String name;
        private final int capacity;

        private StubTaskQueueMeta( String name, int capacity ) {
            this.name = name;
            this.capacity = capacity;
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public int getMaxCapacity() {
            return this.capacity;
        }

        @Override
        public int getMinCapacity() {
            return 0;
        }

        @Override
        public int getUsedCapacity() {
            return 0;
        }

        @Override
        public int getRuntimeInstanceCapacity() {
            return 0;
        }
    }
}
