package com.pinecone.hydra.device.registry.client.transport;

import com.pinecone.framework.system.prototype.Pinenut;

public class DeviceClientTransportSyncReasons implements Pinenut {

    public static final String Startup = "Startup";

    public static final String StreamConnected = "StreamConnected";

    public static final String StreamRecovered = "StreamRecovered";

    public static final String StreamError = "StreamError";

    public static final String RegisterRejected = "RegisterRejected";

    protected DeviceClientTransportSyncReasons() {
    }
}
