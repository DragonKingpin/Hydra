package com.walnut.odin.proc.server.transport;

import java.util.Collection;

import com.pinecone.framework.system.prototype.Pinenut;
import com.walnut.odin.proc.RemoteProcessServiceRPCException;
import com.walnut.odin.proc.server.transport.entity.TransportHandle;

public interface RemoteProcessControlTransportRegistry extends Pinenut {

    RemoteProcessControlTransportRegistry hookTransport( RemoteProcessControlTransport transport );

    RemoteProcessControlTransport queryTransport( long clientId );

    TransportHandle queryTransportHandle( long clientId );

    Collection<TransportHandle> transportHandles();

    RemoteProcessControlTransport requireTransport( long clientId ) throws RemoteProcessServiceRPCException;

    Collection<RemoteProcessControlTransport> transports();

    void bindClient( long clientId, RemoteProcessControlTransport transport );

    void detachClient( long clientId );

    boolean hasClient( long clientId );

}
