package com.service.auto;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.json.JSONMaptron;
import com.pinecone.hydra.service.kom.entity.ServiceInstanceEntry;
import com.pinecone.hydra.service.registry.constant.ServiceInstanceStatus;
import com.pinecone.hydra.service.registry.server.detached.ServiceDetachedObservationConfig;
import com.pinecone.tritium.Tritium;

public class ServiceLegionaryOfflineDevilCase implements Pinenut {

    protected Tritium mSystem;

    protected ServiceLegionarySmokeCase mSmokeCase;

    public ServiceLegionaryOfflineDevilCase( Tritium system ) {
        this.mSystem = system;
        this.mSmokeCase = new ServiceLegionarySmokeCase( system );
    }

    public void run( ServiceLegionaryTransportScenario scenario ) throws Exception {
        ServiceLegionarySmokeContext context = this.createContext( scenario );
        try {
            this.mSmokeCase.prepareLegionary( context );
            context.legionary.joinRegiment();
            context.probe.record( "OfflineDevil", 0, "INITIAL_ONLINE", context.legionary );
            ServiceLegionaryAssertions.assertOnline( context.legionary );

            GUID instanceGuid = context.legionary.getInstanceGuid();
            context.probe.record( "OfflineDevil", 1, "BREAK", context.legionary );
            context.scenario.breakClientConnection( context );

            ServiceLegionaryAssertions.awaitTrue(
                    context.scenario.name() + " did not enter Detached.",
                    3000L,
                    () -> this.hasStatus( context, instanceGuid, ServiceInstanceStatus.Detached )
            );
            context.probe.record( "OfflineDevil", 1, "DETACHED", context.legionary );

            ServiceLegionaryAssertions.awaitTrue(
                    context.scenario.name() + " did not go Offline.",
                    10000L,
                    () -> this.hasStatus( context, instanceGuid, ServiceInstanceStatus.Offline )
                            && !context.serviceManager.hasOwnedInstance( instanceGuid )
            );
            context.probe.record( "OfflineDevil", 1, "OFFLINE", context.legionary );
            Debug.greenfs( "[ServiceLegionaryOfflineDevil] " + scenario.name() + " PASS" );
        }
        finally {
            context.probe.dump();
            context.cleanup();
        }
    }

    protected ServiceLegionarySmokeContext createContext( ServiceLegionaryTransportScenario scenario ) throws Exception {
        ServiceLegionarySmokeContext context = new ServiceLegionarySmokeContext( this.mSystem, scenario );
        context.detachedObservationConfig = new ServiceDetachedObservationConfig(
                new JSONMaptron( "{enable:true, graceMillis:300, sweepMillis:50, expireAsyncThreads:2, missingAfterReconnectPolicy:\"Offline\"}" )
        );
        return this.mSmokeCase.createStartedContext( context );
    }

    protected boolean hasStatus( ServiceLegionarySmokeContext context, GUID instanceGuid, ServiceInstanceStatus status ) {
        ServiceInstanceEntry entry = context.serviceInstrument.queryServiceInstance( instanceGuid );
        return entry != null && status.getName().equals( entry.getStatus() );
    }
}
