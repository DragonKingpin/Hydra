package com.walnut.odin.dispatch;

import java.util.Collection;
import java.util.Map;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.Identification;
import com.pinecone.hydra.proc.UProcess;
import com.walnut.odin.conduct.CollectiveTaskRegiment;
import com.walnut.odin.dispatch.entity.TaskProcessorEntity;
import com.walnut.odin.processor.anonymous.AnonymousTaskProcessorRegistry;
import com.walnut.odin.processor.event.TaskProcessorEventHookRegistry;
import com.walnut.odin.processor.runtime.TaskProcessorRuntime;
import com.walnut.odin.task.RavenTaskInstance;
import com.walnut.odin.task.troll.InstanceLaunchException;
import com.walnut.odin.task.troll.LaunchFeature;
import com.walnut.odin.task.troll.TaskExecutionLauncher;

public interface TaskDispatcher extends Pinenut {

    TaskExecutionLauncher taskExecutionLauncher();

    CollectiveTaskRegiment collectiveTaskRegiment();

    void registerProcessor( TaskExecutionProcessor processor );

    TaskProcessorEntity registerProcessor( String szProcessorName, long nClientId ) throws IllegalArgumentException;

    TaskProcessorEntity registerProcessor( String szProcessorName, long nClientId, Map<String, String> metadata ) throws IllegalArgumentException;

    TaskProcessorEntity registerIncorporatedProcessor( String szProcessorName, long nClientId, Map<String, String> metadata ) throws IllegalArgumentException;

    void unregisterProcessor( String szProcessorName );

    void unregisterProcessor( long nClientId );

    TaskExecutionProcessor removeIncorporatedProcessorByClientId( long nClientId );

    AnonymousTaskProcessorRegistry anonymousProcessorRegistry();

    TaskProcessorEventHookRegistry processorEventHookRegistry();

    TaskProcessorRuntime processorRuntime();

    Collection<TaskExecutionProcessor> fetchProcessors();

    TaskExecutionProcessor getProcessorByName( String szProcessorName );

    TaskExecutionProcessor getProcessorByClientId( long nClientId );


    void setProcessorAffinity( String szProcessorName, TaskLaunchContext launchContext );

    TaskExecutionProcessor getAffinityTasks( Identification taskId );

    TaskExecutionProcessor getAffinityTask( TaskLaunchContext launchContext );

    Collection<TaskLaunchContext> queryAffinityTasks( String szProcessorName );


    PipelineLaunchReport pipeCreate( Collection<TaskLaunchContext> contexts ) throws InstanceLaunchException, TaskDispatchException;

    PipelineLaunchReport pipeCreatePrepared( Collection<TaskLaunchContext> contexts ) throws InstanceLaunchException, TaskDispatchException;

    PipelineLaunchReport pipeLaunch( Collection<TaskLaunchContext> contexts ) throws InstanceLaunchException, TaskDispatchException;

    PipelineLaunchReport pipeLaunchPrepared( Collection<TaskLaunchContext> contexts ) throws InstanceLaunchException, TaskDispatchException;

    PipelineLaunchReport pipeStartPrepared( Collection<TaskLaunchContext> contexts ) throws InstanceLaunchException, TaskDispatchException;


    UProcess create( RavenTaskInstance instance, LaunchFeature feature ) throws InstanceLaunchException, TaskDispatchException;

    UProcess launch( RavenTaskInstance instance, LaunchFeature feature ) throws InstanceLaunchException, TaskDispatchException;


}
