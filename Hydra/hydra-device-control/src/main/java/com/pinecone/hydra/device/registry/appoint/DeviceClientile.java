package com.pinecone.hydra.device.registry.appoint;

import java.net.SocketAddress;

public interface DeviceClientile extends AutoCloseable {

    Long getClientId();

    SocketAddress getRemoteAddress();

    @Override
    void close();
}
