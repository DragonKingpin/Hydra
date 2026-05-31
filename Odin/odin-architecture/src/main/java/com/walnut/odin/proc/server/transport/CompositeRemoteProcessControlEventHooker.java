package com.walnut.odin.proc.server.transport;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class CompositeRemoteProcessControlEventHooker implements RemoteProcessControlEventHooker {

    protected Set<RemoteProcessControlEventHooker> mHookerSet;

    public CompositeRemoteProcessControlEventHooker() {
        this.mHookerSet = ConcurrentHashMap.newKeySet();
    }

    public CompositeRemoteProcessControlEventHooker addHooker( RemoteProcessControlEventHooker hooker ) {
        if ( hooker == null || hooker == this ) {
            return this;
        }
        this.mHookerSet.add( hooker );
        return this;
    }

    public Collection<RemoteProcessControlEventHooker> hookers() {
        return Collections.unmodifiableCollection( this.mHookerSet );
    }

    @Override
    public void onTransportHooked( RemoteProcessControlTransport transport ) {
        for ( RemoteProcessControlEventHooker hooker : this.mHookerSet ) {
            hooker.onTransportHooked( transport );
        }
    }

    @Override
    public void onAdditionalServiceRegistered( RemoteProcessControlTransport transport, Object service ) {
        for ( RemoteProcessControlEventHooker hooker : this.mHookerSet ) {
            hooker.onAdditionalServiceRegistered( transport, service );
        }
    }

    @Override
    public void onClientInitialized( RemoteProcessControlTransport transport, long clientId ) {
        for ( RemoteProcessControlEventHooker hooker : this.mHookerSet ) {
            hooker.onClientInitialized( transport, clientId );
        }
    }

    @Override
    public void onClientDetached( RemoteProcessControlTransport transport, long clientId ) {
        for ( RemoteProcessControlEventHooker hooker : this.mHookerSet ) {
            hooker.onClientDetached( transport, clientId );
        }
    }

}
