package com.pinecone.hydra.system.ko;

import com.pinecone.framework.system.regime.Instrument;
import com.pinecone.framework.util.id.GuidAllocator;

public interface KernelObjectInstrument extends Instrument {
    GuidAllocator getGuidAllocator();

    KernelObjectConfig getConfig();
}
