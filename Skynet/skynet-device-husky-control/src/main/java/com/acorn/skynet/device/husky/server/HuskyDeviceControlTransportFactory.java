package com.acorn.skynet.device.husky.server;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.device.registry.server.DeviceManager;
import com.pinecone.hydra.uma.DuplexAppointServer;

public class HuskyDeviceControlTransportFactory implements Pinenut {

    public HuskyDeviceControlTransport create( DeviceManager deviceManager, DuplexAppointServer appointServer ) {
        HuskyDeviceControlTransport transport = new HuskyDeviceControlTransport( appointServer );
        transport.hookDeviceManager( deviceManager );
        return transport;
    }
}
