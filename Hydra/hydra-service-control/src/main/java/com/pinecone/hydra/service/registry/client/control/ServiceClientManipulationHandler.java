package com.pinecone.hydra.service.registry.client.control;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.service.registry.instruction.ServiceShutdownInstruction;

public interface ServiceClientManipulationHandler extends Pinenut {

    void shutdownService( ServiceShutdownInstruction instruction );

}
