package com.pinecone.hydra.device.registry.server;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.device.kom.DeviceInstrument;
import com.pinecone.hydra.device.kom.entity.ElementNode;
import com.pinecone.hydra.device.registry.DeviceValidationException;
import com.pinecone.hydra.device.registry.server.connection.DeviceConnection;
import com.pinecone.hydra.device.registry.server.connection.DeviceConnectionRegistry;
import com.pinecone.hydra.device.registry.server.connection.UniformDeviceConnectionRegistry;
import com.pinecone.hydra.device.DeviceStatus;
import com.pinecone.hydra.device.registry.identity.DeviceClientIdentity;
import com.pinecone.hydra.device.registry.instruction.DeviceDeregisterInstruction;
import com.pinecone.hydra.device.registry.instruction.DeviceRegisterInstruction;
import com.pinecone.hydra.device.kom.instance.DeviceInstanceEntry;
import com.pinecone.hydra.device.kom.instance.GenericDeviceInstanceEntry;
import com.pinecone.hydra.device.registry.server.runtime.DeviceRuntimeRegistry;
import com.pinecone.hydra.device.registry.server.runtime.UniformDeviceRuntimeRegistry;

public class DeviceRuntimeService implements Pinenut {

    protected final DeviceManager mDeviceManager;

    protected final DeviceInstrument mDeviceInstrument;

    protected final GuidAllocator mGuidAllocator;

    protected final DeviceConnectionRegistry mConnectionRegistry;

    protected final DeviceRuntimeRegistry mRuntimeRegistry;

    public DeviceRuntimeService( DeviceManager deviceManager ) {
        this.mDeviceManager = deviceManager;
        this.mDeviceInstrument = deviceManager.getDeviceInstrument();
        this.mGuidAllocator = this.mDeviceInstrument.getGuidAllocator();
        this.mConnectionRegistry = new UniformDeviceConnectionRegistry();
        this.mRuntimeRegistry = new UniformDeviceRuntimeRegistry();
    }

    public synchronized DeviceInstanceEntry registerDevice( DeviceConnection connection, DeviceRegisterInstruction command ) {
        this.validateConnection( connection );
        this.validateRegisterCommand( command );

        ElementNode device = this.resolveDevice( command );
        if ( device == null ) {
            throw new DeviceValidationException( "Device element must exist before runtime registration." );
        }

        GUID deviceGuid = device.getGuid();
        long clientId = DeviceClientIdentity.fromDeviceGuid( deviceGuid );
        if ( command.getClientId() != null && command.getClientId() != clientId ) {
            throw new DeviceValidationException( "Device client id does not match device guid." );
        }

        DeviceInstanceEntry boundByConnection = this.mConnectionRegistry.queryBoundInstance( connection.getConnectionId() );
        if ( boundByConnection != null ) {
            if ( deviceGuid.equals( boundByConnection.getDeviceGuid() ) ) {
                if ( this.isRestoreRegistration( boundByConnection, command ) ) {
                    this.refreshDeviceInstanceRegistration( boundByConnection, connection, command, true );
                    this.bindRuntime( connection, boundByConnection );
                }
                return boundByConnection;
            }
            throw new DeviceValidationException( "Device connection has already bound another device instance." );
        }

        DeviceInstanceEntry active = this.mRuntimeRegistry.queryByDeviceGuid( deviceGuid );
        if ( active != null ) {
            if ( this.isRestoreRegistration( active, command ) ) {
                this.refreshDeviceInstanceRegistration( active, connection, command, true );
                this.bindRuntime( connection, active );
                return active;
            }
            throw new DeviceValidationException( "Device already has an active runtime instance." );
        }

        DeviceInstanceEntry resumed = this.resumeDeviceInstance( connection, command, deviceGuid );
        if ( resumed != null ) {
            return resumed;
        }

        GUID instanceGuid = command.getInstanceGuid() == null ? this.mGuidAllocator.nextGUID() : command.getInstanceGuid();
        DeviceInstanceEntry instance = new GenericDeviceInstanceEntry();
        instance.setInstanceGuid( instanceGuid );
        instance.setDeviceGuid( deviceGuid );
        instance.setClientId( clientId );
        instance.setConnectionId( connection.getConnectionId() );
        instance.setSessionGuid( connection.getSessionGuid() );
        instance.setTransportType( connection.getTransportType() );
        instance.setRemoteAddress( connection.getRemoteAddress() );
        instance.setEndpointProtocol( command.getEndpointProtocol() );
        instance.setEndpointHost( command.getEndpointHost() );
        instance.setEndpointPort( command.getEndpointPort() );
        instance.setEndpointPath( command.getEndpointPath() );
        instance.setEndpointAddress( command.getEndpointAddress() );
        instance.setStatus( DeviceStatus.Online );
        instance.setMetadataJson( command.getMetadataJson() );
        LocalDateTime now = LocalDateTime.now();
        instance.setRegisterTime( now );
        instance.setLastHeartbeatTime( now );
        instance.setLatestStartTime( now );
        instance.setConnectionCount( 1 );

        this.mDeviceInstrument.createDeviceInstance( instance );
        this.bindRuntime( connection, instance );
        return instance;
    }

