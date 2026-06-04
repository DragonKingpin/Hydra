package com.pinecone.hydra.device.registry.server.connection;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.device.kom.instance.DeviceInstanceEntry;

public class UniformDeviceConnectionRegistry implements DeviceConnectionRegistry {

    protected final ConcurrentMap<String, DeviceConnection> connections = new ConcurrentHashMap<>();

    protected final ConcurrentMap<String, DeviceInstanceEntry> connectionInstances = new ConcurrentHashMap<>();

    protected final ConcurrentMap<GUID, String> instanceConnections = new ConcurrentHashMap<>();

    @Override
    public void bindConnection( DeviceConnection connection ) {
        this.connections.put( connection.getConnectionId(), connection );
    }

    @Override
    public DeviceConnection detachConnection( String connectionId ) {
        this.unbindInstance( connectionId );
        return this.connections.remove( connectionId );
    }

    @Override
    public void bindInstance( String connectionId, DeviceInstanceEntry instance ) {
        this.connectionInstances.put( connectionId, instance );
        this.instanceConnections.put( instance.getInstanceGuid(), connectionId );
    }

    @Override
    public DeviceInstanceEntry unbindInstance( String connectionId ) {
        DeviceInstanceEntry instance = this.connectionInstances.remove( connectionId );
        if ( instance != null ) {
            this.instanceConnections.remove( instance.getInstanceGuid() );
        }
        return instance;
    }

    @Override
    public DeviceConnection queryConnection( String connectionId ) {
        return this.connections.get( connectionId );
    }

    @Override
    public DeviceInstanceEntry queryBoundInstance( String connectionId ) {
        return this.connectionInstances.get( connectionId );
    }

    @Override
    public String queryBoundConnectionId( GUID instanceGuid ) {
        return this.instanceConnections.get( instanceGuid );
    }
}
