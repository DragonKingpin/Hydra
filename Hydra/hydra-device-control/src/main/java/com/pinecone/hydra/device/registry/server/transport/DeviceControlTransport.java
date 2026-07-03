package com.pinecone.hydra.device.registry.server.transport;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.config.PatriarchalConfig;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.device.registry.DeviceControlRPCException;
import com.pinecone.hydra.device.registry.server.DeviceManager;

public interface DeviceControlTransport extends AutoCloseable, Pinenut {

    DeviceControlTransport hookDeviceManager( DeviceManager deviceManager );

    Long getTransportId();

    String getName();

    PatriarchalConfig getConfig();

    void execute() throws Exception;

    boolean isStarted();

    default boolean containsClient( long clientId ) {
        return false;
    }

    default void shutdownClientDevice( long clientId, GUID instanceGuid, String reason ) throws DeviceControlRPCException {
        throw new DeviceControlRPCException( "Device control transport does not support passive device shutdown." );
    }

    @Override
    void close();
}
