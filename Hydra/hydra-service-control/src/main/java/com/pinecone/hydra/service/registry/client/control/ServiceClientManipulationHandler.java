package com.pinecone.hydra.service.registry.client.control;

import com.pinecone.framework.system.prototype.Pinenut;

public interface ServiceClientManipulationHandler extends Pinenut {

    void shutdownService( ServiceClientShutdownInstruction instruction );

}
