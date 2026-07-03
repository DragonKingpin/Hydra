package com.pinecone.hydra.device.kom.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.device.kom.DeviceInstanceQuery;
import com.pinecone.hydra.device.kom.instance.DeviceInstanceEntry;

import java.util.List;

public interface DeviceInstanceManipulator extends Pinenut {

    void initDeviceInstance( DeviceInstanceEntry element );

    DeviceInstanceEntry queryDeviceInstance( GUID instanceGuid );

    List<DeviceInstanceEntry> fetchDeviceInstances( DeviceInstanceQuery query );

    long countDeviceInstances( DeviceInstanceQuery query );

    List<DeviceInstanceEntry> fetchDeviceInstancesByDeviceGuid( GUID deviceGuid );

    List<DeviceInstanceEntry> fetchDeviceInstancesByOwnerInstanceGuid( GUID ownerInstanceGuid );

    void updateDeviceInstance( DeviceInstanceEntry element );
}
