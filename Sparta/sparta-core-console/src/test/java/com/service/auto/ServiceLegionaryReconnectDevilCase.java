package com.service.auto;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.id.GUID;
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

        ServiceLegionaryAssertions.awaitTrue(
                context.scenario.name() + " round " + nRound + " did not rejoin.",
                15000L,
                () -> context.legionary.getInstanceGuid() != null
                        && !context.legionary.getInstanceGuid().equals( before )
                        && context.legionary.getState() == com.acorn.redqueen.service.conduct.ServiceLegionaryState.Online
        );

        GUID after = context.legionary.getInstanceGuid();
        context.probe.record( "Devil", nRound, "ONLINE", context.legionary );
        ServiceLegionaryAssertions.assertInstanceChanged( before, after );
        ServiceLegionaryAssertions.assertOnline( context.legionary );
        this.mSmokeCase.assertServiceMetaVisible( context );
    }
}
