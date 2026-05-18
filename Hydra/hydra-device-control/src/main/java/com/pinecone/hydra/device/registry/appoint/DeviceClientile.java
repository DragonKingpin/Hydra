package com.pinecone.hydra.device.registry.appoint;

import java.net.SocketAddress;

import com.pinecone.framework.system.prototype.Pinenut;

public interface DeviceClientile extends AutoCloseable, Pinenut {

    Long getClientId();

    SocketAddress getRemoteAddress();

    @Override
    void close();
}
