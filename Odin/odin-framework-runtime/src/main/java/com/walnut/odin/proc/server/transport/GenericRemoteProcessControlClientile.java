package com.walnut.odin.proc.server.transport;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.walnut.odin.proc.server.transport.RemoteProcessControlClientile;
import com.walnut.odin.proc.server.transport.RemoteProcessControlSession;
import com.walnut.odin.proc.server.transport.RemoteProcessControlTransport;

public class GenericRemoteProcessControlClientile implements RemoteProcessControlClientile {

    protected long                                               mnClientId;

    protected RemoteProcessControlTransport                      mTransport;

    protected Map<RemoteProcessControlSession, RemoteProcessControlSession> mSessionMap;

    public GenericRemoteProcessControlClientile( long clientId, RemoteProcessControlTransport transport ) {
        this.mnClientId   = clientId;
        this.mTransport   = transport;
        this.mSessionMap  = new ConcurrentHashMap<>();
    }

    @Override
    public long clientId() {
        return this.mnClientId;
    }

    @Override
    public RemoteProcessControlTransport transport() {
        return this.mTransport;
    }

    @Override
    public Collection<RemoteProcessControlSession> sessions() {
        return Collections.unmodifiableCollection( this.mSessionMap.values() );
    }

    @Override
    public void attachSession( RemoteProcessControlSession session ) {
        this.mSessionMap.put( session, session );
    }

    @Override
    public void detachSession( RemoteProcessControlSession session ) {
        this.mSessionMap.remove( session );
    }

    @Override
    public boolean isActive() {
        for ( RemoteProcessControlSession session : this.mSessionMap.values() ) {
            if ( session.isActive() ) {
                return true;
            }
        }
        return false;
    }

}
