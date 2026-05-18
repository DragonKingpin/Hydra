package com.acorn.skynet.system;

import com.acorn.skynet.device.conduct.CollectiveDeviceRegiment;
import com.pinecone.framework.system.ModularizedSubsystem;
import com.pinecone.framework.system.SynergicSystem;
import com.pinecone.framework.system.regime.arch.Lord;
import com.pinecone.hydra.device.kom.DeviceInstrument;
import com.pinecone.hydra.device.registry.server.DeviceManager;
import com.pinecone.hydra.system.component.Slf4jTraceable;

public interface SkynetSubsystem extends SynergicSystem, ModularizedSubsystem, Lord, Slf4jTraceable {

    CollectiveDeviceRegiment deviceRegiment();

    DeviceInstrument deviceInstrument();

    DeviceManager deviceManager();
}
