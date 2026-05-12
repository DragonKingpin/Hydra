package com.pinecone.hydra.device.registry.appoint;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.device.registry.server.DeviceManager;

public interface DeviceAppointServer extends AutoCloseable, Pinenut {

    Long getMessageNodeId();

    DeviceAppointServer hookDeviceManager( DeviceManager deviceManager );

    void execute() throws Exception;

    boolean isStarted();

    @Override
    void close();
}
