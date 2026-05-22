package com.walnut.odin.proc.server.transport;

import java.util.Collection;

import com.pinecone.framework.system.prototype.Pinenut;

public interface RemoteProcessControlClientile extends Pinenut {

    long clientId();

    RemoteProcessControlTransport transport();

    Collection<RemoteProcessControlSession> sessions();

    void attachSession( RemoteProcessControlSession session );

    void detachSession( RemoteProcessControlSession session );

    boolean isActive();

}
