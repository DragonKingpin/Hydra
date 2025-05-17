package com.pinecone.hydra.system.ko.runtime;

import com.pinecone.hydra.system.ko.KernelObjectConfig;
import com.pinecone.hydra.system.ko.handle.KOMMountPointHandle;
import com.pinecone.hydra.system.ko.kom.KOMInstrument;
import com.pinecone.hydra.system.ko.kom.ProxiedKOMMountPointHandle;

public class KernelExpressInstrument extends ArchDirectMappingTrieRuntimeKOMTree implements CentralizedRuntimeInstrument {

    public KernelExpressInstrument( String superiorPathScope, KernelObjectConfig kernelObjectConfig ) {
        super( superiorPathScope, kernelObjectConfig );
    }

    @Override
    public KOMInstrument mount( String mountPointPath, KOMInstrument that ) {
        this.mount( mountPointPath, that.getSimpleName(), that );
        return that;
    }

    @Override
    public KOMInstrument mount( String mountPointPath, String treeNodeName, KOMInstrument that ) {
        KOMMountPointHandle handle = new ProxiedKOMMountPointHandle(
                treeNodeName, this.guidAllocator.nextGUID(), that
        );
        this.add( mountPointPath, handle );
        return that;
    }
}
