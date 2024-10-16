package com.pinecone.hydra.system.ko;

import com.pinecone.hydra.unit.udtt.DistributedTrieTree;
import com.pinecone.ulf.util.id.GuidAllocator;

public interface KOMInstrument extends KernelObjectInstrument {
    GuidAllocator getGuidAllocator();

    DistributedTrieTree getMasterTrieTree();
}
