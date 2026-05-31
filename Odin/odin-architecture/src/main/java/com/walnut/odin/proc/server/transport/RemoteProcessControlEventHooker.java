package com.walnut.odin.proc.server.transport;

import com.pinecone.framework.system.prototype.Pinenut;

public interface RemoteProcessControlEventHooker extends Pinenut {

    default void onTransportHooked( RemoteProcessControlTransport transport ) {

    }

    default void onAdditionalServiceRegistered( RemoteProcessControlTransport transport, Object service ) {

    }

    void onClientInitialized( RemoteProcessControlTransport transport, long clientId );

    void onClientDetached( RemoteProcessControlTransport transport, long clientId );

}
