package com.pinecone.hydra.device.registry.client.control;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.device.registry.instruction.DeviceShutdownInstruction;

public interface DeviceClientManipulationHandler extends Pinenut {

    void shutdownDevice( DeviceShutdownInstruction instruction );
}
