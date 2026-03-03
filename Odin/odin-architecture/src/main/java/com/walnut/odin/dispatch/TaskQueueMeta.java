package com.walnut.odin.dispatch;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.deploy.Server;

public interface TaskQueueMeta extends Pinenut {

    String getName();

    Server getDeployClusterServer();

    String getClusterPath();

    String getClusterName();

    int getControlClientId();

    int getCapacity();

    int getMaxCapacity();

    int getMinCapacity();

    int getUsedCapacity();

    int getRuntimeInstanceCapacity();

}
