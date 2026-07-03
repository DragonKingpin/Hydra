package com.pinecone.hydra.device.registry.server.transport;

import java.util.Collection;

import com.pinecone.framework.system.prototype.Pinenut;

public interface DeviceControlTransportRegistry extends Pinenut {

    DeviceControlTransport addTransport( DeviceControlTransport transport );

    DeviceControlTransport getTransportById( Long transportId );

    DeviceControlTransport evictTransportById( Long transportId );

    Collection<DeviceControlTransport> fetchTransports();

    int size();
}
