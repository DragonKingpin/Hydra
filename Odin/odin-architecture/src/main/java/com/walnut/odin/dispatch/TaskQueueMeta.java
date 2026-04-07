package com.walnut.odin.dispatch;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.deploy.Server;

public interface TaskQueueMeta extends Pinenut {

    String getName();

    int getMaxCapacity();

    int getMinCapacity();

    int getUsedCapacity();

    int getRuntimeInstanceCapacity();

}