    public synchronized void deregisterDevice( DeviceConnection connection, DeviceDeregisterInstruction command ) {
        this.validateConnection( connection );
        DeviceInstanceEntry instance = this.resolveBoundInstance( connection, command == null ? null : command.getInstanceGuid() );
        if ( instance == null ) {
            return;
        }

        instance.setStatus( DeviceStatus.Deregistered );
        instance.setStatusReason( command == null ? null : command.getReason() );
        LocalDateTime now = LocalDateTime.now();
        instance.setDeregisterTime( now );
        instance.setLatestEndTime( now );
        this.mDeviceInstrument.updateDeviceInstance( instance );
        this.unbind( instance );
    }

    public synchronized void detachConnection( DeviceConnection connection ) {
        if ( connection == null || this.isBlank( connection.getConnectionId() ) ) {
            return;
        }

        DeviceInstanceEntry instance = this.mConnectionRegistry.queryBoundInstance( connection.getConnectionId() );
        if ( instance == null ) {
            this.mConnectionRegistry.detachConnection( connection.getConnectionId() );
            return;
        }

        instance.setStatus( DeviceStatus.Offline );
        instance.setStatusReason( "Connection detached." );
        LocalDateTime now = LocalDateTime.now();
        instance.setOfflineTime( now );
        instance.setLatestEndTime( now );
        this.mDeviceInstrument.updateDeviceInstance( instance );
        this.unbind( instance );
    }

    public synchronized void detachConnectionByClientId( long clientId, String reason ) {
        DeviceInstanceEntry instance = this.mRuntimeRegistry.queryByClientId( clientId );
        if ( instance == null ) {
            return;
        }

        instance.setStatus( DeviceStatus.Offline );
        instance.setStatusReason( this.isBlank( reason ) ? "Connection detached." : reason );
        LocalDateTime now = LocalDateTime.now();
        instance.setOfflineTime( now );
        instance.setLatestEndTime( now );
        this.mDeviceInstrument.updateDeviceInstance( instance );
        this.unbind( instance );
    }

    public DeviceInstanceEntry queryDeviceRuntime( GUID deviceGuid ) {
        return this.mRuntimeRegistry.queryByDeviceGuid( deviceGuid );
    }

    public DeviceInstanceEntry queryDeviceInstance( GUID instanceGuid ) {
        return this.mRuntimeRegistry.queryByInstanceGuid( instanceGuid );
    }

    public DeviceInstanceEntry queryDeviceRuntimeByClientId( long clientId ) {
        return this.mRuntimeRegistry.queryByClientId( clientId );
    }

    public Collection<DeviceInstanceEntry> fetchDeviceRuntimes() {
        return this.mRuntimeRegistry.fetchInstances();
    }

    protected DeviceInstanceEntry resolveBoundInstance( DeviceConnection connection, GUID instanceGuid ) {
        DeviceInstanceEntry instance = this.mConnectionRegistry.queryBoundInstance( connection.getConnectionId() );
        if ( instance == null ) {
            return null;
        }
        if ( instanceGuid != null && !instanceGuid.equals( instance.getInstanceGuid() ) ) {
            throw new DeviceValidationException( "Device instance does not belong to current connection." );
        }
        return instance;
    }

    protected void unbind( DeviceInstanceEntry instance ) {
        this.mRuntimeRegistry.unbindByInstanceGuid( instance.getInstanceGuid() );
        this.mConnectionRegistry.unbindInstance( instance.getConnectionId() );
        this.mConnectionRegistry.detachConnection( instance.getConnectionId() );
    }

