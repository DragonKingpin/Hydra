package com.walnut.odin.conduct;

import com.pinecone.framework.system.regime.Regiment;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.Identification;
import com.pinecone.hydra.proc.ProcessManager;
import com.pinecone.hydra.task.kom.entity.TaskElement;
import com.walnut.odin.proc.server.RemoteProcessManagerServer;
import com.walnut.odin.task.RavenTask;

public interface CollectiveTaskRegiment extends Regiment {

    RavenTask createTask( TaskElement taskElement, Identification serviceId );

    void purgeTask( GUID guid );

    RavenTask affirmTask( String path, Identification serviceId, TaskElement metaInfos );




    RemoteProcessManagerServer remoteProcessManagerServer();

    ProcessManager processManager();
}
