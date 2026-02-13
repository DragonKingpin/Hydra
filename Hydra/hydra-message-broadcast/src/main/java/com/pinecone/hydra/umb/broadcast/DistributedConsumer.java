package com.pinecone.hydra.umb.broadcast;

import com.pinecone.framework.system.prototype.Pinenut;

public interface DistributedConsumer extends Pinenut {

    DistributedConsumer parentConsumer();

    BroadcastConsumer mainConsumer();

    String mainTopic();

    String routerPath();

}
