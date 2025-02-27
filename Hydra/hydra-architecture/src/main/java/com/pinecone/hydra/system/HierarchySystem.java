package com.pinecone.hydra.system;

import com.pinecone.framework.system.prototype.Pinenut;

public interface HierarchySystem extends Pinenut {

    HyHierarchy getServiceArch();

    boolean isTopmostArchy();

    HyHierarchy getTopmostArchy();

    boolean isBottommostArchy();

    HyHierarchy getBottommostArchy();

}
