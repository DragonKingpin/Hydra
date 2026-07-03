package com.device.auto;

import com.acorn.skynet.device.conduct.DeviceLegionaryException;
import com.acorn.skynet.device.conduct.DeviceLegionaryJoinResponse;
import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.device.DeviceStatus;
import com.pinecone.hydra.device.kom.instance.DeviceInstanceEntry;
import com.pinecone.hydra.device.registry.instruction.DeviceRegisterInstruction;
import com.pinecone.hydra.device.registry.server.connection.DeviceConnection;
import com.pinecone.tritium.Tritium;

public class DeviceLegionaryDetachedGraceDevilCase implements Pinenut {

    protected static final long GraceMillis = 900L;

    protected static final long SweepMillis = 100L;

    protected static final long AwaitMillis = 8000L;

    protected Tritium system;

    protected DeviceLegionarySmokeCase smokeCase;

    public DeviceLegionaryDetachedGraceDevilCase( Tritium system ) {
        this.system = system;
        this.smokeCase = new DeviceLegionarySmokeCase( system );
    }

    public void run() throws Exception {
        this.runRecover();
        this.runOffline();
        this.runExpired();
        this.runDuplicateOnline();
        this.runDetachedOccupy();
        this.runReentryAfterOffline();
        this.runResumeAfterOfflineRejected();
        this.runResumeAfterExpiredRejected();
    }

    protected void runRecover() throws Exception {
        DeviceLegionarySmokeContext context = this.startedContext( "Offline" );
        try {
            this.join( context );
            GUID instanceGuid = context.legionary.getInstanceGuid();
            int connectionCount = this.queryConnectionCount( context, instanceGuid );

            this.detach( context, "DeviceDetachedGraceRecover" );
            this.assertRuntimeStatus( context, instanceGuid, DeviceStatus.Detached );

            context.legionary.joinRegiment();
            DeviceLegionaryAssertions.assertSameInstance( instanceGuid, context.legionary.getInstanceGuid() );
            DeviceLegionaryAssertions.awaitTrue(
                    "Detached device instance did not recover online.",
                    AwaitMillis,
                    () -> this.runtimeStatusIs( context, instanceGuid, DeviceStatus.Online )
                            && this.queryConnectionCount( context, instanceGuid ) > connectionCount
            );
            this.smokeCase.passiveShutdown( context );
            Debug.greenfs( "[DeviceLegionaryDetachedGraceDevil] Recover PASS" );
        }
        finally {
            context.cleanup();
        }
    }

    protected void runOffline() throws Exception {
        DeviceLegionarySmokeContext context = this.startedContext( "Offline" );
        try {
            this.join( context );
            GUID instanceGuid = context.legionary.getInstanceGuid();
            this.detach( context, "DeviceDetachedGraceOffline" );
            this.assertRuntimeStatus( context, instanceGuid, DeviceStatus.Detached );
            this.awaitPersistentStatus( context, instanceGuid, DeviceStatus.Offline );
            this.awaitRuntimeUnbound( context );
            Debug.greenfs( "[DeviceLegionaryDetachedGraceDevil] Offline PASS" );
        }
        finally {
            context.cleanup();
        }
    }

    protected void runExpired() throws Exception {
        DeviceLegionarySmokeContext context = this.startedContext( "Expired" );
        try {
            this.join( context );
            GUID instanceGuid = context.legionary.getInstanceGuid();
            this.detach( context, "DeviceDetachedGraceExpired" );
            this.assertRuntimeStatus( context, instanceGuid, DeviceStatus.Detached );
            this.awaitPersistentStatus( context, instanceGuid, DeviceStatus.Expired );
            Debug.greenfs( "[DeviceLegionaryDetachedGraceDevil] Expired PASS" );
        }
        finally {
            context.cleanup();
        }
    }

    protected void runDuplicateOnline() throws Exception {
        DeviceLegionarySmokeContext context = this.startedContext( "Offline" );
        try {
            this.join( context );
            GUID instanceGuid = context.legionary.getInstanceGuid();
            this.assertJoinFailsWithDifferentInstance( context, "Duplicate online device should be rejected." );
            this.assertRuntimeStatus( context, instanceGuid, DeviceStatus.Online );
            this.smokeCase.passiveShutdown( context );
            Debug.greenfs( "[DeviceLegionaryDetachedGraceDevil] DuplicateOnline PASS" );
        }
        finally {
            context.cleanup();
        }
    }

