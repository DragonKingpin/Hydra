package com.pinecone.hydra.system.centrum;

import com.pinecone.framework.system.architecture.Component;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.system.DistributedSystem;
import com.pinecone.hydra.system.HierarchySystem;

public interface UniformCentralSystem extends HierarchySystem, DistributedSystem {

    GuidAllocator getSystemGuidAllocator();

    Component imageLoader();

}
