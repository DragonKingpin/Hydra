package com.pinecone.hydra.device.registry.server;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.device.kom.DeviceInstrument;
import com.pinecone.hydra.device.kom.entity.ElementNode;
import com.pinecone.hydra.device.registry.DeviceControlRPCException;
import com.pinecone.hydra.device.registry.DeviceValidationException;
import com.pinecone.hydra.device.registry.appoint.DeviceAppointServer;
import com.pinecone.hydra.device.registry.dto.DeviceRegistrationDTO;
import com.pinecone.hydra.system.component.LogStatuses;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;

public class UniformDeviceManager implements DeviceManager {

    protected final DeviceInstrument mDeviceInstrument;

    protected final ConcurrentMap<Long, DeviceAppointServer> mServerPoolMap;

    protected final DeviceLifecycleService mDeviceLifecycleService;

    protected final DeviceMetaService mDeviceMetaService;

    protected final DeviceTopologyService mDeviceTopologyService;

    protected final Logger mLogger;

    public UniformDeviceManager( DeviceInstrument deviceInstrument ) {
        if ( deviceInstrument == null ) {
            throw new DeviceValidationException( "DeviceInstrument is required." );
        }

        this.mDeviceInstrument = deviceInstrument;
        this.mServerPoolMap = new ConcurrentHashMap<>();
        this.mDeviceLifecycleService = new DeviceLifecycleService( this );
        this.mDeviceMetaService = new DeviceMetaService( this );
        this.mDeviceTopologyService = new DeviceTopologyService( this );
        this.mLogger = LoggerFactory.getLogger( this.getClass() );
    }

    @Override
    public Logger getLogger() {
        return this.mLogger;
    }

    @Override
    public DeviceInstrument getDeviceInstrument() {
        return this.mDeviceInstrument;
    }

    @Override
    public Collection<DeviceAppointServer> getServers() {
        return this.mServerPoolMap.values();
    }

    @Override
    public DeviceManager addAppointServer( DeviceAppointServer appointServer ) {
        this.mServerPoolMap.put( appointServer.getMessageNodeId(), appointServer );
        return this;
    }

    @Override
    public DeviceManager hookAppointServer( DeviceAppointServer appointServer ) {
        this.addAppointServer( appointServer );
        appointServer.hookDeviceManager( this );
        return this;
    }

    @Override
    public DeviceManager vitalizeAppointServer( DeviceAppointServer appointServer ) throws DeviceControlRPCException {
        try {
            this.hookAppointServer( appointServer );
            appointServer.execute();
            return this;
        }
        catch ( Exception e ) {
            throw new DeviceControlRPCException( e );
        }
    }

    @Override
    public DeviceAppointServer getAppointServerById( Long appointNodeId ) {
        return this.mServerPoolMap.get( appointNodeId );
    }

    @Override
    public DeviceAppointServer evictAppointServerById( Long appointNodeId ) {
        DeviceAppointServer legacy = this.mServerPoolMap.remove( appointNodeId );
        if ( legacy != null ) {
            legacy.close();
        }
        return legacy;
    }

    @Override
    public int serverSize() {
        return this.mServerPoolMap.size();
    }

    @Override
    public void startDeviceManager() throws DeviceControlRPCException {
        try {
            for ( Map.Entry<Long, DeviceAppointServer> entry : this.mServerPoolMap.entrySet() ) {
                if ( !entry.getValue().isStarted() ) {
                    entry.getValue().execute();
                }
            }
            this.infoLifecycle( "Device Manager RPC Subsystem Vitalization", LogStatuses.StatusDone );
        }
        catch ( Exception e ) {
            throw new DeviceControlRPCException( e );
        }
    }

    @Override
    public synchronized GUID enrollDevice( DeviceRegistrationDTO registrationDTO ) {
        this.validateRegistration( registrationDTO );
        ElementNode node = this.resolveElement( registrationDTO );
        if ( node == null ) {
            throw new DeviceValidationException( "Device element must exist before enroll." );
        }

        if ( registrationDTO.getMeta() != null ) {
            registrationDTO.getMeta().applyTo( node, this.mDeviceInstrument.getGuidAllocator() );
            this.mDeviceInstrument.update( node );
        }
        return node.getGuid();
    }

    @Override
    public ElementNode queryDeviceByPath( String path ) {
        return this.mDeviceInstrument.queryElement( path );
    }

    @Override
    public ElementNode queryDeviceByGuid( GUID guid ) {
        if ( guid == null ) {
            return null;
        }
        TreeNode node = this.mDeviceInstrument.get( guid );
        return node instanceof ElementNode ? (ElementNode) node : null;
    }

    @Override
    public void updateDevice( ElementNode elementNode ) {
        if ( elementNode == null ) {
            throw new DeviceValidationException( "Device element is required." );
        }
        this.mDeviceInstrument.update( elementNode );
    }

    @Override
    public void removeDevice( GUID guid ) {
        if ( guid != null ) {
            this.mDeviceInstrument.remove( guid );
        }
    }

    @Override
    public DeviceLifecycleService deviceLifecycleService() {
        return this.mDeviceLifecycleService;
    }

    @Override
    public DeviceMetaService deviceMetaService() {
        return this.mDeviceMetaService;
    }

    @Override
    public DeviceTopologyService deviceTopologyService() {
        return this.mDeviceTopologyService;
    }

    protected void validateRegistration( DeviceRegistrationDTO registrationDTO ) {
        if ( registrationDTO == null ) {
            throw new DeviceValidationException( "Device registration is required." );
        }
        if ( isBlank( registrationDTO.getGuid() ) && isBlank( registrationDTO.getPath() ) ) {
            throw new DeviceValidationException( "Device guid or path is required." );
        }
    }

    protected ElementNode resolveElement( DeviceRegistrationDTO registrationDTO ) {
        if ( !isBlank( registrationDTO.getGuid() ) ) {
            return this.queryDeviceByGuid( this.mDeviceInstrument.getGuidAllocator().parse( registrationDTO.getGuid() ) );
        }
        if ( !isBlank( registrationDTO.getPath() ) ) {
            return this.queryDeviceByPath( registrationDTO.getPath() );
        }
        return null;
    }

    protected boolean isBlank( String value ) {
        return value == null || value.trim().isEmpty();
    }
}