    protected DeviceInstanceEntry resumeDeviceInstance(
            DeviceConnection connection,
            DeviceRegisterInstruction command,
            GUID deviceGuid
    ) {
        if ( command.getInstanceGuid() == null ) {
            return null;
        }

        DeviceInstanceEntry entry = this.mDeviceInstrument.queryDeviceInstance( command.getInstanceGuid() );
        if ( entry == null ) {
            return null;
        }
        if ( !Objects.equals( entry.getDeviceGuid(), deviceGuid ) ) {
            throw new DeviceValidationException( "Device instance does not belong to requested device." );
        }

        this.refreshDeviceInstanceRegistration( entry, connection, command, true );
        this.bindRuntime( connection, entry );
        return entry;
    }

    protected void refreshDeviceInstanceRegistration(
            DeviceInstanceEntry entry,
            DeviceConnection connection,
            DeviceRegisterInstruction command,
            boolean increaseConnectionCount
    ) {
        LocalDateTime now = LocalDateTime.now();
        entry.setClientId( command.getClientId() == null ? entry.getClientId() : command.getClientId() );
        entry.setConnectionId( connection.getConnectionId() );
        entry.setSessionGuid( connection.getSessionGuid() );
        entry.setTransportType( connection.getTransportType() );
        entry.setRemoteAddress( connection.getRemoteAddress() );
        entry.setEndpointProtocol( command.getEndpointProtocol() );
        entry.setEndpointHost( command.getEndpointHost() );
        entry.setEndpointPort( command.getEndpointPort() );
        entry.setEndpointPath( command.getEndpointPath() );
        entry.setEndpointAddress( command.getEndpointAddress() );
        entry.setStatus( DeviceStatus.Online );
        entry.setStatusReason( null );
        entry.setLastHeartbeatTime( now );
        entry.setLatestStartTime( now );
        if ( increaseConnectionCount ) {
            entry.setConnectionCount( entry.getConnectionCount() + 1 );
        }
        entry.setMetadataJson( command.getMetadataJson() );
        this.mDeviceInstrument.updateDeviceInstance( entry );
    }

    protected boolean isRestoreRegistration( DeviceInstanceEntry active, DeviceRegisterInstruction command ) {
        return command.getInstanceGuid() != null && Objects.equals( active.getInstanceGuid(), command.getInstanceGuid() );
    }

    protected void bindRuntime( DeviceConnection connection, DeviceInstanceEntry instance ) {
        String previousConnectionId = this.mConnectionRegistry.queryBoundConnectionId( instance.getInstanceGuid() );
        if ( previousConnectionId != null && !Objects.equals( previousConnectionId, connection.getConnectionId() ) ) {
            this.mConnectionRegistry.unbindInstance( previousConnectionId );
            this.mConnectionRegistry.detachConnection( previousConnectionId );
        }

        this.mConnectionRegistry.bindConnection( connection );
        this.mRuntimeRegistry.bind( instance );
        this.mConnectionRegistry.bindInstance( connection.getConnectionId(), instance );
    }

    protected ElementNode resolveDevice( DeviceRegisterInstruction command ) {
        if ( command.getDeviceGuid() != null ) {
            return this.mDeviceManager.queryDeviceByGuid( command.getDeviceGuid() );
        }
        if ( !this.isBlank( command.getDeviceGuidText() ) ) {
            return this.mDeviceManager.queryDeviceByGuid( this.mGuidAllocator.parse( command.getDeviceGuidText() ) );
        }
        if ( !this.isBlank( command.getDevicePath() ) ) {
            return this.mDeviceManager.queryDeviceByPath( command.getDevicePath() );
        }
        return null;
    }

    protected void validateConnection( DeviceConnection connection ) {
        if ( connection == null || this.isBlank( connection.getConnectionId() ) ) {
            throw new DeviceValidationException( "Device connection is required." );
        }
    }

    protected void validateRegisterCommand( DeviceRegisterInstruction command ) {
        if ( command == null ) {
            throw new DeviceValidationException( "Device register command is required." );
        }
        if ( command.getDeviceGuid() == null && this.isBlank( command.getDeviceGuidText() ) && this.isBlank( command.getDevicePath() ) ) {
            throw new DeviceValidationException( "Device guid or path is required." );
        }
    }

    protected boolean isBlank( String value ) {
        return value == null || value.trim().isEmpty();
    }
}
