package com.service.auto;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.kom.entity.ServiceInstanceEntry;
import com.pinecone.hydra.service.registry.constant.ServiceInstanceStatus;
import com.pinecone.tritium.Tritium;

public class ServiceLegionaryHuskyAutoReconnectDevilCase implements Pinenut {

    protected Tritium mSystem;

    protected ServiceLegionarySmokeCase mSmokeCase;

    public ServiceLegionaryHuskyAutoReconnectDevilCase( Tritium system ) {
        this.mSystem = system;
        this.mSmokeCase = new ServiceLegionarySmokeCase( system );
    }

    public void run( int nRounds ) throws Exception {
        ServiceLegionarySmokeContext context = this.mSmokeCase.createStartedContext(
                new HuskyAutoReconnectServiceLegionaryScenario()
        );
        try {
            this.mSmokeCase.prepareLegionary( context );
            context.legionary.joinRegiment();
            ServiceLegionaryAssertions.assertOnline( context.legionary );

            GUID instanceGuid = context.legionary.getInstanceGuid();
            int nConnectionCount = this.queryConnectionCount( context, instanceGuid );
            context.probe.record( "HuskyAutoReconnectDevil", 0, "INITIAL_ONLINE", context.legionary );

            for ( int i = 1; i <= nRounds; ++i ) {
                int nPreviousConnectionCount = nConnectionCount;
                ServiceLegionaryAssertions.awaitTrue(
                        "Husky auto reconnect round " + i + " did not recover online with increased connection count.",
                        30000L,
                        () -> this.isRecovered( context, instanceGuid, nPreviousConnectionCount )
                );
                nConnectionCount = this.queryConnectionCount( context, instanceGuid );
                context.probe.record( "HuskyAutoReconnectDevil", i, "RECOVERED", context.legionary );
                if ( i < nRounds ) {
                    Thread.sleep( 10000L );
                }
            }

            this.mSmokeCase.passiveShutdown( context );
            Debug.greenfs( "[ServiceLegionaryHuskyAutoReconnectDevil] " + nRounds + "/" + nRounds + " PASS" );
        }
        finally {
            context.probe.dump();
            context.cleanup();
        }
    }

    protected boolean isRecovered( ServiceLegionarySmokeContext context, GUID instanceGuid, int nPreviousConnectionCount ) {
        if ( context.legionary.getInstanceGuid() == null || !instanceGuid.equals( context.legionary.getInstanceGuid() ) ) {
            return false;
        }
        if ( context.legionary.getState() != com.acorn.redqueen.service.conduct.ServiceLegionaryState.Online ) {
            return false;
        }

        ServiceInstanceEntry entry = context.serviceInstrument.queryServiceInstance( instanceGuid );
        return entry != null
                && ServiceInstanceStatus.Online.getName().equals( entry.getStatus() )
                && entry.getConnectionCount() > nPreviousConnectionCount;
    }

    protected int queryConnectionCount( ServiceLegionarySmokeContext context, GUID instanceGuid ) {
        ServiceInstanceEntry entry = context.serviceInstrument.queryServiceInstance( instanceGuid );
        ServiceLegionaryAssertions.assertNotNull( entry, "Service instance entry is null." );
        return entry.getConnectionCount();
    }
}
