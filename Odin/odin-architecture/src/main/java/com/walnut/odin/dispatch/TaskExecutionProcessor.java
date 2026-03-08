package com.walnut.odin.dispatch;

import java.util.Collection;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.Identification;
import com.pinecone.hydra.deploy.Server;
import com.pinecone.hydra.proc.UProcess;
import com.walnut.odin.task.RavenTaskInstance;
import com.walnut.odin.task.troll.InstanceLaunchException;
import com.walnut.odin.task.troll.LaunchFeature;

public interface TaskExecutionProcessor extends Pinenut {

    String getName();

    Server getDeployClusterServer();

    String getClusterPath();

    String getClusterName();

    long getControlClientId();

    TaskExecutionQueue getTaskExecutionQueue();

    boolean isLocal();

    int getPriority();

    TaskLaunchContext getTaskLaunchContextByPID( GUID pid );




    UProcess directlyLaunch( RavenTaskInstance instance, LaunchFeature feature ) throws InstanceLaunchException;

    UProcess directlyElevate( RavenTaskInstance instance, LaunchFeature feature ) throws InstanceLaunchException;



    PipelineElevationReport recycleTerminated( Collection<Identification> terminatedIds );

    PipelineElevationReport elevatesPending() throws TaskDispatchException;

    PipelineElevationReport shiftElevatesPipeline( Collection<Identification> terminatedIds ) throws TaskDispatchException;


    PipelineElevationReport prepare( Collection<TaskLaunchContext> contexts ) throws TaskDispatchException;

    PipelineElevationReport pipeLaunch ( Collection<TaskLaunchContext> contexts ) throws TaskDispatchException;

    PipelineElevationReport pipeElevate( Collection<TaskLaunchContext> contexts ) throws TaskDispatchException;

}