    protected void runDetachedOccupy() throws Exception {
        DeviceLegionarySmokeContext context = this.startedContext( "Offline" );
        try {
            this.join( context );
            GUID instanceGuid = context.legionary.getInstanceGuid();
            this.detach( context, "DeviceDetachedGraceOccupy" );
            this.assertRuntimeStatus( context, instanceGuid, DeviceStatus.Detached );
            this.assertJoinFailsWithDifferentInstance( context, "Detached device should still be occupied." );
            context.legionary.joinRegiment();
            this.assertRuntimeStatus( context, instanceGuid, DeviceStatus.Online );
            this.smokeCase.passiveShutdown( context );
            Debug.greenfs( "[DeviceLegionaryDetachedGraceDevil] DetachedOccupy PASS" );
        }
        finally {
            context.cleanup();
        }
    }

    protected void runReentryAfterOffline() throws Exception {
        DeviceLegionarySmokeContext context = this.startedContext( "Offline" );
        try {
            this.join( context );
            GUID firstInstanceGuid = context.legionary.getInstanceGuid();
            this.detach( context, "DeviceDetachedGraceReentry" );
            this.awaitPersistentStatus( context, firstInstanceGuid, DeviceStatus.Offline );
            this.awaitRuntimeUnbound( context );
            context.legionary.deregister( "clear-old-instance-before-reentry" );
            DeviceLegionaryJoinResponse response = context.legionary.joinRegiment();
            DeviceLegionaryAssertions.assertNotNull( response.getInstanceGuid(), "Reentry instance guid is null." );
            DeviceLegionaryAssertions.assertTrue(
                    !firstInstanceGuid.equals( response.getInstanceGuid() ),
                    "Reentry after Offline should create a fresh instance."
            );
            this.assertRuntimeStatus( context, response.getInstanceGuid(), DeviceStatus.Online );
            this.smokeCase.passiveShutdown( context );
            Debug.greenfs( "[DeviceLegionaryDetachedGraceDevil] ReentryAfterOffline PASS" );
        }
        finally {
            context.cleanup();
        }
    }

    protected void runResumeAfterOfflineRejected() throws Exception {
        this.runResumeAfterTerminalStatusRejected( "Offline", DeviceStatus.Offline );
        Debug.greenfs( "[DeviceLegionaryDetachedGraceDevil] ResumeAfterOfflineRejected PASS" );
    }

    protected void runResumeAfterExpiredRejected() throws Exception {
        this.runResumeAfterTerminalStatusRejected( "Expired", DeviceStatus.Expired );
        Debug.greenfs( "[DeviceLegionaryDetachedGraceDevil] ResumeAfterExpiredRejected PASS" );
    }

    protected void runResumeAfterTerminalStatusRejected( String missingPolicy, DeviceStatus terminalStatus ) throws Exception {
        DeviceLegionarySmokeContext context = this.startedContext( missingPolicy );
        try {
            this.join( context );
            GUID instanceGuid = context.legionary.getInstanceGuid();
            this.detach( context, "DeviceDetachedGraceTerminalResume" + terminalStatus.getName() );
            this.awaitPersistentStatus( context, instanceGuid, terminalStatus );
            this.awaitRuntimeUnbound( context );
            this.assertResumeFailsWithInstance( context, instanceGuid, "Terminal device instance should not resume: " + terminalStatus.getName() );
            DeviceInstanceEntry entry = context.deviceInstrument.queryDeviceInstance( instanceGuid );
            DeviceLegionaryAssertions.assertNotNull( entry, "Terminal device instance entry is null." );
            DeviceLegionaryAssertions.assertTrue( entry.getStatus() == terminalStatus, "Terminal device instance status should remain " + terminalStatus.getName() );
            this.smokeCase.assertDeviceStatus( context, terminalStatus );
        }
        finally {
            context.cleanup();
        }
    }

    protected DeviceLegionarySmokeContext startedContext( String missingPolicy ) throws Exception {
        return this.smokeCase.createStartedContext(
                new HuskyAutoReconnectDeviceLegionaryScenario(),
                this.smokeCase.detachedObservationConfig( true, GraceMillis, SweepMillis, missingPolicy )
        );
    }

    protected void join( DeviceLegionarySmokeContext context ) throws Exception {
        this.smokeCase.prepareLegionary( context );
        context.legionary.joinRegiment();
        DeviceLegionaryAssertions.assertOnline( context.legionary );
        this.smokeCase.assertDeviceRuntimeOnline( context );
    }

    protected void detach( DeviceLegionarySmokeContext context, String reason ) {
        context.deviceManager.deviceRuntimeService().detachConnectionByClientId(
                context.legionary.getClientId(),
                reason
        );
    }

