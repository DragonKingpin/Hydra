package com.device.auto;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.device.registry.client.UniformDeviceClient;

public interface DeviceLegionaryTransportScenario extends Pinenut {

    String name();

    String endpointProtocol();

    int endpointPort();

    void hookServerTransport( DeviceLegionarySmokeContext context ) throws Exception;

    UniformDeviceClient createDeviceClient( DeviceLegionarySmokeContext context ) throws Exception;

    default void requestStateSynchronization( DeviceLegionarySmokeContext context, String reason ) throws Exception {
    }

    void cleanup( DeviceLegionarySmokeContext context );
}
