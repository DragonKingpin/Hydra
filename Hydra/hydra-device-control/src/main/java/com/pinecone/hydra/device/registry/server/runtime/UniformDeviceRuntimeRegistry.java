package com.pinecone.hydra.device.registry.server.runtime;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.device.kom.instance.DeviceInstanceEntry;
import com.pinecone.hydra.device.registry.DeviceValidationException;

public class UniformDeviceRuntimeRegistry implements DeviceRuntimeRegistry {

    protected final ConcurrentMap<GUID, DeviceInstanceEntry> deviceInstances = new ConcurrentHashMap<>();

    protected final ConcurrentMap<GUID, DeviceInstanceEntry> instances = new ConcurrentHashMap<>();

    protected final ConcurrentMap<Long, DeviceInstanceEntry> clientInstances = new ConcurrentHashMap<>();

    @Override
    public DeviceInstanceEntry bind( DeviceInstanceEntry instance ) {
        if ( instance == null || instance.getDeviceGuid() == null || instance.getInstanceGuid() == null ) {
            throw new DeviceValidationException( "Device instance identity is required." );
        }
        DeviceInstanceEntry active = this.deviceInstances.putIfAbsent( instance.getDeviceGuid(), instance );
        if ( active != null ) {
            if ( !instance.getInstanceGuid().equals( active.getInstanceGuid() ) ) {
                throw new DeviceValidationException( "Device already has an active runtime instance." );
            }
            this.deviceInstances.put( instance.getDeviceGuid(), instance );
            this.instances.put( instance.getInstanceGuid(), instance );
            this.clientInstances.put( instance.getClientId(), instance );
            return instance;
        }

        DeviceInstanceEntry activeInstance = this.instances.putIfAbsent( instance.getInstanceGuid(), instance );
        if ( activeInstance != null ) {
            if ( instance.getDeviceGuid().equals( activeInstance.getDeviceGuid() ) ) {
                this.deviceInstances.put( instance.getDeviceGuid(), instance );
                this.instances.put( instance.getInstanceGuid(), instance );
                this.clientInstances.put( instance.getClientId(), instance );
                return instance;
            }
            this.deviceInstances.remove( instance.getDeviceGuid() );
            throw new DeviceValidationException( "Device instance guid has already been registered." );
        }
        this.clientInstances.put( instance.getClientId(), instance );
        return instance;
    }

    @Override
    public DeviceInstanceEntry unbindByDeviceGuid( GUID deviceGuid ) {
        DeviceInstanceEntry instance = this.deviceInstances.remove( deviceGuid );
        if ( instance != null ) {
            this.instances.remove( instance.getInstanceGuid() );
            this.clientInstances.remove( instance.getClientId() );
        }
        return instance;
    }

    @Override
    public DeviceInstanceEntry unbindByInstanceGuid( GUID instanceGuid ) {
        DeviceInstanceEntry instance = this.instances.remove( instanceGuid );
        if ( instance != null ) {
            this.deviceInstances.remove( instance.getDeviceGuid() );
            this.clientInstances.remove( instance.getClientId() );
        }
        return instance;
    }

    @Override
    public DeviceInstanceEntry queryByDeviceGuid( GUID deviceGuid ) {
        return this.deviceInstances.get( deviceGuid );
    }

    @Override
    public DeviceInstanceEntry queryByInstanceGuid( GUID instanceGuid ) {
        return this.instances.get( instanceGuid );
    }

    @Override
    public DeviceInstanceEntry queryByClientId( long clientId ) {
        return this.clientInstances.get( clientId );
    }

    @Override
    public Collection<DeviceInstanceEntry> fetchInstances() {
        return new ArrayList<>( this.instances.values() );
    }

    @Override
    public boolean containsDevice( GUID deviceGuid ) {
        return this.deviceInstances.containsKey( deviceGuid );
    }

    @Override
    public boolean containsInstance( GUID instanceGuid ) {
        return this.instances.containsKey( instanceGuid );
    }
}
