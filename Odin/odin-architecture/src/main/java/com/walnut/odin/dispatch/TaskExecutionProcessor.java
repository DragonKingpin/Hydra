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

    boolean isExclusive();

    TaskLaunchContext getTaskLaunchContextByPID( GUID pid );


    int getRunningSize();


    int getWaitingSize();


    UProcess directlyCreate( RavenTaskInstance instance, LaunchFeature feature ) throws InstanceLaunchException;

    UProcess directlyLaunch( RavenTaskInstance instance, LaunchFeature feature ) throws InstanceLaunchException;



    PipelineLaunchReport recycleTerminated(Collection<Identification> terminatedIds );

    PipelineLaunchReport launchsPending() throws TaskDispatchException;

    PipelineLaunchReport shiftLaunchsPipeline(Collection<Identification> terminatedIds ) throws TaskDispatchException;


    PipelineLaunchReport prepare(Collection<TaskLaunchContext> contexts ) throws TaskDispatchException;

    PipelineLaunchReport pipeCreate(Collection<TaskLaunchContext> contexts ) throws TaskDispatchException;

    PipelineLaunchReport pipeLaunch(Collection<TaskLaunchContext> contexts ) throws TaskDispatchException;

}
