package com.pinecone.hydra.device.control.appoint;

import com.pinecone.hydra.device.control.server.DeviceControlManager;

public interface DeviceAppointServer extends AutoCloseable {

    Long getMessageNodeId();

    DeviceAppointServer hookDeviceControlManager( DeviceControlManager deviceControlManager );

    void execute() throws Exception;

    boolean isStarted();

    @Override
    void close();
}
