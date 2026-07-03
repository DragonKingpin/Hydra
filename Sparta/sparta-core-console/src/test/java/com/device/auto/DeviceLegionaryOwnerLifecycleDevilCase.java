package com.device.auto;

import java.util.Collection;

import com.acorn.skynet.device.conduct.DeviceLegionaryJoinResponse;
import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.device.DeviceStatus;
import com.pinecone.hydra.device.kom.entity.PhysicalHostElement;
import com.pinecone.hydra.device.kom.instance.DeviceInstanceEntry;
import com.pinecone.hydra.device.registry.instruction.DeviceRegisterInstruction;
import com.pinecone.hydra.device.registry.server.connection.DeviceConnection;
import com.pinecone.tritium.Tritium;

public class DeviceLegionaryOwnerLifecycleDevilCase implements Pinenut {

    protected static final String MinorAPath = "root/test/device/auto/clara-husky-auto-minor-a";

    protected static final String MinorBPath = "root/test/device/auto/clara-husky-auto-minor-b";

    protected static final long GraceMillis = 900L;

    protected static final long SweepMillis = 100L;

    protected static final long AwaitMillis = 8000L;

    protected final DeviceLegionarySmokeCase smokeCase;

    public DeviceLegionaryOwnerLifecycleDevilCase( Tritium system ) {
        this.smokeCase = new DeviceLegionarySmokeCase( system );
    }

    public void run() throws Exception {
        DeviceLegionarySmokeContext context = this.smokeCase.createStartedContext(
                new HuskyAutoReconnectDeviceLegionaryScenario(),
                this.smokeCase.detachedObservationConfig( true, GraceMillis, SweepMillis, "Offline" )
        );
        try {
            this.smokeCase.prepareLegionary( context );
            GUID minorA = this.prepareOwnedDevice( context, MinorAPath, "Clara Husky Auto Minor A" );
            GUID minorB = this.prepareOwnedDevice( context, MinorBPath, "Clara Husky Auto Minor B" );

            DeviceLegionaryJoinResponse response = context.legionary.joinRegiment();
            GUID majorInstanceGuid = response.getInstanceGuid();
            DeviceLegionaryAssertions.assertNotNull( majorInstanceGuid, "Major instance guid is null." );
            this.assertOwnedOnline( context, majorInstanceGuid, minorA, minorB );
            this.assertMinorRegisterRejected( context, minorA );

            context.deviceManager.deviceRuntimeService().detachConnectionByClientId(
                    context.legionary.getClientId(),
                    "DeviceOwnerLifecycleOffline"
            );
            this.assertOwnedStatus( context, majorInstanceGuid, DeviceStatus.Detached, minorA, minorB );
            this.awaitOwnedPersistentStatus( context, majorInstanceGuid, DeviceStatus.Offline, minorA, minorB );

            Debug.greenfs( "[DeviceLegionaryOwnerLifecycleDevil] OwnerLifecycle PASS" );
        }
        finally {
            context.cleanup();
        }
    }

    protected GUID prepareOwnedDevice( DeviceLegionarySmokeContext context, String path, String alias ) {
        PhysicalHostElement host = context.deviceInstrument.affirmPhysicalHost( path );
        host.setAlias( alias );
        host.setIpAddress( "127.0.0.1" );
        host.setStatus( DeviceStatus.New.getName() );
        host.setDescription( "Device legionary owner lifecycle minor host." );
        context.deviceInstrument.update( host );
        context.deviceInstrument.updateDeviceNodeOwnership( host.getGuid(), context.deviceGuid, true );
        return host.getGuid();
    }

    protected void assertOwnedOnline( DeviceLegionarySmokeContext context, GUID majorInstanceGuid, GUID... minorDeviceGuids ) {
        DeviceInstanceEntry major = context.deviceInstrument.queryDeviceInstance( majorInstanceGuid );
        DeviceLegionaryAssertions.assertNotNull( major, "Major instance is null." );
        DeviceLegionaryAssertions.assertTrue( major.getOwnerInstanceGuid() == null, "Major owner instance guid should be null." );
        DeviceLegionaryAssertions.assertTrue(
                context.deviceManager.deviceRuntimeService().queryDeviceRuntime( context.deviceGuid ) != null,
                "Major runtime should exist."
        );

        Collection<DeviceInstanceEntry> minors = context.deviceInstrument.fetchDeviceInstancesByOwnerInstanceGuid( majorInstanceGuid );
        DeviceLegionaryAssertions.assertTrue( minors.size() == minorDeviceGuids.length, "Minor instance count mismatch." );
        this.assertOwnedStatus( context, majorInstanceGuid, DeviceStatus.Online, minorDeviceGuids );
        for ( GUID minorDeviceGuid : minorDeviceGuids ) {
            DeviceLegionaryAssertions.assertTrue(
                    context.deviceManager.deviceRuntimeService().queryDeviceRuntime( minorDeviceGuid ) == null,
                    "Minor runtime should not be bound: " + minorDeviceGuid
            );
        }
    }

