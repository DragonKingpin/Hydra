package com.pinecone.hydra.registry;

import com.pinecone.hydra.system.ko.KernelObjectInstrument;

public interface Registry extends KernelObjectInstrument {
    RegistryConfig KernelRegistryConfig = new KernelRegistryConfig();

    RegistryConfig getRegistryConfig();
}
