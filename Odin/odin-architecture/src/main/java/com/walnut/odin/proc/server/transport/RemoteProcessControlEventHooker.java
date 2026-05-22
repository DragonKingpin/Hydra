package com.walnut.odin.proc.server.transport;

import com.pinecone.framework.system.prototype.Pinenut;

public interface RemoteProcessControlEventHooker extends Pinenut {

    void onClientInitialized( RemoteProcessControlTransport transport, long clientId );

    void onClientDetached( RemoteProcessControlTransport transport, long clientId );

}
