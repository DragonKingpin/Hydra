package com.acorn.skynet.system;

import com.pinecone.framework.system.ModularizedSubsystem;
import com.pinecone.framework.system.SynergicSystem;
import com.pinecone.framework.system.regime.arch.Lord;
import com.pinecone.hydra.system.component.Slf4jTraceable;

public interface SkynetSubsystem extends SynergicSystem, ModularizedSubsystem, Lord, Slf4jTraceable {
}
