package com.pinecone.hydra.service.registry.server.transport;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.pinecone.hydra.service.registry.ServiceControlRPCException;
import com.pinecone.hydra.service.registry.server.transport.entity.ServiceTransportHandle;

public class UniformServiceControlTransportRegistry implements ServiceControlTransportRegistry {

    protected Map<Long, ServiceTransportHandle>      mClientTransportMap;

    protected Map<String, ServiceControlTransport>   mTransportMap;

    public UniformServiceControlTransportRegistry() {
        this.mClientTransportMap = new ConcurrentHashMap<>();
        this.mTransportMap       = new ConcurrentHashMap<>();
    }

    protected String makeTransportKey( ServiceControlTransport transport ) {
        return transport.transportType().name() + "@" + System.identityHashCode( transport );
    }

    @Override
    public ServiceControlTransportRegistry hookTransport( ServiceControlTransport transport ) {
        if ( transport == null ) {
            return this;
        }

        this.mTransportMap.put( this.makeTransportKey( transport ), transport );
        return this;
    }

    @Override
    public ServiceControlTransport queryTransport( long nClientId ) {
        ServiceTransportHandle handle = this.queryTransportHandle( nClientId );
        if ( handle != null ) {
            return handle.getTransport();
        }
        if ( this.mTransportMap.size() == 1 ) {
            return this.mTransportMap.values().iterator().next();
        }
        return null;
    }

    @Override
    public ServiceTransportHandle queryTransportHandle( long nClientId ) {
        ServiceTransportHandle handle = this.mClientTransportMap.get( nClientId );
        if ( handle != null ) {
            return handle;
        }

        for ( ServiceControlTransport candidate : this.mTransportMap.values() ) {
            if ( candidate.containsClient( nClientId ) ) {
                this.bindClient( nClientId, candidate );
                return this.mClientTransportMap.get( nClientId );
            }
        }
        return null;
    }

    @Override
    public Collection<ServiceTransportHandle> transportHandles() {
        return Collections.unmodifiableCollection( this.mClientTransportMap.values() );
    }

    @Override
    public ServiceControlTransport requireTransport( long nClientId ) throws ServiceControlRPCException {
        ServiceControlTransport transport = this.queryTransport( nClientId );
        if ( transport == null ) {
            throw new ServiceControlRPCException( "No service control transport for client `" + nClientId + "`." );
        }
        return transport;
    }

    @Override
    public Collection<ServiceControlTransport> transports() {
        return Collections.unmodifiableCollection( this.mTransportMap.values() );
    }

    @Override
    public void bindClient( long nClientId, ServiceControlTransport transport ) {
        if ( transport == null ) {
            this.detachClient( nClientId );
            return;
        }

        this.mClientTransportMap.compute( nClientId, ( key, handle ) -> {
            if ( handle == null ) {
                handle = new ServiceTransportHandle();
                handle.setClientId( nClientId );
                handle.setRegisterTimeMillis( System.currentTimeMillis() );
            }
            handle.setTransport( transport );
            return handle;
        } );
    }

    @Override
    public void detachClient( long nClientId ) {
        this.mClientTransportMap.remove( nClientId );
    }

    @Override
    public boolean hasClient( long nClientId ) {
        ServiceTransportHandle handle = this.mClientTransportMap.get( nClientId );
        ServiceControlTransport transport = null;
        if ( handle != null ) {
            transport = handle.getTransport();
        }
        if ( transport != null && transport.containsClient( nClientId ) ) {
            return true;
        }

        for ( ServiceControlTransport candidate : this.mTransportMap.values() ) {
            if ( candidate.containsClient( nClientId ) ) {
                this.bindClient( nClientId, candidate );
                return true;
            }
        }
        return false;
    }

}
