package com.pinecone.hydra.system.ko;

import com.pinecone.framework.system.regime.Instrument;
import com.pinecone.hydra.unit.imperium.ImperialTree;
import com.pinecone.framework.util.id.GuidAllocator;

public interface KernelObjectInstrument extends Instrument {
    GuidAllocator getGuidAllocator();

    ImperialTree getMasterTrieTree();

    KernelObjectConfig getConfig();
}
