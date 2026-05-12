package com.pinecone.hydra.device.registry.appoint;

import com.pinecone.hydra.device.registry.server.DeviceControlManager;

public interface DeviceAppointServer extends AutoCloseable {

    Long getMessageNodeId();

    DeviceAppointServer hookDeviceControlManager( DeviceControlManager deviceControlManager );

    void execute() throws Exception;

    boolean isStarted();

    @Override
    void close();
}
