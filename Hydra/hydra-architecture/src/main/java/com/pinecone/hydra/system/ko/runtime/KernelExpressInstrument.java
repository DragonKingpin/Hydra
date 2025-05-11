package com.pinecone.hydra.system.ko.runtime;

import com.pinecone.hydra.system.ko.KernelObjectConfig;

public class KernelExpressInstrument extends ArchRuntimeKOMTree implements CentralizedRuntimeInstrument {


    public KernelExpressInstrument( String superiorPathScope, KernelObjectConfig kernelObjectConfig ) {
        super( superiorPathScope, kernelObjectConfig );
    }

}
