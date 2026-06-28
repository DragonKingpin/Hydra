package com.service.auto;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.json.JSONMaptron;
import com.pinecone.hydra.service.kom.entity.ServiceInstanceEntry;
import com.pinecone.hydra.service.registry.constant.ServiceInstanceStatus;
import com.pinecone.hydra.service.registry.server.detached.ServiceDetachedObservationConfig;
import com.pinecone.tritium.Tritium;

public class ServiceLegionaryDetachedGraceDevilCase implements Pinenut {

    protected Tritium mSystem;

    protected ServiceLegionarySmokeCase mSmokeCase;

    public ServiceLegionaryDetachedGraceDevilCase( Tritium system ) {
        this.mSystem = system;
        this.mSmokeCase = new ServiceLegionarySmokeCase( system );
    }

    public void run( ServiceLegionaryTransportScenario scenario, int nRounds ) throws Exception {
        ServiceLegionarySmokeContext context = this.createContext( scenario );
        try {
            this.mSmokeCase.prepareLegionary( context );
            context.legionary.joinRegiment();
            context.probe.record( "DetachedGraceDevil", 0, "INITIAL_ONLINE", context.legionary );
            ServiceLegionaryAssertions.assertOnline( context.legionary );

            for ( int i = 1; i <= nRounds; ++i ) {
                this.runGraceRound( context, i );
            }

            this.mSmokeCase.passiveShutdown( context );
            Debug.greenfs( "[ServiceLegionaryDetachedGraceDevil] " + scenario.name() + " " + nRounds + "/" + nRounds + " PASS" );
        }
        finally {
            context.probe.dump();
            context.cleanup();
        }
    }

    protected ServiceLegionarySmokeContext createContext( ServiceLegionaryTransportScenario scenario ) throws Exception {
        ServiceLegionarySmokeContext context = new ServiceLegionarySmokeContext( this.mSystem, scenario );
        context.detachedObservationConfig = new ServiceDetachedObservationConfig(
                new JSONMaptron( "{enable:true, graceMillis:3000, sweepMillis:300, expireAsyncThreads:2, missingAfterReconnectPolicy:\"expire\"}" )
        );
        return this.mSmokeCase.createStartedContext( context );
    }

    protected void runGraceRound( ServiceLegionarySmokeContext context, int nRound ) throws Exception {
        GUID before = context.legionary.getInstanceGuid();
        context.probe.record( "DetachedGraceDevil", nRound, "BREAK", context.legionary );
        context.scenario.breakClientConnection( context );

        ServiceLegionaryAssertions.awaitTrue(
                context.scenario.name() + " round " + nRound + " did not enter Detached.",
                3000L,
                () -> this.hasStatus( context, before, ServiceInstanceStatus.Detached )
        );
        context.probe.record( "DetachedGraceDevil", nRound, "DETACHED", context.legionary );

        context.scenario.requestStateSynchronization( context, "DetachedGraceDevilRecover" );

        ServiceLegionaryAssertions.awaitTrue(
                context.scenario.name() + " round " + nRound + " did not recover Online.",
                10000L,
                () -> context.legionary.getInstanceGuid() != null
                        && context.legionary.getInstanceGuid().equals( before )
                        && context.legionary.getState() == com.acorn.redqueen.service.conduct.ServiceLegionaryState.Online
                        && this.hasStatus( context, before, ServiceInstanceStatus.Online )
        );
        context.probe.record( "DetachedGraceDevil", nRound, "RECOVERED", context.legionary );
    }

    protected boolean hasStatus( ServiceLegionarySmokeContext context, GUID instanceGuid, ServiceInstanceStatus status ) {
        ServiceInstanceEntry entry = context.serviceInstrument.queryServiceInstance( instanceGuid );
        return entry != null && status.getName().equals( entry.getStatus() );
    }
}
