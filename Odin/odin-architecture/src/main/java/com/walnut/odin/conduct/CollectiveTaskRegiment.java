package com.walnut.odin.conduct;

import com.pinecone.framework.system.regime.Regiment;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.Identification;
import com.pinecone.hydra.proc.ProcessManager;
import com.pinecone.hydra.system.component.Slf4jTraceable;
import com.pinecone.hydra.task.kom.entity.TaskElement;
import com.walnut.odin.conduct.entity.RegimentJoinRequest;
import com.walnut.odin.conduct.entity.RegimentJoinResponse;
import com.walnut.odin.dispatch.TaskDispatcher;
import com.walnut.odin.proc.RemoteProcessServiceRPCException;
import com.walnut.odin.proc.server.RemoteProcessManagerServer;
import com.walnut.odin.task.CentralizedTaskInstrument;
import com.walnut.odin.task.RavenTask;
import com.walnut.odin.task.troll.TaskExecutionElevator;

public interface CollectiveTaskRegiment extends Regiment, Slf4jTraceable {

    RavenTask createTask( TaskElement taskElement, Identification serviceId );

    void purgeTask( GUID guid );

    RavenTask affirmTask( String path, Identification serviceId, TaskElement metaInfos );

    RavenTask queryTaskByPath( String path ) ;

    RavenTask getTaskByGuid( GUID taskGuid ) ;



    RemoteProcessManagerServer remoteProcessManagerServer();

    ProcessManager processManager();

    CentralizedTaskInstrument taskInstrument();

    TaskExecutionElevator taskExecutionElevator();

    TaskDispatcher taskDispatcher();

    void startRemoteProcessServer() throws RemoteProcessServiceRPCException;




    RegimentJoinResponse invokeJoinRegiment( RegimentJoinRequest request );

}
