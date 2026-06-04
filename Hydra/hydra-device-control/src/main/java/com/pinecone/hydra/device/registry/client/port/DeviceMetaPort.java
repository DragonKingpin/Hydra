package com.pinecone.hydra.device.registry.client.port;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.device.registry.dto.DeviceMetaDTO;

public interface DeviceMetaPort extends Pinenut {

    DeviceMetaDTO queryDeviceMetaByPath( String path );

    DeviceMetaDTO queryDeviceMetaByGuid( String guid );

    boolean updateDeviceMetaByPath( String path, DeviceMetaDTO meta );

    boolean updateDeviceMetaByGuid( String guid, DeviceMetaDTO meta );
}
