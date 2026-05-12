package com.pinecone.hydra.device.registry.server;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.device.registry.dto.DeviceMetaDTO;
import com.pinecone.hydra.umct.stereotype.Iface;

@Iface
public interface DeviceMetaManipulationIface extends Pinenut {

    DeviceMetaDTO queryDeviceMetaByPath( String path );

    DeviceMetaDTO queryDeviceMetaByGuid( String guid );

    boolean updateDeviceMetaByPath( String path, DeviceMetaDTO meta );

    boolean updateDeviceMetaByGuid( String guid, DeviceMetaDTO meta );
}
