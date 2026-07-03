package com.pinecone.hydra.service.registry.client.transport;

import com.pinecone.framework.system.prototype.Pinenut;

public class ServiceClientTransportSyncReasons implements Pinenut {

    public static final String Startup = "Startup";

    public static final String StreamConnected = "StreamConnected";

    public static final String StreamRecovered = "StreamRecovered";

    public static final String StreamError = "StreamError";

    public static final String RegisterRejected = "RegisterRejected";

    protected ServiceClientTransportSyncReasons() {
    }

}
