package com.pinecone.hydra.device.registry.server.connection;

import com.pinecone.framework.system.prototype.Pinenut;

public class DeviceConnectionContext implements Pinenut {

    protected DeviceConnection connection;

    public DeviceConnection getConnection() {
        return this.connection;
    }

    public void setConnection( DeviceConnection connection ) {
        this.connection = connection;
    }
}
