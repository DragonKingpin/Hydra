package com.walnut.odin.proc.server.transport;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.walnut.odin.proc.RemoteProcessServiceRPCException;
import com.walnut.odin.proc.server.transport.entity.TransportHandle;

public class GenericRemoteProcessControlTransportRegistry implements RemoteProcessControlTransportRegistry {

    protected Map<Long, TransportHandle>                                            mClientTransportMap;

    protected Map<String, RemoteProcessControlTransport>                            mTransportMap;

    public GenericRemoteProcessControlTransportRegistry() {
        this.mClientTransportMap = new ConcurrentHashMap<>();
        this.mTransportMap       = new ConcurrentHashMap<>();
    }

    protected String makeTransportKey( RemoteProcessControlTransport transport ) {
        return transport.transportType().name() + "@" + System.identityHashCode( transport );
    }

    @Override
    public RemoteProcessControlTransportRegistry hookTransport( RemoteProcessControlTransport transport ) {
        if ( transport == null ) {
            return this;
        }

        this.mTransportMap.put( this.makeTransportKey( transport ), transport );
        return this;
    }

    @Override
    public RemoteProcessControlTransport queryTransport( long clientId ) {
        TransportHandle handle = this.queryTransportHandle( clientId );
        if ( handle != null ) {
            return handle.getTransport();
        }
        if ( this.mTransportMap.size() == 1 ) {
            return this.mTransportMap.values().iterator().next();
        }
        return null;
    }

    @Override
    public TransportHandle queryTransportHandle( long clientId ) {
        TransportHandle handle = this.mClientTransportMap.get( clientId );
        if ( handle != null ) {
            return handle;
        }
        for ( RemoteProcessControlTransport candidate : this.mTransportMap.values() ) {
            if ( candidate.containsClient( clientId ) ) {
                this.bindClient( clientId, candidate );
                return this.mClientTransportMap.get( clientId );
            }
        }
        return null;
    }

    @Override
    public Collection<TransportHandle> transportHandles() {
        return Collections.unmodifiableCollection( this.mClientTransportMap.values() );
    }

    @Override
    public RemoteProcessControlTransport requireTransport( long clientId ) throws RemoteProcessServiceRPCException {
        RemoteProcessControlTransport transport = this.queryTransport( clientId );
        if ( transport == null ) {
            throw new RemoteProcessServiceRPCException( "No remote process control transport for client `" + clientId + "`." );
        }
        return transport;
    }

    @Override
    public Collection<RemoteProcessControlTransport> transports() {
        return Collections.unmodifiableCollection( this.mTransportMap.values() );
    }

    @Override
    public void bindClient( long clientId, RemoteProcessControlTransport transport ) {
        if ( transport == null ) {
            this.detachClient( clientId );
            return;
        }
        this.mClientTransportMap.compute( clientId, ( key, handle ) -> {
            if ( handle == null ) {
                handle = new TransportHandle();
                handle.setClientId( clientId );
                handle.setRegisterTimeMillis( System.currentTimeMillis() );
            }
            handle.setTransport( transport );
            return handle;
        } );
    }

    @Override
    public void detachClient( long clientId ) {
        this.mClientTransportMap.remove( clientId );
    }

    @Override
    public boolean hasClient( long clientId ) {
        TransportHandle handle = this.mClientTransportMap.get( clientId );
        RemoteProcessControlTransport transport = handle == null ? null : handle.getTransport();
        if ( transport != null && transport.containsClient( clientId ) ) {
            return true;
        }

        for ( RemoteProcessControlTransport candidate : this.mTransportMap.values() ) {
            if ( candidate.containsClient( clientId ) ) {
                this.bindClient( clientId, candidate );
                return true;
            }
        }

        return false;
    }

}
