package com.service.auto;

import java.util.Collection;

import com.acorn.redqueen.service.conduct.ServiceLegionaryJoinResponse;
import com.acorn.redqueen.service.conduct.ServiceLegionaryState;
import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.ServiceInstance;
import com.pinecone.hydra.service.kom.entity.ServiceInstanceEntry;
import com.pinecone.hydra.service.registry.constant.ServiceInstanceStatus;
import com.pinecone.hydra.service.registry.client.transport.ServiceClientTransportSyncReasons;
import com.pinecone.tritium.Tritium;

public class ServiceLegionaryIdempotencyDevilCase implements Pinenut {

    protected Tritium mSystem;

    protected ServiceLegionarySmokeCase mSmokeCase;

    public ServiceLegionaryIdempotencyDevilCase( Tritium system ) {
        this.mSystem = system;
        this.mSmokeCase = new ServiceLegionarySmokeCase( system );
    }

    public void run( ServiceLegionaryTransportScenario scenario, int nRounds ) throws Exception {
        ServiceLegionarySmokeContext context = this.mSmokeCase.createStartedContext( scenario );
        try {
            this.mSmokeCase.prepareLegionary( context );
            context.legionary.joinRegiment();
            context.probe.record( "IdempotencyDevil", 0, "INITIAL_ONLINE", context.legionary );
            ServiceLegionaryAssertions.assertOnline( context.legionary );

            for ( int i = 1; i <= nRounds; ++i ) {
                this.runIdempotencyRound( context, i );
            }

            this.mSmokeCase.passiveShutdown( context );
            Debug.greenfs( "[ServiceLegionaryIdempotencyDevil] " + scenario.name() + " " + nRounds + "/" + nRounds + " PASS" );
        }
        finally {
            context.probe.dump();
            context.cleanup();
        }
    }

    protected void runIdempotencyRound( ServiceLegionarySmokeContext context, int nRound ) throws Exception {
        GUID stable = context.legionary.getInstanceGuid();
        context.probe.record( "IdempotencyDevil", nRound, "JOIN_REPEAT", context.legionary );
        for ( int i = 0; i < 3; ++i ) {
            ServiceLegionaryJoinResponse response = context.legionary.joinRegiment();
            ServiceLegionaryAssertions.assertTrue(
                    stable.equals( response.getInstanceGuid() ),
                    context.scenario.name() + " repeated join changed instance. before => " + stable + ", after => " + response.getInstanceGuid()
            );
            this.assertSingleOnlineRuntimeInstance( context, stable );
        }

        context.probe.record( "IdempotencyDevil", nRound, "SYNC_REPEAT", context.legionary );
        for ( int i = 0; i < 3; ++i ) {
            context.scenario.requestStateSynchronization( context, ServiceClientTransportSyncReasons.StreamRecovered );
        }

        ServiceLegionaryAssertions.awaitTrue(
                context.scenario.name() + " idempotent sync did not settle online.",
                5000L,
                () -> context.legionary.getInstanceGuid() != null
                        && stable.equals( context.legionary.getInstanceGuid() )
                        && context.legionary.getState() == ServiceLegionaryState.Online
        );
        this.sleepQuietly( 500L );
        ServiceLegionaryAssertions.assertTrue(
                stable.equals( context.legionary.getInstanceGuid() ),
                context.scenario.name() + " repeated sync changed instance. before => " + stable + ", after => " + context.legionary.getInstanceGuid()
        );
        this.assertSingleOnlineRuntimeInstance( context, stable );
        this.assertCurrentInstanceEntryOnline( context, stable );
        context.probe.record( "IdempotencyDevil", nRound, "STABLE", context.legionary );
    }

    protected void assertSingleOnlineRuntimeInstance( ServiceLegionarySmokeContext context, GUID expectedInstanceGuid ) {
        Collection<ServiceInstance> instances = context.serviceManager.fetchServiceInstance(
                context.serviceInstrument.getGuidAllocator().parse( context.serviceMeta.getGuid() )
        );
        ServiceLegionaryAssertions.assertNotNull( instances, "Runtime service instance collection is null." );
        ServiceLegionaryAssertions.assertTrue(
                instances.size() == 1,
                context.scenario.name() + " should have exactly one runtime instance, actual => " + instances.size()
        );

        ServiceInstance instance = instances.iterator().next();
        ServiceLegionaryAssertions.assertNotNull( instance, "Runtime service instance is null." );
        ServiceLegionaryAssertions.assertTrue(
                expectedInstanceGuid.equals( instance.getId() ),
                context.scenario.name() + " runtime instance mismatch. expected => " + expectedInstanceGuid + ", actual => " + instance.getId()
        );
    }

    protected void assertCurrentInstanceEntryOnline( ServiceLegionarySmokeContext context, GUID expectedInstanceGuid ) {
        ServiceInstanceEntry entry = context.serviceInstrument.queryServiceInstance( expectedInstanceGuid );
        ServiceLegionaryAssertions.assertNotNull( entry, "Current service instance entry is null." );
        ServiceLegionaryAssertions.assertTrue(
                ServiceInstanceStatus.Online.getName().equals( entry.getStatus() ),
                context.scenario.name() + " current service instance should be Online, actual => " + entry.getStatus()
        );
    }

    protected void sleepQuietly( long nMillis ) {
        try {
            Thread.sleep( nMillis );
        }
        catch ( InterruptedException e ) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException( e );
        }
    }
}
