package com.pinecone.hydra.device.registry.server;

import java.util.Collection;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.device.kom.DeviceInstrument;
import com.pinecone.hydra.device.kom.entity.ElementNode;
import com.pinecone.hydra.device.registry.DeviceControlRPCException;
import com.pinecone.hydra.device.registry.appoint.DeviceAppointServer;
import com.pinecone.hydra.device.registry.dto.DeviceRegistrationDTO;
import com.pinecone.hydra.system.component.Slf4jTraceable;

public interface DeviceManager extends Slf4jTraceable {

    DeviceInstrument getDeviceInstrument();

    Collection<DeviceAppointServer> getServers();

    DeviceManager addAppointServer( DeviceAppointServer appointServer );

    DeviceManager hookAppointServer( DeviceAppointServer appointServer );

    DeviceManager vitalizeAppointServer( DeviceAppointServer appointServer ) throws DeviceControlRPCException;

    DeviceAppointServer getAppointServerById( Long appointNodeId );

    DeviceAppointServer evictAppointServerById( Long appointNodeId );

    int serverSize();

    void startDeviceManager() throws DeviceControlRPCException;

    GUID enrollDevice( DeviceRegistrationDTO registrationDTO );

    ElementNode queryDeviceByPath( String path );

    ElementNode queryDeviceByGuid( GUID guid );

    void updateDevice( ElementNode elementNode );

    void removeDevice( GUID guid );

    DeviceLifecycleService deviceLifecycleService();

    DeviceMetaService deviceMetaService();

    DeviceTopologyService deviceTopologyService();
}