    protected void assertJoinFailsWithDifferentInstance( DeviceLegionarySmokeContext context, String message ) {
        GUID original = context.legionary.getInstanceGuid();
        try {
            DeviceRegisterInstruction instruction = context.legionary.joinRequest().toDeviceRegisterInstruction(
                    context.legionary.getClientId()
            );
            instruction.setInstanceGuid( context.deviceInstrument.getGuidAllocator().nextGUID() );
            context.deviceManager.deviceRuntimeService().registerDevice(
                    this.createForeignConnection( context, "duplicate-instance" ),
                    instruction
            );
        }
        catch ( RuntimeException expected ) {
            return;
        }
        throw new IllegalStateException( message + " original => " + original + ", actual => " + context.legionary.getInstanceGuid() );
    }

    protected void assertResumeFailsWithInstance( DeviceLegionarySmokeContext context, GUID instanceGuid, String message ) {
        try {
            DeviceRegisterInstruction instruction = context.legionary.joinRequest().toDeviceRegisterInstruction(
                    context.legionary.getClientId()
            );
            instruction.setInstanceGuid( instanceGuid );
            context.deviceManager.deviceRuntimeService().registerDevice(
                    this.createForeignConnection( context, "terminal-resume" ),
                    instruction
            );
        }
        catch ( RuntimeException expected ) {
            return;
        }
        throw new IllegalStateException( message + " instance => " + instanceGuid );
    }

    protected DeviceConnection createForeignConnection( DeviceLegionarySmokeContext context, String suffix ) {
        DeviceConnection connection = new DeviceConnection();
        connection.setConnectionId( "device-devil-" + suffix + "-" + System.nanoTime() );
        connection.setSessionGuid( context.deviceInstrument.getGuidAllocator().nextGUID() );
        connection.setTransportType( context.scenario.endpointProtocol() );
        connection.setRemoteAddress( "device-devil-local" );
        connection.setConnectedTime( System.currentTimeMillis() );
        return connection;
    }

    protected void assertRuntimeStatus( DeviceLegionarySmokeContext context, GUID instanceGuid, DeviceStatus status ) {
        DeviceLegionaryAssertions.assertTrue(
                this.runtimeStatusIs( context, instanceGuid, status ),
                "Device runtime status should be " + status.getName()
        );
        this.smokeCase.assertDeviceStatus( context, status );
    }

    protected void awaitPersistentStatus( DeviceLegionarySmokeContext context, GUID instanceGuid, DeviceStatus status ) {
        DeviceLegionaryAssertions.awaitTrue(
                "Device persistent status did not become " + status.getName(),
                AwaitMillis,
                () -> {
                    DeviceInstanceEntry entry = context.deviceInstrument.queryDeviceInstance( instanceGuid );
                    return entry != null
                            && entry.getStatus() == status
                            && this.deviceStatusIs( context, status );
                }
        );
    }

    protected void awaitRuntimeUnbound( DeviceLegionarySmokeContext context ) {
        DeviceLegionaryAssertions.awaitTrue(
                "Device runtime should be unbound.",
                AwaitMillis,
                () -> context.deviceManager.deviceRuntimeService().queryDeviceRuntime( context.deviceGuid ) == null
        );
    }

    protected boolean runtimeStatusIs( DeviceLegionarySmokeContext context, GUID instanceGuid, DeviceStatus status ) {
        DeviceInstanceEntry runtime = context.deviceManager.deviceRuntimeService().queryDeviceRuntime( context.deviceGuid );
        DeviceInstanceEntry entry = context.deviceInstrument.queryDeviceInstance( instanceGuid );
        return runtime != null
                && entry != null
                && instanceGuid.equals( runtime.getInstanceGuid() )
                && runtime.getStatus() == status
                && entry.getStatus() == status
                && this.deviceStatusIs( context, status );
    }

    protected boolean deviceStatusIs( DeviceLegionarySmokeContext context, DeviceStatus status ) {
        return status.getName().equals( context.deviceManager.queryDeviceByGuid( context.deviceGuid ).getStatus() );
    }

    protected int queryConnectionCount( DeviceLegionarySmokeContext context, GUID instanceGuid ) {
        DeviceInstanceEntry entry = context.deviceInstrument.queryDeviceInstance( instanceGuid );
        DeviceLegionaryAssertions.assertNotNull( entry, "Device instance entry is null." );
        return entry.getConnectionCount();
    }
}
