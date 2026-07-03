package com.service.auto;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.service.registry.client.UniformServiceClient;

public interface ServiceLegionaryTransportScenario extends Pinenut {

    String name();

    String endpointProtocol();

    int endpointPort();

    void hookServerTransport( ServiceLegionarySmokeContext context ) throws Exception;

    UniformServiceClient createServiceClient( ServiceLegionarySmokeContext context ) throws Exception;

    void breakClientConnection( ServiceLegionarySmokeContext context ) throws Exception;

    default void requestStateSynchronization( ServiceLegionarySmokeContext context, String szReason ) throws Exception {
    }

    void cleanup( ServiceLegionarySmokeContext context );
}
