package com.acorn.skynet.device.conduct;

import java.util.Collection;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.system.regime.Regiment;
import com.pinecone.hydra.device.kom.DeviceInstrument;
import com.pinecone.hydra.device.kom.entity.ElementNode;
import com.pinecone.hydra.device.kom.instance.DeviceInstanceEntry;
import com.pinecone.hydra.device.registry.DeviceControlException;
import com.pinecone.hydra.device.registry.server.DeviceManager;
import com.pinecone.hydra.system.component.Slf4jTraceable;

public interface CollectiveDeviceRegiment extends Regiment, Slf4jTraceable {

    DeviceInstrument deviceInstrument();

    DeviceManager deviceManager();

    ElementNode queryDeviceByPath( String szPath );

    ElementNode getDeviceByGuid( GUID guid );

    ElementNode affirmDevice( String szPath, ElementNode elementNode );

    void updateDeviceMeta( ElementNode elementNode );

    void purgeDevice( GUID guid );

    void startDeviceManager() throws DeviceControlException;

    DeviceInstanceEntry queryDeviceRuntime( GUID deviceGuid );

    DeviceInstanceEntry queryDeviceInstance( GUID instanceGuid );

    Collection<DeviceInstanceEntry> fetchDeviceRuntimes();

    void shutdownDeviceInstance( GUID instanceGuid, String reason ) throws DeviceControlException;

    void stopDeviceManager();
}