    protected void assertMinorRegisterRejected( DeviceLegionarySmokeContext context, GUID minorDeviceGuid ) {
        DeviceRegisterInstruction instruction = new DeviceRegisterInstruction();
        instruction.setDeviceGuid( minorDeviceGuid );
        instruction.setEndpointProtocol( context.scenario.endpointProtocol() );
        instruction.setEndpointHost( "127.0.0.1" );
        instruction.setEndpointPort( context.scenario.endpointPort() );
        instruction.setEndpointPath( "/device/minor-direct-register" );
        instruction.setEndpointAddress( context.scenario.endpointProtocol() + "://127.0.0.1:" + context.scenario.endpointPort() + "/device/minor-direct-register" );

        try {
            context.deviceManager.deviceRuntimeService().registerDevice(
                    this.createForeignConnection( context, "minor-direct-register" ),
                    instruction
            );
        }
        catch ( RuntimeException expected ) {
            return;
        }
        throw new IllegalStateException( "Logic minor device should not register runtime directly." );
    }

    protected void assertOwnedStatus( DeviceLegionarySmokeContext context, GUID majorInstanceGuid, DeviceStatus status, GUID... minorDeviceGuids ) {
        DeviceInstanceEntry major = context.deviceInstrument.queryDeviceInstance( majorInstanceGuid );
        DeviceLegionaryAssertions.assertNotNull( major, "Major instance is null." );
        DeviceLegionaryAssertions.assertTrue( major.getStatus() == status, "Major status should be " + status.getName() );
        DeviceLegionaryAssertions.assertTrue(
                status.getName().equals( context.deviceManager.queryDeviceByGuid( context.deviceGuid ).getStatus() ),
                "Major device status should be " + status.getName()
        );

        Collection<DeviceInstanceEntry> minors = context.deviceInstrument.fetchDeviceInstancesByOwnerInstanceGuid( majorInstanceGuid );
        for ( GUID minorDeviceGuid : minorDeviceGuids ) {
            DeviceInstanceEntry minor = this.findMinorInstance( minors, minorDeviceGuid );
            DeviceLegionaryAssertions.assertNotNull( minor, "Minor instance is null: " + minorDeviceGuid );
            DeviceLegionaryAssertions.assertTrue( majorInstanceGuid.equals( minor.getOwnerInstanceGuid() ), "Minor owner instance guid mismatch." );
            DeviceLegionaryAssertions.assertTrue( minor.getStatus() == status, "Minor status should be " + status.getName() );
            DeviceLegionaryAssertions.assertTrue(
                    status.getName().equals( context.deviceManager.queryDeviceByGuid( minorDeviceGuid ).getStatus() ),
                    "Minor device status should be " + status.getName()
            );
        }
    }

    protected void awaitOwnedPersistentStatus( DeviceLegionarySmokeContext context, GUID majorInstanceGuid, DeviceStatus status, GUID... minorDeviceGuids ) {
        DeviceLegionaryAssertions.awaitTrue(
                "Owned device instances did not become " + status.getName(),
                AwaitMillis,
                () -> {
                    try {
                        this.assertOwnedStatus( context, majorInstanceGuid, status, minorDeviceGuids );
                        return context.deviceManager.deviceRuntimeService().queryDeviceRuntime( context.deviceGuid ) == null;
                    }
                    catch ( RuntimeException e ) {
                        return false;
                    }
                }
        );
    }

    protected DeviceInstanceEntry findMinorInstance( Collection<DeviceInstanceEntry> instances, GUID minorDeviceGuid ) {
        if ( instances == null ) {
            return null;
        }
        for ( DeviceInstanceEntry instance : instances ) {
            if ( minorDeviceGuid.equals( instance.getDeviceGuid() ) ) {
                return instance;
            }
        }
        return null;
    }

    protected DeviceConnection createForeignConnection( DeviceLegionarySmokeContext context, String suffix ) {
        DeviceConnection connection = new DeviceConnection();
        connection.setConnectionId( "device-owner-devil-" + suffix + "-" + System.nanoTime() );
        connection.setSessionGuid( context.deviceInstrument.getGuidAllocator().nextGUID() );
        connection.setTransportType( context.scenario.endpointProtocol() );
        connection.setRemoteAddress( "device-owner-devil-local" );
        connection.setConnectedTime( System.currentTimeMillis() );
        return connection;
    }
}
