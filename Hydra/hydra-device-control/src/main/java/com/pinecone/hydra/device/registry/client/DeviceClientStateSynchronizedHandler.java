package com.pinecone.hydra.device.registry.client;

import com.pinecone.framework.system.prototype.Pinenut;

public interface DeviceClientStateSynchronizedHandler extends Pinenut {

    void afterDeviceClientStateSynchronized( String reason );
}
