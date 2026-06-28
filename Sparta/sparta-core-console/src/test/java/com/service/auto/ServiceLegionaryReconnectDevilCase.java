package com.service.auto;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.kom.entity.ServiceInstanceEntry;
import com.pinecone.hydra.service.registry.client.transport.ServiceClientTransportSyncReasons;
import com.pinecone.hydra.service.registry.constant.ServiceInstanceStatus;
import com.pinecone.tritium.Tritium;

public class ServiceLegionaryReconnectDevilCase implements Pinenut {

    protected Tritium mSystem;

    protected ServiceLegionarySmokeCase mSmokeCase;

    public ServiceLegionaryReconnectDevilCase( Tritium system ) {
        this.mSystem = system;
        this.mSmokeCase = new ServiceLegionarySmokeCase( system );
    }

    public void run( ServiceLegionaryTransportScenario scenario, int nRounds ) throws Exception {
        ServiceLegionarySmokeContext context = this.mSmokeCase.createStartedContext( scenario );
        try {
            this.mSmokeCase.prepareLegionary( context );
            context.legionary.joinRegiment();
            context.probe.record( "Devil", 0, "INITIAL_ONLINE", context.legionary );
            ServiceLegionaryAssertions.assertOnline( context.legionary );

            for ( int i = 1; i <= nRounds; ++i ) {
                this.runReconnectRound( context, i );
            }

            this.mSmokeCase.passiveShutdown( context );
            Debug.greenfs( "[ServiceLegionaryDevil] " + scenario.name() + " " + nRounds + "/" + nRounds + " PASS" );
        }
        finally {
            context.probe.dump();
            context.cleanup();
        }
    }

    protected void runReconnectRound( ServiceLegionarySmokeContext context, int nRound ) throws Exception {
        GUID before = context.legionary.getInstanceGuid();
        context.probe.record( "Devil", nRound, "BREAK", context.legionary );
        context.scenario.breakClientConnection( context );
        context.scenario.requestStateSynchronization( context, ServiceClientTransportSyncReasons.StreamError );

        ServiceLegionaryAssertions.awaitTrue(
                context.scenario.name() + " round " + nRound + " did not rejoin.",
                15000L,
                () -> context.legionary.getInstanceGuid() != null
                        && context.legionary.getInstanceGuid().equals( before )
                        && context.legionary.getState() == com.acorn.redqueen.service.conduct.ServiceLegionaryState.Online
                        && this.isInstanceOnlineWithConnectionCount( context, before, nRound + 1 )
        );

        GUID after = context.legionary.getInstanceGuid();
        context.probe.record( "Devil", nRound, "ONLINE", context.legionary );
        ServiceLegionaryAssertions.assertTrue(
                before.equals( after ),
                context.scenario.name() + " rejoin should keep instance stable. before => " + before + ", after => " + after
        );
        ServiceLegionaryAssertions.assertOnline( context.legionary );
        this.assertConnectionCount( context, after, nRound + 1 );
        this.mSmokeCase.assertServiceMetaVisible( context );
    }

    protected boolean isInstanceOnlineWithConnectionCount( ServiceLegionarySmokeContext context, GUID instanceGuid, int nExpected ) {
        ServiceInstanceEntry entry = context.serviceInstrument.queryServiceInstance( instanceGuid );
        return entry != null
                && ServiceInstanceStatus.Online.getName().equals( entry.getStatus() )
                && entry.getConnectionCount() == nExpected;
    }

    protected void assertConnectionCount( ServiceLegionarySmokeContext context, GUID instanceGuid, int nExpected ) {
        ServiceInstanceEntry entry = context.serviceInstrument.queryServiceInstance( instanceGuid );
        ServiceLegionaryAssertions.assertNotNull( entry, "Current service instance entry is null." );
        ServiceLegionaryAssertions.assertTrue(
                ServiceInstanceStatus.Online.getName().equals( entry.getStatus() ),
                context.scenario.name() + " current service instance should be Online, actual => " + entry.getStatus()
        );
        ServiceLegionaryAssertions.assertTrue(
                entry.getConnectionCount() == nExpected,
                context.scenario.name() + " connection count mismatch. expected => " + nExpected + ", actual => " + entry.getConnectionCount()
        );
    }
}
