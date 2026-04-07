package com.pinecone.hydra.system.centrum;

import com.pinecone.framework.system.RuntimeSystem;
import com.pinecone.framework.system.architecture.Component;
import com.pinecone.framework.system.executum.Processum;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.system.DistributedSystem;
import com.pinecone.hydra.system.HierarchySystem;
import com.pinecone.hydra.system.imperium.ImperiumPrivy;
import com.pinecone.hydra.system.ko.KernelObjectConfig;
import com.pinecone.ulf.util.guid.i64.GuidAllocator72;

public interface UniformCentralSystem extends HierarchySystem, DistributedSystem {

    GuidAllocator getSystemGuidAllocator();

    GuidAllocator72 getSystemGuidAllocator72();

    Component imageLoader();

    KernelObjectConfig fundamentalKernelObjectConfig();

    ImperiumPrivy imperiumPrivy();


    static UniformCentralSystem evalCentralSystem( Processum that ) {
        if ( that instanceof UniformCentralSystem ) {
            return (UniformCentralSystem) that;
        }

        RuntimeSystem rs = that.parentSystem();
        if ( rs instanceof UniformCentralSystem ) {
            return (UniformCentralSystem) rs;
        }

        throw new IllegalArgumentException( "Not in UniformCentralSystem family." );
    }

}
