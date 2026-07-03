package com.walnut.redstone.ether.shuttle.kernel;

import com.pinecone.framework.system.prototype.Pinenut;

public interface KernelNamespaceBackend extends Pinenut {
    KernelMappedFileMeta stat( String szPath );

    KernelMappedFile read( String szPath );

    default boolean isWritable( String szPath ) {
        return false;
    }
}
