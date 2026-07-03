package com.device.auto;

import com.acorn.skynet.device.conduct.DeviceLegionaryJoinRequest;
import com.acorn.skynet.device.conduct.DeviceLegionaryJoinResponse;
import com.acorn.skynet.device.conduct.SkyCollectiveDeviceLegionary;
import com.acorn.skynet.device.conduct.SkyCollectiveDeviceRegiment;
import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.json.JSONMaptron;
import com.pinecone.hydra.device.DeviceStatus;
import com.pinecone.hydra.device.ibatis.hydranium.DeviceMappingDriver;
import com.pinecone.hydra.device.kom.UniformDeviceInstrument;
import com.pinecone.hydra.device.kom.entity.ElementNode;
import com.pinecone.hydra.device.kom.entity.PhysicalHostElement;
import com.pinecone.hydra.device.kom.instance.DeviceInstanceEntry;
import com.pinecone.hydra.device.registry.server.detached.DeviceDetachedObservationConfig;
import com.pinecone.hydra.device.registry.server.UniformDeviceManager;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.slime.jelly.source.ibatis.IbatisClient;
import com.pinecone.tritium.Tritium;

public class DeviceLegionarySmokeCase implements Pinenut {

    protected static final String DevicePath = "root/test/device/auto/clara-husky-auto";

    protected Tritium system;

    public DeviceLegionarySmokeCase( Tritium system ) {
        this.system = system;
    }

    public void run( DeviceLegionaryTransportScenario scenario ) throws Exception {
        DeviceLegionarySmokeContext context = this.createStartedContext( scenario );
        try {
            this.prepareLegionary( context );
            DeviceLegionaryJoinResponse response = context.legionary.joinRegiment();
            context.probe.record( "Smoke", 0, "JOINED", context.legionary );
            DeviceLegionaryAssertions.assertNotNull( response.getInstanceGuid(), "Join response instance guid is null." );
            DeviceLegionaryAssertions.assertOnline( context.legionary );
            this.assertDeviceRuntimeOnline( context );
            this.passiveShutdown( context );
            Debug.greenfs( "[DeviceLegionarySmoke] " + scenario.name() + " PASS" );
        }
        finally {
            context.cleanup();
        }
    }

    protected DeviceLegionarySmokeContext createStartedContext( DeviceLegionaryTransportScenario scenario ) throws Exception {
        return this.createStartedContext( scenario, null );
    }

    protected DeviceLegionarySmokeContext createStartedContext(
            DeviceLegionaryTransportScenario scenario,
            DeviceDetachedObservationConfig detachedObservationConfig
    ) throws Exception {
        DeviceLegionarySmokeContext context = new DeviceLegionarySmokeContext( this.system, scenario );
        context.detachedObservationConfig = detachedObservationConfig;
        KOIMappingDriver driver = new DeviceMappingDriver(
                this.system,
                (IbatisClient) this.system.getMiddlewareDirector().getRDBManager().getRDBClientByName( "MySQLKingHydranium" ),
                this.system.getDispenserCenter()
        );
        context.deviceInstrument = new UniformDeviceInstrument( driver );
        context.deviceManager = new UniformDeviceManager( context.deviceInstrument );
        if ( context.detachedObservationConfig != null ) {
            context.deviceManager.configureDetachedObservation( context.detachedObservationConfig );
        }
        scenario.hookServerTransport( context );
        context.regiment = new SkyCollectiveDeviceRegiment( context.deviceInstrument, context.deviceManager );
        context.regiment.startDeviceManager();
        return context;
    }

    protected DeviceDetachedObservationConfig detachedObservationConfig(
            boolean enable,
            long graceMillis,
            long sweepMillis,
            String missingAfterReconnectPolicy
    ) {
        return new DeviceDetachedObservationConfig( new JSONMaptron(
                "{enable:" + enable
                        + ", graceMillis:" + graceMillis
                        + ", sweepMillis:" + sweepMillis
                        + ", expireAsyncThreads:2"
                        + ", missingAfterReconnectPolicy:\"" + missingAfterReconnectPolicy + "\"}"
        ) );
    }

