package com.device.auto;

import com.acorn.skynet.device.conduct.DeviceLegionaryState;
import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.device.DeviceStatus;
import com.pinecone.hydra.device.kom.instance.DeviceInstanceEntry;
import com.pinecone.tritium.Tritium;

public class DeviceLegionaryHuskyAutoReconnectDevilCase implements Pinenut {

    protected static final long AutoReconnectRoundTimeoutMillis = 45000L;

    protected Tritium system;

    protected DeviceLegionarySmokeCase smokeCase;

    public DeviceLegionaryHuskyAutoReconnectDevilCase( Tritium system ) {
        this.system = system;
        this.smokeCase = new DeviceLegionarySmokeCase( system );
    }

    public void run( int rounds ) throws Exception {
        DeviceLegionarySmokeContext context = this.smokeCase.createStartedContext(
                new HuskyAutoReconnectDeviceLegionaryScenario()
        );
        try {
            this.smokeCase.prepareLegionary( context );
            context.legionary.joinRegiment();
            DeviceLegionaryAssertions.assertOnline( context.legionary );

            GUID instanceGuid = context.legionary.getInstanceGuid();
            int connectionCount = this.queryConnectionCount( context, instanceGuid );
            context.probe.record( "HuskyAutoReconnectDevil", 0, "INITIAL_ONLINE", context.legionary );

            for ( int i = 1; i <= rounds; ++i ) {
                int previousConnectionCount = connectionCount;
                DeviceLegionaryAssertions.awaitTrue(
                        "Husky device auto reconnect round " + i + " did not recover online with increased connection count.",
                        AutoReconnectRoundTimeoutMillis,
                        () -> this.isRecovered( context, instanceGuid, previousConnectionCount )
                );
                connectionCount = this.queryConnectionCount( context, instanceGuid );
                context.probe.record( "HuskyAutoReconnectDevil", i, "RECOVERED", context.legionary );
                if ( i < rounds ) {
                    Thread.sleep( 10000L );
                }
            }

            this.smokeCase.passiveShutdown( context );
            Debug.greenfs( "[DeviceLegionaryHuskyAutoReconnectDevil] " + rounds + "/" + rounds + " PASS" );
        }
        finally {
            context.probe.dump();
            context.cleanup();
        }
    }

    protected boolean isRecovered( DeviceLegionarySmokeContext context, GUID instanceGuid, int previousConnectionCount ) {
        if ( context.legionary.getInstanceGuid() == null || !instanceGuid.equals( context.legionary.getInstanceGuid() ) ) {
            return false;
        }
        if ( context.legionary.getState() != DeviceLegionaryState.Online ) {
            return false;
        }

        DeviceInstanceEntry runtime = context.deviceManager.deviceRuntimeService().queryDeviceRuntime( context.deviceGuid );
        DeviceInstanceEntry entry = context.deviceInstrument.queryDeviceInstance( instanceGuid );
        return runtime != null
                && entry != null
                && runtime.getStatus() == DeviceStatus.Online
                && entry.getStatus() == DeviceStatus.Online
                && entry.getConnectionCount() > previousConnectionCount
                && runtime.getConnectionCount() > previousConnectionCount;
    }

    protected int queryConnectionCount( DeviceLegionarySmokeContext context, GUID instanceGuid ) {
        DeviceInstanceEntry entry = context.deviceInstrument.queryDeviceInstance( instanceGuid );
        DeviceLegionaryAssertions.assertNotNull( entry, "Device instance entry is null." );
        return entry.getConnectionCount();
    }
}
