package com.walnut.odin.dispatch.entity;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.deploy.Server;
import com.walnut.odin.dispatch.TaskQueueMeta;

public interface TaskProcessorEntity extends Pinenut {

    GUID getGuid();

    String getName();

    void setName( String name );

    void setGuid( GUID guid);

    Server getDeployClusterServer();

    String getClusterPath();

    String getClusterName();

    long getControlClientId();

    void setControlClientId( long controlClientId );

    boolean isLocal();

    boolean isExclusive();

    int getPriority();

    boolean isEnable();

    TaskQueueMeta getTaskQueueMeta();


}
