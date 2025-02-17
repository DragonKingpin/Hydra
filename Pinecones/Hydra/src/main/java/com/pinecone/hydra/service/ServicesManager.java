package com.pinecone.hydra.service;

import com.pinecone.hydra.service.registry.ServiceControlBlock;
import com.pinecone.hydra.system.ko.KernelObjectInstrument;

public interface ServicesManager extends KernelObjectInstrument {
    void registryService(Long serviceId, ServiceControlBlock serviceControlBlock);

    ServiceControlBlock getServiceControlBlock( Long serviceId );

    void removeServiceControlBlock( Long serviceId );
}
