package com.pinecone.hydra.device.control.server;

import java.util.Collection;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.deploy.kom.DeployInstrument;
import com.pinecone.hydra.deploy.kom.entity.ElementNode;
import com.pinecone.hydra.device.control.DeviceControlRPCException;
import com.pinecone.hydra.device.control.appoint.DeviceAppointServer;
import com.pinecone.hydra.device.control.dto.DeviceRegistrationDTO;
import com.pinecone.hydra.system.component.Slf4jTraceable;

public interface DeviceControlManager extends Slf4jTraceable {

    DeployInstrument getDeployInstrument();

    Collection<DeviceAppointServer> getServers();

    DeviceControlManager addAppointServer( DeviceAppointServer appointServer );

    DeviceControlManager hookAppointServer( DeviceAppointServer appointServer );

    DeviceControlManager vitalizeAppointServer( DeviceAppointServer appointServer ) throws DeviceControlRPCException;

    DeviceAppointServer getAppointServerById( Long appointNodeId );

    DeviceAppointServer evictAppointServerById( Long appointNodeId );

    int serverSize();

    void startDeviceControl() throws DeviceControlRPCException;

    GUID registerDevice( DeviceRegistrationDTO registrationDTO );

    ElementNode queryDeviceByPath( String path );

    ElementNode queryDeviceByGuid( GUID guid );

    void updateDevice( ElementNode elementNode );

    void removeDevice( GUID guid );

    DeviceLifecycleService deviceLifecycleService();

    DeviceMetaService deviceMetaService();

    DeviceTopologyService deviceTopologyService();
}
