package com.acorn.skynet.device.husky.protocol;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.device.registry.dto.DeviceRegistrationDTO;
import com.pinecone.hydra.umct.stereotype.Iface;

@Iface
public interface HuskyDeviceLifecycleIface extends Pinenut {

    String enrollDevice( DeviceRegistrationDTO registrationDTO );

    void dismissDeviceByGuid( String guid );

    void dismissDeviceByPath( String path );

    boolean hasDeviceByGuid( String guid );

    boolean hasDeviceByPath( String path );
}
