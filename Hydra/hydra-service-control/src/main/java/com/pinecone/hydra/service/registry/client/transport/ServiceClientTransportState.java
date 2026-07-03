package com.pinecone.hydra.service.registry.client.transport;

import com.pinecone.framework.system.prototype.Pinenut;

public enum ServiceClientTransportState implements Pinenut {

    New,

    Starting,

    Synchronizing,

    Ready,

    Disconnected,

    Terminated

}
