package com.pinecone.hydra.device.registry.server;

import java.util.Collection;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.device.kom.DeviceInstrument;
import com.pinecone.hydra.device.kom.entity.ElementNode;
import com.pinecone.hydra.device.registry.DeviceControlRPCException;
import com.pinecone.hydra.device.registry.dto.DeviceRegistrationDTO;
import com.pinecone.hydra.device.registry.server.transport.DeviceControlTransport;
import com.pinecone.hydra.device.registry.server.transport.DeviceControlTransportRegistry;
import com.pinecone.hydra.system.component.Slf4jTraceable;

public interface DeviceManager extends Slf4jTraceable {

    DeviceInstrument getDeviceInstrument();

    Collection<DeviceControlTransport> getTransports();

    DeviceManager addTransport( DeviceControlTransport transport );

    DeviceManager hookTransport( DeviceControlTransport transport );

    DeviceManager vitalizeTransport( DeviceControlTransport transport ) throws DeviceControlRPCException;

    DeviceControlTransport getTransportById( Long transportId );

    DeviceControlTransport evictTransportById( Long transportId );

    int transportSize();

    void startDeviceManager() throws DeviceControlRPCException;

    GUID enrollDevice( DeviceRegistrationDTO registrationDTO );

    ElementNode queryDeviceByPath( String path );

    ElementNode queryDeviceByGuid( GUID guid );

    void updateDevice( ElementNode elementNode );

    void removeDevice( GUID guid );

    DeviceLifecycleService deviceLifecycleService();

    DeviceMetaService deviceMetaService();

    DeviceTopologyService deviceTopologyService();

    DeviceRuntimeService deviceRuntimeService();

    DeviceControlTransportRegistry deviceControlTransportRegistry();
}
