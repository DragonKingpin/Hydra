package com.pinecone.hydra.system.ko.runtime;

import java.util.Map;

import com.pinecone.hydra.system.ko.ArchKernelObjectConfig;
import com.pinecone.hydra.system.ko.KernelObjectConfig;

public class GenericRuntimeInstrumentConfig extends ArchKernelObjectConfig implements KernelObjectConfig {
    public GenericRuntimeInstrumentConfig() {
        super();
    }

    public GenericRuntimeInstrumentConfig( Map<String, Object> config ){
        super( config );
    }
}
