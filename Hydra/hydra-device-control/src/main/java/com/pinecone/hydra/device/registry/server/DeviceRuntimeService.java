package com.pinecone.hydra.device.registry.server;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Locale;
import java.util.Objects;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.device.kom.DeviceInstrument;
import com.pinecone.hydra.device.kom.entity.DeviceNodeOwnershipEntry;
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
import com.pinecone.hydra.device.registry.server.detached.DeviceDetachedObservationEntry;
import com.pinecone.hydra.device.registry.server.detached.DeviceDetachedObservationRegistry;
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
        this.assertMajorDevice( deviceGuid );
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
                    this.ensureOwnedDeviceInstances( boundByConnection );
                    this.syncOwnedDeviceInstancesStatus( boundByConnection, DeviceStatus.Online, null );
                }
                else if ( command.getInstanceGuid() != null ) {
                    throw new DeviceValidationException( "Device connection has already bound another device instance." );
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
                this.recoverDetachedDeviceInstance( active );
                this.ensureOwnedDeviceInstances( active );
                this.syncOwnedDeviceInstancesStatus( active, DeviceStatus.Online, null );
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
        this.markDeviceStatus( instance.getDeviceGuid(), DeviceStatus.Online );
        this.createOwnedDeviceInstances( instance );
        return instance;
    }

    public synchronized void deregisterDevice( DeviceConnection connection, DeviceDeregisterInstruction command ) {
        this.validateConnection( connection );
        DeviceInstanceEntry instance = this.resolveBoundInstance( connection, command == null ? null : command.getInstanceGuid() );
        if ( instance == null ) {
            return;
        }

        this.removeDetachedObservation( instance );
        this.markInstanceStatus( instance, DeviceStatus.Deregistered, command == null ? null : command.getReason() );
        LocalDateTime now = LocalDateTime.now();
        instance.setDeregisterTime( now );
        instance.setLatestEndTime( now );
        this.mDeviceInstrument.updateDeviceInstance( instance );
        this.markDeviceStatus( instance.getDeviceGuid(), DeviceStatus.Deregistered );
        this.syncOwnedDeviceInstancesStatus( instance, DeviceStatus.Deregistered, command == null ? null : command.getReason() );
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

        this.markConnectionDetached( instance, "Connection detached." );
    }

    public synchronized void detachConnectionByClientId( long clientId, String reason ) {
        DeviceInstanceEntry instance = this.mRuntimeRegistry.queryByClientId( clientId );
        if ( instance == null ) {
            return;
        }

        this.markConnectionDetached( instance, this.isBlank( reason ) ? "Connection detached." : reason );
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
        if ( !this.canResumeDeviceInstance( entry ) ) {
            throw new DeviceValidationException( "Device instance status cannot resume runtime registration." );
        }

        this.refreshDeviceInstanceRegistration( entry, connection, command, true );
        this.bindRuntime( connection, entry );
        this.recoverDetachedDeviceInstance( entry );
        this.ensureOwnedDeviceInstances( entry );
        this.syncOwnedDeviceInstancesStatus( entry, DeviceStatus.Online, null );
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
        this.markDeviceStatus( entry.getDeviceGuid(), DeviceStatus.Online );
    }

    protected boolean isRestoreRegistration( DeviceInstanceEntry active, DeviceRegisterInstruction command ) {
        return command.getInstanceGuid() != null && Objects.equals( active.getInstanceGuid(), command.getInstanceGuid() );
    }

    protected boolean canResumeDeviceInstance( DeviceInstanceEntry entry ) {
        if ( entry == null || entry.getStatus() == null ) {
            return false;
        }

        return entry.getStatus() == DeviceStatus.Online
                || entry.getStatus() == DeviceStatus.Detached
                || entry.getStatus() == DeviceStatus.Suspect;
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

    public synchronized void settleDetachedDeviceInstance( DeviceDetachedObservationEntry entry ) {
        if ( entry == null || entry.getClientId() == null ) {
            return;
        }

        DeviceDetachedObservationRegistry registry = this.detachedObservationRegistry();
        if ( registry == null || registry.remove( entry.getClientId() ) == null ) {
            return;
        }

        DeviceInstanceEntry instance = this.mRuntimeRegistry.queryByClientId( entry.getClientId() );
        if ( instance == null || !Objects.equals( instance.getInstanceGuid(), entry.getInstanceGuid() ) ) {
            return;
        }

        DeviceStatus status = this.resolveDetachedMissingStatus();
        this.markInstanceStatus( instance, status, "Detached grace expired." );
        this.mDeviceInstrument.updateDeviceInstance( instance );
        this.markDeviceStatus( instance.getDeviceGuid(), status );
        this.syncOwnedDeviceInstancesStatus( instance, status, "Detached grace expired." );
        this.unbind( instance );
        this.mDeviceManager.getLogger().info(
                "Detached device instance settled, { clientId: {}, instanceId: {}, deviceId: {}, status: {} }. <{}>",
                new Object[]{
                        entry.getClientId(),
                        entry.getInstanceGuid(),
                        entry.getDeviceGuid(),
                        status.getName(),
                        status.getName()
                }
        );
    }

    protected void markConnectionDetached( DeviceInstanceEntry instance, String reason ) {
        if ( instance == null ) {
            return;
        }

        if ( !this.mDeviceManager.detachedObservationConfig().isEnable() ) {
            this.markInstanceStatus( instance, DeviceStatus.Offline, reason );
            this.mDeviceInstrument.updateDeviceInstance( instance );
            this.markDeviceStatus( instance.getDeviceGuid(), DeviceStatus.Offline );
            this.syncOwnedDeviceInstancesStatus( instance, DeviceStatus.Offline, reason );
            this.unbind( instance );
            return;
        }

        long nowMillis = System.currentTimeMillis();
        DeviceDetachedObservationRegistry registry = this.detachedObservationRegistry();
        if ( registry != null ) {
            registry.put(
                    new DeviceDetachedObservationEntry(
                            instance.getClientId(),
                            instance.getInstanceGuid(),
                            instance.getDeviceGuid(),
                            instance.getConnectionId(),
                            nowMillis,
                            nowMillis + this.mDeviceManager.detachedObservationConfig().getGraceMillis(),
                            reason
                    )
            );
        }

        this.mConnectionRegistry.detachConnection( instance.getConnectionId() );
        this.markInstanceStatus( instance, DeviceStatus.Detached, reason );
        this.mDeviceInstrument.updateDeviceInstance( instance );
        this.markDeviceStatus( instance.getDeviceGuid(), DeviceStatus.Detached );
        this.syncOwnedDeviceInstancesStatus( instance, DeviceStatus.Detached, reason );
        this.mDeviceManager.getLogger().info(
                "Device instance detached, { clientId: {}, instanceId: {}, deviceId: {}, graceMillis: {} }. <Detached>",
                new Object[]{
                        instance.getClientId(),
                        instance.getInstanceGuid(),
                        instance.getDeviceGuid(),
                        this.mDeviceManager.detachedObservationConfig().getGraceMillis()
                }
        );
    }

    protected void recoverDetachedDeviceInstance( DeviceInstanceEntry instance ) {
        if ( instance == null ) {
            return;
        }

        DeviceDetachedObservationRegistry registry = this.detachedObservationRegistry();
        DeviceDetachedObservationEntry detached = registry == null ? null : registry.removeByInstanceGuid( instance.getInstanceGuid() );
        if ( detached == null ) {
            return;
        }

        this.mDeviceManager.getLogger().info(
                "Detached device instance recovered, { clientId: {}, instanceId: {}, deviceId: {} }. <Recovered>",
                new Object[]{ instance.getClientId(), instance.getInstanceGuid(), instance.getDeviceGuid() }
        );
    }

    protected void assertMajorDevice( GUID deviceGuid ) {
        DeviceNodeOwnershipEntry ownership = this.mDeviceInstrument.queryDeviceNodeOwnership( deviceGuid );
        if ( ownership != null && ownership.getOwnerDeviceGuid() != null ) {
            throw new DeviceValidationException( "Logic minor device cannot register runtime directly." );
        }
    }

    protected void ensureOwnedDeviceInstances( DeviceInstanceEntry majorInstance ) {
        if ( majorInstance == null || majorInstance.getInstanceGuid() == null ) {
            return;
        }

        Collection<DeviceInstanceEntry> existed = this.mDeviceInstrument.fetchDeviceInstancesByOwnerInstanceGuid( majorInstance.getInstanceGuid() );
        if ( existed == null || existed.isEmpty() ) {
            this.createOwnedDeviceInstances( majorInstance );
        }
    }

    protected void createOwnedDeviceInstances( DeviceInstanceEntry majorInstance ) {
        if ( majorInstance == null || majorInstance.getDeviceGuid() == null || majorInstance.getInstanceGuid() == null ) {
            return;
        }

        Collection<GUID> ownedDeviceGuids = this.mDeviceInstrument.fetchOwnedDeviceGuids( majorInstance.getDeviceGuid() );
        if ( ownedDeviceGuids == null || ownedDeviceGuids.isEmpty() ) {
            return;
        }

        for ( GUID ownedDeviceGuid : ownedDeviceGuids ) {
            this.createOwnedDeviceInstance( majorInstance, ownedDeviceGuid );
        }
    }

    protected DeviceInstanceEntry createOwnedDeviceInstance( DeviceInstanceEntry majorInstance, GUID ownedDeviceGuid ) {
        DeviceInstanceEntry instance = new GenericDeviceInstanceEntry();
        instance.setInstanceGuid( this.mGuidAllocator.nextGUID() );
        instance.setOwnerInstanceGuid( majorInstance.getInstanceGuid() );
        instance.setDeviceGuid( ownedDeviceGuid );
        instance.setClientId( DeviceClientIdentity.fromDeviceGuid( ownedDeviceGuid ) );
        instance.setConnectionId( majorInstance.getConnectionId() );
        instance.setSessionGuid( majorInstance.getSessionGuid() );
        instance.setTransportType( majorInstance.getTransportType() );
        instance.setRemoteAddress( majorInstance.getRemoteAddress() );
        instance.setEndpointProtocol( majorInstance.getEndpointProtocol() );
        instance.setEndpointHost( majorInstance.getEndpointHost() );
        instance.setEndpointPort( majorInstance.getEndpointPort() );
        instance.setEndpointPath( majorInstance.getEndpointPath() );
        instance.setEndpointAddress( majorInstance.getEndpointAddress() );
        instance.setStatus( majorInstance.getStatus() );
        instance.setStatusReason( majorInstance.getStatusReason() );
        instance.setRegisterTime( majorInstance.getRegisterTime() );
        instance.setLastHeartbeatTime( majorInstance.getLastHeartbeatTime() );
        instance.setConnectionCount( majorInstance.getConnectionCount() );
        instance.setLatestStartTime( majorInstance.getLatestStartTime() );
        instance.setMetadataJson( majorInstance.getMetadataJson() );

        this.mDeviceInstrument.createDeviceInstance( instance );
        this.markDeviceStatus( ownedDeviceGuid, majorInstance.getStatus() );
        return instance;
    }

    protected void syncOwnedDeviceInstancesStatus( DeviceInstanceEntry majorInstance, DeviceStatus status, String reason ) {
        if ( majorInstance == null || majorInstance.getInstanceGuid() == null || status == null ) {
            return;
        }

        Collection<DeviceInstanceEntry> ownedInstances = this.mDeviceInstrument.fetchDeviceInstancesByOwnerInstanceGuid( majorInstance.getInstanceGuid() );
        if ( ownedInstances == null || ownedInstances.isEmpty() ) {
            return;
        }

        for ( DeviceInstanceEntry ownedInstance : ownedInstances ) {
            this.markInstanceStatus( ownedInstance, status, reason );
            ownedInstance.setConnectionId( majorInstance.getConnectionId() );
            ownedInstance.setSessionGuid( majorInstance.getSessionGuid() );
            ownedInstance.setTransportType( majorInstance.getTransportType() );
            ownedInstance.setRemoteAddress( majorInstance.getRemoteAddress() );
            ownedInstance.setEndpointProtocol( majorInstance.getEndpointProtocol() );
            ownedInstance.setEndpointHost( majorInstance.getEndpointHost() );
            ownedInstance.setEndpointPort( majorInstance.getEndpointPort() );
            ownedInstance.setEndpointPath( majorInstance.getEndpointPath() );
            ownedInstance.setEndpointAddress( majorInstance.getEndpointAddress() );
            ownedInstance.setLastHeartbeatTime( majorInstance.getLastHeartbeatTime() );
            ownedInstance.setConnectionCount( majorInstance.getConnectionCount() );
            ownedInstance.setLatestStartTime( majorInstance.getLatestStartTime() );
            ownedInstance.setMetadataJson( majorInstance.getMetadataJson() );
            this.mDeviceInstrument.updateDeviceInstance( ownedInstance );
            this.markDeviceStatus( ownedInstance.getDeviceGuid(), status );
        }
    }

    protected void removeDetachedObservation( DeviceInstanceEntry instance ) {
        DeviceDetachedObservationRegistry registry = this.detachedObservationRegistry();
        if ( registry != null && instance != null ) {
            registry.removeByInstanceGuid( instance.getInstanceGuid() );
        }
    }

    protected DeviceDetachedObservationRegistry detachedObservationRegistry() {
        if ( this.mDeviceManager instanceof UniformDeviceManager ) {
            return ( (UniformDeviceManager) this.mDeviceManager ).detachedObservationRegistry();
        }
        return null;
    }

    protected void markInstanceStatus( DeviceInstanceEntry instance, DeviceStatus status, String reason ) {
        if ( instance == null || status == null ) {
            return;
        }
        instance.setStatus( status );
        instance.setStatusReason( reason );
        if ( this.isTerminalStatus( status ) ) {
            LocalDateTime now = LocalDateTime.now();
            if ( DeviceStatus.Deregistered == status ) {
                instance.setDeregisterTime( now );
            }
            else {
                instance.setOfflineTime( now );
            }
            instance.setLatestEndTime( now );
        }
    }

    protected void markDeviceStatus( GUID deviceGuid, DeviceStatus status ) {
        if ( deviceGuid == null || status == null ) {
            return;
        }

        ElementNode device = this.mDeviceManager.queryDeviceByGuid( deviceGuid );
        if ( device == null ) {
            return;
        }
        device.setStatus( status.getName() );
        this.mDeviceInstrument.update( device );
    }

    protected boolean isTerminalStatus( DeviceStatus status ) {
        return DeviceStatus.Deregistered == status
                || DeviceStatus.Offline == status
                || DeviceStatus.Expired == status
                || DeviceStatus.Error == status;
    }

    protected DeviceStatus resolveDetachedMissingStatus() {
        String policy = this.mDeviceManager.detachedObservationConfig().getMissingAfterReconnectPolicy();
        String normalizedPolicy = policy == null ? "" : policy.trim().toUpperCase( Locale.ROOT );
        if ( DeviceStatus.Expired.getName().toUpperCase( Locale.ROOT ).equals( normalizedPolicy ) ) {
            return DeviceStatus.Expired;
        }
        if ( DeviceStatus.Offline.getName().toUpperCase( Locale.ROOT ).equals( normalizedPolicy )
                || "OFFLINE".equals( normalizedPolicy ) ) {
            return DeviceStatus.Offline;
        }

        this.mDeviceManager.getLogger().warn(
                "Unknown detached missing policy `{}`, fallback to `{}`.",
                policy,
                DeviceStatus.Offline.getName()
        );
        return DeviceStatus.Offline;
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
