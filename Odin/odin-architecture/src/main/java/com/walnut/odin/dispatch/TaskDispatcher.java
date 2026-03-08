package com.walnut.odin.dispatch;

import java.util.Collection;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.proc.UProcess;
import com.walnut.odin.dispatch.entity.TaskProcessorEntity;
import com.walnut.odin.task.RavenTaskInstance;
import com.walnut.odin.task.troll.InstanceLaunchException;
import com.walnut.odin.task.troll.LaunchFeature;

public interface TaskDispatcher extends Pinenut {

    void registerProcessor( TaskExecutionProcessor processor );

    TaskProcessorEntity registerProcessor( String szProcessorName, long nClientId ) throws IllegalArgumentException;

    void unregisterProcessor( String szProcessorName );

    void unregisterProcessor( long nClientId );

    Collection<TaskExecutionProcessor> fetchProcessors();


    void setProcessorAffinity( String szProcessorName, TaskLaunchContext launchContext );

    Collection<TaskLaunchContext> queryAffinityTasks( String szProcessorName );


    PipelineElevationReport pipeLaunch( Collection<TaskLaunchContext> contexts ) throws InstanceLaunchException, TaskDispatchException;

    PipelineElevationReport pipeElevate( Collection<TaskLaunchContext> contexts ) throws InstanceLaunchException, TaskDispatchException;


    UProcess launch( RavenTaskInstance instance, LaunchFeature feature ) throws InstanceLaunchException, TaskDispatchException;

    UProcess elevate( RavenTaskInstance instance, LaunchFeature feature ) throws InstanceLaunchException, TaskDispatchException;


}
