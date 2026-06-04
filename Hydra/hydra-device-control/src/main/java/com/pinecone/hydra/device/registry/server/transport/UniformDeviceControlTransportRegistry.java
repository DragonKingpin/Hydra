package com.pinecone.hydra.device.registry.server.transport;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.pinecone.hydra.device.registry.DeviceValidationException;

public class UniformDeviceControlTransportRegistry implements DeviceControlTransportRegistry {

    protected final ConcurrentMap<Long, DeviceControlTransport> transports = new ConcurrentHashMap<>();

    @Override
    public DeviceControlTransport addTransport( DeviceControlTransport transport ) {
        if ( transport == null || transport.getTransportId() == null ) {
            throw new DeviceValidationException( "Device control transport id is required." );
        }
        this.transports.put( transport.getTransportId(), transport );
        return transport;
    }

    @Override
    public DeviceControlTransport getTransportById( Long transportId ) {
        return this.transports.get( transportId );
    }

    @Override
    public DeviceControlTransport evictTransportById( Long transportId ) {
        DeviceControlTransport transport = this.transports.remove( transportId );
        if ( transport != null ) {
            transport.close();
        }
        return transport;
    }

    @Override
    public Collection<DeviceControlTransport> fetchTransports() {
        return this.transports.values();
    }

    @Override
    public int size() {
        return this.transports.size();
    }
}
