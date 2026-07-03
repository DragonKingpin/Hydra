package com.walnut.odin.conduct;

import com.pinecone.framework.system.regime.Regiment;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.Identification;
import com.pinecone.hydra.proc.ProcessManager;
import com.pinecone.hydra.system.component.Slf4jTraceable;
import com.pinecone.hydra.task.kom.entity.TaskElement;
import com.walnut.odin.conduct.entity.LaunchedContext;
import com.walnut.odin.conduct.entity.RegimentJoinRequest;
import com.walnut.odin.conduct.entity.RegimentJoinResponse;
import com.walnut.odin.conduct.lifecycle.TaskInstanceLifecycleExaminer;
import com.walnut.odin.dispatch.TaskDispatchException;
import com.walnut.odin.dispatch.TaskDispatcher;
import com.walnut.odin.proc.RemoteProcessServiceRPCException;
import com.walnut.odin.proc.server.RemoteProcessManagerServer;
import com.walnut.odin.task.CentralizedTaskInstrument;
import com.walnut.odin.task.RavenTask;
import com.walnut.odin.task.troll.InstanceLaunchException;
import com.walnut.odin.task.troll.LaunchFeature;
import com.walnut.odin.task.troll.TaskExecutionLauncher;

public interface CollectiveTaskRegiment extends Regiment, Slf4jTraceable {

    RavenTask createTask( TaskElement taskElement, Identification serviceId );

    void purgeTask( GUID guid );

    RavenTask affirmTask( String path, Identification serviceId, TaskElement metaInfos );

    RavenTask queryTaskByPath( String path ) ;

    RavenTask getTaskByGuid( GUID taskGuid ) ;



    RemoteProcessManagerServer remoteProcessManagerServer();

    ProcessManager processManager();

    CentralizedTaskInstrument taskInstrument();

    TaskExecutionLauncher taskExecutionLauncher();

    TaskDispatcher taskDispatcher();

    TaskInstanceLifecycleExaminer taskInstanceLifecycleExaminer();

    void startRemoteProcessServer() throws RemoteProcessServiceRPCException;



    LaunchedContext create( GUID taskGuid, LaunchFeature feature ) throws InstanceLaunchException, TaskDispatchException;

    LaunchedContext launch( GUID taskGuid, LaunchFeature feature ) throws InstanceLaunchException, TaskDispatchException;


    LaunchedContext create( String path, LaunchFeature feature ) throws InstanceLaunchException, TaskDispatchException;

    LaunchedContext launch( String path, LaunchFeature feature ) throws InstanceLaunchException, TaskDispatchException;


    RegimentJoinResponse invokeJoinRegiment( RegimentJoinRequest request );

}