    protected void prepareLegionary( DeviceLegionarySmokeContext context ) throws Exception {
        context.deviceGuid = this.prepareDeviceMeta( context );
        context.devicePath = DevicePath;
        context.deviceClient = context.scenario.createDeviceClient( context );
        context.deviceClient.startDevice();

        DeviceLegionaryJoinRequest joinRequest = new DeviceLegionaryJoinRequest();
        joinRequest.setDeviceGuid( context.deviceGuid );
        joinRequest.setEndpointProtocol( context.scenario.endpointProtocol() );
        joinRequest.setEndpointHost( "127.0.0.1" );
        joinRequest.setEndpointPort( context.scenario.endpointPort() );
        joinRequest.setEndpointPath( "/device/clara-husky-auto" );
        joinRequest.setEndpointAddress( context.scenario.endpointProtocol() + "://127.0.0.1:" + context.scenario.endpointPort() + "/device/clara-husky-auto" );
        joinRequest.setMetadataJson( "{source:\"device-legionary-smoke\"}" );

        context.legionary = new SkyCollectiveDeviceLegionary(
                context.scenario.name() + "-device-legionary",
                context.deviceClient,
                joinRequest
        );
        context.legionary.startDevice();
        context.probe.record( "Smoke", 0, "CONTROL_READY", context.legionary );
    }

    protected com.pinecone.framework.util.id.GUID prepareDeviceMeta( DeviceLegionarySmokeContext context ) {
        PhysicalHostElement host = context.deviceInstrument.affirmPhysicalHost( DevicePath );
        host.setAlias( "Clara Husky Auto Host" );
        host.setIpAddress( "127.0.0.1" );
        host.setStatus( DeviceStatus.New.getName() );
        host.setDescription( "Device legionary husky auto reconnect test host." );
        context.deviceInstrument.update( host );

        PhysicalHostElement queried = (PhysicalHostElement) context.deviceInstrument.queryElement( DevicePath );
        DeviceLegionaryAssertions.assertNotNull( queried, "Device meta is null: " + DevicePath );
        DeviceLegionaryAssertions.assertNotNull( queried.getGuid(), "Device meta guid is null: " + DevicePath );
        return queried.getGuid();
    }

    protected void assertDeviceRuntimeOnline( DeviceLegionarySmokeContext context ) {
        DeviceInstanceEntry runtime = context.deviceManager.deviceRuntimeService().queryDeviceRuntime( context.deviceGuid );
        DeviceLegionaryAssertions.assertNotNull( runtime, "Device runtime is null." );
        DeviceLegionaryAssertions.assertTrue(
                runtime.getStatus() == DeviceStatus.Online,
                "Device runtime should be Online, actual => " + runtime.getStatus()
        );
        this.assertDeviceStatus( context, DeviceStatus.Online );
    }

    protected void assertDeviceStatus( DeviceLegionarySmokeContext context, DeviceStatus status ) {
        ElementNode device = context.deviceManager.queryDeviceByGuid( context.deviceGuid );
        DeviceLegionaryAssertions.assertNotNull( device, "Device meta is null." );
        DeviceLegionaryAssertions.assertTrue(
                status.getName().equals( device.getStatus() ),
                "Device status should be " + status.getName() + ", actual => " + device.getStatus()
        );
    }

    protected void passiveShutdown( DeviceLegionarySmokeContext context ) throws Exception {
        context.regiment.shutdownDeviceInstance( context.legionary.getInstanceGuid(), context.scenario.name() + "SmokePassiveShutdown" );
        DeviceLegionaryAssertions.awaitTrue(
                context.scenario.name() + " device legionary did not terminate after passive shutdown.",
                5000L,
                () -> context.legionary.getState() == com.acorn.skynet.device.conduct.DeviceLegionaryState.Terminated
        );
        DeviceLegionaryAssertions.assertTerminated( context.legionary );
        context.probe.record( "Smoke", 0, "TERMINATED", context.legionary );
    }
}
