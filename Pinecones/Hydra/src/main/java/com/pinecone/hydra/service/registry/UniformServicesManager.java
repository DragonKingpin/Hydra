package com.pinecone.hydra.service.registry;

import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.service.ServicesManager;
import com.pinecone.hydra.service.kom.ServicesInstrument;
import com.pinecone.hydra.system.ko.KernelObjectConfig;
import com.pinecone.hydra.unit.imperium.ImperialTree;

import java.util.concurrent.ConcurrentMap;

public class UniformServicesManager implements ServicesManager {
    protected ServicesInstrument  mServicesInstrument;

    protected ConcurrentMap<Long, ServiceControlBlock > mServiceRegistry;





    @Override
    public GuidAllocator getGuidAllocator() {
        return null;
    }

    @Override
    public ImperialTree getMasterTrieTree() {
        return null;
    }

    @Override
    public KernelObjectConfig getConfig() {
        return null;
    }
}
