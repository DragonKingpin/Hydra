package com.walnut.odin.proc.server.transport;

import com.pinecone.framework.system.prototype.Pinenut;

public interface RemoteProcessControlSession extends Pinenut {

    long clientId();

    RemoteProcessControlTransportType transportType();

    boolean isActive();

}
